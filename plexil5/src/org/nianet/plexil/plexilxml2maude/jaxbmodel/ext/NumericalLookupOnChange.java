package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.LookupOnChange;


/**
 * ArithmeticExpression adapter to allow lookup with numerical return types.
 * @author hcadavid
 *
 */
public class NumericalLookupOnChange implements ArithmeticExpression {

	LookupOnChange lookup;
	
	
	
	public NumericalLookupOnChange(LookupOnChange lookup) {
		super();
		this.lookup = lookup;
	}



	@Override
	public String getExpression(Map<String, String> variablesContext) {
		return lookup.getExpression(variablesContext);
	}

}
