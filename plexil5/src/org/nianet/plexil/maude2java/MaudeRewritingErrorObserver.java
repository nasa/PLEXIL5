package org.nianet.plexil.maude2java;

import java.util.Observable;
import java.util.Observer;

import org.nianet.plexil.maude2java.MaudeIOSyncrhonizationMonitor;

public class MaudeRewritingErrorObserver implements Observer {

	private String lastError;
	
		
	public MaudeRewritingErrorObserver() {
		super();
		lastError="";
	}

	@Override
	public void update(Observable o, Object arg) {		
		lastError=arg.toString();
		
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			MaudeIOSyncrhonizationMonitor.monitor.notifyAll();	
		}
		
	}

	public String getLastError() {
		return lastError;
	}

}
