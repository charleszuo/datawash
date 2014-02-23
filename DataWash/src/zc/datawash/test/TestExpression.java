package zc.datawash.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import zc.datawash.model.context.ExecutionContext;
import zc.datawash.model.expression.AndExpression;
import zc.datawash.model.expression.BinaryExpression;
import zc.datawash.model.expression.EqualExpression;
import zc.datawash.model.expression.Expression;
import zc.datawash.model.expression.NotEqualExpression;
import zc.datawash.model.expression.OrExpression;
import zc.datawash.model.expression.UnaryExpression;

public class TestExpression {

	public static void main(String[] args) {
		// String expressionText =
		// "Q1 != [1,2] && (Q2 != [1,3] || (Q3 = [1,4] && Q4 = 1) && Q5 = 5)";
		// String expressionText =
		// "Q1 = [1,2] && (Q2 != [1,3] || (Q3 = [1,4] && Q4 = 1)) && Q5 = [5,6]";
		// String expressionText =
		// "(Q1 != [1,2] && Q2 != [1,3] || Q3 = [1,4]) || (Q4 = 1 && Q5 = 5)";
		// String expressionText =
		// "Q1 = [1,3] || Q2 != [3,4] && (Q3 = 5 && Q4 = 6)";
		String expressionText = "Q1 = empty && (Q3 = [O1:3, O2:4] || Q2 = 2) || Q4 = 4";
		Expression expression = parse(expressionText);
		//printExpression(expression);
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		Map<String, String> values1 = new HashMap<String, String>();
		values1.put("O1", "empty");
		Map<String, String> values2 = new HashMap<String, String>();
		values2.put("O1", "2");
		Map<String, String> values3 = new HashMap<String, String>();
		values3.put("O1", "2");
		values3.put("O2", "4");
		values3.put("O3", "5");
		Map<String, String> values4 = new HashMap<String, String>();
		values4.put("O1", "1");
		values4.put("O2", "1");
		values4.put("O3", "2");
		data.put("1", values1);
		data.put("2", values2);
		data.put("3", values3);
		data.put("4", values4);
		System.out.println(match(data, expression));
	}

	public static Expression parse(String expressionText) {

		ExecutionContext executionContext = null;
		Stack<ExecutionContext> contextStack = new Stack<ExecutionContext>();
		Stack<Character> charStack = null;
		Stack<Expression> currentExpStack = null;
		char[] expressionChars = expressionText.trim().toCharArray();

		for (int i = 0; i < expressionChars.length; i++) {
			if (i == 0) {
				executionContext = new ExecutionContext();
				executionContext.getContextChain().add(executionContext);
				contextStack.push(executionContext);
			}

			char currentChar = expressionChars[i];
			if (currentChar == '(') {
				ExecutionContext bracketContext = new ExecutionContext();
				bracketContext.getContextChain().add(bracketContext);
				bracketContext.getContextChain().add(contextStack.peek());
				contextStack.push(bracketContext);
				continue;
			} else if (currentChar == ')') {
				ExecutionContext bracketContext = contextStack.pop();
				if (charStack.size() > 0) {
					Expression expression = currentExpStack.peek();
					List<Character> chars = charStack.subList(0,
							charStack.size());
					List<String> value = new ArrayList<String>();
					value.add(charListToString(chars));
					((UnaryExpression) expression).setValues(value);
					charStack.clear();
				}
				Expression rootOfBracketContext = bracketContext
						.getRootExpression();
				ExecutionContext parentContext = contextStack.peek();
				if (parentContext.getCurrentExpStack().size() == 0) {
					parentContext.getCurrentExpStack().push(
							rootOfBracketContext);
				} else {
					Expression currentExpressionInParenet = parentContext
							.getCurrentExpStack().peek();
					if (currentExpressionInParenet instanceof BinaryExpression) {
						((BinaryExpression) currentExpressionInParenet)
								.setRightExpression(rootOfBracketContext);
						rootOfBracketContext
								.setParentPoint(currentExpressionInParenet);
					}
				}
				continue;
			}

			charStack = contextStack.peek().getCharStack();
			currentExpStack = contextStack.peek().getCurrentExpStack();

			if (currentChar != ' ' && currentChar != '[' && currentChar != ']'
					&& currentChar != '=' && currentChar != '&'
					&& currentChar != '|' && i != expressionChars.length - 1) {
				charStack.push(currentChar);
				continue;
			} else if (currentChar == ' ' || currentChar == '[') {
				continue;
			}

			if (currentChar == '=') {
				char topChar = charStack.peek();
				Expression expression;
				if (topChar == '!') {
					charStack.pop();
					expression = new NotEqualExpression();
				} else {
					expression = new EqualExpression();
				}
				if (currentExpStack.size() > 0) {
					Expression currentExpInStack = currentExpStack.pop();
					if (currentExpInStack instanceof BinaryExpression) {
						// always be a right child
						((BinaryExpression) currentExpInStack)
								.setRightExpression(expression);
						expression.setParentPoint(currentExpInStack);
						// set root pointer
						contextStack.peek()
								.setRootExpression(currentExpInStack);
					}
				} else {
					contextStack.peek().setRootExpression(expression);
				}
				currentExpStack.push(expression);
				List<Character> chars = charStack.subList(0, charStack.size());
				((UnaryExpression) expression).setKey(charListToString(chars));
				charStack.clear();

			} else if (currentChar == ']') {
				Expression expression = currentExpStack.peek();
				List<Character> chars = charStack.subList(0, charStack.size());
				String values = charListToString(chars);
				((UnaryExpression) expression).setValues(Arrays.asList(values
						.split(",")));
				charStack.clear();
			} else if (currentChar == '&' || currentChar == '|') {
				if (charStack.size() > 0) {
					// handle &, like Q1 = 1 && Q2 =2
					contextStack.peek().setBinaryOperatorFlag(true);
					List<Character> chars = charStack.subList(0,
							charStack.size());
					Expression expression = currentExpStack.peek();
					if (expression instanceof UnaryExpression) {
						// single value
						List<String> value = new ArrayList<String>();
						value.add(charListToString(chars));
						((UnaryExpression) expression).setValues(value);
						charStack.clear();
					}

				} else {
					if (contextStack.peek().getBinaryOperatorFlag()) {
						// handle &&
						BinaryExpression expression = null;
						if (currentChar == '&') {
							expression = new AndExpression();
						} else {
							expression = new OrExpression();
						}
						Expression currentExpInStack = currentExpStack.pop();
						Expression leftExpression;
						if (currentExpInStack.getParent() == null) {
							leftExpression = currentExpInStack;
						} else {
							leftExpression = currentExpInStack.getParent();
						}
						expression.setLeftExpression(leftExpression);
						leftExpression.setParentPoint(expression);
						currentExpStack.push(expression);
						// set root pointer
						contextStack.peek().setRootExpression(expression);
						contextStack.peek().setBinaryOperatorFlag(false);
					} else {
						// charStack = 0 means last char should be ']'
						contextStack.peek().setBinaryOperatorFlag(true);
						continue;
					}
				}
			} else if (i == expressionChars.length - 1) {
				// like Q1 != [1,2] && (Q2 != [1,3] || (Q3 = [1,4] && Q4 = 1))
				// && Q5 = 5, push the last digit into charStack
				charStack.push(currentChar);
				Expression expression = currentExpStack.pop();
				if (expression instanceof UnaryExpression) {
					List<Character> chars = charStack.subList(0,
							charStack.size());
					String values = charListToString(chars);
					((UnaryExpression) expression).setValues(Arrays
							.asList(values.split(",")));
				}
			}

		}

		return contextStack.peek().getRootExpression();
	}

