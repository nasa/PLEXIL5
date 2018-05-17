package org.nianet.plexil.maude2java.observers;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class Observer2 implements Observer {

	public void update(Observable o, Object arg) {
		System.out.println("XXXXXXXXXXXXSDOUT-----");
		
			System.out.println(arg.toString().toUpperCase());
		System.out.println("XXXXXXXXXXXXXSDOUT");

	}

}
