package zc.datawash.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zc.datawash.model.expression.Expression;
import zc.datawash.model.expression.ExpressionFilter;
import zc.datawash.util.DataWashContant;
import zc.datawash.util.UniqueList;

public class AnswerPool {

	private final List<Integer> removedCustomerAnswerList = new UniqueList<Integer>();
	
	private final Map<Integer, String> removedDuplicateMap = new HashMap<Integer, String>();
	
	private final Map<Integer, String> removedStartTimeMap = new HashMap<Integer, String>();
	
	private final Map<Integer, String> removedTimespanMap = new HashMap<Integer, String>();
	
	private final Map<Integer, String> removedKeyQuestionMap = new HashMap<Integer, String>();
	
	private final Map<Integer, String> removedNotTargetUserMap = new HashMap<Integer, String>();
	
	private final Map<Integer, String> removedLogicConflictMap = new HashMap<Integer, String>();
	
	private final List<CustomerAnswer> allAnswers = new ArrayList<CustomerAnswer>();
	
	private final Comparator<CustomerAnswer> customerAnswernewComparator = new Comparator<CustomerAnswer>() {
		public int compare(CustomerAnswer o1, CustomerAnswer o2) {
			return o1.getStartTimeText().compareTo(o2.getStartTimeText());
		}
	};

	public List<Integer> getRemovedCustomerAnswerList() {
		return removedCustomerAnswerList;
	}

	public List<CustomerAnswer> getAllAnswers(){
		return this.allAnswers;
	}
	
	// step1
	public void removeDuplidateByWangwangId(String idQuestionNum) {
		HashMap<String, List<CustomerAnswer>> answerPool = new HashMap<String, List<CustomerAnswer>>();
		// inital answerPool
		for(CustomerAnswer answer: allAnswers){
			if (!this.removedCustomerAnswerList.contains(answer.getCvsRowNum())){
				Map<String, String> answerOptions = answer.getQuestionAnswers().get(idQuestionNum);
				String id = answerOptions.get("O1");
				answer.setWangwangId(id);
				if(!answerPool.containsKey(id)){
					answerPool.put(id, new ArrayList<CustomerAnswer>());
				}
				answerPool.get(id).add(answer);
			}
		}
		
		for (List<CustomerAnswer> duplicateAnswers : answerPool.values()) {
			if (duplicateAnswers.size() > 1
					&& !DataWashContant.VAL_EMPTY
							.equals(duplicateAnswers.get(0).getWangwangId())) {
				sortCustomerAnswerByStartTimeAsc(duplicateAnswers);
				for (int i = 1; i < duplicateAnswers.size(); i++) {
					int rowNum = duplicateAnswers.get(i).getCvsRowNum();
					this.removedCustomerAnswerList.add(rowNum);
					this.removedDuplicateMap.put(rowNum, "被清洗，重复ID记录, 和" + (duplicateAnswers.get(0).getCvsRowNum() + 1)  + "行重复");
				}
			} else if (duplicateAnswers.size() > 1
					&& DataWashContant.VAL_EMPTY
							.equals(duplicateAnswers.get(0).getWangwangId())) {
				sortCustomerAnswerByStartTimeAsc(duplicateAnswers);
				for (int i = 0; i < duplicateAnswers.size(); i++) {
					if (i == duplicateAnswers.size() - 1) {
						break;
					}
					
					CustomerAnswer prevAnswer = duplicateAnswers.get(i);
					CustomerAnswer nextAnswer = duplicateAnswers.get(i + 1);
					if(!(prevAnswer.getStartTimeText().equals(nextAnswer.getStartTimeText())) || !(prevAnswer.getEndTimeText().equals(nextAnswer.getEndTimeText()))){
						continue;
					}
					
					Map<String, Map<String, String>> prevAnswerMap = prevAnswer.getQuestionAnswers();
					Map<String, Map<String, String>> nextAnswerMap = nextAnswer.getQuestionAnswers();
					boolean answerSameFlag = true;
					for(String questionNumKey: prevAnswerMap.keySet()){
						Map<String, String> prevAnswerOptionMap = prevAnswerMap.get(questionNumKey);
						Map<String, String> nextAnswerOptionMap = nextAnswerMap.get(questionNumKey);
						for(String optionKey: prevAnswerOptionMap.keySet()){
							if(!prevAnswerOptionMap.get(optionKey).equals(nextAnswerOptionMap.get(optionKey))){
								answerSameFlag = false;
								break;
							}
						}
						if(!answerSameFlag){
							break;
						}
					}
					if(answerSameFlag){
						// remove the duplicate first record. and start from 0, so + 2
						int rowNum = duplicateAnswers.get(i).getCvsRowNum();
						this.removedCustomerAnswerList.add(rowNum);
						this.removedDuplicateMap.put(rowNum, "被清洗，重复ID记录, 和" + (rowNum + 2)  + "行重复");
					}

				}
			}
		}
	}

