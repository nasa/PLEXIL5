package org.nianet.plexil5.maude2java.modelchecking.ltlparser;

import org.antlr.runtime.RecognitionException;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar.ParsingRuntimeException;

public class MalformedLTLExpressionException extends Exception {

	private String tokenNames[];
	private RecognitionException cause;
	
	
	
	public MalformedLTLExpressionException(String[] tokenNames,
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



	public MalformedLTLExpressionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MalformedLTLExpressionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MalformedLTLExpressionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
