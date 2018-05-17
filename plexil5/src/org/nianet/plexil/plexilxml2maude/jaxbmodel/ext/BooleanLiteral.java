package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

public class BooleanLiteral implements BooleanExpression {

	private Boolean value;
	
	public Boolean getValue(){
		return value;
	}
	
	public BooleanLiteral(Boolean value) {
		super();
		this.value = value;
	}

	public BooleanLiteral(String value) {
		super();
		if (value.trim().toLowerCase().equals("true") || value.trim().toLowerCase().equals("false")){
			this.value = Boolean.parseBoolean(value.trim());	
		}
		else{
			throw new RuntimeException("Invalid boolean literal:"+value);
		}
		
	}
	

	@Override
	public String getExpression(Map<String, String> variablesContext) {
		return "const(v("+value.toString()+"))";
	}

}
