package zc.datawash.model;


public class QuestionOption {
	private String optionNumber;
	
	private String optionContent;
	
	String questionContent;

	private Question question;
	
	private int questionNum;
	
	private int colNum;
	
	public String getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(String optionNumber) {
		this.optionNumber = optionNumber;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public int getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
	
	public String toString(){
		return this.optionContent;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	
}
