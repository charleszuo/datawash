package zc.datawash.model.expression;

import java.util.List;

public abstract class UnaryExpression extends Expression{
	protected String key;
	
	protected List<String> values;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
}
