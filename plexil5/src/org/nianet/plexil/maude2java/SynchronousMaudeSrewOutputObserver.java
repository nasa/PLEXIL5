package org.nianet.plexil.maude2java;

import java.util.Observable;
import java.util.Observer;

public class SynchronousMaudeSrewOutputObserver implements Observer {

	public SynchronousMaudeSrewOutputObserver() {
		super();
		lastestOutput=null;
		newOutputAvailable=false;
	}

	private String lastestOutput;
	
	private boolean newOutputAvailable;
		

	public void update(Observable arg0, Object arg1) {
		if (newOutputAvailable){
			throw new RuntimeException("Shell interaction synchronization exception. A shell output wasn't read:"+lastestOutput);
		}
		OutputBlock out=(OutputBlock)arg1;		
		newOutputAvailable=true;
		
		lastestOutput=out.getPackedLines();
		
		//notify runtime engine to complete I/O synchronization process.
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			MaudeIOSyncrhonizationMonitor.monitor.notifyAll();	
		}		
		
	}



	public String getLastestOutput() {
		if (!newOutputAvailable){
			throw new RuntimeException("Shell interaction synchronization exception. The shell output is already read.:"+lastestOutput);
		}
		newOutputAvailable=false;
		return lastestOutput;
	}

	public boolean isNewOutputAvailable() {
		return newOutputAvailable;
	}

	
}
