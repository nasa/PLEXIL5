package org.nianet.plexil.maude2java.modelchecking.ltlassistant;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

public class UnicodeLogicalSymbolsMap {

	
	/*

LTLAND	:	'$land';
LTLOR	:	'$lor';
LTLNOT	:	'$lnot$';
UNTIL	:	'$luntil$';
RELEASE	:	'$lrelease$';
GLOBALLY:	'$lglobally$';
FINALLY	:	'$lfinally$';
NEXT	:	'$lnext$';
IMPLICATION:	'$limplication$';
EQUIVALENCE:	'$lequivalent$';
//strong implication and equivalence (not integrated yet).
SIMPLICATION:	'$simplication$';
SEQUIVALENCE:	'$sequivalent$';
*/
	
	private static Hashtable<String,Character> symbolsMap;
	private static Hashtable<Character,String> reverseSymbolsMap;
	private static Hashtable<String,String> symbolDescription;
	private static TreeSet<String> symbolNames;
	
	//ltl operators
	public static final String _LTLAND="land";
	public static final String _LTLOR="lor";
	public static final String _LTLNOT="lnot";
	public static final String _LTLUNTIL="luntil";
	public static final String _LTLRELEASE="lrelease";
	public static final String _LTLGLOBALLY="lglobally";
	public static final String _LTLFINALLY="lfinally";
	public static final String _LTLNEXT="lnext";
	public static final String _LTLIMPLICATION="limplication";
	public static final String _LTLEQUIVALENT="lequivalent";
	public static final String _LTLTRUE="ltrue";
	public static final String _LTLFALSE="lfalse";

	

	/*
	OR 	: 	'$or$';
	AND 	: 	'$and$';
	EQUALS	:	'$eq$';
	NOTEQUALS :	'$noteq$';
	LT	:	'<';
	LTEQ	:	'<=';
	GT	:	'>';
	GTEQ	:	'>=';
	PLUS	:	'+';
	MINUS	:	'-';
	MULT	:	'*';
	DIV	:	'/';
	NOT	:	'$not$';
		 */

	//plexil operators
	
	/*public static final String _OR="or";
	public static final String _AND="and";*/
	//public static final String _EQUALS="equ";
	public static final String _NOTEQUALS="nequ";
	public static final String _NOT="not";
	public static final String _TRUE="true";
	public static final String _FALSE="false";
	
	
	static{
		symbolsMap=new Hashtable<String, Character>();
		reverseSymbolsMap=new Hashtable<Character, String>();
		symbolNames=new TreeSet<String>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
					
		});
		
		
		symbolsMap.put(_LTLNOT, "\u00ac".charAt(0));
		symbolsMap.put(_LTLNEXT, "\u041e".charAt(0));
		symbolsMap.put(_LTLGLOBALLY, "\u25a1".charAt(0));
		symbolsMap.put(_LTLFINALLY, "\u25c7".charAt(0));
		symbolsMap.put(_LTLUNTIL, "\u054d".charAt(0));
		symbolsMap.put(_LTLRELEASE, "\u211b".charAt(0));
		symbolsMap.put(_LTLAND, "\u2227".charAt(0));
		symbolsMap.put(_LTLOR, "\u2228".charAt(0));
		symbolsMap.put(_LTLEQUIVALENT, "\u2261".charAt(0));
		symbolsMap.put(_LTLIMPLICATION, "\u2192".charAt(0));
		symbolsMap.put(_LTLTRUE, "\u22A4".charAt(0));
		symbolsMap.put(_LTLFALSE, "\u22A5".charAt(0));	
		
		//symbolsMap.put(_EQUALS, '=');
		symbolsMap.put(_NOTEQUALS, "\u2260".charAt(0));
		symbolsMap.put(_NOT, "\u007E".charAt(0));
		
		Set<String> ks=symbolsMap.keySet();
		for (String k:ks){
			reverseSymbolsMap.put(symbolsMap.get(k), k);
		}
				
		symbolDescription=new Hashtable<String, String>();
		symbolDescription.put(_LTLNOT, "LTL Not");
		symbolDescription.put(_LTLNEXT, "LTL Next");
		symbolDescription.put(_LTLGLOBALLY, "LTL Always");
		symbolDescription.put(_LTLFINALLY, "LTL Eventually");
		symbolDescription.put(_LTLUNTIL, "LTL Until");
		symbolDescription.put(_LTLRELEASE, "LTL Release");
		symbolDescription.put(_LTLAND, "LTL And");
		symbolDescription.put(_LTLOR, "LTL Or");
		symbolDescription.put(_LTLEQUIVALENT, "LTL Equivalence");
		symbolDescription.put(_LTLIMPLICATION, "LTL Implication");
		symbolDescription.put(_LTLTRUE, "LTL True");
		symbolDescription.put(_LTLFALSE, "LTL False");	
		symbolDescription.put(_NOTEQUALS, "Plexil Not equals");
		symbolDescription.put(_NOT, "Plexil Not");
		
	
		Set<String> snames=symbolsMap.keySet();
		
		for (String sname:snames){
			symbolNames.add(sname);
		}
	}

	static char getSymbol(String name){
		Character rs=symbolsMap.get(name);
		if (rs!=null){
			return rs;
		}
		else{
			//unknown symbol - symbol.
			return "\uFFFD".charAt(0);
		}
		
		
	}
	
	static String getSymbolDescription(String name){
		String d=symbolDescription.get(name);
		return (d!=null?d:"");
	}
	
	
	static Set<String> availableSymbolNames(){
		return symbolNames.descendingSet();
	}

	static Collection<Character> availableSymbols(){
		return symbolsMap.values();
	}
	
	static String getSymbolName(char c){
		return reverseSymbolsMap.get(c);
	}

	
}
