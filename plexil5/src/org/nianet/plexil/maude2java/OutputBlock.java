package org.nianet.plexil.maude2java;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class OutputBlock {

	private StringBuffer sb;
	int numLines=0;
	
	public OutputBlock(){
		sb=new StringBuffer();
	}
	
	public int countLines(){
		return numLines;
	}
	
	public void addLine(String l){
		numLines++;
		sb.append(l);
		sb.append("\n");
	}
	
	public String getPackedLines(){
		return sb.toString();
	}
		
	public String toString(){
		return getPackedLines();
	}
	
	
}