/**
 * 
 */
package org.nianet.plexil;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class Context {

	private LinkedHashMap<String,Variable> variables;
	
	public Context(){
		variables = new LinkedHashMap<String,Variable>();
	}
	
	public void put( Variable p ){
		variables.put(p.getName(), p);
	}
	
	public Variable get( String name ){
		return variables.get( name );
	}
	
	public Set<String> keys(){
		return variables.keySet();
	}
	
	public void remove( Variable p ){
		variables.remove( p.getName() );
	}
}
