// $ANTLR 3.3 Nov 30, 2010 12:45:30 LTLPlexil5.g 2011-09-12 22:11:25

	package org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar;
		


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class LTLPlexil5Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DOT", "STI", "STW", "STE", "STF", "STT", "STA", "STN", "ACI", "ACW", "ACE", "ACF", "ACT", "ACA", "ACN", "ONO", "OSK", "OSU", "OPF", "OPI", "OPA", "OPL", "OFA", "END", "PRE", "POS", "RPT", "INV", "TRT", "CAI", "LTLTRUE", "LTLFALSE", "LTLAND", "LTLOR", "LTLNOT", "UNTIL", "RELEASE", "GLOBALLY", "FINALLY", "NEXT", "IMPL", "EQUIV", "EVAL", "SIMPL", "SEQUIV", "PLXTRUE", "PLXFALSE", "PLXAND", "PLXOR", "PLXEQ", "PLXNEQ", "PLXNOT", "LT", "LTEQ", "GT", "GTEQ", "PLUS", "MINUS", "MULT", "DIV", "LPAR", "RPAR", "STATUS", "CONDITION", "NODEVAR", "STAEXT", "OUTCOME", "NAME", "NUMBER", "DIGIT", "WHITESPACE"
    };
    public static final int EOF=-1;
    public static final int DOT=4;
    public static final int STI=5;
    public static final int STW=6;
    public static final int STE=7;
    public static final int STF=8;
    public static final int STT=9;
    public static final int STA=10;
    public static final int STN=11;
    public static final int ACI=12;
    public static final int ACW=13;
    public static final int ACE=14;
    public static final int ACF=15;
    public static final int ACT=16;
    public static final int ACA=17;
    public static final int ACN=18;
    public static final int ONO=19;
    public static final int OSK=20;
    public static final int OSU=21;
    public static final int OPF=22;
    public static final int OPI=23;
    public static final int OPA=24;
    public static final int OPL=25;
    public static final int OFA=26;
    public static final int END=27;
    public static final int PRE=28;
    public static final int POS=29;
    public static final int RPT=30;
    public static final int INV=31;
    public static final int TRT=32;
    public static final int CAI=33;
    public static final int LTLTRUE=34;
    public static final int LTLFALSE=35;
    public static final int LTLAND=36;
    public static final int LTLOR=37;
    public static final int LTLNOT=38;
    public static final int UNTIL=39;
    public static final int RELEASE=40;
    public static final int GLOBALLY=41;
    public static final int FINALLY=42;
    public static final int NEXT=43;
    public static final int IMPL=44;
    public static final int EQUIV=45;
    public static final int EVAL=46;
    public static final int SIMPL=47;
    public static final int SEQUIV=48;
    public static final int PLXTRUE=49;
    public static final int PLXFALSE=50;
    public static final int PLXAND=51;
    public static final int PLXOR=52;
    public static final int PLXEQ=53;
    public static final int PLXNEQ=54;
    public static final int PLXNOT=55;
    public static final int LT=56;
    public static final int LTEQ=57;
    public static final int GT=58;
    public static final int GTEQ=59;
    public static final int PLUS=60;
    public static final int MINUS=61;
    public static final int MULT=62;
    public static final int DIV=63;
    public static final int LPAR=64;
    public static final int RPAR=65;
    public static final int STATUS=66;
    public static final int CONDITION=67;
    public static final int NODEVAR=68;
    public static final int STAEXT=69;
    public static final int OUTCOME=70;
    public static final int NAME=71;
    public static final int NUMBER=72;
    public static final int DIGIT=73;
    public static final int WHITESPACE=74;

    // delegates
    // delegators


        public LTLPlexil5Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LTLPlexil5Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return LTLPlexil5Parser.tokenNames; }
    public String getGrammarFileName() { return "LTLPlexil5.g"; }

                              

        public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e) {
            throw new ParsingRuntimeException(tokenNames,e);
        }



    public static class ltl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltl"
    // LTLPlexil5.g:123:1: ltl : ltlformula EOF ;
    public final LTLPlexil5Parser.ltl_return ltl() throws RecognitionException {
        LTLPlexil5Parser.ltl_return retval = new LTLPlexil5Parser.ltl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF2=null;
        LTLPlexil5Parser.ltlformula_return ltlformula1 = null;


        CommonTree EOF2_tree=null;

        try {
            // LTLPlexil5.g:124:5: ( ltlformula EOF )
            // LTLPlexil5.g:124:7: ltlformula EOF
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlformula_in_ltl1339);
            ltlformula1=ltlformula();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, ltlformula1.getTree());
            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_ltl1341); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "ltl"

    public static class ltlformula_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlformula"
    // LTLPlexil5.g:127:1: ltlformula : ltlliteral ( ltlbinop ltlliteral )* ;
    public final LTLPlexil5Parser.ltlformula_return ltlformula() throws RecognitionException {
        LTLPlexil5Parser.ltlformula_return retval = new LTLPlexil5Parser.ltlformula_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        LTLPlexil5Parser.ltlliteral_return ltlliteral3 = null;

        LTLPlexil5Parser.ltlbinop_return ltlbinop4 = null;

        LTLPlexil5Parser.ltlliteral_return ltlliteral5 = null;



        try {
            // LTLPlexil5.g:128:5: ( ltlliteral ( ltlbinop ltlliteral )* )
            // LTLPlexil5.g:128:7: ltlliteral ( ltlbinop ltlliteral )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_ltlliteral_in_ltlformula1360);
            ltlliteral3=ltlliteral();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, ltlliteral3.getTree());
            // LTLPlexil5.g:128:18: ( ltlbinop ltlliteral )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=LTLAND && LA1_0<=LTLOR)||(LA1_0>=UNTIL && LA1_0<=RELEASE)||(LA1_0>=IMPL && LA1_0<=EQUIV)||(LA1_0>=SIMPL && LA1_0<=SEQUIV)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // LTLPlexil5.g:128:20: ltlbinop ltlliteral
            	    {
            	    pushFollow(FOLLOW_ltlbinop_in_ltlformula1364);
            	    ltlbinop4=ltlbinop();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(ltlbinop4.getTree(), root_0);
            	    pushFollow(FOLLOW_ltlliteral_in_ltlformula1367);
            	    ltlliteral5=ltlliteral();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, ltlliteral5.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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

    public static class ltlliteral_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlliteral"
    // LTLPlexil5.g:131:1: ltlliteral : ( ltlunop )? ltlatom ;
    public final LTLPlexil5Parser.ltlliteral_return ltlliteral() throws RecognitionException {
        LTLPlexil5Parser.ltlliteral_return retval = new LTLPlexil5Parser.ltlliteral_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        LTLPlexil5Parser.ltlunop_return ltlunop6 = null;

        LTLPlexil5Parser.ltlatom_return ltlatom7 = null;



        try {
            // LTLPlexil5.g:132:5: ( ( ltlunop )? ltlatom )
            // LTLPlexil5.g:132:7: ( ltlunop )? ltlatom
            {
            root_0 = (CommonTree)adaptor.nil();

            // LTLPlexil5.g:132:7: ( ltlunop )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==LTLNOT||(LA2_0>=GLOBALLY && LA2_0<=NEXT)) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // LTLPlexil5.g:132:9: ltlunop
                    {
                    pushFollow(FOLLOW_ltlunop_in_ltlliteral1389);
                    ltlunop6=ltlunop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(ltlunop6.getTree(), root_0);

                    }
                    break;

            }

            pushFollow(FOLLOW_ltlatom_in_ltlliteral1395);
            ltlatom7=ltlatom();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, ltlatom7.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "ltlliteral"

    public static class ltlbinop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlbinop"
    // LTLPlexil5.g:135:1: ltlbinop : ( LTLAND | LTLOR | UNTIL | RELEASE | IMPL | EQUIV | SIMPL | SEQUIV );
    public final LTLPlexil5Parser.ltlbinop_return ltlbinop() throws RecognitionException {
        LTLPlexil5Parser.ltlbinop_return retval = new LTLPlexil5Parser.ltlbinop_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set8=null;

        CommonTree set8_tree=null;

        try {
            // LTLPlexil5.g:136:5: ( LTLAND | LTLOR | UNTIL | RELEASE | IMPL | EQUIV | SIMPL | SEQUIV )
            // LTLPlexil5.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set8=(Token)input.LT(1);
            if ( (input.LA(1)>=LTLAND && input.LA(1)<=LTLOR)||(input.LA(1)>=UNTIL && input.LA(1)<=RELEASE)||(input.LA(1)>=IMPL && input.LA(1)<=EQUIV)||(input.LA(1)>=SIMPL && input.LA(1)<=SEQUIV) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set8));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "ltlbinop"

    public static class ltlunop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlunop"
    // LTLPlexil5.g:139:1: ltlunop : ( LTLNOT | GLOBALLY | FINALLY | NEXT );
    public final LTLPlexil5Parser.ltlunop_return ltlunop() throws RecognitionException {
        LTLPlexil5Parser.ltlunop_return retval = new LTLPlexil5Parser.ltlunop_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set9=null;

        CommonTree set9_tree=null;

        try {
            // LTLPlexil5.g:140:5: ( LTLNOT | GLOBALLY | FINALLY | NEXT )
            // LTLPlexil5.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set9=(Token)input.LT(1);
            if ( input.LA(1)==LTLNOT||(input.LA(1)>=GLOBALLY && input.LA(1)<=NEXT) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set9));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "ltlunop"

    public static class ltlatom_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ltlatom"
    // LTLPlexil5.g:143:1: ltlatom : ( STATUS | CONDITION | STAEXT | OUTCOME | EVAL LPAR boolexp RPAR | LTLTRUE | LTLFALSE | LPAR ltlformula RPAR );
    public final LTLPlexil5Parser.ltlatom_return ltlatom() throws RecognitionException {
        LTLPlexil5Parser.ltlatom_return retval = new LTLPlexil5Parser.ltlatom_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token STATUS10=null;
        Token CONDITION11=null;
        Token STAEXT12=null;
        Token OUTCOME13=null;
        Token EVAL14=null;
        Token LPAR15=null;
        Token RPAR17=null;
        Token LTLTRUE18=null;
        Token LTLFALSE19=null;
        Token LPAR20=null;
        Token RPAR22=null;
        LTLPlexil5Parser.boolexp_return boolexp16 = null;

        LTLPlexil5Parser.ltlformula_return ltlformula21 = null;


        CommonTree STATUS10_tree=null;
        CommonTree CONDITION11_tree=null;
        CommonTree STAEXT12_tree=null;
        CommonTree OUTCOME13_tree=null;
        CommonTree EVAL14_tree=null;
        CommonTree LPAR15_tree=null;
        CommonTree RPAR17_tree=null;
        CommonTree LTLTRUE18_tree=null;
        CommonTree LTLFALSE19_tree=null;
        CommonTree LPAR20_tree=null;
        CommonTree RPAR22_tree=null;

        try {
            // LTLPlexil5.g:144:5: ( STATUS | CONDITION | STAEXT | OUTCOME | EVAL LPAR boolexp RPAR | LTLTRUE | LTLFALSE | LPAR ltlformula RPAR )
            int alt3=8;
            switch ( input.LA(1) ) {
            case STATUS:
                {
                alt3=1;
                }
                break;
            case CONDITION:
                {
                alt3=2;
                }
                break;
            case STAEXT:
                {
                alt3=3;
                }
                break;
            case OUTCOME:
                {
                alt3=4;
                }
                break;
            case EVAL:
                {
                alt3=5;
                }
                break;
            case LTLTRUE:
                {
                alt3=6;
                }
                break;
            case LTLFALSE:
                {
                alt3=7;
                }
                break;
            case LPAR:
                {
                alt3=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // LTLPlexil5.g:144:7: STATUS
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    STATUS10=(Token)match(input,STATUS,FOLLOW_STATUS_in_ltlatom1488); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STATUS10_tree = (CommonTree)adaptor.create(STATUS10);
                    adaptor.addChild(root_0, STATUS10_tree);
                    }

                    }
                    break;
                case 2 :
                    // LTLPlexil5.g:144:16: CONDITION
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    CONDITION11=(Token)match(input,CONDITION,FOLLOW_CONDITION_in_ltlatom1492); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    CONDITION11_tree = (CommonTree)adaptor.create(CONDITION11);
                    adaptor.addChild(root_0, CONDITION11_tree);
                    }

                    }
                    break;
                case 3 :
                    // LTLPlexil5.g:144:28: STAEXT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    STAEXT12=(Token)match(input,STAEXT,FOLLOW_STAEXT_in_ltlatom1496); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STAEXT12_tree = (CommonTree)adaptor.create(STAEXT12);
                    adaptor.addChild(root_0, STAEXT12_tree);
                    }

                    }
                    break;
                case 4 :
                    // LTLPlexil5.g:144:37: OUTCOME
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    OUTCOME13=(Token)match(input,OUTCOME,FOLLOW_OUTCOME_in_ltlatom1500); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    OUTCOME13_tree = (CommonTree)adaptor.create(OUTCOME13);
                    adaptor.addChild(root_0, OUTCOME13_tree);
                    }

                    }
                    break;
                case 5 :
                    // LTLPlexil5.g:145:7: EVAL LPAR boolexp RPAR
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    EVAL14=(Token)match(input,EVAL,FOLLOW_EVAL_in_ltlatom1508); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EVAL14_tree = (CommonTree)adaptor.create(EVAL14);
                    root_0 = (CommonTree)adaptor.becomeRoot(EVAL14_tree, root_0);
                    }
                    LPAR15=(Token)match(input,LPAR,FOLLOW_LPAR_in_ltlatom1511); if (state.failed) return retval;
                    pushFollow(FOLLOW_boolexp_in_ltlatom1514);
                    boolexp16=boolexp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp16.getTree());
                    RPAR17=(Token)match(input,RPAR,FOLLOW_RPAR_in_ltlatom1516); if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // LTLPlexil5.g:146:7: LTLTRUE
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LTLTRUE18=(Token)match(input,LTLTRUE,FOLLOW_LTLTRUE_in_ltlatom1525); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LTLTRUE18_tree = (CommonTree)adaptor.create(LTLTRUE18);
                    adaptor.addChild(root_0, LTLTRUE18_tree);
                    }

                    }
                    break;
                case 7 :
                    // LTLPlexil5.g:146:17: LTLFALSE
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LTLFALSE19=(Token)match(input,LTLFALSE,FOLLOW_LTLFALSE_in_ltlatom1529); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LTLFALSE19_tree = (CommonTree)adaptor.create(LTLFALSE19);
                    adaptor.addChild(root_0, LTLFALSE19_tree);
                    }

                    }
                    break;
                case 8 :
                    // LTLPlexil5.g:147:7: LPAR ltlformula RPAR
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LPAR20=(Token)match(input,LPAR,FOLLOW_LPAR_in_ltlatom1537); if (state.failed) return retval;
                    pushFollow(FOLLOW_ltlformula_in_ltlatom1540);
                    ltlformula21=ltlformula();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ltlformula21.getTree());
                    RPAR22=(Token)match(input,RPAR,FOLLOW_RPAR_in_ltlatom1542); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "ltlatom"

    public static class pred_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pred"
    // LTLPlexil5.g:150:1: pred : ( STI | STW | STE | STF | STT | STA | STN | ACI | ACW | ACE | ACF | ACT | ACA | ACN | ONO | OSK | OSU | OPF | OPI | OPA | OPL | OFA | END | PRE | POS | RPT | INV | TRT | CAI );
    public final LTLPlexil5Parser.pred_return pred() throws RecognitionException {
        LTLPlexil5Parser.pred_return retval = new LTLPlexil5Parser.pred_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set23=null;

        CommonTree set23_tree=null;

        try {
            // LTLPlexil5.g:151:5: ( STI | STW | STE | STF | STT | STA | STN | ACI | ACW | ACE | ACF | ACT | ACA | ACN | ONO | OSK | OSU | OPF | OPI | OPA | OPL | OFA | END | PRE | POS | RPT | INV | TRT | CAI )
            // LTLPlexil5.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set23=(Token)input.LT(1);
            if ( (input.LA(1)>=STI && input.LA(1)<=CAI) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set23));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "pred"

    public static class qualified_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "qualified"
    // LTLPlexil5.g:157:1: qualified : NAME ( DOT NAME )* ;
    public final LTLPlexil5Parser.qualified_return qualified() throws RecognitionException {
        LTLPlexil5Parser.qualified_return retval = new LTLPlexil5Parser.qualified_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME24=null;
        Token DOT25=null;
        Token NAME26=null;

        CommonTree NAME24_tree=null;
        CommonTree DOT25_tree=null;
        CommonTree NAME26_tree=null;

        try {
            // LTLPlexil5.g:158:5: ( NAME ( DOT NAME )* )
            // LTLPlexil5.g:158:7: NAME ( DOT NAME )*
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME24=(Token)match(input,NAME,FOLLOW_NAME_in_qualified1707); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME24_tree = (CommonTree)adaptor.create(NAME24);
            adaptor.addChild(root_0, NAME24_tree);
            }
            // LTLPlexil5.g:158:12: ( DOT NAME )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==DOT) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // LTLPlexil5.g:158:14: DOT NAME
            	    {
            	    DOT25=(Token)match(input,DOT,FOLLOW_DOT_in_qualified1711); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    DOT25_tree = (CommonTree)adaptor.create(DOT25);
            	    adaptor.addChild(root_0, DOT25_tree);
            	    }
            	    NAME26=(Token)match(input,NAME,FOLLOW_NAME_in_qualified1713); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    NAME26_tree = (CommonTree)adaptor.create(NAME26);
            	    adaptor.addChild(root_0, NAME26_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "qualified"

    public static class boolexp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolexp"
    // LTLPlexil5.g:161:1: boolexp : boolterm ( boolbinop boolterm )* ;
    public final LTLPlexil5Parser.boolexp_return boolexp() throws RecognitionException {
        LTLPlexil5Parser.boolexp_return retval = new LTLPlexil5Parser.boolexp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        LTLPlexil5Parser.boolterm_return boolterm27 = null;

        LTLPlexil5Parser.boolbinop_return boolbinop28 = null;

        LTLPlexil5Parser.boolterm_return boolterm29 = null;



        try {
            // LTLPlexil5.g:162:5: ( boolterm ( boolbinop boolterm )* )
            // LTLPlexil5.g:162:7: boolterm ( boolbinop boolterm )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_boolterm_in_boolexp1734);
            boolterm27=boolterm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolterm27.getTree());
            // LTLPlexil5.g:162:16: ( boolbinop boolterm )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=PLXAND && LA5_0<=PLXNEQ)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // LTLPlexil5.g:162:18: boolbinop boolterm
            	    {
            	    pushFollow(FOLLOW_boolbinop_in_boolexp1738);
            	    boolbinop28=boolbinop();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(boolbinop28.getTree(), root_0);
            	    pushFollow(FOLLOW_boolterm_in_boolexp1741);
            	    boolterm29=boolterm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolterm29.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "boolexp"

    public static class boolterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolterm"
    // LTLPlexil5.g:165:1: boolterm : ( PLXNOT )? boolnumexp ;
    public final LTLPlexil5Parser.boolterm_return boolterm() throws RecognitionException {
        LTLPlexil5Parser.boolterm_return retval = new LTLPlexil5Parser.boolterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token PLXNOT30=null;
        LTLPlexil5Parser.boolnumexp_return boolnumexp31 = null;


        CommonTree PLXNOT30_tree=null;

        try {
            // LTLPlexil5.g:166:5: ( ( PLXNOT )? boolnumexp )
            // LTLPlexil5.g:166:7: ( PLXNOT )? boolnumexp
            {
            root_0 = (CommonTree)adaptor.nil();

            // LTLPlexil5.g:166:7: ( PLXNOT )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==PLXNOT) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // LTLPlexil5.g:166:9: PLXNOT
                    {
                    PLXNOT30=(Token)match(input,PLXNOT,FOLLOW_PLXNOT_in_boolterm1763); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PLXNOT30_tree = (CommonTree)adaptor.create(PLXNOT30);
                    root_0 = (CommonTree)adaptor.becomeRoot(PLXNOT30_tree, root_0);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_boolnumexp_in_boolterm1769);
            boolnumexp31=boolnumexp();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolnumexp31.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "boolterm"

    public static class boolnumexp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolnumexp"
    // LTLPlexil5.g:169:1: boolnumexp : ( PLXTRUE | PLXFALSE | numexp numrelbinop numexp | NODEVAR | LPAR boolexp RPAR );
    public final LTLPlexil5Parser.boolnumexp_return boolnumexp() throws RecognitionException {
        LTLPlexil5Parser.boolnumexp_return retval = new LTLPlexil5Parser.boolnumexp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token PLXTRUE32=null;
        Token PLXFALSE33=null;
        Token NODEVAR37=null;
        Token LPAR38=null;
        Token RPAR40=null;
        LTLPlexil5Parser.numexp_return numexp34 = null;

        LTLPlexil5Parser.numrelbinop_return numrelbinop35 = null;

        LTLPlexil5Parser.numexp_return numexp36 = null;

        LTLPlexil5Parser.boolexp_return boolexp39 = null;


        CommonTree PLXTRUE32_tree=null;
        CommonTree PLXFALSE33_tree=null;
        CommonTree NODEVAR37_tree=null;
        CommonTree LPAR38_tree=null;
        CommonTree RPAR40_tree=null;

        try {
            // LTLPlexil5.g:170:5: ( PLXTRUE | PLXFALSE | numexp numrelbinop numexp | NODEVAR | LPAR boolexp RPAR )
            int alt7=5;
            switch ( input.LA(1) ) {
            case PLXTRUE:
                {
                alt7=1;
                }
                break;
            case PLXFALSE:
                {
                alt7=2;
                }
                break;
            case MINUS:
            case NUMBER:
                {
                alt7=3;
                }
                break;
            case NODEVAR:
                {
                int LA7_5 = input.LA(2);

                if ( (synpred53_LTLPlexil5()) ) {
                    alt7=3;
                }
                else if ( (synpred54_LTLPlexil5()) ) {
                    alt7=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 5, input);

                    throw nvae;
                }
                }
                break;
            case LPAR:
                {
                int LA7_6 = input.LA(2);

                if ( (synpred53_LTLPlexil5()) ) {
                    alt7=3;
                }
                else if ( (true) ) {
                    alt7=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 6, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // LTLPlexil5.g:170:7: PLXTRUE
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    PLXTRUE32=(Token)match(input,PLXTRUE,FOLLOW_PLXTRUE_in_boolnumexp1786); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PLXTRUE32_tree = (CommonTree)adaptor.create(PLXTRUE32);
                    adaptor.addChild(root_0, PLXTRUE32_tree);
                    }

                    }
                    break;
                case 2 :
                    // LTLPlexil5.g:170:17: PLXFALSE
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    PLXFALSE33=(Token)match(input,PLXFALSE,FOLLOW_PLXFALSE_in_boolnumexp1790); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PLXFALSE33_tree = (CommonTree)adaptor.create(PLXFALSE33);
                    adaptor.addChild(root_0, PLXFALSE33_tree);
                    }

                    }
                    break;
                case 3 :
                    // LTLPlexil5.g:171:7: numexp numrelbinop numexp
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_numexp_in_boolnumexp1798);
                    numexp34=numexp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numexp34.getTree());
                    pushFollow(FOLLOW_numrelbinop_in_boolnumexp1800);
                    numrelbinop35=numrelbinop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(numrelbinop35.getTree(), root_0);
                    pushFollow(FOLLOW_numexp_in_boolnumexp1803);
                    numexp36=numexp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numexp36.getTree());

                    }
                    break;
                case 4 :
                    // LTLPlexil5.g:172:7: NODEVAR
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NODEVAR37=(Token)match(input,NODEVAR,FOLLOW_NODEVAR_in_boolnumexp1811); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NODEVAR37_tree = (CommonTree)adaptor.create(NODEVAR37);
                    adaptor.addChild(root_0, NODEVAR37_tree);
                    }

                    }
                    break;
                case 5 :
                    // LTLPlexil5.g:173:7: LPAR boolexp RPAR
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LPAR38=(Token)match(input,LPAR,FOLLOW_LPAR_in_boolnumexp1820); if (state.failed) return retval;
                    pushFollow(FOLLOW_boolexp_in_boolnumexp1823);
                    boolexp39=boolexp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolexp39.getTree());
                    RPAR40=(Token)match(input,RPAR,FOLLOW_RPAR_in_boolnumexp1825); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "boolnumexp"

    public static class numrelbinop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numrelbinop"
    // LTLPlexil5.g:176:1: numrelbinop : ( PLXEQ | PLXNEQ | LT | LTEQ | GT | GTEQ );
    public final LTLPlexil5Parser.numrelbinop_return numrelbinop() throws RecognitionException {
        LTLPlexil5Parser.numrelbinop_return retval = new LTLPlexil5Parser.numrelbinop_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set41=null;

        CommonTree set41_tree=null;

        try {
            // LTLPlexil5.g:177:5: ( PLXEQ | PLXNEQ | LT | LTEQ | GT | GTEQ )
            // LTLPlexil5.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set41=(Token)input.LT(1);
            if ( (input.LA(1)>=PLXEQ && input.LA(1)<=PLXNEQ)||(input.LA(1)>=LT && input.LA(1)<=GTEQ) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set41));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "numrelbinop"

    public static class boolbinop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolbinop"
    // LTLPlexil5.g:180:1: boolbinop : ( PLXAND | PLXOR | PLXEQ | PLXNEQ );
    public final LTLPlexil5Parser.boolbinop_return boolbinop() throws RecognitionException {
        LTLPlexil5Parser.boolbinop_return retval = new LTLPlexil5Parser.boolbinop_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set42=null;

        CommonTree set42_tree=null;

        try {
            // LTLPlexil5.g:181:5: ( PLXAND | PLXOR | PLXEQ | PLXNEQ )
            // LTLPlexil5.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set42=(Token)input.LT(1);
            if ( (input.LA(1)>=PLXAND && input.LA(1)<=PLXNEQ) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set42));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "boolbinop"

    public static class numexp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numexp"
    // LTLPlexil5.g:184:1: numexp : numterm ( ( PLUS | MINUS ) numterm )* ;
    public final LTLPlexil5Parser.numexp_return numexp() throws RecognitionException {
        LTLPlexil5Parser.numexp_return retval = new LTLPlexil5Parser.numexp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set44=null;
        LTLPlexil5Parser.numterm_return numterm43 = null;

        LTLPlexil5Parser.numterm_return numterm45 = null;


        CommonTree set44_tree=null;

        try {
            // LTLPlexil5.g:185:5: ( numterm ( ( PLUS | MINUS ) numterm )* )
            // LTLPlexil5.g:185:7: numterm ( ( PLUS | MINUS ) numterm )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_numterm_in_numexp1909);
            numterm43=numterm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, numterm43.getTree());
            // LTLPlexil5.g:185:15: ( ( PLUS | MINUS ) numterm )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>=PLUS && LA8_0<=MINUS)) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // LTLPlexil5.g:185:17: ( PLUS | MINUS ) numterm
            	    {
            	    set44=(Token)input.LT(1);
            	    set44=(Token)input.LT(1);
            	    if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set44), root_0);
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_numterm_in_numexp1924);
            	    numterm45=numterm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, numterm45.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "numexp"

    public static class numterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numterm"
    // LTLPlexil5.g:188:1: numterm : numfactor ( ( MULT | DIV ) numfactor )* ;
    public final LTLPlexil5Parser.numterm_return numterm() throws RecognitionException {
        LTLPlexil5Parser.numterm_return retval = new LTLPlexil5Parser.numterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set47=null;
        LTLPlexil5Parser.numfactor_return numfactor46 = null;

        LTLPlexil5Parser.numfactor_return numfactor48 = null;


        CommonTree set47_tree=null;

        try {
            // LTLPlexil5.g:189:5: ( numfactor ( ( MULT | DIV ) numfactor )* )
            // LTLPlexil5.g:189:7: numfactor ( ( MULT | DIV ) numfactor )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_numfactor_in_numterm1944);
            numfactor46=numfactor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, numfactor46.getTree());
            // LTLPlexil5.g:189:17: ( ( MULT | DIV ) numfactor )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>=MULT && LA9_0<=DIV)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // LTLPlexil5.g:189:19: ( MULT | DIV ) numfactor
            	    {
            	    set47=(Token)input.LT(1);
            	    set47=(Token)input.LT(1);
            	    if ( (input.LA(1)>=MULT && input.LA(1)<=DIV) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set47), root_0);
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_numfactor_in_numterm1959);
            	    numfactor48=numfactor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, numfactor48.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "numterm"

    public static class numfactor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numfactor"
    // LTLPlexil5.g:192:1: numfactor : ( ( MINUS )? NUMBER | NODEVAR | LPAR numexp RPAR );
    public final LTLPlexil5Parser.numfactor_return numfactor() throws RecognitionException {
        LTLPlexil5Parser.numfactor_return retval = new LTLPlexil5Parser.numfactor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token MINUS49=null;
        Token NUMBER50=null;
        Token NODEVAR51=null;
        Token LPAR52=null;
        Token RPAR54=null;
        LTLPlexil5Parser.numexp_return numexp53 = null;


        CommonTree MINUS49_tree=null;
        CommonTree NUMBER50_tree=null;
        CommonTree NODEVAR51_tree=null;
        CommonTree LPAR52_tree=null;
        CommonTree RPAR54_tree=null;

        try {
            // LTLPlexil5.g:193:5: ( ( MINUS )? NUMBER | NODEVAR | LPAR numexp RPAR )
            int alt11=3;
            switch ( input.LA(1) ) {
            case MINUS:
            case NUMBER:
                {
                alt11=1;
                }
                break;
            case NODEVAR:
                {
                alt11=2;
                }
                break;
            case LPAR:
                {
                alt11=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // LTLPlexil5.g:193:7: ( MINUS )? NUMBER
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    // LTLPlexil5.g:193:7: ( MINUS )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==MINUS) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // LTLPlexil5.g:0:0: MINUS
                            {
                            MINUS49=(Token)match(input,MINUS,FOLLOW_MINUS_in_numfactor1979); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            MINUS49_tree = (CommonTree)adaptor.create(MINUS49);
                            adaptor.addChild(root_0, MINUS49_tree);
                            }

                            }
                            break;

                    }

                    NUMBER50=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_numfactor1982); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMBER50_tree = (CommonTree)adaptor.create(NUMBER50);
                    adaptor.addChild(root_0, NUMBER50_tree);
                    }

                    }
                    break;
                case 2 :
                    // LTLPlexil5.g:194:7: NODEVAR
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NODEVAR51=(Token)match(input,NODEVAR,FOLLOW_NODEVAR_in_numfactor1990); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NODEVAR51_tree = (CommonTree)adaptor.create(NODEVAR51);
                    adaptor.addChild(root_0, NODEVAR51_tree);
                    }

                    }
                    break;
                case 3 :
                    // LTLPlexil5.g:195:7: LPAR numexp RPAR
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LPAR52=(Token)match(input,LPAR,FOLLOW_LPAR_in_numfactor1999); if (state.failed) return retval;
                    pushFollow(FOLLOW_numexp_in_numfactor2002);
                    numexp53=numexp();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numexp53.getTree());
                    RPAR54=(Token)match(input,RPAR,FOLLOW_RPAR_in_numfactor2004); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
    // $ANTLR end "numfactor"

    // $ANTLR start synpred53_LTLPlexil5
    public final void synpred53_LTLPlexil5_fragment() throws RecognitionException {   
        // LTLPlexil5.g:171:7: ( numexp numrelbinop numexp )
        // LTLPlexil5.g:171:7: numexp numrelbinop numexp
        {
        pushFollow(FOLLOW_numexp_in_synpred53_LTLPlexil51798);
        numexp();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_numrelbinop_in_synpred53_LTLPlexil51800);
        numrelbinop();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_numexp_in_synpred53_LTLPlexil51803);
        numexp();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred53_LTLPlexil5

    // $ANTLR start synpred54_LTLPlexil5
    public final void synpred54_LTLPlexil5_fragment() throws RecognitionException {   
        // LTLPlexil5.g:172:7: ( NODEVAR )
        // LTLPlexil5.g:172:7: NODEVAR
        {
        match(input,NODEVAR,FOLLOW_NODEVAR_in_synpred54_LTLPlexil51811); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred54_LTLPlexil5

    // Delegated rules

    public final boolean synpred54_LTLPlexil5() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred54_LTLPlexil5_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred53_LTLPlexil5() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_LTLPlexil5_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_ltlformula_in_ltl1339 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_ltl1341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ltlliteral_in_ltlformula1360 = new BitSet(new long[]{0x0001B1B000000002L});
    public static final BitSet FOLLOW_ltlbinop_in_ltlformula1364 = new BitSet(new long[]{0x00004E4C00000000L,0x000000000000006DL});
    public static final BitSet FOLLOW_ltlliteral_in_ltlformula1367 = new BitSet(new long[]{0x0001B1B000000002L});
    public static final BitSet FOLLOW_ltlunop_in_ltlliteral1389 = new BitSet(new long[]{0x00004E4C00000000L,0x000000000000006DL});
    public static final BitSet FOLLOW_ltlatom_in_ltlliteral1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_ltlbinop0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_ltlunop0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATUS_in_ltlatom1488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONDITION_in_ltlatom1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAEXT_in_ltlatom1496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OUTCOME_in_ltlatom1500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EVAL_in_ltlatom1508 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_LPAR_in_ltlatom1511 = new BitSet(new long[]{0x2086000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_boolexp_in_ltlatom1514 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_RPAR_in_ltlatom1516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTLTRUE_in_ltlatom1525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTLFALSE_in_ltlatom1529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_ltlatom1537 = new BitSet(new long[]{0x00004E4C00000000L,0x000000000000006DL});
    public static final BitSet FOLLOW_ltlformula_in_ltlatom1540 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_RPAR_in_ltlatom1542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_pred0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_qualified1707 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_DOT_in_qualified1711 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_NAME_in_qualified1713 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_boolterm_in_boolexp1734 = new BitSet(new long[]{0x0078000000000002L});
    public static final BitSet FOLLOW_boolbinop_in_boolexp1738 = new BitSet(new long[]{0x2086000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_boolterm_in_boolexp1741 = new BitSet(new long[]{0x0078000000000002L});
    public static final BitSet FOLLOW_PLXNOT_in_boolterm1763 = new BitSet(new long[]{0x2086000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_boolnumexp_in_boolterm1769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLXTRUE_in_boolnumexp1786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLXFALSE_in_boolnumexp1790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numexp_in_boolnumexp1798 = new BitSet(new long[]{0x0F60000000000000L});
    public static final BitSet FOLLOW_numrelbinop_in_boolnumexp1800 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_numexp_in_boolnumexp1803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NODEVAR_in_boolnumexp1811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_boolnumexp1820 = new BitSet(new long[]{0x2086000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_boolexp_in_boolnumexp1823 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_RPAR_in_boolnumexp1825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_numrelbinop0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_boolbinop0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numterm_in_numexp1909 = new BitSet(new long[]{0x3000000000000002L});
    public static final BitSet FOLLOW_set_in_numexp1913 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_numterm_in_numexp1924 = new BitSet(new long[]{0x3000000000000002L});
    public static final BitSet FOLLOW_numfactor_in_numterm1944 = new BitSet(new long[]{0xC000000000000002L});
    public static final BitSet FOLLOW_set_in_numterm1948 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_numfactor_in_numterm1959 = new BitSet(new long[]{0xC000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_numfactor1979 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_NUMBER_in_numfactor1982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NODEVAR_in_numfactor1990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAR_in_numfactor1999 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_numexp_in_numfactor2002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_RPAR_in_numfactor2004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numexp_in_synpred53_LTLPlexil51798 = new BitSet(new long[]{0x0F60000000000000L});
    public static final BitSet FOLLOW_numrelbinop_in_synpred53_LTLPlexil51800 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000111L});
    public static final BitSet FOLLOW_numexp_in_synpred53_LTLPlexil51803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NODEVAR_in_synpred54_LTLPlexil51811 = new BitSet(new long[]{0x0000000000000002L});

}