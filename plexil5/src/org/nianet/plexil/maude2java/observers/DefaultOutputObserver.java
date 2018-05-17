package org.nianet.plexil.maude2java.observers;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class DefaultOutputObserver implements Observer {

	public void update(Observable o, Object arg) {
		System.out.println("**********SDOUT-----");
		System.out.println(arg);
		System.out.println(arg.getClass());
		System.out.println("-----SDOUT");
	}

}
