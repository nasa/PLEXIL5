package org.nianet.plexil.maude2java.observers;

import java.util.Observable;
import java.util.Observer;

public class DefaultErrorObserver implements Observer {

	public void update(Observable arg0, Object arg1) {
		System.err.println("ERROR:{");
		System.err.println(arg1);
		System.err.println("}");

	}

}
