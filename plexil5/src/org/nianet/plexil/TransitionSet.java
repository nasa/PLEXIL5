/**
 * 
 */
package org.nianet.plexil;

import java.util.ArrayList;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class TransitionSet {
	private ArrayList<String> transitions;
	
	public TransitionSet(){
		transitions = new ArrayList<String>();
	}
	
	public int size(){
		return transitions.size();
	}
	
	public void add(String a){
		transitions.add(a);
	}
	
	public String get(int i){
		return transitions.get(i);
	}
	
	public void remove(int i){
		transitions.remove(i);
	}
  
  public String toString() {
    String s="{";
    boolean first = true;
    for (String t:transitions) {
      s += (first?"":", ") + t;
      first = false;
    }
    return s+"}";
 }
}
