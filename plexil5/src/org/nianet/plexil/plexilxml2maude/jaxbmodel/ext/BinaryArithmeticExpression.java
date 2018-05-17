package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.LookupOnChange;

/**
 * 
 * @author hcadavid
 *
 */
public abstract class BinaryArithmeticExpression implements ArithmeticExpression {

	@SuppressWarnings("unchecked")
	public ArithmeticExpression getLeftExpression(){
		Object o=getNumericExpression().get(0);
		if (o instanceof ArithmeticExpression){
			return (ArithmeticExpression)o;
		}
		else if (o instanceof JAXBElement){
			String exp=((JAXBElement)o).getValue().toString();
			if (isNumber(exp)){
				return new NumericalLiteral(exp);	
			}
			else{
				return new NodeVariable(exp);
			}
		}
		else if (o instanceof LookupOnChange){
			return new NumericalLookupOnChange((LookupOnChange)o);
		}
		else{
			throw new RuntimeException("Datatype not supported for binary arithmetic operator:"+o.getClass());
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArithmeticExpression getRighExpression(){
		Object o=getNumericExpression().get(1);
		if (o instanceof ArithmeticExpression){
			return (ArithmeticExpression)o;
		}
		else if (o instanceof JAXBElement){
			String exp=((JAXBElement)o).getValue().toString();
			if (isNumber(exp)){
				return new NumericalLiteral(exp);	
			}
			else{
				return new NodeVariable(exp);
			}
		}
		else if (o instanceof LookupOnChange){
			return new NumericalLookupOnChange((LookupOnChange)o);
		}
		else{
			throw new RuntimeException("Datatype not supported for binary arithmetic operator:"+o.getClass());
		}
	}
	
	public abstract List<Object> getNumericExpression();
	
	 private boolean isNumber(String val){
		 try{
			 Double.parseDouble(val);
		 }
		 catch (NumberFormatException e){
			 return false;
		 }
		 return true;
	 }
	 
	
}
