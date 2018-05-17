/**
 * 
 */
package org.nianet.plexil;

/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class Variable {

	private final String name;
	
	private String value;
	
	public Variable( String name, String value ){
		this.name = name;
		this.setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
