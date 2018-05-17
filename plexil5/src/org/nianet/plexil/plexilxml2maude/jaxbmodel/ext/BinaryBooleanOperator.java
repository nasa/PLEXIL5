package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.List;

import javax.xml.bind.JAXBElement;

/**
 * Abstraction for AND, OR, XOR
 * @author Hector Fabio Cadavid Rengifo
 * hector.cadavid@escuelaing.edu.co
 *
 */
public abstract class BinaryBooleanOperator implements BooleanExpression{

	@SuppressWarnings("unchecked")
	public BooleanExpression getLeftExpression(){
		Object o=getBooleanExpression().get(0);		
		if (o instanceof BooleanExpression){
			return (BooleanExpression)o;
		}
		else if (o instanceof JAXBElement){
			//allow boolean literals
			String val=((JAXBElement)o).getValue().toString();
			if (val.equals("true") || val.equals("false")){
				return new BooleanLiteral(Boolean.parseBoolean(val));
			}
			else{
				throw new RuntimeException("Datatype used with the boolean binary operator isn't supported:"+o.getClass());
			}
		}
		else{
			throw new RuntimeException("Datatype used with the boolean binary operator isn't supported:"+o.getClass());
		}
	}
	
	@SuppressWarnings("unchecked")
	public BooleanExpression getRightExpression(){
		if (getBooleanExpression().size()<2){
			throw new RuntimeException("Missing right expression on binary boolean expression. Left expression:"+this.getLeftExpression()+".");
		}
		Object o=getBooleanExpression().get(1);		
		if (o instanceof BooleanExpression){
			return (BooleanExpression)o;
		}
		else if (o instanceof JAXBElement){
			//allow boolean literals
			String val=((JAXBElement)o).getValue().toString();
			if (val.equals("true") || val.equals("false")){
				return new BooleanLiteral(Boolean.parseBoolean(val));
			}
			else{
				throw new RuntimeException("Datatype used with the boolean binary operator isn't supported:"+o.getClass());
			}
		}
		else{
			throw new RuntimeException("Datatype used with the boolean binary operator isn't supported:"+o.getClass());
		}
	}
	
    public abstract List<Object> getBooleanExpression();
	
}
