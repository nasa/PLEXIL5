package org.nianet.plexil.luvintegration.remotecommands;

public class RMICommandMonitor {

	private RMICommandMonitor(){};
	
	public static final RMICommandMonitor monitor=new RMICommandMonitor();
	
	private RMIRequestType lastRequest=null;
	
	public void setRequest(RMIRequestType rt){
		lastRequest=rt;
	}

	public RMIRequestType getLastRequest() {
		return lastRequest;
	}
	
}
