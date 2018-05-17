package org.nianet.plexil.luvintegration.remotecommands;

import java.rmi.RemoteException;

public class RemoteStepServiceImpl implements RemoteStepService {

	@Override
	public void doMicroStep() throws RemoteException {
		
		synchronized (RMICommandMonitor.monitor) {
			RMICommandMonitor.monitor.setRequest(RMIRequestType.MICRO);
			RMICommandMonitor.monitor.notifyAll();
		}
		
	}

	@Override
	public void doMacroStep() throws RemoteException {
		synchronized (RMICommandMonitor.monitor) {
			RMICommandMonitor.monitor.setRequest(RMIRequestType.MACRO);
			RMICommandMonitor.monitor.notifyAll();
		}
		
	}

	@Override
	public void doQuiescenceStep() throws RemoteException {
		synchronized (RMICommandMonitor.monitor) {
			RMICommandMonitor.monitor.setRequest(RMIRequestType.QUIESCENCE);
			RMICommandMonitor.monitor.notifyAll();
		}
		
	}

	@Override
	public void doExecuteStep() throws RemoteException {
		synchronized (RMICommandMonitor.monitor) {
			RMICommandMonitor.monitor.setRequest(RMIRequestType.EXECUTE);
			RMICommandMonitor.monitor.notifyAll();
		}
		
	}

	
	
}
