package org.nianet.plexil.maude2java;

public class OutputDescription{
	
	String output;
	
	boolean hasLineFeed;

	public OutputDescription(String output, boolean hasLineFeed) {
		super();
		this.output = output;
		this.hasLineFeed = hasLineFeed;
	}

	public String getOutput() {
		return output;
	}

	public boolean hasLineFeed() {
		return hasLineFeed;
	}
	
	
	
}
