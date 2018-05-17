package org.nianet.plexil.maude2java.modelchecking;

import java.util.Observable;
import java.util.Observer;

import org.nianet.plexil.maude2java.MaudeIOSyncrhonizationMonitor;

public class MaudeModelCheckingErrorObserver implements Observer {

	private String lastError;
	
	
		
	public MaudeModelCheckingErrorObserver() {
		super();
		lastError="";
	}

	@Override
	public void update(Observable o, Object arg) {		
		lastError=arg.toString();		
		
		synchronized (MaudeIOSyncrhonizationMonitor.modelCheckingMonitor) {
			MaudeIOSyncrhonizationMonitor.modelCheckingMonitor.notifyAll();	
		}
		
	}

	public String getLastError() {
		return lastError;
	}

}
