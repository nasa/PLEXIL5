grammar LTLExpression;



options 
{
	output=AST;
	ASTLabelType=CommonTree;
}

tokens
{
	PARAM;
	NEGATE;
	STRING;
}

@header {
	package org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar;
		
}

@lexer::header {package org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar;}

@members {                          

    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        throw new ParsingRuntimeException(tokenNames,e);
    }

}

@lexer::members {
    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        throw new ParsingRuntimeException(tokenNames,e);
    }
}



//tokens with '$' will be shown with special (unicode) characters.
OR 	: 	'or';
AND 	: 	'and';
EQUALS	:	'$equ$';
NOTEQUALS :	'$nequ$';
LT	:	'<';
LTEQ	:	'<=';
GT	:	'>';
GTEQ	:	'>=';
PLUS	:	'+';
MINUS	:	'-';
MULT	:	'*';
DIV	:	'/';
NOT	:	'$not$';


LTLAND	:	'$land$';
LTLOR	:	'$lor$';
LTLNOT	:	'$lnot$';
UNTIL	:	'$luntil$';
RELEASE	:	'$lrelease$';
GLOBALLY:	'$lglobally$';
FINALLY	:	'$lfinally$';
NEXT	:	'$lnext$';
IMPLICATION:	'$limplication$';
EQUIVALENCE:	'$lequivalent$';
EVAL	:	'eval';

//strong implication and equivalence (not integrated yet).
SIMPLICATION:	'$simplication$';
SEQUIVALENCE:	'$sequivalent$';


STATUS    : ('a'..'z'|'A'..'Z'|'_'|'-')+('.')('inactive'|'waiting'|'executing'|'finishing'|'iterationEnded'|'failing'|'finished');
STAEXT    : ('a'..'z'|'A'..'Z'|'_'|'-')+('.')('allChildrenInactive'|'allChildrenWaiting'|'allChildrenExecuting'|'allChildrenFinishing'|'allChildrenIterationEnded'|'allChildrenFailing'|'allChildrenFinished');
NODEVAR   : ('a'..'z'|'A'..'Z'|'_'|'-')+('.{')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+('}');
OUTCOME   : ('a'..'z'|'A'..'Z'|'_'|'-')+('.')('noOutcome'|'skipped'|'success'|'parentFails'|'invFails'|'preFails'|'postFails'|'fail');
CONDITION : ('a'..'z'|'A'..'Z'|'_'|'-')+('.')('end'|'inv'|'pre'|'start'|'post'|'repeat');
CONDEXT   : ('a'..'z'|'A'..'Z'|'_'|'-')+('.')('ancestorInv');


/*ltlexp			:	(LTLNOT^|NEXT^|FINALLY^|GLOBALLY^)? ltlUntilExp EOF! | (LTLNOT^|NEXT^|FINALLY^|GLOBALLY^)?'('!ltlUntilExp')'! EOF!;
ltlUntilExp		:	ltlReleaseExp(UNTIL^ ltlReleaseExp)*;
ltlReleaseExp		:	ltlBooleanOrExp (RELEASE^ ltlBooleanOrExp)*;
ltlBooleanOrExp		:	ltlBooleanAndExp (LTLOR^ ltlBooleanAndExp)*;
ltlBooleanAndExp	:	ltlBooleanEquivExp (LTLAND^ ltlBooleanEquivExp)*;
ltlBooleanEquivExp	:	ltlformula (EQUIVALENCE^ ltlformula)*;
ltlformula		:	(EVAL^)'('!plexilexp')'!|ltlvalue;

plexilexp		: 	(NOT^)? booleanOrExpression;
booleanOrExpression 	:	booleanAndExpression (OR^ booleanAndExpression)* ;
booleanAndExpression   	:	equalityExpression (AND^ equalityExpression)*;
equalityExpression 	:	relationalExpression ((EQUALS|NOTEQUALS)^ relationalExpression)* ;
relationalExpression 	:	additiveExpression ( (LT|LTEQ|GT|GTEQ)^ additiveExpression )*;
additiveExpression 	:	multiplicativeExpression ( (PLUS|MINUS)^ multiplicativeExpression )*;
multiplicativeExpression :	unaryExpression ( (MULT|DIV)^ unaryExpression )*;
unaryExpression 	:	primaryExpression;
primaryExpression	:	'('! plexilexp ')'! |	value 
	;*/
		
ltlexp			:	ltlUntilExp EOF!;
ltlUntilExp		:	ltlReleaseExp(UNTIL^ ltlReleaseExp)*;
ltlReleaseExp		:	ltlBooleanOrExp (RELEASE^ ltlBooleanOrExp)*;
ltlBooleanOrExp		:	ltlBooleanAndExp (LTLOR^ ltlBooleanAndExp)*;
ltlBooleanAndExp	:	ltlBooleanEquivExp (LTLAND^ ltlBooleanEquivExp)*;
ltlBooleanEquivExp	:	ltlBooleanImplExp (EQUIVALENCE^ ltlBooleanImplExp)*;
ltlBooleanImplExp	:   ltlunary (IMPLICATION^ ltlunary)*;
ltlunary		:	ltlformula | (LTLNOT^|NEXT^|FINALLY^|GLOBALLY^) ltlformula;
ltlformula		:	'('! ltlUntilExp ')'!|ltlvalue|(EVAL^)'('!plexilexp')'!;

plexilexp		: 	booleanOrExpression;
booleanOrExpression 	:	booleanAndExpression (OR^ booleanAndExpression)* ;
booleanAndExpression   	:	equalityExpression (AND^ equalityExpression)*;
equalityExpression 	:	relationalExpression ((EQUALS|NOTEQUALS)^ relationalExpression)* ;
relationalExpression 	:	additiveExpression ( (LT|LTEQ|GT|GTEQ)^ additiveExpression )*;
additiveExpression 	:	multiplicativeExpression ( (PLUS|MINUS)^ multiplicativeExpression )*;
multiplicativeExpression :	unaryExpression ( (MULT|DIV)^ unaryExpression )*;
unaryExpression 	:	primaryExpression | (NOT^) primaryExpression;
primaryExpression	:	'('! plexilexp ')'! |	value 
	;
	

value	
	: 	INTEGER
	|	FLOAT
	|	BOOLEAN
	|	NODEVAR
	|	STAEXT
	|	CONDEXT
	;
ltlvalue
	:	LTLBOOL
	|	OUTCOME
	|	CONDITION
	|	STATUS

	;

	
STRING   	:  	'\'' ( EscapeSequence | (options {greedy=false;} : ~('\u0000'..'\u001f' | '\\' | '\'' ) ) )* '\'';
INTEGER	 	:	('0'..'9')+ ;
FLOAT 		:	('0'..'9')* '.' ('0'..'9')+;
BOOLEAN 	:	'true'
		|	'false'
			;
LTLBOOL		:	'$TRUE$'
		|	'$FALSE$'
			;
// Must be declared after the BOOLEAN token or it will hide it
/*IDENT
	:	('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
	;*/

fragment EscapeSequence 
	:	'\\'
  	(	
  		'n' 
	|	'r' 
	|	't'
	|	'\'' 
	|	'\\'
	|	UnicodeEscape
	)
  ;

fragment UnicodeEscape
    	:    	'u' HexDigit HexDigit HexDigit HexDigit 
    	;

fragment HexDigit 
	: 	('0'..'9'|'a'..'f'|'A'..'F') ;

/* Ignore white spaces */	
WS	
	:  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
	;
