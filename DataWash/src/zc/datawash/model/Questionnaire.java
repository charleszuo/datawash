package zc.datawash.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Questionnaire {
	private String naireId;
		
	List<Question> questionList = new ArrayList<Question>();
	
	HashMap<Integer, QuestionOption> questionOptions = new HashMap<Integer, QuestionOption>();

	public String getNaireId() {
		return naireId;
	}

	public void setNaireId(String naireId) {
		this.naireId = naireId;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void addQuestion(Question question) {
		this.questionList.add(question);
	}
	
	public void addQuestions(List<Question> questions) {
		this.questionList.addAll(questions);
	}
	
	public void setQuestionOptions(HashMap<Integer, QuestionOption> questionOptions) {
		this.questionOptions = questionOptions;
	}

	public QuestionOption getQuestionOptionByColNum(Integer colNum){
		return this.questionOptions.get(colNum);
	}
	
	public void sort(){
		Collections.sort(this.questionList, new Comparator<Question>(){

			public int compare(Question arg0, Question arg1) {
				return 0;
			}
			
		});
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Œ æÌID: ").append(this.naireId).append(":\r\n");
		for(Question question : this.questionList){
			sb.append(question.toString()).append("\r\n");
		}
		return sb.toString();
	}
}
