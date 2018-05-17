package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.EQInternal;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.EQNumeric;

public class PlexilXMLConditionsPrettyPrinterGenerator {

//    <xsd:element ref="OR"/>
//    <xsd:element ref="XOR"/>
//    <xsd:element ref="AND"/>
//    <xsd:element ref="NOT"/>
//    <xsd:element ref="IsKnown"/>
//    <xsd:element ref="GT"/>
//    <xsd:element ref="GE"/>
//    <xsd:element ref="LT"/>
//    <xsd:element ref="LE"/>
//    <xsd:element ref="EQBoolean"/>
//    <xsd:element ref="EQNumeric"/>
//    <xsd:element ref="EQInternal"/>
//    <xsd:element ref="EQString"/>
//    <xsd:element ref="NEBoolean"/>
//    <xsd:element ref="NENumeric"/>
//    <xsd:element ref="NEInternal"/>
//    <xsd:element ref="NEString"/>
//    <xsd:element ref="BooleanVariable"/>
//    <xsd:element ref="BooleanValue"/>
//    <xsd:group ref="Lookup"/>
//    <xsd:element ref="ArrayElement"/>
	
	
	private PlexilXMLConditionsPrettyPrinterGenerator(){}
	
	public static String getBooleanExpressionPrettyPrinter(PlexilNodeCondition e, Map<String,String> variablesContext){
		BooleanExpression be=e.getCondition();
		
		if (be!=null){
			return be.getExpression(variablesContext);
		}
		else{
			throw new RuntimeException(e.getClass()+" boolean expression not supported yet.");
		}
	}
//
//	public static String getEQBooleanPrettyPrinter(EQNumeric e){
//		throw new RuntimeException("EQNumeric not supported yet");
//	}
//
//	public static String getEQNumericPrettyPrinter(EQNumeric e){
//		throw new RuntimeException("EQNumeric not supported yet");
//	}
	
	
}
