package org.nianet.plexil.logging;

public class LoggerBuilder {

	private static Logger instance=new StdoutLogger();
	
	private LoggerBuilder(){};
	
	public static Logger getLogger(){
		return instance;
	}
	
}
