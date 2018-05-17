package org.nianet.plexil.maude2java.mauderuntime;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.nianet.plexil.maude2java.mauderuntime.impl.LinuxRuntime;
import org.nianet.plexil.maude2java.mauderuntime.impl.MacOSRuntime;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class OSShellRuntimePropertiesFactory {


	private static final String MAUDE_EXECUTABLE = "MAUDE_EXECUTABLE";

	private Hashtable<String, OSShellRuntimeProperties> classMap;
	
	private static OSShellRuntimePropertiesFactory instance=null;
	
	private static String userSelectedExecutable=null;
	

	public static String getCurrentMaudeProperties() throws MaudeRuntimeConfigurationException{
		if (userSelectedExecutable==null){
			String mePath=System.getenv(MAUDE_EXECUTABLE);
			if (mePath!=null){
				return mePath;
			}
			else{
				return "Undefined environment variable:MAUDE_EXECUTABLE.";
			}			
		}
		else{
			return userSelectedExecutable;
		}
	}
	
	
	private OSShellRuntimePropertiesFactory() throws MaudeRuntimeConfigurationException{		
		
		classMap=new Hashtable<String, OSShellRuntimeProperties>();
		
		//add supported OS
		classMap.put(new LinuxRuntime().getSupportedOSName(), new LinuxRuntime());
		classMap.put(new MacOSRuntime().getSupportedOSName(),new MacOSRuntime());
		
		
	}
	
	public static OSShellRuntimePropertiesFactory getInstance() throws MaudeRuntimeConfigurationException{
		if (instance==null){
			instance=new OSShellRuntimePropertiesFactory();
		}
		return instance;
		
	}

	
	public static void updateConfigurationPropertiesMaudePath(String newPath) throws IOException{		
		userSelectedExecutable=newPath;
	}
	
	
	public OSShellRuntimeProperties buildOSShellRuntimeProperties() throws MaudeRuntimeConfigurationException{
		
		
		String osName=System.getProperty("os.name");				

		if (!classMap.containsKey(osName)){
			throw new MaudeRuntimeConfigurationException("Operating system :"+osName +" isn't supported yet.");
		}
		OSShellRuntimeProperties runtimeProps=classMap.get(osName);
		
		if (userSelectedExecutable==null){
			if (System.getenv(MAUDE_EXECUTABLE)==null){
				throw new MaudeRuntimeConfigurationException("Missing environment variable:MAUDE_EXECUTABLE");
			}
			if (!new File(System.getenv(MAUDE_EXECUTABLE)).exists()){
				throw new MaudeRuntimeConfigurationException("File defined on MAUDE_EXECUTABLE doesn't exists.");
			}
			runtimeProps.setCommandPath(System.getenv(MAUDE_EXECUTABLE));	
		}
		else{
			runtimeProps.setCommandPath(userSelectedExecutable);
		}
		
		return runtimeProps;
	}
	

	
}