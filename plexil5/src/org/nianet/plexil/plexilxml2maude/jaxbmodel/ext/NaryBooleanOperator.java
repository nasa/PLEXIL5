package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;


/**
 * Abstraction for AND, OR, XOR
 * @author Hector Fabio Cadavid Rengifo
 * hector.cadavid@escuelaing.edu.co
 *
 */
public abstract class NaryBooleanOperator implements BooleanExpression {

	public List<BooleanExpression> getOperands(){		
		List<BooleanExpression> operands=new LinkedList<BooleanExpression>();
		
		for (Object o:getBooleanExpression()){
			if (o instanceof BooleanExpression){
				operands.add((BooleanExpression)o);
			}
			else if (o instanceof JAXBElement){
				//allow boolean literals
				Object value=((JAXBElement)o).getValue();
				if (value instanceof String){
					String strval=(String)((JAXBElement)o).getValue();
					if (strval.equals("true") || strval.equals("false")){
						operands.add(new BooleanLiteral(Boolean.parseBoolean(strval)));
					}
					else{
						operands.add(new BooleanVariable(strval));
					}
				}
				else if (value instanceof Boolean){
					operands.add(new BooleanLiteral(((Boolean)value).booleanValue()));
				}
				else{
					throw new RuntimeException("Datatype used with the boolean binary operator isn't supported:"+((JAXBElement)o).getValue().getClass());
				}					

			}
			else{
				throw new RuntimeException("Datatype used with the boolean binary operator isn't supported:"+o.getClass());
			}	
		}
		return operands;
		
	}
	

	public abstract List<Object> getBooleanExpression();
	
}
