package zc.datawash.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerAnswer {
	private Date startTime;

	private Date endTime;

	private String startTimeText;

	private String endTimeText;

	private int cvsRowNum;

	private String wangwangId;

	private Questionnaire naire;

	private String completeFlag;

	private String memberId;

	private final SimpleDateFormat sdfFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final SimpleDateFormat sdfFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private final SimpleDateFormat sdfFormat3 = new SimpleDateFormat("yyyy/M/dd HH:mm");

	// questionId - answer values
	private Map<String, Map<String, String>> answers = new HashMap<String, Map<String, String>>();

	public void generateAnswers(List<AnswerOption> answerOptions){
		for(AnswerOption option: answerOptions){
			Question question = option.getQuestionOption().getQuestion();
			Map<String, String> answerValues = answers.get(new Integer(question.getQuestionId()).toString());
			if(answerValues == null){
				answerValues = new HashMap<String, String>();
				answers.put(new Integer(question.getQuestionId()).toString(), answerValues);
			}
			answerValues.put("O" + option.getQuestionOption().getOptionNumber(), option.getAnswerValue());
		}
	}
	
	public Map<String, Map<String, String>> getQuestionAnswers(){
		return answers;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getCvsRowNum() {
		return cvsRowNum;
	}

	public void setCvsRowNum(int cvsRowNum) {
		this.cvsRowNum = cvsRowNum;
	}

	public String getWangwangId() {
		return wangwangId;
	}

	public void setWangwangId(String wangwangId) {
		this.wangwangId = wangwangId;
	}

	public Questionnaire getNaire() {
		return naire;
	}

	public void setNaire(Questionnaire naire) {
		this.naire = naire;
	}

	public String getStartTimeText() {
		return startTimeText;
	}

	public void setStartTimeText(String startTimeText) {
		this.startTimeText = startTimeText;
		Date startTimeTemp = null;
		try{
			startTimeTemp = sdfFormat1.parse(startTimeText);
		}catch(Exception e1){
			try{
				startTimeTemp = sdfFormat2.parse(startTimeText);
			}catch(Exception e2){
				try{
					startTimeTemp = sdfFormat3.parse(startTimeText);
				}catch(Exception e3){
					
				}
			}
		}
		if(startTimeTemp != null){
			this.setStartTime(startTimeTemp);
		}
	}

	public String getEndTimeText() {
		return endTimeText;
	}

	public void setEndTimeText(String endTimeText) {
		this.endTimeText = endTimeText;
		Date endTimeTemp = null;
		try{
			endTimeTemp = sdfFormat1.parse(endTimeText);
		}catch(Exception e1){
			try{
				endTimeTemp = sdfFormat2.parse(endTimeText);
			}catch(Exception e2){
				try{
					endTimeTemp = sdfFormat3.parse(endTimeText);
				}catch(Exception e3){
					
				}
			}
		}
		if(endTimeTemp != null){
			this.setEndTime(endTimeTemp);
		}
	}

	public String getCompleteFlag() {
		return completeFlag;
	}

	public void setCompleteFlag(String completeFlag) {
		this.completeFlag = completeFlag;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}
