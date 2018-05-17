package org.nianet.plexil.maude2java;

import java.util.Observable;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class OutputMonitor extends Observable{

	public void newOutput(Object o){
		this.setChanged();
		this.notifyObservers(o);
	}
}