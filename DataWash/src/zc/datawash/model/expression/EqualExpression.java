package zc.datawash.model.expression;

import java.util.ArrayList;

public class EqualExpression extends UnaryExpression{
	public EqualExpression(){
		this.operator = "EQUAL";
		this.values = new ArrayList<String>();
	}
}
