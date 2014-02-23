package zc.datawash.model;

public class AnswerOption {
	private String answerValue;
	
	private QuestionOption questionOption;
	
	private CustomerAnswer answer;

	public String getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

	public QuestionOption getQuestionOption() {
		return questionOption;
	}

	public void setQuestionOption(QuestionOption questionOption) {
		this.questionOption = questionOption;
	}

	public CustomerAnswer getAnswer() {
		return answer;
	}

	public void setAnswer(CustomerAnswer answer) {
		this.answer = answer;
	}
	
}
