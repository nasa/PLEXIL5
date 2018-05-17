package org.nianet.plexil.maude2java.mauderuntime;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public abstract class OSShellRuntimeProperties {

	private String commandPath;
	
	public abstract String getAvailableOSShellCommand();
	
	public abstract String getSupportedOSName();

	public String getCommandPath() {
		return commandPath;
	}

	public void setCommandPath(String commandPath) {
		this.commandPath = commandPath;
	}
	
}
