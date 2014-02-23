package zc.datawash;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import zc.datawash.model.AnswerOption;
import zc.datawash.model.AnswerPool;
import zc.datawash.model.CustomerAnswer;
import zc.datawash.model.Question;
import zc.datawash.model.QuestionOption;
import zc.datawash.model.Questionnaire;
import zc.datawash.model.SpreadSheet;
import zc.datawash.util.DataWashContant;
import zc.datawash.util.DataWashUtil;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class DataWasher {
	private final AnswerPool answerPool = new AnswerPool();
	
	private int sourceRecordCount = 0;
	
	private String csvFilePath = null;
	
	private SimpleDateFormat timeStampSDF = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public DataWasher(){
		
	}
	
	public void parseCSVFile(String csvFilePath) throws Exception{
		this.csvFilePath = csvFilePath;
		List<String[]> csvList = new ArrayList<String[]>();
		CsvReader reader = new CsvReader(csvFilePath, ',',
				Charset.forName("GBK"));
		Questionnaire naire = new Questionnaire();
		SpreadSheet spreadSheet = new SpreadSheet();
		
		List<AnswerOption> answerOptions = new ArrayList<AnswerOption>();
		int rowNum = 0;
		while (reader.readRecord()) {
			csvList.add(reader.getValues());
			
			if (rowNum == 0) {
				String[] rowContent = csvList.get(0);
				
				HashMap<Integer, List<QuestionOption>> questionOptionMap = new HashMap<Integer, List<QuestionOption>>();
				HashMap<Integer, QuestionOption> questionOptions = new HashMap<Integer, QuestionOption>();

				for (int i = 0; i < rowContent.length; i++) {
					String cell = rowContent[i];
					if (DataWashContant.COL_NAIRE_ID.equals(cell)) {
						spreadSheet.setNaireIdColNum(i);
					} else if (DataWashContant.COL_START_TIME.equals(cell)) {
						spreadSheet.setStartTimeColNum(i);
					} else if (DataWashContant.COL_END_TIME.equals(cell)) {
						spreadSheet.setEndTimeColNum(i);
					} else if (DataWashContant.COL_IS_COMPLETE.equals(cell)) {
						spreadSheet.setCompleteFlagColNum(i);
					} else if (DataWashContant.COL_MEMBER_ID.equals(cell)) {
						spreadSheet.setMemberIdColNum(i);
					} else if(DataWashContant.COL_EMPTY_COLUMN.equals(cell)){
						spreadSheet.addEmptyColumn(i);
					}else {
						QuestionOption questionOption = DataWashUtil
								.generateQuestionOption(cell, i);
						if (questionOption != null) {
							Integer questionNum = questionOption
									.getQuestionNum();
							List<QuestionOption> optionList = questionOptionMap
									.get(questionNum);
							if (optionList == null) {
								optionList = new ArrayList<QuestionOption>();
								questionOptionMap.put(questionNum,
										optionList);
							}
							optionList.add(questionOption);

							// set colnum - question option mapping
							questionOptions.put(i, questionOption);
						}else{
							throw new RuntimeException("不支持该问卷格式!");
						}
					}
				}

				naire.setQuestionOptions(questionOptions);

				List<Question> questionList = new ArrayList<Question>();
				for (Integer key : questionOptionMap.keySet()) {
					Question question = new Question();
					question.setNaire(naire);
					question.setQuestionId(key.intValue());
					question.addOptions(questionOptionMap.get(key));
					List<QuestionOption> options = question.getOptionList();
					// set question content
					QuestionOption option = options.get(0);
					if (option != null) {
						question.setQuestionContent(option
								.getQuestionContent());
					}

					// set question type: single or multiple
					if (options.size() == 1) {
						question.setType(0);
					} else if (options.size() > 2) {
						question.setType(1);
					} else if (options.size() == 2) {
						if (DataWashContant.TXT_QITA.equals(options.get(0)
								.getOptionNumber())
								|| DataWashContant.TXT_QITA.equals(options
										.get(1).getOptionNumber())) {
							question.setType(0);
						} else {
							question.setType(1);
						}
					}

					questionList.add(question);
				}

				naire.addQuestions(questionList);

			} else {
				answerOptions.clear();
				CustomerAnswer customerAnswer = new CustomerAnswer();
				customerAnswer.setNaire(naire);
				customerAnswer.setCvsRowNum(rowNum);
				answerPool.getAllAnswers().add(customerAnswer);
				
				String[] rowContent = csvList.get(rowNum);
				for (int i = 0; i < rowContent.length; i++) {
					if (i == spreadSheet.getNaireIdColNum()) {
						if (rowNum == 1) {
							naire.setNaireId(rowContent[i]);
						}
					} else if (i == spreadSheet.getStartTimeColNum()) {
						customerAnswer.setStartTimeText(rowContent[i]);
					} else if (i == spreadSheet.getEndTimeColNum()) {
						customerAnswer.setEndTimeText(rowContent[i]);
					} else if (i == spreadSheet.getCompleteFlagColNum()) {
						customerAnswer.setCompleteFlag(rowContent[i]);
					} else if (i == spreadSheet.getMemberIdColNum()) {
						customerAnswer.setMemberId(rowContent[i]);
					} else if (spreadSheet.getEmptyColumns().contains(i)) {
						// don't handle the empty question column
						continue;
					} else {
						AnswerOption answerOption = new AnswerOption();
						QuestionOption questionOption = naire
								.getQuestionOptionByColNum(i);
						answerOption.setQuestionOption(questionOption);
						answerOption.setAnswer(customerAnswer);
						if("".equals(rowContent[i])){
							answerOption.setAnswerValue(DataWashContant.VAL_EMPTY);
						}else{
							answerOption.setAnswerValue(rowContent[i]);
						}
						
						answerOptions.add(answerOption);
					}
				}

				customerAnswer.generateAnswers(answerOptions);
			}
			rowNum++;
		}
		// for last rowNum ++ 
		sourceRecordCount = rowNum - 1;
		answerOptions.clear();
		reader.close();
	}
	
	public String[] generateWashedFile() throws Exception{
		String[] generatedFiles = new String[2];
		
		List<Integer> removedCsvRowNumList = this.answerPool.getRemovedCustomerAnswerList();
		int rowNum = 0;
		CsvReader reader = new CsvReader(this.csvFilePath, ',', Charset.forName("GBK"));
		String prefixFilePath = this.csvFilePath.substring(0,this.csvFilePath.lastIndexOf("."));
		
		StringBuilder fileNameBuilder = new StringBuilder();
		String timeStemp = getTimeStamp();
		fileNameBuilder.append(prefixFilePath).append("_washed_").append(timeStemp).append(".csv");
		String washedFileName = fileNameBuilder.toString();
		
		CsvWriter washedFileWriter = new CsvWriter(washedFileName, ',', Charset.forName("GBK"));
		while (reader.readRecord()) {
			if(!removedCsvRowNumList.contains(rowNum)){
				washedFileWriter.writeRecord(reader.getValues());
			}
			rowNum ++;
		}
		reader.close();
		washedFileWriter.close();
		generatedFiles[0] = washedFileName;
		
		fileNameBuilder.delete(0, fileNameBuilder.length());
		fileNameBuilder.append(prefixFilePath).append("_deleteflag_").append(timeStemp).append(".csv");
		String deleteFlagFileName = fileNameBuilder.toString();
		
		CsvReader readerAgain = new CsvReader(this.csvFilePath, ',', Charset.forName("GBK"));
		CsvWriter deleteFlagFileWriter = new CsvWriter(deleteFlagFileName, ',', Charset.forName("GBK"));
		rowNum = 0;
		while (readerAgain.readRecord()) {
			if(rowNum == 0){
				String[] titles = readerAgain.getValues();
				String[] newTitles = addOneColumnToRecord(titles);
				newTitles[newTitles.length - 1] = "清洗状态";
				deleteFlagFileWriter.writeRecord(newTitles);
			}else{
				String[] record = readerAgain.getValues();
				String[] newRecord = addOneColumnToRecord(record);
				if(this.answerPool.getRemovedDuplicateMap().containsKey(rowNum)){
					newRecord[newRecord.length - 1] = this.answerPool.getRemovedDuplicateMap().get(rowNum);
				}else if(this.answerPool.getRemovedStartTimeMap().containsKey(rowNum)){
					newRecord[newRecord.length - 1] = this.answerPool.getRemovedStartTimeMap().get(rowNum);
				}else if(this.answerPool.getRemovedTimespanMap().containsKey(rowNum)){
					newRecord[newRecord.length - 1] = this.answerPool.getRemovedTimespanMap().get(rowNum);
				}else if(this.answerPool.getRemovedKeyQuestionMap().containsKey(rowNum)){
					newRecord[newRecord.length - 1] = this.answerPool.getRemovedKeyQuestionMap().get(rowNum);
				}else if(this.answerPool.getRemovedNotTargetUserMap().containsKey(rowNum)){
					newRecord[newRecord.length - 1] = this.answerPool.getRemovedNotTargetUserMap().get(rowNum);
				}else if(this.answerPool.getRemovedLogicConflictMap().containsKey(rowNum)){
					newRecord[newRecord.length - 1] = this.answerPool.getRemovedLogicConflictMap().get(rowNum);
				}else{
					newRecord[newRecord.length - 1] = "";
				}
				deleteFlagFileWriter.writeRecord(newRecord);
			}
			rowNum ++;
		}
		readerAgain.close();
		deleteFlagFileWriter.close();
		generatedFiles[1] = deleteFlagFileName;
		
		return generatedFiles;
	}
	
	private String[] addOneColumnToRecord(String[] oldColumns){
		String[] newTitles = new String[oldColumns.length + 1];
		for(int i=0; i<oldColumns.length; i++){
			newTitles[i] = oldColumns[i];
		}
		return newTitles;
	}
	
	private String getTimeStamp(){
		return timeStampSDF.format(new Date());
	}
	
	public void removeDuplidateByWangwangId(String idQuestionNum){
		answerPool.removeDuplidateByWangwangId(idQuestionNum);
	}
	
	public void removeByStartTime(String inputTime) throws Exception{
		answerPool.removeByStartTime(inputTime);
	}
	
	public void removeByAnswerTimespan(int shortestSpan, int longestSpan) throws Exception{
		answerPool.removeByAnswerTimespan(shortestSpan, longestSpan);
	}
	
	public void removeByKeyQuestionMiss(String expression, String originalExpressionText) throws Exception{
		answerPool.removeByKeyQuestionMiss(expression, originalExpressionText);
	}
	
	public void removeByNotTargetUser(String expression) throws Exception{
		answerPool.removeByNotTargetUser(expression);
	}
	
	public void removeByLogicConflict(String expression) throws Exception{
		answerPool.removeByLogicConflict(expression);
	}
	
	public int getSourceRecordCount(){
		return this.sourceRecordCount;
	}
	
	public int getWashedRecordCount(){
		return this.sourceRecordCount - this.answerPool.getRemovedRecordCount();
	}
	
	public int getRemovedDuplicateRecordCount(){
		return this.answerPool.getRemovedDuplicateRecordCount();
	}
	
	public int getRemovedStartTimeRecordCount(){
		return this.answerPool.getRemovedStartTimeRecordCount();
	}
	
	public int getRemovedTimespanRecordCount(){
		return this.answerPool.getRemovedTimespanRecordCount();
	}
	
	public int getRemoedKeyQuestionRecordCount(){
		return this.answerPool.getRemoedKeyQuestionRecordCount();
	}
	
	public int getRemovedNotTargetUserRecordCount(){
		return this.answerPool.getRemovedNotTargetUserRecordCount();
	}
	
	public int getRemovedLogicConflictRecordCount(){
		return this.answerPool.getRemovedLogicConflictRecordCount();
	}
	
	public void clear(){
		this.answerPool.clear();
	}
}
