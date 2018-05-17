package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

/**
 * This class represents a single boolean variable as a boolean expression
 * @author hector
 *
 */
public class BooleanVariable implements BooleanExpression {

	private String varname;
	
	public BooleanVariable(String varname){
		this.varname=varname;
	}
	
	public String getVarname(){
		return varname;
	}
	
	@Override
	public String getExpression(Map<String, String> variablesContext) {
		String absVarName=new NodeVariable(varname).getExpression(variablesContext);
		return "("+absVarName+" equ const(v(true)))";
	}

	
	
}
