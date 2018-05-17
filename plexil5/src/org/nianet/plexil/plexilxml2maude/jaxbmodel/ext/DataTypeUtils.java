package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

public class DataTypeUtils {

	private DataTypeUtils(){}

	public static boolean isNumber(String val){
		try{
			Double.parseDouble(val);
		}
		catch (NumberFormatException e){
			return false;
		}
		return true;
	}
	
}
