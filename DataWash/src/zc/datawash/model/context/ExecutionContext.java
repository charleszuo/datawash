package zc.datawash.model.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import zc.datawash.model.expression.Expression;

public class ExecutionContext {
	private Expression rootExpression = null;
	
	private List<ExecutionContext> contextChain = new ArrayList<ExecutionContext>();

	private Stack<Character> charStack = new Stack<Character>();
	
	private Stack<Expression> currentExpStack = new Stack<Expression>();
	
	private boolean binaryOperatorFlag = false;

	public List<ExecutionContext> getContextChain() {
		return contextChain;
	}

	public Stack<Character> getCharStack() {
		return charStack;
	}

	public void setCharStack(Stack<Character> charStack) {
		this.charStack = charStack;
	}

	public Stack<Expression> getCurrentExpStack() {
		return currentExpStack;
	}

	public Expression getRootExpression() {
		return rootExpression;
	}

	public void setRootExpression(Expression rootExpression) {
		this.rootExpression = rootExpression;
	}

	public boolean getBinaryOperatorFlag() {
		return binaryOperatorFlag;
	}

	public void setBinaryOperatorFlag(boolean binaryOperatorFlag) {
		this.binaryOperatorFlag = binaryOperatorFlag;
	}
	
}
