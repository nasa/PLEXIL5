package org.nianet.plexil.luvintegration.remotecommands;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteStepService extends Remote {

	public void doMicroStep() throws RemoteException;

	public void doMacroStep() throws RemoteException;
	
	public void doQuiescenceStep() throws RemoteException;
	
	public void doExecuteStep() throws RemoteException;
	
	
}
