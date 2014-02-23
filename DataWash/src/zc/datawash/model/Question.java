package zc.datawash.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Question {
	private int questionId;
	
	// 0 for single, 1 for multiple
	private Integer type;
	
	private Questionnaire naire;
	
	private List<QuestionOption> optionList = new ArrayList<QuestionOption>();
	
	private String questionContent;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Questionnaire getNaire() {
		return naire;
	}

	public void setNaire(Questionnaire naire) {
		this.naire = naire;
	}

	public List<QuestionOption> getOptionList() {
		return optionList;
	}

	public void addOption(QuestionOption option) {
		this.optionList.add(option);
	}
	
	public void addOptions(List<QuestionOption> optoins){
		this.optionList.addAll(optoins);
		for(QuestionOption option: optionList){
			option.setQuestion(this);
		}
	}
	
	public boolean isSingle(){
		return this.type == 0;
	}
	
	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public void sort(){
		Collections.sort(this.optionList, new Comparator<QuestionOption>(){

			public int compare(QuestionOption arg0, QuestionOption arg1) {
				return 0;
			}
			
		});
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("题目").append(this.questionId).append(": ").append(this.questionContent).append("\r\n类型:");
		if(this.isSingle()){
			sb.append("单选题\r\n");
		}else{
			sb.append("多选题\r\n");
		}
		for(QuestionOption option: this.optionList){
			sb.append(option.toString()).append("\r\n");
		}
		sb.append("-----------------------------------");
		return sb.toString();
	}
}
