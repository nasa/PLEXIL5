package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

public class NumericalLiteral implements ArithmeticExpression{
	
	private String stringValue;
	
	public String getValue(){
		return stringValue;
	}

	public NumericalLiteral(String stringValue) {
		super();
		this.stringValue = stringValue;
	}

	@Override
	public String getExpression(Map<String,String> variablesContext) {
		return "const(v("+stringValue+"))";
	}
	

}
