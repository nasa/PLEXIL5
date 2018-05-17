package org.nianet.plexil.configuration;

import java.util.Properties;

public class PlexilPropertiesLoader {

	private PlexilPropertiesLoader(){}
	
	private static Properties properties;
	
	public static Properties getPlexilProperties(){
		if (properties!=null){
			return properties;
		}
		else{
			properties=new Properties();
			properties.put("listNodeImplicitEndCondition", "true");
			return properties;
		}		
	}
	
	
}
