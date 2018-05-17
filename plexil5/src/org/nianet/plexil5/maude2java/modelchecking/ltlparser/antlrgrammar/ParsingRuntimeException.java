package org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar;

import org.antlr.runtime.RecognitionException;

public class ParsingRuntimeException extends RuntimeException {

	private String[] tokenNames;
	RecognitionException cause;
	
	
	
	
	public ParsingRuntimeException(String[] tokenNames,
			RecognitionException cause) {
		super();
		this.tokenNames = tokenNames;
		this.cause = cause;
	}




	public String[] getTokenNames() {
		return tokenNames;
	}




	public RecognitionException getCause() {
		return cause;
	}


}