	private List<CustomerAnswer> sortCustomerAnswerByStartTimeAsc(
			List<CustomerAnswer> duplicateAnswers) {
		Collections.sort(duplicateAnswers, customerAnswernewComparator);
		return duplicateAnswers;
	}

	// step2
	public void removeByStartTime(String inputTime) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = sdf.parse(inputTime);
		for (CustomerAnswer answer : allAnswers) {
			if (!this.removedCustomerAnswerList.contains(answer.getCvsRowNum())){
				if (answer.getStartTime().compareTo(time) < 0) {
					int rowNum = answer.getCvsRowNum();
					this.removedCustomerAnswerList.add(rowNum);
					this.removedStartTimeMap.put(rowNum, "被清洗，填答时间早于" + inputTime);
				}
			}
		}
	}

	// step3
	public void removeByAnswerTimespan(int shortestSpan, int longestSpan)
			throws Exception {
		for (CustomerAnswer answer : allAnswers) {
			if (!this.removedCustomerAnswerList.contains(answer.getCvsRowNum())){
				long minuteTimespan = (answer.getEndTime().getTime() - answer.getStartTime().getTime()) / 1000 / 60;
				int rowNum = answer.getCvsRowNum();
				if (shortestSpan != 0 && minuteTimespan < shortestSpan) {
					this.removedCustomerAnswerList.add(rowNum);
					this.removedTimespanMap.put(rowNum, "被清洗，填答时间为" + minuteTimespan + "分钟, 小于最短填答时间" + shortestSpan + "分钟");
				}
				if(longestSpan != 0 && minuteTimespan > longestSpan) {
					this.removedCustomerAnswerList.add(rowNum);
					this.removedTimespanMap.put(rowNum, "被清洗，填答时间为" + minuteTimespan + "分钟, 大于最长填答时间" + longestSpan + "分钟");
				}
			}
		}
	}

	// step4
	public void removeByKeyQuestionMiss(String expressionText, String originalExpressionText) throws Exception {
		removeByExpression(expressionText, this.removedKeyQuestionMap, "被清洗， 关键问题缺失" + originalExpressionText);
	}
		
	// step5
	public void removeByNotTargetUser(String expressionText) throws Exception {
		removeByExpression(expressionText, this.removedNotTargetUserMap, "被清洗， 非目标用户" + expressionText);
	}

	// step5
	public void removeByLogicConflict(String expressionText) throws Exception {
		removeByExpression(expressionText, this.removedLogicConflictMap, "被清洗， 逻辑矛盾" + expressionText);
	}
		
	private void removeByExpression(String expressionText, Map<Integer, String> removedList, String removeMessage) throws Exception {
		Expression expression = ExpressionFilter.parse(expressionText);

		for (CustomerAnswer answer : allAnswers) {
			if (!this.removedCustomerAnswerList.contains(answer.getCvsRowNum())){
				if (ExpressionFilter.match(answer.getQuestionAnswers(),
						expression)) {
					int rowNum = answer.getCvsRowNum();
					this.removedCustomerAnswerList.add(rowNum);
					removedList.put(rowNum, removeMessage);
				}
			} 
		}
	}

	public int getRemovedRecordCount(){
		return this.removedCustomerAnswerList.size();
	}
	
	public int getRemovedDuplicateRecordCount(){
		return this.removedDuplicateMap.size();
	}
	
	public int getRemovedStartTimeRecordCount(){
		return this.removedStartTimeMap.size();
	}
	
	public int getRemovedTimespanRecordCount(){
		return this.removedTimespanMap.size();
	}
	
	public int getRemoedKeyQuestionRecordCount(){
		return this.removedKeyQuestionMap.size();
	}
	
	public int getRemovedNotTargetUserRecordCount(){
		return this.removedNotTargetUserMap.size();
	}
	
	public int getRemovedLogicConflictRecordCount(){
		return this.removedLogicConflictMap.size();
	}
	
	public void clear(){
		this.removedCustomerAnswerList.clear();
		this.removedDuplicateMap.clear();
		this.removedStartTimeMap.clear();
		this.removedTimespanMap.clear();
		this.removedKeyQuestionMap.clear();
		this.removedNotTargetUserMap.clear();
		this.removedLogicConflictMap.clear();
	}

	public Map<Integer, String> getRemovedDuplicateMap() {
		return removedDuplicateMap;
	}

	public Map<Integer, String> getRemovedStartTimeMap() {
		return removedStartTimeMap;
	}

	public Map<Integer, String> getRemovedTimespanMap() {
		return removedTimespanMap;
	}

	public Map<Integer, String> getRemovedKeyQuestionMap() {
		return removedKeyQuestionMap;
	}

	public Map<Integer, String> getRemovedNotTargetUserMap() {
		return removedNotTargetUserMap;
	}

	public Map<Integer, String> getRemovedLogicConflictMap() {
		return removedLogicConflictMap;
	}

}
