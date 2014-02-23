package zc.datawash.model.expression;

import java.util.ArrayList;

public class NotEqualExpression extends UnaryExpression{
	public NotEqualExpression(){
		this.operator = "NOTEQUAL";
		this.values = new ArrayList<String>();
	}
}
