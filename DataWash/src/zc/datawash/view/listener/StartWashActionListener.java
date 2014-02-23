package zc.datawash.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

import zc.datawash.view.MainFrame;

public class StartWashActionListener implements ActionListener {
	private MainFrame frame;

	public StartWashActionListener(MainFrame frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		frame.cleanDisplayedMessage();
		try {
			boolean step1Checked = false;
			boolean step2Checked = false;
			boolean step31Checked = false;
			boolean step32Checked = false;
			boolean step4Checked = false;
			boolean step5Checked = true;
			boolean step6Checked = true;

			// clear DataWasher
			frame.getDataWasher().clear();

			// validate step2 remove by startTime
			String startTimeInput = frame.getStartTimeTextBox().getText()
					.trim();
			Date startDate = null;
			if (!"".equals(startTimeInput)) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				try {
					startDate = sdf.parse(startTimeInput);
					step2Checked = true;
				} catch (ParseException e1) {
					frame.logErrorMessage("��������ȷ�����ڸ�ʽ!");
					return;
				}
			}

			String digitReg = "^\\d+$";
			// validate step3
			String shortSpanValue = frame.getStep3ShortSpanTextBox().getText()
					.trim();
			String longSpanValue = frame.getStep3LongSpanTextBox().getText()
					.trim();
			int shortSpan = 0;
			int longSpan = 0;
			if (!"".equals(shortSpanValue)) {
				if (!shortSpanValue.matches(digitReg)) {
					frame.logErrorMessage("������ʱ�����Ϊ����!");
					return;
				} else {
					step31Checked = true;
					shortSpan = new Integer(shortSpanValue).intValue();
				}
			}

			if (!"".equals(longSpanValue)) {
				if (!longSpanValue.matches(digitReg)) {
					frame.logErrorMessage("����ʱ�����Ϊ����!");
					return;
				} else {
					step32Checked = true;
					longSpan = new Integer(longSpanValue).intValue();
				}
			}

			if (step31Checked && step32Checked) {
				if (shortSpan > longSpan) {
					frame.logErrorMessage("������ʱ�䲻�ܴ�������ʱ��!");
					return;
				}
			}

			// validate step4
			String keyQuestionReg = "^(Q|q)\\d+\\s*(,\\s*(Q|q)\\d+\\s*)*$";
			String keyQuestionInputExpression = frame
					.getStep4KeyQuestionTextBox().getText().trim();

			if (!"".equals(keyQuestionInputExpression)) {
				if (!keyQuestionInputExpression.matches(keyQuestionReg)) {
					frame.logErrorMessage("ȥ���ؼ�����ȱʧ���ʽ�����Ϲ淶!");
					return;
				} else {
					step4Checked = true;
				}
			}

			// validate step5
			String expressionReg = "^\\(*(Q|q)\\d+\\s*!?=\\s*(\\w+|\\[\\s*(O|o)\\d+\\s*:\\s*\\w+\\s*(,\\s*(O|o)\\d+\\s*:\\s*\\w+\\s*)*\\])\\s*\\)*\\s*((&&|\\|\\|)\\s*\\(*\\s*(Q|q)\\d+\\s*!?=\\s*(\\w+|\\[\\s*(O|o)\\d+\\s*:\\s*\\w+\\s*(,\\s*(O|o)\\d+\\s*:\\s*\\w+\\s*)*\\])\\s*\\)*\\s*)*$";
			String notTargetUserExpressionInput = frame
					.getStep5NotTargetUserTextBox().getText().trim();
			String[] notTargetUserExpressions = notTargetUserExpressionInput
					.split("\n");
			if (!"".equals(notTargetUserExpressionInput)) {
				for (String expression : notTargetUserExpressions) {
					if (!"".equals(expression)) {
						if (!expression.matches(expressionReg)
								|| !validateExpressionWithBracket(expression)) {
							step5Checked = false;
							frame.logErrorMessage("ȥ����Ŀ���û����ʽ�����Ϲ淶!");
							return;
						}
					}
				}
			}

			// validate step6
			String logicConflictExpressionInput = frame
					.getStep6LogicConflictTextBox().getText().trim();
			String[] logicConflictExpressions = logicConflictExpressionInput
					.split("\n");
			if (!"".equals(logicConflictExpressionInput)) {
				for (String expression : logicConflictExpressions) {
					if (!"".equals(expression)) {
						if (!expression.matches(expressionReg)
								|| !validateExpressionWithBracket(expression)) {
							step6Checked = false;
							frame.logErrorMessage("ȥ���߼�ì�ܱ��ʽ�����Ϲ淶!");
							return;
						}
					}
				}
			}

			// validate step1
			String questionNumReg = "^(Q|q)\\d+\\s*$";
			String idQuestionNumTemp = frame.getStep1IdTextBox().getText();
			if (!"".equals(idQuestionNumTemp)) {
				if (!idQuestionNumTemp.matches(questionNumReg)) {
					frame.logErrorMessage("ȥ�����ݵ�������ű���ΪQ+����!");
					return;
				} else {
					step1Checked = true;
				}
			}

			// step2
			if (step2Checked) {
				frame.getDataWasher().removeByStartTime(startTimeInput);
				frame.logWashMessage(new StringBuilder()
						.append("��һ��ȥ����")
						.append(frame.getDataWasher()
								.getRemovedStartTimeRecordCount())
						.append("����¼").toString());
			}
			// step3
			if (step31Checked || step32Checked) {
				frame.getDataWasher().removeByAnswerTimespan(shortSpan,
						longSpan);
				frame.logWashMessage(new StringBuilder()
						.append("�ڶ���ȥ����")
						.append(frame.getDataWasher()
								.getRemovedTimespanRecordCount()).append("����¼")
						.toString());
			}
			// step4
			if (step4Checked) {
				String[] questions = keyQuestionInputExpression.split(",");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < questions.length; i++) {
					sb.append(questions[i]).append(" = [O1:empty]");
					if (i != questions.length - 1) {
						sb.append(" || ");
					}
				}
				String keyQuestionExpression = sb.toString();
				frame.getDataWasher().removeByKeyQuestionMiss(
						keyQuestionExpression, keyQuestionInputExpression);
				frame.logWashMessage(new StringBuilder()
						.append("������ȥ����")
						.append(frame.getDataWasher()
								.getRemoedKeyQuestionRecordCount())
						.append("����¼").toString());
			}
			// step5
			if (!"".equals(notTargetUserExpressionInput) && step5Checked) {
				for (String expression : notTargetUserExpressions) {
					if (!"".equals(expression)) {
						frame.getDataWasher().removeByNotTargetUser(expression);
					}
				}
				frame.logWashMessage(new StringBuilder()
						.append("���Ĳ�ȥ����")
						.append(frame.getDataWasher()
								.getRemovedNotTargetUserRecordCount())
						.append("����¼").toString());
			}
			// step6
			if (!"".equals(logicConflictExpressionInput) && step6Checked) {
				for (String expression : logicConflictExpressions) {
					if (!"".equals(expression)) {
						frame.getDataWasher().removeByLogicConflict(expression);
					}
				}
				frame.logWashMessage(new StringBuilder()
						.append("���岽ȥ����")
						.append(frame.getDataWasher()
								.getRemovedLogicConflictRecordCount())
						.append("����¼").toString());
			}

			// step1 remove duplicate record
			if (step1Checked) {
				String idQuestionNum = idQuestionNumTemp.trim().substring(1);
				frame.getDataWasher()
						.removeDuplidateByWangwangId(idQuestionNum);
				frame.logWashMessage(new StringBuilder()
						.append("������ȥ����")
						.append(frame.getDataWasher()
								.getRemovedDuplicateRecordCount())
						.append("����¼").toString());
			}
			// update screen
			frame.displayWashedRecordCount();
			frame.enableExportButton(true);
		} catch (Exception ex) {
			frame.logErrorMessage("Sorry, �������ڲ�����!");
			return;
		}
	}

	private boolean validateExpressionWithBracket(String expression) {
		if (expression.contains("(") || expression.contains(")")) {
			Stack<Character> bracketStack = new Stack<Character>();
			char[] chars = expression.toCharArray();
			for (char c : chars) {
				if (c == '(') {
					bracketStack.push(c);
				} else if (c == ')') {
					if (bracketStack.size() == 0) {
						return false;
					} else {
						bracketStack.pop();
					}
				}
			}
			if (bracketStack.size() > 0) {
				return false;
			}
		}
		return true;
	}

}
