package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.LookupNow;

public class NumericalLookupNow implements ArithmeticExpression {

	LookupNow lookup;
	
	public NumericalLookupNow(LookupNow lookup) {
		super();
		this.lookup = lookup;
	}

	@Override
	public String getExpression(Map<String, String> variablesContext) {
		return lookup.getExpression(variablesContext);
	}

}
