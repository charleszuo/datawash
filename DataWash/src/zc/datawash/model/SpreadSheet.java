package zc.datawash.model;

import java.util.ArrayList;
import java.util.List;

public class SpreadSheet {
	private int naireIdColNum = -1;
	
	private int startTimeColNum = -1;
	
	private int endTimeColNum = -1;
	
	private int memberIdColNum = -1;
	
	private int completeFlagColNum = -1;
	
	private int wangwangIdColNum = -1;
	
	private List<Integer> emptyColumns = new ArrayList<Integer>();

	public int getNaireIdColNum() {
		return naireIdColNum;
	}

	public void setNaireIdColNum(int naireIdColNum) {
		this.naireIdColNum = naireIdColNum;
	}

	public int getStartTimeColNum() {
		return startTimeColNum;
	}

	public void setStartTimeColNum(int startTimeColNum) {
		this.startTimeColNum = startTimeColNum;
	}

	public int getEndTimeColNum() {
		return endTimeColNum;
	}

	public void setEndTimeColNum(int endTimeColNum) {
		this.endTimeColNum = endTimeColNum;
	}

	public int getMemberIdColNum() {
		return memberIdColNum;
	}

	public void setMemberIdColNum(int memberIdColNum) {
		this.memberIdColNum = memberIdColNum;
	}

	public int getCompleteFlagColNum() {
		return completeFlagColNum;
	}

	public void setCompleteFlagColNum(int completeFlagColNum) {
		this.completeFlagColNum = completeFlagColNum;
	}

	public int getWangwangIdColNum() {
		return wangwangIdColNum;
	}

	public void setWangwangIdColNum(int wangwangIdColNum) {
		this.wangwangIdColNum = wangwangIdColNum;
	}

	public List<Integer> getEmptyColumns() {
		return emptyColumns;
	}

	public void addEmptyColumn(Integer emptyColumns) {
		this.emptyColumns.add(emptyColumns);
	}
	
}
