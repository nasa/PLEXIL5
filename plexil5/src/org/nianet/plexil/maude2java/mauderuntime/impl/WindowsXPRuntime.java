package org.nianet.plexil.maude2java.mauderuntime.impl;

import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimeProperties;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class WindowsXPRuntime extends OSShellRuntimeProperties{

	@Override
	public String getAvailableOSShellCommand() {
		return "cmd /c";
	}

	@Override
	public String getSupportedOSName() {
		return "Windows XP";
	}

	
}
