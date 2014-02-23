package zc.datawash.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zc.datawash.model.QuestionOption;

public class DataWashUtil {
	private static final String singlePattern1Txt = "^[A-Za-z]*(\\d{1,4})(\\S*\\t*\\s*\\S*\\s*\\S*)$";
	private static final String singleMutiplePattern2Txt = "^\\[(\\S*)\\][A-Za-z]*(\\d{1,4})(\\S*\\t*\\s*\\S*\\s*\\S*)$";
	private static final String mutiplePattern3Txt = "^\\[(\\S*)\\]\\[(\\S*)\\][A-Za-z]*(\\d{1,4})(\\S*\\t*\\s*\\S*\\s*\\S*)$";
	private static final String wrongFormatPatternTxt = "^(\\S*\\t*\\s*\\S*\\s*\\S*)$";
	private static final Pattern singlePattern1 = Pattern.compile(singlePattern1Txt);
	private static final Pattern singleMutiplePattern2 = Pattern.compile(singleMutiplePattern2Txt);
	private static final Pattern mutiplePattern3 = Pattern.compile(mutiplePattern3Txt);
	private static final Pattern wrongFormatPattern = Pattern.compile(wrongFormatPatternTxt);
	
	public static QuestionOption generateQuestionOption(String optionTxt, int colNum){
		QuestionOption option = null;
		Matcher singlePattern1Matcher = singlePattern1.matcher(optionTxt);
		Matcher singleMutiplePattern2Matcher = singleMutiplePattern2.matcher(optionTxt);
		Matcher mutiplePattern3Matcher = mutiplePattern3.matcher(optionTxt);
		Matcher wrongFormatPatternMatcher = wrongFormatPattern.matcher(optionTxt);
		
		if(singlePattern1Matcher.find()){
			String questionNum = singlePattern1Matcher.group(1);
			String questionContent = singlePattern1Matcher.group(2);
			String optionContent = singlePattern1Matcher.group(0);
			option = newQuestionOption("1", new Integer(questionNum).intValue(), optionContent, questionContent, colNum);
		}else if(singleMutiplePattern2Matcher.find()){
			String optionNumber = singleMutiplePattern2Matcher.group(1);
			String questionNum = singleMutiplePattern2Matcher.group(2);
			String questionContent = singleMutiplePattern2Matcher.group(3);
			String optionContent = singleMutiplePattern2Matcher.group(0);
			option = newQuestionOption(optionNumber, new Integer(questionNum).intValue(), optionContent, questionContent, colNum);
		}else if(mutiplePattern3Matcher.find()){
			String optionNumber1 = mutiplePattern3Matcher.group(1);
			String optionNumber2 = mutiplePattern3Matcher.group(2);
			String questionNum = mutiplePattern3Matcher.group(3);
			String questionContent = mutiplePattern3Matcher.group(4);
			String optionContent = mutiplePattern3Matcher.group(0);
			option = newQuestionOption(optionNumber1 + optionNumber2, new Integer(questionNum).intValue(), optionContent, questionContent, colNum);
		}else if(wrongFormatPatternMatcher.find()){
			String questionContent = wrongFormatPatternMatcher.group(1);
			// set question num to 1000 for the wrong format
			option = newQuestionOption("1", new Integer(1000).intValue(), questionContent, questionContent, colNum);
		}
		
		return option;
	}
	
	private static QuestionOption newQuestionOption(String optionNumber, int questionNumber, String optionContent, String questionContent, int colNum){
		QuestionOption option = new QuestionOption();
		option.setOptionNumber(optionNumber);
		option.setOptionContent(optionContent);
		option.setQuestionNum(questionNumber);
		option.setColNum(colNum);
		option.setQuestionContent(questionContent);
		return option;
	}
	
	public static void main(String[] args){
		String txt = "Q3	请问以下彩种中，您经常购买的彩种有哪些？(多选)";
		Pattern p = Pattern.compile("^[A-Za-z]*(\\d{1,4})(\\S*\\t*\\s*\\S*\\s*\\S*)$");
		Matcher m = p.matcher(txt);
		if(m.find()){
			System.out.println(m.group(0));
			System.out.println(m.group(1));
		}else{
			System.out.println("Not found");
		}
	}
	
}
