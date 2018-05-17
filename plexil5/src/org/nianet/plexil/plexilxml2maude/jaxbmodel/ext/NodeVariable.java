package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

public class NodeVariable implements ArithmeticExpression {

	private String stringValue;
	
	public String getValue(){
		return stringValue;
	}

	public NodeVariable(String stringValue) {
		this.stringValue = stringValue;
	}
	
	@Override
	public String getExpression(Map<String,String> variablesContext) {
		String absVarName=variablesContext.get(stringValue);
		if (absVarName!=null){
			return "var(("+absVarName+"))"; 
		}
		else{
			throw new RuntimeException("The variable "+stringValue+" doesn't exists on node's context.");
		}
	}

	/**
	 * Returns variable expression without 'var' function
	 * @param variablesContext
	 * @return
	 */
	public String getNoVarExpression(Map<String,String> variablesContext){
		String absVarName=variablesContext.get(stringValue);
		if (absVarName!=null){
			return "("+absVarName+")"; 
		}
		else{
			throw new RuntimeException("The variable "+stringValue+" doesn't exists on node's context.");
		}
		
	}
}
