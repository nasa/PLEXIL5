/**
 * 
 */
package org.nianet.plexil;

import java.util.ArrayList;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class Trace {
	private ArrayList<TransitionSet> trace;
	
	public Trace(){
		trace = new ArrayList<TransitionSet>();
	}
	
	public boolean isEmpty(){
		return trace.isEmpty();
	}
	
	public void add( TransitionSet c ){
		trace.add(c);
	}
	
	public TransitionSet get(int i){
		return trace.get(i);
	}
	
	public int size(){
		return trace.size();
	}
	
	public void remove(int i){
		trace.remove(i);
	}
  
  public String toString() {
    String s="";
    boolean first = true;
    for (TransitionSet set:trace) {
      s += (first?"":"\n <- ") + set;
      first = false;
    }
    return s;
  }
}
