/**
 * 
 */
package org.nianet.plexil;

import java.util.ArrayList;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class ActionSet {
	private ArrayList<String> actions;
	
	public ActionSet(){
		actions = new ArrayList<String>();
	}
	
	public int size(){
		return actions.size();
	}
	
	public void add(String a){
		actions.add(a);
	}
	
	public String get(int i){
		return actions.get(i);
	}
	
	public void remove(int i){
		actions.remove(i);
	}
}
