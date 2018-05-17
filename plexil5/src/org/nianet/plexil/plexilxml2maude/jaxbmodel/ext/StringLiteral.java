package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

public class StringLiteral {
	String string;
	
	
	
	public StringLiteral(String string) {
		super();
		this.string = string;
	}



	public String getExpression(Map<String,String> variablesContext) {
		return "const(v(\""+string+"\"))";
	}
	
}