	public static boolean match(Map<String, Map<String, String>> data,
			Expression expression) {
		if (expression instanceof EqualExpression) {
			String key = ((EqualExpression) expression).getKey().substring(1);
			Map<String, String> valuesInMap = data.get(key);
			List<String> expressionValues = ((EqualExpression) expression)
					.getValues();
			boolean equalFlag = true;
			if (expressionValues.size() == 1) {
				// only have one value, like Q1 = 1
				expressionValues.set(0, "O1:" + expressionValues.get(0));
			}
			// have multiple values, like Q1 = [O1:1, O2:1]
			for (String expressionValue : expressionValues) {
				String[] optionValues = expressionValue.split(":");
				if (!valuesInMap.containsKey(optionValues[0].toUpperCase())) {
					equalFlag = false;
					break;
				} else {
					String optionValue = optionValues.length > 1 ? optionValues[1]
							.trim() : optionValues[0].trim();
					String valueInMap = valuesInMap.get(optionValues[0].toUpperCase());
					if (!valueInMap.equals(optionValue)) {
						equalFlag = false;
						break;
					}
				}
			}

			return equalFlag;
		} else if (expression instanceof NotEqualExpression) {
			String key = ((NotEqualExpression) expression).getKey()
					.substring(1);
			Map<String, String> valuesInMap = data.get(key);
			List<String> expressionValues = ((NotEqualExpression) expression)
					.getValues();
			boolean notEqualFlag = true;
			if (expressionValues.size() == 1
					&& !expressionValues.get(0).contains(":")) {
				// only have one value, like Q1 = 1
				expressionValues.set(0, "O1:" + expressionValues.get(0));
			}

			for (String expressionValue : expressionValues) {
				String[] optionValues = expressionValue.split(":");
				if (valuesInMap.containsKey(optionValues[0].toUpperCase())) {
					String optionValue = optionValues.length > 1 ? optionValues[1]
							.trim() : optionValues[0].trim();
					String valueInMap = valuesInMap.get(optionValues[0].toUpperCase());
					if (valueInMap.equals(optionValue)) {
						notEqualFlag = false;
						break;
					}
				}
			}

			return notEqualFlag;
		} else if (expression instanceof AndExpression) {
			if (match(data, ((BinaryExpression) expression).getLeftExpression())
					&& match(data,
							((BinaryExpression) expression)
									.getRightExpression())) {
				return true;
			} else {
				return false;
			}
		} else if (expression instanceof OrExpression) {
			if (match(data, ((BinaryExpression) expression).getLeftExpression())
					|| match(data,
							((BinaryExpression) expression)
									.getRightExpression())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private static String charListToString(List<Character> chars) {
		StringBuilder sb = new StringBuilder();
		for (char character : chars) {
			sb.append(character);
		}
		return sb.toString();
	}

	private static void printExpression(Expression expression) {
		if (expression instanceof UnaryExpression) {
			System.out.println(((UnaryExpression) expression).getKey() + " "
					+ ((UnaryExpression) expression).getOperator() + " "
					+ ((UnaryExpression) expression).getValues());
		} else if (expression instanceof BinaryExpression) {
			System.out.println(expression.getOperator());
			Expression leftExpression = ((BinaryExpression) expression)
					.getLeftExpression();
			System.out.println("left pointer");
			printExpression(leftExpression);
			Expression rightExpression = ((BinaryExpression) expression)
					.getRightExpression();
			System.out.println("right pointer");
			printExpression(rightExpression);
		}
	}
}
