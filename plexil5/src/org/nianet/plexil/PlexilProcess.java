/**
 * 
 */

package org.nianet.plexil;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * @author Camilo Rocha
 * @version 0.1
 */
public class PlexilProcess {
	
	/**
	 * Process' name
	 */
	private final String name;
	
	/**
	 * Type of process
	 */
	private final ProcessType type;
	
	/**
	 * Attributes
	 */
	private Hashtable<String,String> attributes;
	
	/**
	 * 
	 * @param type Type of the process
	 */
	public PlexilProcess( String name, ProcessType type ){
		this.name = name;
		this.type = type;
		attributes = new Hashtable<String,String>();
	}
	
	public void setAttribute( String key, String value){
		attributes.put(key, value);
	}
	
	public String getAttribute( String key ){
		return attributes.get(key);
	}
	
	public Enumeration<String> keys(){
		return attributes.keys();
	}

	public ProcessType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public int attributeSize() {
		return attributes.size();
	}
}
