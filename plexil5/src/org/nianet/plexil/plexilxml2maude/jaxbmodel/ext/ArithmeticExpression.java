package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

/**
 * 
 * @author hcadavid
 *
 */
public interface ArithmeticExpression {

	public String getExpression(Map<String,String> variablesContext);
	
}
