// $ANTLR 3.3 Nov 30, 2010 12:45:30 LTLExpression.g 2011-06-15 09:52:43
package org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LTLExpressionLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int PARAM=4;
    public static final int NEGATE=5;
    public static final int STRING=6;
    public static final int OR=7;
    public static final int AND=8;
    public static final int EQUALS=9;
    public static final int NOTEQUALS=10;
    public static final int LT=11;
    public static final int LTEQ=12;
    public static final int GT=13;
    public static final int GTEQ=14;
    public static final int PLUS=15;
    public static final int MINUS=16;
    public static final int MULT=17;
    public static final int DIV=18;
    public static final int NOT=19;
    public static final int LTLAND=20;
    public static final int LTLOR=21;
    public static final int LTLNOT=22;
    public static final int UNTIL=23;
    public static final int RELEASE=24;
    public static final int GLOBALLY=25;
    public static final int FINALLY=26;
    public static final int NEXT=27;
    public static final int IMPLICATION=28;
    public static final int EQUIVALENCE=29;
    public static final int EVAL=30;
    public static final int SIMPLICATION=31;
    public static final int SEQUIVALENCE=32;
    public static final int STATUS=33;
    public static final int STAEXT=34;
    public static final int NODEVAR=35;
    public static final int OUTCOME=36;
    public static final int CONDITION=37;
    public static final int CONDEXT=38;
    public static final int INTEGER=39;
    public static final int FLOAT=40;
    public static final int BOOLEAN=41;
    public static final int LTLBOOL=42;
    public static final int EscapeSequence=43;
    public static final int UnicodeEscape=44;
    public static final int HexDigit=45;
    public static final int WS=46;

        public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e) {
            throw new ParsingRuntimeException(tokenNames,e);
        }


    // delegates
    // delegators

    public LTLExpressionLexer() {;} 
    public LTLExpressionLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public LTLExpressionLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "LTLExpression.g"; }

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:11:7: ( '(' )
            // LTLExpression.g:11:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:12:7: ( ')' )
            // LTLExpression.g:12:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:44:5: ( 'or' )
            // LTLExpression.g:44:8: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:45:6: ( 'and' )
            // LTLExpression.g:45:9: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:46:8: ( '$equ$' )
            // LTLExpression.g:46:10: '$equ$'
            {
            match("$equ$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "NOTEQUALS"
    public final void mNOTEQUALS() throws RecognitionException {
        try {
            int _type = NOTEQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:47:11: ( '$nequ$' )
            // LTLExpression.g:47:13: '$nequ$'
            {
            match("$nequ$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOTEQUALS"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:48:4: ( '<' )
            // LTLExpression.g:48:6: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LT"

    // $ANTLR start "LTEQ"
    public final void mLTEQ() throws RecognitionException {
        try {
            int _type = LTEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:49:6: ( '<=' )
            // LTLExpression.g:49:8: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTEQ"

    // $ANTLR start "GT"
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:50:4: ( '>' )
            // LTLExpression.g:50:6: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GT"

    // $ANTLR start "GTEQ"
    public final void mGTEQ() throws RecognitionException {
        try {
            int _type = GTEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:51:6: ( '>=' )
            // LTLExpression.g:51:8: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GTEQ"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:52:6: ( '+' )
            // LTLExpression.g:52:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:53:7: ( '-' )
            // LTLExpression.g:53:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "MULT"
    public final void mMULT() throws RecognitionException {
        try {
            int _type = MULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:54:6: ( '*' )
            // LTLExpression.g:54:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULT"

    // $ANTLR start "DIV"
    public final void mDIV() throws RecognitionException {
        try {
            int _type = DIV;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:55:5: ( '/' )
            // LTLExpression.g:55:7: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIV"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:56:5: ( '$not$' )
            // LTLExpression.g:56:7: '$not$'
            {
            match("$not$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "LTLAND"
    public final void mLTLAND() throws RecognitionException {
        try {
            int _type = LTLAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:59:8: ( '$land$' )
            // LTLExpression.g:59:10: '$land$'
            {
            match("$land$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTLAND"

    // $ANTLR start "LTLOR"
    public final void mLTLOR() throws RecognitionException {
        try {
            int _type = LTLOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:60:7: ( '$lor$' )
            // LTLExpression.g:60:9: '$lor$'
            {
            match("$lor$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTLOR"

    // $ANTLR start "LTLNOT"
    public final void mLTLNOT() throws RecognitionException {
        try {
            int _type = LTLNOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:61:8: ( '$lnot$' )
            // LTLExpression.g:61:10: '$lnot$'
            {
            match("$lnot$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTLNOT"

    // $ANTLR start "UNTIL"
    public final void mUNTIL() throws RecognitionException {
        try {
            int _type = UNTIL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:62:7: ( '$luntil$' )
            // LTLExpression.g:62:9: '$luntil$'
            {
            match("$luntil$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNTIL"

    // $ANTLR start "RELEASE"
    public final void mRELEASE() throws RecognitionException {
        try {
            int _type = RELEASE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:63:9: ( '$lrelease$' )
            // LTLExpression.g:63:11: '$lrelease$'
            {
            match("$lrelease$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RELEASE"

    // $ANTLR start "GLOBALLY"
    public final void mGLOBALLY() throws RecognitionException {
        try {
            int _type = GLOBALLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:64:9: ( '$lglobally$' )
            // LTLExpression.g:64:11: '$lglobally$'
            {
            match("$lglobally$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GLOBALLY"

    // $ANTLR start "FINALLY"
    public final void mFINALLY() throws RecognitionException {
        try {
            int _type = FINALLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:65:9: ( '$lfinally$' )
            // LTLExpression.g:65:11: '$lfinally$'
            {
            match("$lfinally$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FINALLY"

    // $ANTLR start "NEXT"
    public final void mNEXT() throws RecognitionException {
        try {
            int _type = NEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:66:6: ( '$lnext$' )
            // LTLExpression.g:66:8: '$lnext$'
            {
            match("$lnext$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEXT"

    // $ANTLR start "IMPLICATION"
    public final void mIMPLICATION() throws RecognitionException {
        try {
            int _type = IMPLICATION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:67:12: ( '$limplication$' )
            // LTLExpression.g:67:14: '$limplication$'
            {
            match("$limplication$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IMPLICATION"

    // $ANTLR start "EQUIVALENCE"
    public final void mEQUIVALENCE() throws RecognitionException {
        try {
            int _type = EQUIVALENCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:68:12: ( '$lequivalent$' )
            // LTLExpression.g:68:14: '$lequivalent$'
            {
            match("$lequivalent$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUIVALENCE"

    // $ANTLR start "EVAL"
    public final void mEVAL() throws RecognitionException {
        try {
            int _type = EVAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:69:6: ( 'eval' )
            // LTLExpression.g:69:8: 'eval'
            {
            match("eval"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EVAL"

    // $ANTLR start "SIMPLICATION"
    public final void mSIMPLICATION() throws RecognitionException {
        try {
            int _type = SIMPLICATION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:72:13: ( '$simplication$' )
            // LTLExpression.g:72:15: '$simplication$'
            {
            match("$simplication$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIMPLICATION"

    // $ANTLR start "SEQUIVALENCE"
    public final void mSEQUIVALENCE() throws RecognitionException {
        try {
            int _type = SEQUIVALENCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:73:13: ( '$sequivalent$' )
            // LTLExpression.g:73:15: '$sequivalent$'
            {
            match("$sequivalent$"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEQUIVALENCE"

    // $ANTLR start "STATUS"
    public final void mSTATUS() throws RecognitionException {
        try {
            int _type = STATUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:76:11: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'inactive' | 'waiting' | 'executing' | 'finishing' | 'iterationEnded' | 'failing' | 'finished' ) )
            // LTLExpression.g:76:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'inactive' | 'waiting' | 'executing' | 'finishing' | 'iterationEnded' | 'failing' | 'finished' )
            {
            // LTLExpression.g:76:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='-'||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            // LTLExpression.g:76:41: ( '.' )
            // LTLExpression.g:76:42: '.'
            {
            match('.'); 

            }

            // LTLExpression.g:76:46: ( 'inactive' | 'waiting' | 'executing' | 'finishing' | 'iterationEnded' | 'failing' | 'finished' )
            int alt2=7;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // LTLExpression.g:76:47: 'inactive'
                    {
                    match("inactive"); 


                    }
                    break;
                case 2 :
                    // LTLExpression.g:76:58: 'waiting'
                    {
                    match("waiting"); 


                    }
                    break;
                case 3 :
                    // LTLExpression.g:76:68: 'executing'
                    {
                    match("executing"); 


                    }
                    break;
                case 4 :
                    // LTLExpression.g:76:80: 'finishing'
                    {
                    match("finishing"); 


                    }
                    break;
                case 5 :
                    // LTLExpression.g:76:92: 'iterationEnded'
                    {
                    match("iterationEnded"); 


                    }
                    break;
                case 6 :
                    // LTLExpression.g:76:109: 'failing'
                    {
                    match("failing"); 


                    }
                    break;
                case 7 :
                    // LTLExpression.g:76:119: 'finished'
                    {
                    match("finished"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STATUS"

    // $ANTLR start "STAEXT"
    public final void mSTAEXT() throws RecognitionException {
        try {
            int _type = STAEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:77:11: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'allChildrenInactive' | 'allChildrenWaiting' | 'allChildrenExecuting' | 'allChildrenFinishing' | 'allChildrenIterationEnded' | 'allChildrenFailing' | 'allChildrenFinished' ) )
            // LTLExpression.g:77:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'allChildrenInactive' | 'allChildrenWaiting' | 'allChildrenExecuting' | 'allChildrenFinishing' | 'allChildrenIterationEnded' | 'allChildrenFailing' | 'allChildrenFinished' )
            {
            // LTLExpression.g:77:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='-'||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            // LTLExpression.g:77:41: ( '.' )
            // LTLExpression.g:77:42: '.'
            {
            match('.'); 

            }

            // LTLExpression.g:77:46: ( 'allChildrenInactive' | 'allChildrenWaiting' | 'allChildrenExecuting' | 'allChildrenFinishing' | 'allChildrenIterationEnded' | 'allChildrenFailing' | 'allChildrenFinished' )
            int alt4=7;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // LTLExpression.g:77:47: 'allChildrenInactive'
                    {
                    match("allChildrenInactive"); 


                    }
                    break;
                case 2 :
                    // LTLExpression.g:77:69: 'allChildrenWaiting'
                    {
                    match("allChildrenWaiting"); 


                    }
                    break;
                case 3 :
                    // LTLExpression.g:77:90: 'allChildrenExecuting'
                    {
                    match("allChildrenExecuting"); 


                    }
                    break;
                case 4 :
                    // LTLExpression.g:77:113: 'allChildrenFinishing'
                    {
                    match("allChildrenFinishing"); 


                    }
                    break;
                case 5 :
                    // LTLExpression.g:77:136: 'allChildrenIterationEnded'
                    {
                    match("allChildrenIterationEnded"); 


                    }
                    break;
                case 6 :
                    // LTLExpression.g:77:164: 'allChildrenFailing'
                    {
                    match("allChildrenFailing"); 


                    }
                    break;
                case 7 :
                    // LTLExpression.g:77:185: 'allChildrenFinished'
                    {
                    match("allChildrenFinished"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STAEXT"

    // $ANTLR start "NODEVAR"
    public final void mNODEVAR() throws RecognitionException {
        try {
            int _type = NODEVAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:78:11: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.{' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )+ ( '}' ) )
            // LTLExpression.g:78:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.{' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )+ ( '}' )
            {
            // LTLExpression.g:78:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='-'||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            // LTLExpression.g:78:41: ( '.{' )
            // LTLExpression.g:78:42: '.{'
            {
            match(".{"); 


            }

            // LTLExpression.g:78:47: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='-'||(LA6_0>='0' && LA6_0<='9')||(LA6_0>='A' && LA6_0<='Z')||LA6_0=='_'||(LA6_0>='a' && LA6_0<='z')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            // LTLExpression.g:78:84: ( '}' )
            // LTLExpression.g:78:85: '}'
            {
            match('}'); 

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NODEVAR"

    // $ANTLR start "OUTCOME"
    public final void mOUTCOME() throws RecognitionException {
        try {
            int _type = OUTCOME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:79:11: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'noOutcome' | 'skipped' | 'success' | 'parentFails' | 'invFails' | 'preFails' | 'postFails' | 'fail' ) )
            // LTLExpression.g:79:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'noOutcome' | 'skipped' | 'success' | 'parentFails' | 'invFails' | 'preFails' | 'postFails' | 'fail' )
            {
            // LTLExpression.g:79:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='-'||(LA7_0>='A' && LA7_0<='Z')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            // LTLExpression.g:79:41: ( '.' )
            // LTLExpression.g:79:42: '.'
            {
            match('.'); 

            }

            // LTLExpression.g:79:46: ( 'noOutcome' | 'skipped' | 'success' | 'parentFails' | 'invFails' | 'preFails' | 'postFails' | 'fail' )
            int alt8=8;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // LTLExpression.g:79:47: 'noOutcome'
                    {
                    match("noOutcome"); 


                    }
                    break;
                case 2 :
                    // LTLExpression.g:79:59: 'skipped'
                    {
                    match("skipped"); 


                    }
                    break;
                case 3 :
                    // LTLExpression.g:79:69: 'success'
                    {
                    match("success"); 


                    }
                    break;
                case 4 :
                    // LTLExpression.g:79:79: 'parentFails'
                    {
                    match("parentFails"); 


                    }
                    break;
                case 5 :
                    // LTLExpression.g:79:93: 'invFails'
                    {
                    match("invFails"); 


                    }
                    break;
                case 6 :
                    // LTLExpression.g:79:104: 'preFails'
                    {
                    match("preFails"); 


                    }
                    break;
                case 7 :
                    // LTLExpression.g:79:115: 'postFails'
                    {
                    match("postFails"); 


                    }
                    break;
                case 8 :
                    // LTLExpression.g:79:127: 'fail'
                    {
                    match("fail"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OUTCOME"

    // $ANTLR start "CONDITION"
    public final void mCONDITION() throws RecognitionException {
        try {
            int _type = CONDITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:80:11: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'end' | 'inv' | 'pre' | 'start' | 'post' | 'repeat' ) )
            // LTLExpression.g:80:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'end' | 'inv' | 'pre' | 'start' | 'post' | 'repeat' )
            {
            // LTLExpression.g:80:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='-'||(LA9_0>='A' && LA9_0<='Z')||LA9_0=='_'||(LA9_0>='a' && LA9_0<='z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);

            // LTLExpression.g:80:41: ( '.' )
            // LTLExpression.g:80:42: '.'
            {
            match('.'); 

            }

            // LTLExpression.g:80:46: ( 'end' | 'inv' | 'pre' | 'start' | 'post' | 'repeat' )
            int alt10=6;
            switch ( input.LA(1) ) {
            case 'e':
                {
                alt10=1;
                }
                break;
            case 'i':
                {
                alt10=2;
                }
                break;
            case 'p':
                {
                int LA10_3 = input.LA(2);

                if ( (LA10_3=='r') ) {
                    alt10=3;
                }
                else if ( (LA10_3=='o') ) {
                    alt10=5;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 3, input);

                    throw nvae;
                }
                }
                break;
            case 's':
                {
                alt10=4;
                }
                break;
            case 'r':
                {
                alt10=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // LTLExpression.g:80:47: 'end'
                    {
                    match("end"); 


                    }
                    break;
                case 2 :
                    // LTLExpression.g:80:53: 'inv'
                    {
                    match("inv"); 


                    }
                    break;
                case 3 :
                    // LTLExpression.g:80:59: 'pre'
                    {
                    match("pre"); 


                    }
                    break;
                case 4 :
                    // LTLExpression.g:80:65: 'start'
                    {
                    match("start"); 


                    }
                    break;
                case 5 :
                    // LTLExpression.g:80:73: 'post'
                    {
                    match("post"); 


                    }
                    break;
                case 6 :
                    // LTLExpression.g:80:80: 'repeat'
                    {
                    match("repeat"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONDITION"

    // $ANTLR start "CONDEXT"
    public final void mCONDEXT() throws RecognitionException {
        try {
            int _type = CONDEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:81:11: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'ancestorInv' ) )
            // LTLExpression.g:81:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+ ( '.' ) ( 'ancestorInv' )
            {
            // LTLExpression.g:81:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='-'||(LA11_0>='A' && LA11_0<='Z')||LA11_0=='_'||(LA11_0>='a' && LA11_0<='z')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // LTLExpression.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);

            // LTLExpression.g:81:41: ( '.' )
            // LTLExpression.g:81:42: '.'
            {
            match('.'); 

            }

            // LTLExpression.g:81:46: ( 'ancestorInv' )
            // LTLExpression.g:81:47: 'ancestorInv'
            {
            match("ancestorInv"); 


            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONDEXT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:142:11: ( '\\'' ( EscapeSequence | ( options {greedy=false; } : ~ ( '\\u0000' .. '\\u001f' | '\\\\' | '\\'' ) ) )* '\\'' )
            // LTLExpression.g:142:15: '\\'' ( EscapeSequence | ( options {greedy=false; } : ~ ( '\\u0000' .. '\\u001f' | '\\\\' | '\\'' ) ) )* '\\''
            {
            match('\''); 
            // LTLExpression.g:142:20: ( EscapeSequence | ( options {greedy=false; } : ~ ( '\\u0000' .. '\\u001f' | '\\\\' | '\\'' ) ) )*
            loop12:
            do {
                int alt12=3;
                int LA12_0 = input.LA(1);

                if ( (LA12_0=='\\') ) {
                    alt12=1;
                }
                else if ( ((LA12_0>=' ' && LA12_0<='&')||(LA12_0>='(' && LA12_0<='[')||(LA12_0>=']' && LA12_0<='\uFFFF')) ) {
                    alt12=2;
                }


                switch (alt12) {
            	case 1 :
            	    // LTLExpression.g:142:22: EscapeSequence
            	    {
            	    mEscapeSequence(); 

            	    }
            	    break;
            	case 2 :
            	    // LTLExpression.g:142:39: ( options {greedy=false; } : ~ ( '\\u0000' .. '\\u001f' | '\\\\' | '\\'' ) )
            	    {
            	    // LTLExpression.g:142:39: ( options {greedy=false; } : ~ ( '\\u0000' .. '\\u001f' | '\\\\' | '\\'' ) )
            	    // LTLExpression.g:142:66: ~ ( '\\u0000' .. '\\u001f' | '\\\\' | '\\'' )
            	    {
            	    if ( (input.LA(1)>=' ' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:143:11: ( ( '0' .. '9' )+ )
            // LTLExpression.g:143:13: ( '0' .. '9' )+
            {
            // LTLExpression.g:143:13: ( '0' .. '9' )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='0' && LA13_0<='9')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // LTLExpression.g:143:14: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:144:9: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ )
            // LTLExpression.g:144:11: ( '0' .. '9' )* '.' ( '0' .. '9' )+
            {
            // LTLExpression.g:144:11: ( '0' .. '9' )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='0' && LA14_0<='9')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // LTLExpression.g:144:12: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            match('.'); 
            // LTLExpression.g:144:27: ( '0' .. '9' )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>='0' && LA15_0<='9')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // LTLExpression.g:144:28: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "BOOLEAN"
    public final void mBOOLEAN() throws RecognitionException {
        try {
            int _type = BOOLEAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:145:10: ( 'true' | 'false' )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='t') ) {
                alt16=1;
            }
            else if ( (LA16_0=='f') ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // LTLExpression.g:145:12: 'true'
                    {
                    match("true"); 


                    }
                    break;
                case 2 :
                    // LTLExpression.g:146:5: 'false'
                    {
                    match("false"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BOOLEAN"

    // $ANTLR start "LTLBOOL"
    public final void mLTLBOOL() throws RecognitionException {
        try {
            int _type = LTLBOOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:148:10: ( '$TRUE$' | '$FALSE$' )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='$') ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1=='T') ) {
                    alt17=1;
                }
                else if ( (LA17_1=='F') ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // LTLExpression.g:148:12: '$TRUE$'
                    {
                    match("$TRUE$"); 


                    }
                    break;
                case 2 :
                    // LTLExpression.g:149:5: '$FALSE$'
                    {
                    match("$FALSE$"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTLBOOL"

    // $ANTLR start "EscapeSequence"
    public final void mEscapeSequence() throws RecognitionException {
        try {
            // LTLExpression.g:157:2: ( '\\\\' ( 'n' | 'r' | 't' | '\\'' | '\\\\' | UnicodeEscape ) )
            // LTLExpression.g:157:4: '\\\\' ( 'n' | 'r' | 't' | '\\'' | '\\\\' | UnicodeEscape )
            {
            match('\\'); 
            // LTLExpression.g:158:4: ( 'n' | 'r' | 't' | '\\'' | '\\\\' | UnicodeEscape )
            int alt18=6;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt18=1;
                }
                break;
            case 'r':
                {
                alt18=2;
                }
                break;
            case 't':
                {
                alt18=3;
                }
                break;
            case '\'':
                {
                alt18=4;
                }
                break;
            case '\\':
                {
                alt18=5;
                }
                break;
            case 'u':
                {
                alt18=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // LTLExpression.g:159:5: 'n'
                    {
                    match('n'); 

                    }
                    break;
                case 2 :
                    // LTLExpression.g:160:4: 'r'
                    {
                    match('r'); 

                    }
                    break;
                case 3 :
                    // LTLExpression.g:161:4: 't'
                    {
                    match('t'); 

                    }
                    break;
                case 4 :
                    // LTLExpression.g:162:4: '\\''
                    {
                    match('\''); 

                    }
                    break;
                case 5 :
                    // LTLExpression.g:163:4: '\\\\'
                    {
                    match('\\'); 

                    }
                    break;
                case 6 :
                    // LTLExpression.g:164:4: UnicodeEscape
                    {
                    mUnicodeEscape(); 

                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "EscapeSequence"

    // $ANTLR start "UnicodeEscape"
    public final void mUnicodeEscape() throws RecognitionException {
        try {
            // LTLExpression.g:169:6: ( 'u' HexDigit HexDigit HexDigit HexDigit )
            // LTLExpression.g:169:12: 'u' HexDigit HexDigit HexDigit HexDigit
            {
            match('u'); 
            mHexDigit(); 
            mHexDigit(); 
            mHexDigit(); 
            mHexDigit(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UnicodeEscape"

    // $ANTLR start "HexDigit"
    public final void mHexDigit() throws RecognitionException {
        try {
            // LTLExpression.g:173:2: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // LTLExpression.g:173:5: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HexDigit"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LTLExpression.g:177:2: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // LTLExpression.g:177:5: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // LTLExpression.g:1:8: ( T__47 | T__48 | OR | AND | EQUALS | NOTEQUALS | LT | LTEQ | GT | GTEQ | PLUS | MINUS | MULT | DIV | NOT | LTLAND | LTLOR | LTLNOT | UNTIL | RELEASE | GLOBALLY | FINALLY | NEXT | IMPLICATION | EQUIVALENCE | EVAL | SIMPLICATION | SEQUIVALENCE | STATUS | STAEXT | NODEVAR | OUTCOME | CONDITION | CONDEXT | STRING | INTEGER | FLOAT | BOOLEAN | LTLBOOL | WS )
        int alt19=40;
        alt19 = dfa19.predict(input);
        switch (alt19) {
            case 1 :
                // LTLExpression.g:1:10: T__47
                {
                mT__47(); 

                }
                break;
            case 2 :
                // LTLExpression.g:1:16: T__48
                {
                mT__48(); 

                }
                break;
            case 3 :
                // LTLExpression.g:1:22: OR
                {
                mOR(); 

                }
                break;
            case 4 :
                // LTLExpression.g:1:25: AND
                {
                mAND(); 

                }
                break;
            case 5 :
                // LTLExpression.g:1:29: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 6 :
                // LTLExpression.g:1:36: NOTEQUALS
                {
                mNOTEQUALS(); 

                }
                break;
            case 7 :
                // LTLExpression.g:1:46: LT
                {
                mLT(); 

                }
                break;
            case 8 :
                // LTLExpression.g:1:49: LTEQ
                {
                mLTEQ(); 

                }
                break;
            case 9 :
                // LTLExpression.g:1:54: GT
                {
                mGT(); 

                }
                break;
            case 10 :
                // LTLExpression.g:1:57: GTEQ
                {
                mGTEQ(); 

                }
                break;
            case 11 :
                // LTLExpression.g:1:62: PLUS
                {
                mPLUS(); 

                }
                break;
            case 12 :
                // LTLExpression.g:1:67: MINUS
                {
                mMINUS(); 

                }
                break;
            case 13 :
                // LTLExpression.g:1:73: MULT
                {
                mMULT(); 

                }
                break;
            case 14 :
                // LTLExpression.g:1:78: DIV
                {
                mDIV(); 

                }
                break;
            case 15 :
                // LTLExpression.g:1:82: NOT
                {
                mNOT(); 

                }
                break;
            case 16 :
                // LTLExpression.g:1:86: LTLAND
                {
                mLTLAND(); 

                }
                break;
            case 17 :
                // LTLExpression.g:1:93: LTLOR
                {
                mLTLOR(); 

                }
                break;
            case 18 :
                // LTLExpression.g:1:99: LTLNOT
                {
                mLTLNOT(); 

                }
                break;
            case 19 :
                // LTLExpression.g:1:106: UNTIL
                {
                mUNTIL(); 

                }
                break;
            case 20 :
                // LTLExpression.g:1:112: RELEASE
                {
                mRELEASE(); 

                }
                break;
            case 21 :
                // LTLExpression.g:1:120: GLOBALLY
                {
                mGLOBALLY(); 

                }
                break;
            case 22 :
                // LTLExpression.g:1:129: FINALLY
                {
                mFINALLY(); 

                }
                break;
            case 23 :
                // LTLExpression.g:1:137: NEXT
                {
                mNEXT(); 

                }
                break;
            case 24 :
                // LTLExpression.g:1:142: IMPLICATION
                {
                mIMPLICATION(); 

                }
                break;
            case 25 :
                // LTLExpression.g:1:154: EQUIVALENCE
                {
                mEQUIVALENCE(); 

                }
                break;
            case 26 :
                // LTLExpression.g:1:166: EVAL
                {
                mEVAL(); 

                }
                break;
            case 27 :
                // LTLExpression.g:1:171: SIMPLICATION
                {
                mSIMPLICATION(); 

                }
                break;
            case 28 :
                // LTLExpression.g:1:184: SEQUIVALENCE
                {
                mSEQUIVALENCE(); 

                }
                break;
            case 29 :
                // LTLExpression.g:1:197: STATUS
                {
                mSTATUS(); 

                }
                break;
            case 30 :
                // LTLExpression.g:1:204: STAEXT
                {
                mSTAEXT(); 

                }
                break;
            case 31 :
                // LTLExpression.g:1:211: NODEVAR
                {
                mNODEVAR(); 

                }
                break;
            case 32 :
                // LTLExpression.g:1:219: OUTCOME
                {
                mOUTCOME(); 

                }
                break;
            case 33 :
                // LTLExpression.g:1:227: CONDITION
                {
                mCONDITION(); 

                }
                break;
            case 34 :
                // LTLExpression.g:1:237: CONDEXT
                {
                mCONDEXT(); 

                }
                break;
            case 35 :
                // LTLExpression.g:1:245: STRING
                {
                mSTRING(); 

                }
                break;
            case 36 :
                // LTLExpression.g:1:252: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 37 :
                // LTLExpression.g:1:260: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 38 :
                // LTLExpression.g:1:266: BOOLEAN
                {
                mBOOLEAN(); 

                }
                break;
            case 39 :
                // LTLExpression.g:1:274: LTLBOOL
                {
                mLTLBOOL(); 

                }
                break;
            case 40 :
                // LTLExpression.g:1:282: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA19 dfa19 = new DFA19(this);
    static final String DFA2_eotS =
        "\17\uffff";
    static final String DFA2_eofS =
        "\17\uffff";
    static final String DFA2_minS =
        "\1\145\1\156\2\uffff\1\141\2\uffff\1\156\1\uffff\1\151\1\163\1\150"+
        "\1\145\2\uffff";
    static final String DFA2_maxS =
        "\1\167\1\164\2\uffff\1\151\2\uffff\1\156\1\uffff\1\151\1\163\1\150"+
        "\1\151\2\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\2\1\3\1\uffff\1\1\1\5\1\uffff\1\6\4\uffff\1\4\1\7";
    static final String DFA2_specialS =
        "\17\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\3\1\4\2\uffff\1\1\15\uffff\1\2",
            "\1\5\5\uffff\1\6",
            "",
            "",
            "\1\10\7\uffff\1\7",
            "",
            "",
            "\1\11",
            "",
            "\1\12",
            "\1\13",
            "\1\14",
            "\1\16\3\uffff\1\15",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "76:46: ( 'inactive' | 'waiting' | 'executing' | 'finishing' | 'iterationEnded' | 'failing' | 'finished' )";
        }
    }
    static final String DFA4_eotS =
        "\32\uffff";
    static final String DFA4_eofS =
        "\32\uffff";
    static final String DFA4_minS =
        "\1\141\2\154\1\103\1\150\1\151\1\154\1\144\1\162\1\145\1\156\1\105"+
        "\1\156\2\uffff\1\141\2\uffff\1\156\1\uffff\1\151\1\163\1\150\1\145"+
        "\2\uffff";
    static final String DFA4_maxS =
        "\1\141\2\154\1\103\1\150\1\151\1\154\1\144\1\162\1\145\1\156\1\127"+
        "\1\164\2\uffff\1\151\2\uffff\1\156\1\uffff\1\151\1\163\1\150\1\151"+
        "\2\uffff";
    static final String DFA4_acceptS =
        "\15\uffff\1\2\1\3\1\uffff\1\1\1\5\1\uffff\1\6\4\uffff\1\4\1\7";
    static final String DFA4_specialS =
        "\32\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\16\1\17\2\uffff\1\14\15\uffff\1\15",
            "\1\20\5\uffff\1\21",
            "",
            "",
            "\1\23\7\uffff\1\22",
            "",
            "",
            "\1\24",
            "",
            "\1\25",
            "\1\26",
            "\1\27",
            "\1\31\3\uffff\1\30",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "77:46: ( 'allChildrenInactive' | 'allChildrenWaiting' | 'allChildrenExecuting' | 'allChildrenFinishing' | 'allChildrenIterationEnded' | 'allChildrenFailing' | 'allChildrenFinished' )";
        }
    }
    static final String DFA8_eotS =
        "\13\uffff";
    static final String DFA8_eofS =
        "\13\uffff";
    static final String DFA8_minS =
        "\1\146\1\uffff\1\153\1\141\7\uffff";
    static final String DFA8_maxS =
        "\1\163\1\uffff\1\165\1\162\7\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\2\uffff\1\5\1\10\1\2\1\3\1\4\1\6\1\7";
    static final String DFA8_specialS =
        "\13\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\5\2\uffff\1\4\4\uffff\1\1\1\uffff\1\3\2\uffff\1\2",
            "",
            "\1\6\11\uffff\1\7",
            "\1\10\15\uffff\1\12\2\uffff\1\11",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "79:46: ( 'noOutcome' | 'skipped' | 'success' | 'parentFails' | 'invFails' | 'preFails' | 'postFails' | 'fail' )";
        }
    }
    static final String DFA19_eotS =
        "\6\uffff\1\35\1\37\1\uffff\1\40\5\uffff\1\43\4\uffff\1\45\33\uffff"+
        "\1\107\31\uffff\1\121\1\122\1\uffff\1\57\1\uffff\1\57\3\uffff\1"+
        "\122\1\54\1\57";
    static final String DFA19_eofS =
        "\126\uffff";
    static final String DFA19_minS =
        "\1\11\2\uffff\2\55\1\106\2\75\1\uffff\1\55\2\uffff\2\55\1\uffff"+
        "\1\56\1\uffff\2\55\1\uffff\1\55\1\141\1\55\1\uffff\1\145\1\141\1"+
        "\145\6\uffff\2\55\1\uffff\1\55\2\uffff\1\156\1\uffff\1\156\1\141"+
        "\1\154\1\uffff\1\153\1\141\1\uffff\1\55\4\uffff\1\145\10\uffff\3"+
        "\55\1\141\1\151\2\uffff\1\145\1\163\3\uffff\3\55\1\106\1\154\1\106"+
        "\1\164\2\uffff\1\55\1\151\1\106";
    static final String DFA19_maxS =
        "\1\172\2\uffff\2\172\1\163\2\75\1\uffff\1\172\2\uffff\2\172\1\uffff"+
        "\1\71\1\uffff\2\172\1\uffff\1\172\1\173\1\172\1\uffff\1\157\1\165"+
        "\1\151\6\uffff\2\172\1\uffff\1\172\2\uffff\1\164\1\uffff\1\170\1"+
        "\151\1\156\1\uffff\1\165\1\162\1\uffff\1\172\4\uffff\1\157\10\uffff"+
        "\3\172\1\166\1\151\2\uffff\1\145\1\163\3\uffff\3\172\1\106\1\154"+
        "\1\106\1\164\2\uffff\1\172\1\151\1\106";
    static final String DFA19_acceptS =
        "\1\uffff\1\1\1\2\5\uffff\1\13\1\uffff\1\15\1\16\2\uffff\1\43\1\uffff"+
        "\1\45\2\uffff\1\50\3\uffff\1\5\3\uffff\1\47\1\10\1\7\1\12\1\11\1"+
        "\14\2\uffff\1\44\1\uffff\1\3\1\37\1\uffff\1\35\3\uffff\1\40\2\uffff"+
        "\1\41\1\uffff\1\6\1\17\1\20\1\21\1\uffff\1\23\1\24\1\25\1\26\1\30"+
        "\1\31\1\33\1\34\5\uffff\1\36\1\42\2\uffff\1\4\1\22\1\27\7\uffff"+
        "\1\32\1\46\3\uffff";
    static final String DFA19_specialS =
        "\126\uffff}>";
    static final String[] DFA19_transitionS = {
            "\2\23\1\uffff\2\23\22\uffff\1\23\3\uffff\1\5\2\uffff\1\16\1"+
            "\1\1\2\1\12\1\10\1\uffff\1\11\1\20\1\13\12\17\2\uffff\1\6\1"+
            "\uffff\1\7\2\uffff\32\22\4\uffff\1\22\1\uffff\1\4\3\22\1\14"+
            "\1\21\10\22\1\3\4\22\1\15\6\22",
            "",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\21\22\1\24\10"+
            "\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\15\22\1\26\14"+
            "\22",
            "\1\33\15\uffff\1\33\20\uffff\1\27\6\uffff\1\31\1\uffff\1\30"+
            "\4\uffff\1\32",
            "\1\34",
            "\1\36",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\25\22\1\41\4"+
            "\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\21\22\1\42\10"+
            "\22",
            "",
            "\1\20\1\uffff\12\17",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\1\44\31\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\53\3\uffff\1\51\1\52\2\uffff\1\47\4\uffff\1\54\1\uffff\1"+
            "\56\1\uffff\1\57\1\55\3\uffff\1\50\3\uffff\1\46",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\3\22\1\60\26"+
            "\22",
            "",
            "\1\61\11\uffff\1\62",
            "\1\63\3\uffff\1\73\1\71\1\70\1\uffff\1\72\4\uffff\1\65\1\64"+
            "\2\uffff\1\67\2\uffff\1\66",
            "\1\75\3\uffff\1\74",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\1\76\31\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\24\22\1\77\5"+
            "\22",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\13\22\1\100"+
            "\16\22",
            "",
            "",
            "\1\101\5\uffff\1\50",
            "",
            "\1\57\11\uffff\1\50",
            "\1\102\7\uffff\1\50",
            "\1\103\1\uffff\1\104",
            "",
            "\1\54\10\uffff\1\57\1\54",
            "\1\54\15\uffff\1\106\2\uffff\1\105",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "",
            "",
            "\1\111\11\uffff\1\110",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\13\22\1\112"+
            "\16\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\4\22\1\113\25"+
            "\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\22\22\1\114"+
            "\7\22",
            "\1\50\24\uffff\1\115",
            "\1\116",
            "",
            "",
            "\1\117",
            "\1\120",
            "",
            "",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\4\22\1\123\25"+
            "\22",
            "\1\54",
            "\1\124",
            "\1\54",
            "\1\125",
            "",
            "",
            "\1\22\1\25\22\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\50",
            "\1\54"
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__47 | T__48 | OR | AND | EQUALS | NOTEQUALS | LT | LTEQ | GT | GTEQ | PLUS | MINUS | MULT | DIV | NOT | LTLAND | LTLOR | LTLNOT | UNTIL | RELEASE | GLOBALLY | FINALLY | NEXT | IMPLICATION | EQUIVALENCE | EVAL | SIMPLICATION | SEQUIVALENCE | STATUS | STAEXT | NODEVAR | OUTCOME | CONDITION | CONDEXT | STRING | INTEGER | FLOAT | BOOLEAN | LTLBOOL | WS );";
        }
    }
 

}