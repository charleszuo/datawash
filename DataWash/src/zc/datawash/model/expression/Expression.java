package zc.datawash.model.expression;

public abstract class Expression {

	protected Expression parent;
	
	protected String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Expression getParent() {
		return parent;
	}

	public void setParentPoint(Expression parent) {
		this.parent = parent;
	}

}
