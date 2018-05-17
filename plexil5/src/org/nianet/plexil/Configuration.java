/**
 * 
 */
package org.nianet.plexil;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class Configuration {
	
	/**
	 * Set of processes
	 */
	private LinkedHashSet<PlexilProcess> processes;
	
	public Configuration(){
		processes = new LinkedHashSet<PlexilProcess>();
	}
	
	public boolean add( PlexilProcess p ){
		return processes.add(p);
	}
	
	public boolean contains( PlexilProcess p ){
		return processes.contains(p);
	}
	
	public boolean isEmpty( PlexilProcess p ){
		return processes.isEmpty();
	}
	
	public Iterator<PlexilProcess> iterator(){
		return processes.iterator();
	}
	
}

