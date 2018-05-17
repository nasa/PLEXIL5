// $ANTLR 3.3 Nov 30, 2010 12:45:30 LTLExpression.g 2011-06-15 09:52:42

	package org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar;
		


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class LTLExpressionParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PARAM", "NEGATE", "STRING", "OR", "AND", "EQUALS", "NOTEQUALS", "LT", "LTEQ", "GT", "GTEQ", "PLUS", "MINUS", "MULT", "DIV", "NOT", "LTLAND", "LTLOR", "LTLNOT", "UNTIL", "RELEASE", "GLOBALLY", "FINALLY", "NEXT", "IMPLICATION", "EQUIVALENCE", "EVAL", "SIMPLICATION", "SEQUIVALENCE", "STATUS", "STAEXT", "NODEVAR", "OUTCOME", "CONDITION", "CONDEXT", "INTEGER", "FLOAT", "BOOLEAN", "LTLBOOL", "EscapeSequence", "UnicodeEscape", "HexDigit", "WS", "'('", "')'"
    };
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

    // delegates
    // delegators


        public LTLExpressionParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LTLExpressionParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return LTLExpressionParser.tokenNames; }
    public String getGrammarFileName() { return "LTLExpression.g"; }

                              

        public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e) {
            throw new ParsingRuntimeException(tokenNames,e);
        }



    public static class ltlexp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlexp"
    // LTLExpression.g:103:1: ltlexp : ltlUntilExp EOF ;
    public final LTLExpressionParser.ltlexp_return ltlexp() throws RecognitionException {
        LTLExpressionParser.ltlexp_return retval = new LTLExpressionParser.ltlexp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF2=null;
        LTLExpressionParser.ltlUntilExp_return ltlUntilExp1 = null;


        CommonTree EOF2_tree=null;

        try {
            // LTLExpression.g:103:10: ( ltlUntilExp EOF )
            // LTLExpression.g:103:12: ltlUntilExp EOF
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlUntilExp_in_ltlexp509);
            ltlUntilExp1=ltlUntilExp();

            state._fsp--;

            adaptor.addChild(root_0, ltlUntilExp1.getTree());
            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_ltlexp511); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlexp"

    public static class ltlUntilExp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlUntilExp"
    // LTLExpression.g:104:1: ltlUntilExp : ltlReleaseExp ( UNTIL ltlReleaseExp )* ;
    public final LTLExpressionParser.ltlUntilExp_return ltlUntilExp() throws RecognitionException {
        LTLExpressionParser.ltlUntilExp_return retval = new LTLExpressionParser.ltlUntilExp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token UNTIL4=null;
        LTLExpressionParser.ltlReleaseExp_return ltlReleaseExp3 = null;

        LTLExpressionParser.ltlReleaseExp_return ltlReleaseExp5 = null;


        CommonTree UNTIL4_tree=null;

        try {
            // LTLExpression.g:104:14: ( ltlReleaseExp ( UNTIL ltlReleaseExp )* )
            // LTLExpression.g:104:16: ltlReleaseExp ( UNTIL ltlReleaseExp )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlReleaseExp_in_ltlUntilExp520);
            ltlReleaseExp3=ltlReleaseExp();

            state._fsp--;

            adaptor.addChild(root_0, ltlReleaseExp3.getTree());
            // LTLExpression.g:104:29: ( UNTIL ltlReleaseExp )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==UNTIL) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // LTLExpression.g:104:30: UNTIL ltlReleaseExp
            	    {
            	    UNTIL4=(Token)match(input,UNTIL,FOLLOW_UNTIL_in_ltlUntilExp522); 
            	    UNTIL4_tree = (CommonTree)adaptor.create(UNTIL4);
            	    root_0 = (CommonTree)adaptor.becomeRoot(UNTIL4_tree, root_0);

            	    pushFollow(FOLLOW_ltlReleaseExp_in_ltlUntilExp525);
            	    ltlReleaseExp5=ltlReleaseExp();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ltlReleaseExp5.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlUntilExp"

    public static class ltlReleaseExp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlReleaseExp"
    // LTLExpression.g:105:1: ltlReleaseExp : ltlBooleanOrExp ( RELEASE ltlBooleanOrExp )* ;
    public final LTLExpressionParser.ltlReleaseExp_return ltlReleaseExp() throws RecognitionException {
        LTLExpressionParser.ltlReleaseExp_return retval = new LTLExpressionParser.ltlReleaseExp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token RELEASE7=null;
        LTLExpressionParser.ltlBooleanOrExp_return ltlBooleanOrExp6 = null;

        LTLExpressionParser.ltlBooleanOrExp_return ltlBooleanOrExp8 = null;


        CommonTree RELEASE7_tree=null;

        try {
            // LTLExpression.g:105:16: ( ltlBooleanOrExp ( RELEASE ltlBooleanOrExp )* )
            // LTLExpression.g:105:18: ltlBooleanOrExp ( RELEASE ltlBooleanOrExp )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlBooleanOrExp_in_ltlReleaseExp535);
            ltlBooleanOrExp6=ltlBooleanOrExp();

            state._fsp--;

            adaptor.addChild(root_0, ltlBooleanOrExp6.getTree());
            // LTLExpression.g:105:34: ( RELEASE ltlBooleanOrExp )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==RELEASE) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // LTLExpression.g:105:35: RELEASE ltlBooleanOrExp
            	    {
            	    RELEASE7=(Token)match(input,RELEASE,FOLLOW_RELEASE_in_ltlReleaseExp538); 
            	    RELEASE7_tree = (CommonTree)adaptor.create(RELEASE7);
            	    root_0 = (CommonTree)adaptor.becomeRoot(RELEASE7_tree, root_0);

            	    pushFollow(FOLLOW_ltlBooleanOrExp_in_ltlReleaseExp541);
            	    ltlBooleanOrExp8=ltlBooleanOrExp();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ltlBooleanOrExp8.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlReleaseExp"

    public static class ltlBooleanOrExp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlBooleanOrExp"
    // LTLExpression.g:106:1: ltlBooleanOrExp : ltlBooleanAndExp ( LTLOR ltlBooleanAndExp )* ;
    public final LTLExpressionParser.ltlBooleanOrExp_return ltlBooleanOrExp() throws RecognitionException {
        LTLExpressionParser.ltlBooleanOrExp_return retval = new LTLExpressionParser.ltlBooleanOrExp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LTLOR10=null;
        LTLExpressionParser.ltlBooleanAndExp_return ltlBooleanAndExp9 = null;

        LTLExpressionParser.ltlBooleanAndExp_return ltlBooleanAndExp11 = null;


        CommonTree LTLOR10_tree=null;

        try {
            // LTLExpression.g:106:18: ( ltlBooleanAndExp ( LTLOR ltlBooleanAndExp )* )
            // LTLExpression.g:106:20: ltlBooleanAndExp ( LTLOR ltlBooleanAndExp )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlBooleanAndExp_in_ltlBooleanOrExp551);
            ltlBooleanAndExp9=ltlBooleanAndExp();

            state._fsp--;

            adaptor.addChild(root_0, ltlBooleanAndExp9.getTree());
            // LTLExpression.g:106:37: ( LTLOR ltlBooleanAndExp )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==LTLOR) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // LTLExpression.g:106:38: LTLOR ltlBooleanAndExp
            	    {
            	    LTLOR10=(Token)match(input,LTLOR,FOLLOW_LTLOR_in_ltlBooleanOrExp554); 
            	    LTLOR10_tree = (CommonTree)adaptor.create(LTLOR10);
            	    root_0 = (CommonTree)adaptor.becomeRoot(LTLOR10_tree, root_0);

            	    pushFollow(FOLLOW_ltlBooleanAndExp_in_ltlBooleanOrExp557);
            	    ltlBooleanAndExp11=ltlBooleanAndExp();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ltlBooleanAndExp11.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlBooleanOrExp"

    public static class ltlBooleanAndExp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlBooleanAndExp"
    // LTLExpression.g:107:1: ltlBooleanAndExp : ltlBooleanEquivExp ( LTLAND ltlBooleanEquivExp )* ;
    public final LTLExpressionParser.ltlBooleanAndExp_return ltlBooleanAndExp() throws RecognitionException {
        LTLExpressionParser.ltlBooleanAndExp_return retval = new LTLExpressionParser.ltlBooleanAndExp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LTLAND13=null;
        LTLExpressionParser.ltlBooleanEquivExp_return ltlBooleanEquivExp12 = null;

        LTLExpressionParser.ltlBooleanEquivExp_return ltlBooleanEquivExp14 = null;


        CommonTree LTLAND13_tree=null;

        try {
            // LTLExpression.g:107:18: ( ltlBooleanEquivExp ( LTLAND ltlBooleanEquivExp )* )
            // LTLExpression.g:107:20: ltlBooleanEquivExp ( LTLAND ltlBooleanEquivExp )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlBooleanEquivExp_in_ltlBooleanAndExp566);
            ltlBooleanEquivExp12=ltlBooleanEquivExp();

            state._fsp--;

            adaptor.addChild(root_0, ltlBooleanEquivExp12.getTree());
            // LTLExpression.g:107:39: ( LTLAND ltlBooleanEquivExp )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==LTLAND) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // LTLExpression.g:107:40: LTLAND ltlBooleanEquivExp
            	    {
            	    LTLAND13=(Token)match(input,LTLAND,FOLLOW_LTLAND_in_ltlBooleanAndExp569); 
            	    LTLAND13_tree = (CommonTree)adaptor.create(LTLAND13);
            	    root_0 = (CommonTree)adaptor.becomeRoot(LTLAND13_tree, root_0);

            	    pushFollow(FOLLOW_ltlBooleanEquivExp_in_ltlBooleanAndExp572);
            	    ltlBooleanEquivExp14=ltlBooleanEquivExp();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ltlBooleanEquivExp14.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlBooleanAndExp"

    public static class ltlBooleanEquivExp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlBooleanEquivExp"
    // LTLExpression.g:108:1: ltlBooleanEquivExp : ltlBooleanImplExp ( EQUIVALENCE ltlBooleanImplExp )* ;
    public final LTLExpressionParser.ltlBooleanEquivExp_return ltlBooleanEquivExp() throws RecognitionException {
        LTLExpressionParser.ltlBooleanEquivExp_return retval = new LTLExpressionParser.ltlBooleanEquivExp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EQUIVALENCE16=null;
        LTLExpressionParser.ltlBooleanImplExp_return ltlBooleanImplExp15 = null;

        LTLExpressionParser.ltlBooleanImplExp_return ltlBooleanImplExp17 = null;


        CommonTree EQUIVALENCE16_tree=null;

        try {
            // LTLExpression.g:108:20: ( ltlBooleanImplExp ( EQUIVALENCE ltlBooleanImplExp )* )
            // LTLExpression.g:108:22: ltlBooleanImplExp ( EQUIVALENCE ltlBooleanImplExp )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlBooleanImplExp_in_ltlBooleanEquivExp581);
            ltlBooleanImplExp15=ltlBooleanImplExp();

            state._fsp--;

            adaptor.addChild(root_0, ltlBooleanImplExp15.getTree());
            // LTLExpression.g:108:40: ( EQUIVALENCE ltlBooleanImplExp )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==EQUIVALENCE) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // LTLExpression.g:108:41: EQUIVALENCE ltlBooleanImplExp
            	    {
            	    EQUIVALENCE16=(Token)match(input,EQUIVALENCE,FOLLOW_EQUIVALENCE_in_ltlBooleanEquivExp584); 
            	    EQUIVALENCE16_tree = (CommonTree)adaptor.create(EQUIVALENCE16);
            	    root_0 = (CommonTree)adaptor.becomeRoot(EQUIVALENCE16_tree, root_0);

            	    pushFollow(FOLLOW_ltlBooleanImplExp_in_ltlBooleanEquivExp587);
            	    ltlBooleanImplExp17=ltlBooleanImplExp();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ltlBooleanImplExp17.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlBooleanEquivExp"

    public static class ltlBooleanImplExp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlBooleanImplExp"
    // LTLExpression.g:109:1: ltlBooleanImplExp : ltlunary ( IMPLICATION ltlunary )* ;
    public final LTLExpressionParser.ltlBooleanImplExp_return ltlBooleanImplExp() throws RecognitionException {
        LTLExpressionParser.ltlBooleanImplExp_return retval = new LTLExpressionParser.ltlBooleanImplExp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token IMPLICATION19=null;
        LTLExpressionParser.ltlunary_return ltlunary18 = null;

        LTLExpressionParser.ltlunary_return ltlunary20 = null;


        CommonTree IMPLICATION19_tree=null;

        try {
            // LTLExpression.g:109:19: ( ltlunary ( IMPLICATION ltlunary )* )
            // LTLExpression.g:109:23: ltlunary ( IMPLICATION ltlunary )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlunary_in_ltlBooleanImplExp598);
            ltlunary18=ltlunary();

            state._fsp--;

            adaptor.addChild(root_0, ltlunary18.getTree());
            // LTLExpression.g:109:32: ( IMPLICATION ltlunary )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==IMPLICATION) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // LTLExpression.g:109:33: IMPLICATION ltlunary
            	    {
            	    IMPLICATION19=(Token)match(input,IMPLICATION,FOLLOW_IMPLICATION_in_ltlBooleanImplExp601); 
            	    IMPLICATION19_tree = (CommonTree)adaptor.create(IMPLICATION19);
            	    root_0 = (CommonTree)adaptor.becomeRoot(IMPLICATION19_tree, root_0);

            	    pushFollow(FOLLOW_ltlunary_in_ltlBooleanImplExp604);
            	    ltlunary20=ltlunary();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ltlunary20.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlBooleanImplExp"

    public static class ltlunary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlunary"
    // LTLExpression.g:110:1: ltlunary : ( ltlformula | ( LTLNOT | NEXT | FINALLY | GLOBALLY ) ltlformula );
    public final LTLExpressionParser.ltlunary_return ltlunary() throws RecognitionException {
        LTLExpressionParser.ltlunary_return retval = new LTLExpressionParser.ltlunary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LTLNOT22=null;
        Token NEXT23=null;
        Token FINALLY24=null;
        Token GLOBALLY25=null;
        LTLExpressionParser.ltlformula_return ltlformula21 = null;

        LTLExpressionParser.ltlformula_return ltlformula26 = null;


        CommonTree LTLNOT22_tree=null;
        CommonTree NEXT23_tree=null;
        CommonTree FINALLY24_tree=null;
        CommonTree GLOBALLY25_tree=null;

        try {
            // LTLExpression.g:110:11: ( ltlformula | ( LTLNOT | NEXT | FINALLY | GLOBALLY ) ltlformula )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==EVAL||LA8_0==STATUS||(LA8_0>=OUTCOME && LA8_0<=CONDITION)||LA8_0==LTLBOOL||LA8_0==47) ) {
                alt8=1;
            }
            else if ( (LA8_0==LTLNOT||(LA8_0>=GLOBALLY && LA8_0<=NEXT)) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // LTLExpression.g:110:13: ltlformula
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ltlformula_in_ltlunary614);
                    ltlformula21=ltlformula();

                    state._fsp--;

                    adaptor.addChild(root_0, ltlformula21.getTree());

                    }
                    break;
                case 2 :
                    // LTLExpression.g:110:26: ( LTLNOT | NEXT | FINALLY | GLOBALLY ) ltlformula
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // LTLExpression.g:110:26: ( LTLNOT | NEXT | FINALLY | GLOBALLY )
                    int alt7=4;
                    switch ( input.LA(1) ) {
                    case LTLNOT:
                        {
                        alt7=1;
                        }
                        break;
                    case NEXT:
                        {
                        alt7=2;
                        }
                        break;
                    case FINALLY:
                        {
                        alt7=3;
                        }
                        break;
                    case GLOBALLY:
                        {
                        alt7=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 7, 0, input);

                        throw nvae;
                    }

                    switch (alt7) {
                        case 1 :
                            // LTLExpression.g:110:27: LTLNOT
                            {
                            LTLNOT22=(Token)match(input,LTLNOT,FOLLOW_LTLNOT_in_ltlunary619); 
                            LTLNOT22_tree = (CommonTree)adaptor.create(LTLNOT22);
                            root_0 = (CommonTree)adaptor.becomeRoot(LTLNOT22_tree, root_0);


                            }
                            break;
                        case 2 :
                            // LTLExpression.g:110:35: NEXT
                            {
                            NEXT23=(Token)match(input,NEXT,FOLLOW_NEXT_in_ltlunary622); 
                            NEXT23_tree = (CommonTree)adaptor.create(NEXT23);
                            root_0 = (CommonTree)adaptor.becomeRoot(NEXT23_tree, root_0);


                            }
                            break;
                        case 3 :
                            // LTLExpression.g:110:41: FINALLY
                            {
                            FINALLY24=(Token)match(input,FINALLY,FOLLOW_FINALLY_in_ltlunary625); 
                            FINALLY24_tree = (CommonTree)adaptor.create(FINALLY24);
                            root_0 = (CommonTree)adaptor.becomeRoot(FINALLY24_tree, root_0);


                            }
                            break;
                        case 4 :
                            // LTLExpression.g:110:50: GLOBALLY
                            {
                            GLOBALLY25=(Token)match(input,GLOBALLY,FOLLOW_GLOBALLY_in_ltlunary628); 
                            GLOBALLY25_tree = (CommonTree)adaptor.create(GLOBALLY25);
                            root_0 = (CommonTree)adaptor.becomeRoot(GLOBALLY25_tree, root_0);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_ltlformula_in_ltlunary632);
                    ltlformula26=ltlformula();

                    state._fsp--;

                    adaptor.addChild(root_0, ltlformula26.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlunary"

    public static class ltlformula_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlformula"
    // LTLExpression.g:111:1: ltlformula : ( '(' ltlUntilExp ')' | ltlvalue | ( EVAL ) '(' plexilexp ')' );
    public final LTLExpressionParser.ltlformula_return ltlformula() throws RecognitionException {
        LTLExpressionParser.ltlformula_return retval = new LTLExpressionParser.ltlformula_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal27=null;
        Token char_literal29=null;
        Token EVAL31=null;
        Token char_literal32=null;
        Token char_literal34=null;
        LTLExpressionParser.ltlUntilExp_return ltlUntilExp28 = null;

        LTLExpressionParser.ltlvalue_return ltlvalue30 = null;

        LTLExpressionParser.plexilexp_return plexilexp33 = null;


        CommonTree char_literal27_tree=null;
        CommonTree char_literal29_tree=null;
        CommonTree EVAL31_tree=null;
        CommonTree char_literal32_tree=null;
        CommonTree char_literal34_tree=null;

        try {
            // LTLExpression.g:111:13: ( '(' ltlUntilExp ')' | ltlvalue | ( EVAL ) '(' plexilexp ')' )
            int alt9=3;
            switch ( input.LA(1) ) {
            case 47:
                {
                alt9=1;
                }
                break;
            case STATUS:
            case OUTCOME:
            case CONDITION:
            case LTLBOOL:
                {
                alt9=2;
                }
                break;
            case EVAL:
                {
                alt9=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // LTLExpression.g:111:15: '(' ltlUntilExp ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal27=(Token)match(input,47,FOLLOW_47_in_ltlformula640); 
                    pushFollow(FOLLOW_ltlUntilExp_in_ltlformula643);
                    ltlUntilExp28=ltlUntilExp();

                    state._fsp--;

                    adaptor.addChild(root_0, ltlUntilExp28.getTree());
                    char_literal29=(Token)match(input,48,FOLLOW_48_in_ltlformula645); 

                    }
                    break;
                case 2 :
                    // LTLExpression.g:111:37: ltlvalue
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ltlvalue_in_ltlformula648);
                    ltlvalue30=ltlvalue();

                    state._fsp--;

                    adaptor.addChild(root_0, ltlvalue30.getTree());

                    }
                    break;
                case 3 :
                    // LTLExpression.g:111:46: ( EVAL ) '(' plexilexp ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // LTLExpression.g:111:46: ( EVAL )
                    // LTLExpression.g:111:47: EVAL
                    {
                    EVAL31=(Token)match(input,EVAL,FOLLOW_EVAL_in_ltlformula651); 
                    EVAL31_tree = (CommonTree)adaptor.create(EVAL31);
                    root_0 = (CommonTree)adaptor.becomeRoot(EVAL31_tree, root_0);


                    }

                    char_literal32=(Token)match(input,47,FOLLOW_47_in_ltlformula654); 
                    pushFollow(FOLLOW_plexilexp_in_ltlformula656);
                    plexilexp33=plexilexp();

                    state._fsp--;

                    adaptor.addChild(root_0, plexilexp33.getTree());
                    char_literal34=(Token)match(input,48,FOLLOW_48_in_ltlformula657); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlformula"

    public static class plexilexp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "plexilexp"
    // LTLExpression.g:113:1: plexilexp : booleanOrExpression ;
    public final LTLExpressionParser.plexilexp_return plexilexp() throws RecognitionException {
        LTLExpressionParser.plexilexp_return retval = new LTLExpressionParser.plexilexp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        LTLExpressionParser.booleanOrExpression_return booleanOrExpression35 = null;



        try {
            // LTLExpression.g:113:12: ( booleanOrExpression )
            // LTLExpression.g:113:15: booleanOrExpression
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_booleanOrExpression_in_plexilexp668);
            booleanOrExpression35=booleanOrExpression();

            state._fsp--;

            adaptor.addChild(root_0, booleanOrExpression35.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "plexilexp"

    public static class booleanOrExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "booleanOrExpression"
    // LTLExpression.g:114:1: booleanOrExpression : booleanAndExpression ( OR booleanAndExpression )* ;
    public final LTLExpressionParser.booleanOrExpression_return booleanOrExpression() throws RecognitionException {
        LTLExpressionParser.booleanOrExpression_return retval = new LTLExpressionParser.booleanOrExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token OR37=null;
        LTLExpressionParser.booleanAndExpression_return booleanAndExpression36 = null;

        LTLExpressionParser.booleanAndExpression_return booleanAndExpression38 = null;


        CommonTree OR37_tree=null;

        try {
            // LTLExpression.g:114:22: ( booleanAndExpression ( OR booleanAndExpression )* )
            // LTLExpression.g:114:24: booleanAndExpression ( OR booleanAndExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_booleanAndExpression_in_booleanOrExpression676);
            booleanAndExpression36=booleanAndExpression();

            state._fsp--;

            adaptor.addChild(root_0, booleanAndExpression36.getTree());
            // LTLExpression.g:114:45: ( OR booleanAndExpression )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==OR) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // LTLExpression.g:114:46: OR booleanAndExpression
            	    {
            	    OR37=(Token)match(input,OR,FOLLOW_OR_in_booleanOrExpression679); 
            	    OR37_tree = (CommonTree)adaptor.create(OR37);
            	    root_0 = (CommonTree)adaptor.becomeRoot(OR37_tree, root_0);

            	    pushFollow(FOLLOW_booleanAndExpression_in_booleanOrExpression682);
            	    booleanAndExpression38=booleanAndExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, booleanAndExpression38.getTree());

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "booleanOrExpression"

    public static class booleanAndExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "booleanAndExpression"
    // LTLExpression.g:115:1: booleanAndExpression : equalityExpression ( AND equalityExpression )* ;
    public final LTLExpressionParser.booleanAndExpression_return booleanAndExpression() throws RecognitionException {
        LTLExpressionParser.booleanAndExpression_return retval = new LTLExpressionParser.booleanAndExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token AND40=null;
        LTLExpressionParser.equalityExpression_return equalityExpression39 = null;

        LTLExpressionParser.equalityExpression_return equalityExpression41 = null;


        CommonTree AND40_tree=null;

        try {
            // LTLExpression.g:115:25: ( equalityExpression ( AND equalityExpression )* )
            // LTLExpression.g:115:27: equalityExpression ( AND equalityExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_equalityExpression_in_booleanAndExpression695);
            equalityExpression39=equalityExpression();

            state._fsp--;

            adaptor.addChild(root_0, equalityExpression39.getTree());
            // LTLExpression.g:115:46: ( AND equalityExpression )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==AND) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // LTLExpression.g:115:47: AND equalityExpression
            	    {
            	    AND40=(Token)match(input,AND,FOLLOW_AND_in_booleanAndExpression698); 
            	    AND40_tree = (CommonTree)adaptor.create(AND40);
            	    root_0 = (CommonTree)adaptor.becomeRoot(AND40_tree, root_0);

            	    pushFollow(FOLLOW_equalityExpression_in_booleanAndExpression701);
            	    equalityExpression41=equalityExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, equalityExpression41.getTree());

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "booleanAndExpression"

    public static class equalityExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "equalityExpression"
    // LTLExpression.g:116:1: equalityExpression : relationalExpression ( ( EQUALS | NOTEQUALS ) relationalExpression )* ;
    public final LTLExpressionParser.equalityExpression_return equalityExpression() throws RecognitionException {
        LTLExpressionParser.equalityExpression_return retval = new LTLExpressionParser.equalityExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set43=null;
        LTLExpressionParser.relationalExpression_return relationalExpression42 = null;

        LTLExpressionParser.relationalExpression_return relationalExpression44 = null;


        CommonTree set43_tree=null;

        try {
            // LTLExpression.g:116:21: ( relationalExpression ( ( EQUALS | NOTEQUALS ) relationalExpression )* )
            // LTLExpression.g:116:23: relationalExpression ( ( EQUALS | NOTEQUALS ) relationalExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_relationalExpression_in_equalityExpression711);
            relationalExpression42=relationalExpression();

            state._fsp--;

            adaptor.addChild(root_0, relationalExpression42.getTree());
            // LTLExpression.g:116:44: ( ( EQUALS | NOTEQUALS ) relationalExpression )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=EQUALS && LA12_0<=NOTEQUALS)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // LTLExpression.g:116:45: ( EQUALS | NOTEQUALS ) relationalExpression
            	    {
            	    set43=(Token)input.LT(1);
            	    set43=(Token)input.LT(1);
            	    if ( (input.LA(1)>=EQUALS && input.LA(1)<=NOTEQUALS) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set43), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_relationalExpression_in_equalityExpression721);
            	    relationalExpression44=relationalExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, relationalExpression44.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "equalityExpression"

    public static class relationalExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relationalExpression"
    // LTLExpression.g:117:1: relationalExpression : additiveExpression ( ( LT | LTEQ | GT | GTEQ ) additiveExpression )* ;
    public final LTLExpressionParser.relationalExpression_return relationalExpression() throws RecognitionException {
        LTLExpressionParser.relationalExpression_return retval = new LTLExpressionParser.relationalExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set46=null;
        LTLExpressionParser.additiveExpression_return additiveExpression45 = null;

        LTLExpressionParser.additiveExpression_return additiveExpression47 = null;


        CommonTree set46_tree=null;

        try {
            // LTLExpression.g:117:23: ( additiveExpression ( ( LT | LTEQ | GT | GTEQ ) additiveExpression )* )
            // LTLExpression.g:117:25: additiveExpression ( ( LT | LTEQ | GT | GTEQ ) additiveExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_additiveExpression_in_relationalExpression732);
            additiveExpression45=additiveExpression();

            state._fsp--;

            adaptor.addChild(root_0, additiveExpression45.getTree());
            // LTLExpression.g:117:44: ( ( LT | LTEQ | GT | GTEQ ) additiveExpression )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=LT && LA13_0<=GTEQ)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // LTLExpression.g:117:46: ( LT | LTEQ | GT | GTEQ ) additiveExpression
            	    {
            	    set46=(Token)input.LT(1);
            	    set46=(Token)input.LT(1);
            	    if ( (input.LA(1)>=LT && input.LA(1)<=GTEQ) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set46), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression747);
            	    additiveExpression47=additiveExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, additiveExpression47.getTree());

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "relationalExpression"

    public static class additiveExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "additiveExpression"
    // LTLExpression.g:118:1: additiveExpression : multiplicativeExpression ( ( PLUS | MINUS ) multiplicativeExpression )* ;
    public final LTLExpressionParser.additiveExpression_return additiveExpression() throws RecognitionException {
        LTLExpressionParser.additiveExpression_return retval = new LTLExpressionParser.additiveExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set49=null;
        LTLExpressionParser.multiplicativeExpression_return multiplicativeExpression48 = null;

        LTLExpressionParser.multiplicativeExpression_return multiplicativeExpression50 = null;


        CommonTree set49_tree=null;

        try {
            // LTLExpression.g:118:21: ( multiplicativeExpression ( ( PLUS | MINUS ) multiplicativeExpression )* )
            // LTLExpression.g:118:23: multiplicativeExpression ( ( PLUS | MINUS ) multiplicativeExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression758);
            multiplicativeExpression48=multiplicativeExpression();

            state._fsp--;

            adaptor.addChild(root_0, multiplicativeExpression48.getTree());
            // LTLExpression.g:118:48: ( ( PLUS | MINUS ) multiplicativeExpression )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>=PLUS && LA14_0<=MINUS)) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // LTLExpression.g:118:50: ( PLUS | MINUS ) multiplicativeExpression
            	    {
            	    set49=(Token)input.LT(1);
            	    set49=(Token)input.LT(1);
            	    if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set49), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression769);
            	    multiplicativeExpression50=multiplicativeExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, multiplicativeExpression50.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "additiveExpression"

    public static class multiplicativeExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "multiplicativeExpression"
    // LTLExpression.g:119:1: multiplicativeExpression : unaryExpression ( ( MULT | DIV ) unaryExpression )* ;
    public final LTLExpressionParser.multiplicativeExpression_return multiplicativeExpression() throws RecognitionException {
        LTLExpressionParser.multiplicativeExpression_return retval = new LTLExpressionParser.multiplicativeExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set52=null;
        LTLExpressionParser.unaryExpression_return unaryExpression51 = null;

        LTLExpressionParser.unaryExpression_return unaryExpression53 = null;


        CommonTree set52_tree=null;

        try {
            // LTLExpression.g:119:26: ( unaryExpression ( ( MULT | DIV ) unaryExpression )* )
            // LTLExpression.g:119:28: unaryExpression ( ( MULT | DIV ) unaryExpression )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression779);
            unaryExpression51=unaryExpression();

            state._fsp--;

            adaptor.addChild(root_0, unaryExpression51.getTree());
            // LTLExpression.g:119:44: ( ( MULT | DIV ) unaryExpression )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=MULT && LA15_0<=DIV)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // LTLExpression.g:119:46: ( MULT | DIV ) unaryExpression
            	    {
            	    set52=(Token)input.LT(1);
            	    set52=(Token)input.LT(1);
            	    if ( (input.LA(1)>=MULT && input.LA(1)<=DIV) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set52), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression790);
            	    unaryExpression53=unaryExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unaryExpression53.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "multiplicativeExpression"

    public static class unaryExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryExpression"
    // LTLExpression.g:120:1: unaryExpression : ( primaryExpression | ( NOT ) primaryExpression );
    public final LTLExpressionParser.unaryExpression_return unaryExpression() throws RecognitionException {
        LTLExpressionParser.unaryExpression_return retval = new LTLExpressionParser.unaryExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NOT55=null;
        LTLExpressionParser.primaryExpression_return primaryExpression54 = null;

        LTLExpressionParser.primaryExpression_return primaryExpression56 = null;


        CommonTree NOT55_tree=null;

        try {
            // LTLExpression.g:120:18: ( primaryExpression | ( NOT ) primaryExpression )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0>=STAEXT && LA16_0<=NODEVAR)||(LA16_0>=CONDEXT && LA16_0<=BOOLEAN)||LA16_0==47) ) {
                alt16=1;
            }
            else if ( (LA16_0==NOT) ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // LTLExpression.g:120:20: primaryExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_primaryExpression_in_unaryExpression801);
                    primaryExpression54=primaryExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, primaryExpression54.getTree());

                    }
                    break;
                case 2 :
                    // LTLExpression.g:120:40: ( NOT ) primaryExpression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // LTLExpression.g:120:40: ( NOT )
                    // LTLExpression.g:120:41: NOT
                    {
                    NOT55=(Token)match(input,NOT,FOLLOW_NOT_in_unaryExpression806); 
                    NOT55_tree = (CommonTree)adaptor.create(NOT55);
                    root_0 = (CommonTree)adaptor.becomeRoot(NOT55_tree, root_0);


                    }

                    pushFollow(FOLLOW_primaryExpression_in_unaryExpression810);
                    primaryExpression56=primaryExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, primaryExpression56.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "unaryExpression"

    public static class primaryExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "primaryExpression"
    // LTLExpression.g:121:1: primaryExpression : ( '(' plexilexp ')' | value );
    public final LTLExpressionParser.primaryExpression_return primaryExpression() throws RecognitionException {
        LTLExpressionParser.primaryExpression_return retval = new LTLExpressionParser.primaryExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal57=null;
        Token char_literal59=null;
        LTLExpressionParser.plexilexp_return plexilexp58 = null;

        LTLExpressionParser.value_return value60 = null;


        CommonTree char_literal57_tree=null;
        CommonTree char_literal59_tree=null;

        try {
            // LTLExpression.g:121:19: ( '(' plexilexp ')' | value )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==47) ) {
                alt17=1;
            }
            else if ( ((LA17_0>=STAEXT && LA17_0<=NODEVAR)||(LA17_0>=CONDEXT && LA17_0<=BOOLEAN)) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // LTLExpression.g:121:21: '(' plexilexp ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal57=(Token)match(input,47,FOLLOW_47_in_primaryExpression817); 
                    pushFollow(FOLLOW_plexilexp_in_primaryExpression820);
                    plexilexp58=plexilexp();

                    state._fsp--;

                    adaptor.addChild(root_0, plexilexp58.getTree());
                    char_literal59=(Token)match(input,48,FOLLOW_48_in_primaryExpression822); 

                    }
                    break;
                case 2 :
                    // LTLExpression.g:121:43: value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_value_in_primaryExpression827);
                    value60=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value60.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "primaryExpression"

    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // LTLExpression.g:125:1: value : ( INTEGER | FLOAT | BOOLEAN | NODEVAR | STAEXT | CONDEXT );
    public final LTLExpressionParser.value_return value() throws RecognitionException {
        LTLExpressionParser.value_return retval = new LTLExpressionParser.value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set61=null;

        CommonTree set61_tree=null;

        try {
            // LTLExpression.g:126:2: ( INTEGER | FLOAT | BOOLEAN | NODEVAR | STAEXT | CONDEXT )
            // LTLExpression.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set61=(Token)input.LT(1);
            if ( (input.LA(1)>=STAEXT && input.LA(1)<=NODEVAR)||(input.LA(1)>=CONDEXT && input.LA(1)<=BOOLEAN) ) {
                input.consume();
                adaptor.addChild(root_0, (CommonTree)adaptor.create(set61));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class ltlvalue_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlvalue"
    // LTLExpression.g:133:1: ltlvalue : ( LTLBOOL | OUTCOME | CONDITION | STATUS );
    public final LTLExpressionParser.ltlvalue_return ltlvalue() throws RecognitionException {
        LTLExpressionParser.ltlvalue_return retval = new LTLExpressionParser.ltlvalue_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set62=null;

        CommonTree set62_tree=null;

        try {
            // LTLExpression.g:134:2: ( LTLBOOL | OUTCOME | CONDITION | STATUS )
            // LTLExpression.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set62=(Token)input.LT(1);
            if ( input.LA(1)==STATUS||(input.LA(1)>=OUTCOME && input.LA(1)<=CONDITION)||input.LA(1)==LTLBOOL ) {
                input.consume();
                adaptor.addChild(root_0, (CommonTree)adaptor.create(set62));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ltlvalue"

    // Delegated rules


 

    public static final BitSet FOLLOW_ltlUntilExp_in_ltlexp509 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_ltlexp511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ltlReleaseExp_in_ltlUntilExp520 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_UNTIL_in_ltlUntilExp522 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlReleaseExp_in_ltlUntilExp525 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_ltlBooleanOrExp_in_ltlReleaseExp535 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_RELEASE_in_ltlReleaseExp538 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlBooleanOrExp_in_ltlReleaseExp541 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_ltlBooleanAndExp_in_ltlBooleanOrExp551 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_LTLOR_in_ltlBooleanOrExp554 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlBooleanAndExp_in_ltlBooleanOrExp557 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_ltlBooleanEquivExp_in_ltlBooleanAndExp566 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_LTLAND_in_ltlBooleanAndExp569 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlBooleanEquivExp_in_ltlBooleanAndExp572 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_ltlBooleanImplExp_in_ltlBooleanEquivExp581 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_EQUIVALENCE_in_ltlBooleanEquivExp584 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlBooleanImplExp_in_ltlBooleanEquivExp587 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_ltlunary_in_ltlBooleanImplExp598 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_IMPLICATION_in_ltlBooleanImplExp601 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlunary_in_ltlBooleanImplExp604 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_ltlformula_in_ltlunary614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTLNOT_in_ltlunary619 = new BitSet(new long[]{0x0000843240000000L});
    public static final BitSet FOLLOW_NEXT_in_ltlunary622 = new BitSet(new long[]{0x0000843240000000L});
    public static final BitSet FOLLOW_FINALLY_in_ltlunary625 = new BitSet(new long[]{0x0000843240000000L});
    public static final BitSet FOLLOW_GLOBALLY_in_ltlunary628 = new BitSet(new long[]{0x0000843240000000L});
    public static final BitSet FOLLOW_ltlformula_in_ltlunary632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_ltlformula640 = new BitSet(new long[]{0x000084324E400000L});
    public static final BitSet FOLLOW_ltlUntilExp_in_ltlformula643 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_ltlformula645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ltlvalue_in_ltlformula648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EVAL_in_ltlformula651 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_ltlformula654 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_plexilexp_in_ltlformula656 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_ltlformula657 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanOrExpression_in_plexilexp668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanAndExpression_in_booleanOrExpression676 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_OR_in_booleanOrExpression679 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_booleanAndExpression_in_booleanOrExpression682 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_equalityExpression_in_booleanAndExpression695 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_AND_in_booleanAndExpression698 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_equalityExpression_in_booleanAndExpression701 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression711 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_set_in_equalityExpression714 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression721 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression732 = new BitSet(new long[]{0x0000000000007802L});
    public static final BitSet FOLLOW_set_in_relationalExpression736 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression747 = new BitSet(new long[]{0x0000000000007802L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression758 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_set_in_additiveExpression762 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression769 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression779 = new BitSet(new long[]{0x0000000000060002L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression783 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression790 = new BitSet(new long[]{0x0000000000060002L});
    public static final BitSet FOLLOW_primaryExpression_in_unaryExpression801 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryExpression806 = new BitSet(new long[]{0x000083CC00000000L});
    public static final BitSet FOLLOW_primaryExpression_in_unaryExpression810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_primaryExpression817 = new BitSet(new long[]{0x000083CC00080000L});
    public static final BitSet FOLLOW_plexilexp_in_primaryExpression820 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_primaryExpression822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_primaryExpression827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_ltlvalue0 = new BitSet(new long[]{0x0000000000000002L});

}