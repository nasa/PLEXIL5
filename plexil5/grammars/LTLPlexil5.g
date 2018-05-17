grammar LTLPlexil5;

options {
    backtrack=true;
	output=AST;
	ASTLabelType=CommonTree;
}



tokens {
    DOT = '.' ;
    STI = 'inactive' ;
    STW = 'waiting' ;
    STE = 'executing' ;
    STF = 'finishing' ;
    STT = 'iterationEnded' ;
    STA = 'failing' ;
    STN = 'finished' ;
    ACI = 'allChildrenInactive' ;
    ACW = 'allChildrenWaiting' ;
    ACE = 'allChildrenExecuting' ;
    ACF = 'allChildrenFinishing' ;
    ACT = 'allChildrenIterationEnded' ;
    ACA = 'allChildrenFailing' ;
    ACN = 'allChildrenFinished' ;
    ONO = 'noOutcome' ;
    OSK = 'skipped' ;
    OSU = 'success' ;
    OPF = 'parentFails' ;
    OPI = 'invFails' ;
    OPA = 'preFails' ;
    OPL = 'postFails' ; 
    OFA = 'fail' ;
    END = 'end' ;
    PRE = 'pre' ;
    POS = 'pos' ;
    RPT = 'repeat' ;
    INV = 'inv' ;
    TRT = 'start' ;
    CAI = 'ancestorInv' ;
    LTLTRUE     = '$ltrue$';
    LTLFALSE    = '$lfalse$';
    LTLAND      = '$land$' ;
    LTLOR       = '$lor$' ;
    LTLNOT      = '$lnot$' ;
    UNTIL       = '$luntil$' ;
    RELEASE     = '$lrelease$' ;
    GLOBALLY    = '$lglobally$' ;
    FINALLY     = '$lfinally$' ;
    NEXT        = '$lnext$' ;
    IMPL        = '$limplication$' ;
    EQUIV       = '$lequivalent$' ;
    EVAL        = 'eval' ;
    SIMPL       = '$simplication$' ;
    SEQUIV      = '$sequivalent$' ;
    EVAL        = 'eval' ;
    PLXTRUE     = 'true' ;
    PLXFALSE    = 'false' ;
    PLXAND      = 'and' ;
    PLXOR       = 'or' ;
    PLXEQ       = '==' ;
    PLXNEQ      = '$nequ$' ;
    PLXNOT      = '$not$' ;
    
    /*EQ          = '$=$' ;
    NEQ         = '$=/=$' ;*/
    
    LT          = '<' ;
    LTEQ        = '<=' ;
    GT          = '>' ;
    GTEQ        = '>=' ;
    PLUS        = '+' ;
    MINUS       = '-' ;
    MULT        = '*' ;
    DIV         = '/' ;
    LPAR        = '(' ;
    RPAR        = ')' ;

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


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

STATUS    : ('a'..'z'|'A'..'Z'|'_'|'-')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+('.')('<inactive>'|'<waiting>'|'<executing>'|'<finishing>'|'<iterationEnded>'|'<failing>'|'<finished>');
CONDITION : ('a'..'z'|'A'..'Z'|'_'|'-')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+('.')('<end>'|'<inv>'|'<pre>'|'<start>'|'<post>'|'<repeat>');
NODEVAR   : ('a'..'z'|'A'..'Z'|'_'|'-')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+('.')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+;

STAEXT    : ('a'..'z'|'A'..'Z'|'_'|'-')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+('.')('<allChildrenInactive>'|'<allChildrenWaiting>'|'<allChildrenExecuting>'|'<allChildrenFinishing>'|'<allChildrenIterationEnded>'|'<allChildrenFailing>'|'<allChildrenFinished>');
OUTCOME   : ('a'..'z'|'A'..'Z'|'_'|'-')('a'..'z'|'A'..'Z'|'_'|'-'|'0'..'9')+('.')('<noOutcome>'|'<skipped>'|'<success>'|'<parentFails>'|'<invFails>'|'<preFails>'|'<postFails>');

/*
CONDEXT   : ('a'..'z'|'A'..'Z'|'_'|'-')+('.')('ancestorInv');
*/


ltl
    : ltlformula EOF!
    ;

ltlformula 
    : ltlliteral ( ltlbinop^ ltlliteral )*
    ;

ltlliteral
    : ( ltlunop^ )? ltlatom
    ;

ltlbinop
    : LTLAND | LTLOR | UNTIL | RELEASE | IMPL | EQUIV | SIMPL | SEQUIV 
    ;

ltlunop
    : LTLNOT | GLOBALLY | FINALLY | NEXT 
    ;

ltlatom
    : STATUS | CONDITION | STAEXT | OUTCOME
    | EVAL^ LPAR! boolexp RPAR!
    | LTLTRUE | LTLFALSE
    | LPAR! ltlformula RPAR!
    ;

pred
    : STI | STW | STE | STF | STT | STA | STN 
    | ACI | ACW | ACE | ACF | ACT | ACA | ACN
    | ONO | OSK | OSU | OPF | OPI | OPA | OPL | OFA
    | END | PRE | POS | RPT | INV | TRT | CAI 
    ;

qualified    
    : NAME ( DOT NAME )* 
    ;

boolexp
    : boolterm ( boolbinop^ boolterm )*
    ;

boolterm
    : ( PLXNOT^ )? boolnumexp
    ;

boolnumexp
    : PLXTRUE | PLXFALSE
    | numexp numrelbinop^ numexp
    | NODEVAR 
    | LPAR! boolexp RPAR!
    ;

numrelbinop
    : PLXEQ | PLXNEQ | LT | LTEQ | GT | GTEQ
    ;

boolbinop
    : PLXAND | PLXOR | PLXEQ | PLXNEQ
    ;

numexp
    : numterm ( ( PLUS | MINUS )^ numterm )*
    ;

numterm
    : numfactor ( ( MULT | DIV )^ numfactor )*
    ;

numfactor
    : MINUS? NUMBER
    | NODEVAR 
    | LPAR! numexp RPAR!
    ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

NUMBER : (DIGIT)+ (DOT (DIGIT)+)? ;

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel = HIDDEN; } ;

NAME : ( 'a'..'z' | 'A'..'Z' | '-' | '_' ) ( DIGIT |'a'..'z' | 'A'..'Z' | '-' | '_' )+ ;

fragment DIGIT: '0'..'9' ;