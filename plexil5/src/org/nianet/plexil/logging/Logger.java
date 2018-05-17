package org.nianet.plexil.logging;


public interface Logger {

	public abstract void debugLog(String msg);
	public abstract void errorLog(String msg);
	public abstract void warningLog(String msg);
	public abstract void infoLog(String msg);
	public abstract void fatalLog(String msg);
	
}
