/**
 * 
 */
package org.nianet.plexil;

import java.util.ArrayList;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class Contexts {
	
	private ArrayList<Context> contexts;
	
	public Contexts(){
		contexts = new ArrayList<Context>();
	}
	
	public boolean isEmpty(){
		return contexts.isEmpty();
	}
	
	public void add( Context c ){
		contexts.add(c);
	}
	
	public Context get(int i){
		return contexts.get(i);
	}
	
	public int size(){
		return contexts.size();
	}
	
	public void remove(int i){
		contexts.remove(i);
	}
}
