package org.nianet.plexil5.maude2java.modelchecking.ltlparser;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.stringtemplate.StringTemplate;
import org.nianet.plexil.maude2java.modelchecking.ltlassistant.UnicodeLogicalSymbolsMap;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.Node;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar.LTLPlexil5Lexer;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar.LTLPlexil5Parser;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.antlrgrammar.ParsingRuntimeException;


public class LTLTreeGenerator {

	private static final String eqpattern="==";
	
	
	
	public static Tree parseAndGetTree(String expression) throws MalformedLTLExpressionException{

    	LTLPlexil5Lexer lex;
		try {
			lex = new LTLPlexil5Lexer(new ANTLRInputStream(new ByteArrayInputStream(expression.getBytes())));
		} catch (IOException e1) {
			throw new RuntimeException("LTL Tree generator internal error:"+e1,e1);
		}
        CommonTokenStream tokens = new CommonTokenStream(lex);

        LTLPlexil5Parser g = new LTLPlexil5Parser(tokens,  null);

        try {
        	LTLPlexil5Parser.ltl_return er=g.ltl();
        	Object o=er.getTree();
        	if (o instanceof CommonTree){
        		return (CommonTree)o;	
        	}
        	else{
        		throw new RuntimeException("LTL Tree generator internal error: invalid tree type:"+o.getClass());
        	}        	
        } catch (RecognitionException e) {
        	throw new MalformedLTLExpressionException(e);
        } catch (ParsingRuntimeException e){
        	throw new MalformedLTLExpressionException(e.getTokenNames(),e.getCause());
        }
        
        
	}
	
	
	public static String generateInfixExpression(Tree root){
		if (root.getChildCount()==0){
			//return root.getText();
			
			
			return (root.getText());
		}
		else if (root.getChildCount()==1){
			//unary operator
			return root.getText()+"("+generateInfixExpression(root.getChild(0))+")";
		}
		else if (root.getChildCount()==2){
			//binary operator

			return "("+generateInfixExpression(root.getChild(0))+") "+root.getText()+" ("+generateInfixExpression(root.getChild(1))+")";
		}
		else{
			throw new RuntimeException("Error on expression generation: only N-ary trees, where N<=2 are allowed");
		}
	}
	
	
	

	public static String generateMaudeExpression(Tree root) throws MalformedLTLExpressionException{
		if (root.getChildCount()==0){
			String s=replaceSpecialSymbols(root.getText());
			return toMaudeSyntax(s);
			//return replaceSpecialSymbols(toMaudeSyntax(root.getText()));
		}
		else if (root.getChildCount()==1){
			//unary operator

			if (root.getText().equals("eval")){
				return ("eval-exp"+"("+generateMaudeExpression(root.getChild(0))+")");
			}
			else{
				return replaceSpecialSymbols(root.getText()+"("+generateMaudeExpression(root.getChild(0))+")");	
			}			
		}
		else if (root.getChildCount()==2){
			//binary operator
			return replaceSpecialSymbols("("+generateMaudeExpression(root.getChild(0))+") "+binOpToMaudeSyntax(root.getText())+" ("+generateMaudeExpression(root.getChild(1))+")");
		}
		else{
			throw new RuntimeException("Error on expression generation: only N-ary trees, where N<=2 are allowed");
		}
	}

	
	
	private static String replaceSpecialSymbols(String expression){
	
		
		StringTemplate expt=new StringTemplate(expression);
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLAND, "/\\");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLEQUIVALENT, "<=>");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLFINALLY, "<>");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLGLOBALLY, "[]");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLNEXT, "O");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLNOT, "~");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLOR, "\\/");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLRELEASE, "R");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLUNTIL, "U");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLIMPLICATION, "->");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLTRUE, "True");
		expt.setAttribute(UnicodeLogicalSymbolsMap._LTLFALSE, "False");
		//expt.setAttribute(UnicodeLogicalSymbolsMap._EQUALS, "equ");
		expt.setAttribute(UnicodeLogicalSymbolsMap._NOTEQUALS, "nequ");	
		expt.setAttribute(UnicodeLogicalSymbolsMap._NOT, "not");
		
		return expt.toString();
	}
	
	
	private static String binOpToMaudeSyntax(String op){

		Matcher m=Pattern.compile(eqpattern).matcher(op);
		if (m.matches()){
			return "equ";
		}
		
		return op;
	}
	
	
		
	
	private static String toMaudeSyntax(String terminal) throws MalformedLTLExpressionException{		
		
		return Plx2MaudeTranslationStrategyBuilder.buildTranslationStrategy(terminal).translate();

	}

}



class Plx2MaudeTranslationStrategyBuilder{
	
	static Set<Class<? extends Plx2MaudeTranslationStrategy>> maudeSyntaxTransClasses = new LinkedHashSet<Class<? extends Plx2MaudeTranslationStrategy>>();
	
	static{
		maudeSyntaxTransClasses.add(StatusMaudeTranslationStrategy.class);
		maudeSyntaxTransClasses.add(ExtStatusMaudeTranslationStrategy.class);
		maudeSyntaxTransClasses.add(NodeOutcomeMaudeTranslationStrategy.class);
		maudeSyntaxTransClasses.add(NodeConditionTranslationStrategy.class);
		maudeSyntaxTransClasses.add(PLXLiteralMaudeTranslationStrategy.class);
		maudeSyntaxTransClasses.add(LTLLiteralMaudeTranslationStrategy.class);
		maudeSyntaxTransClasses.add(NodeVariableMaudeTranslationStrategy.class);
		
		
	}
	                                    
	public static Plx2MaudeTranslationStrategy buildTranslationStrategy(String terminal) throws MalformedLTLExpressionException{
		for (Class<? extends Plx2MaudeTranslationStrategy> str:maudeSyntaxTransClasses){
			try {
				Plx2MaudeTranslationStrategy st=str.getConstructor(new Class[]{String.class}).newInstance(terminal);
				if (st.matches()) return st;
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Invalid transformation strategy class for the LTLTreeGenerator:"+str.getName(),e);
			} catch (SecurityException e) {
				throw new RuntimeException("Invalid transformation strategy class for the LTLTreeGenerator:"+str.getName(),e);
			} catch (InstantiationException e) {
				throw new RuntimeException("Invalid transformation strategy class for the LTLTreeGenerator:"+str.getName(),e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Invalid transformation strategy class for the LTLTreeGenerator:"+str.getName(),e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Invalid transformation strategy class for the LTLTreeGenerator:"+str.getName(),e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Invalid transformation strategy class for the LTLTreeGenerator:"+str.getName()+". Constructor with a single string argument (terminal) required.",e);
			}
		}
		throw new MalformedLTLExpressionException("No translation strategy found for:"+terminal);
	}
	
}


abstract class Plx2MaudeTranslationStrategy{
	
	protected Matcher matcher;
	protected String pattern;
	protected String terminal;

	public Plx2MaudeTranslationStrategy(String pattern, String terminal) {
		this.pattern = pattern;
		this.terminal = terminal;
		matcher=Pattern.compile(pattern).matcher(terminal);
	}
	
	public boolean matches(){
		return matcher.matches();
	}
	

	public abstract String translate() throws MalformedLTLExpressionException;

}

class StatusMaudeTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String nodeStatePattern="([\\w-]+)\\.<(inactive|waiting|executing|finishing|iterationEnded|failing|finished)>";
	
	public StatusMaudeTranslationStrategy(String terminal) {
		super(nodeStatePattern, terminal);		
	}

	@Override
	public String translate() {		
		String nodeName=matcher.group(1);
		String state=matcher.group(2);			
		return "status("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+","+state+")";
	}
	
}


class NodeConditionTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String nodeConditionPattern="([\\w-]+)\\.<(end|inv|pre|start|post|repeat)>";

	public NodeConditionTranslationStrategy(String terminal) {
		super(nodeConditionPattern, terminal);
	}

	@Override
	public String translate() throws MalformedLTLExpressionException{		
		String nodeName=matcher.group(1);
		String condition=matcher.group(2);
		Node n=PlexilPlan.getLastLoadedPlanNodes().get(nodeName);
		if (n==null){
			throw new MalformedLTLExpressionException("Node "+nodeName+ " wasn't defined on the loaded plexil plan.");
		}
		else{
			return condition+"("+n.getAbsoluteID()+",const(v(true))"+")";
		}
	}
	
}


class PLXLiteralMaudeTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String literalPattern="(true|false)|[0-9]+";
	
	public PLXLiteralMaudeTranslationStrategy(String terminal) {
		super(literalPattern, terminal);
	}

	@Override
	public String translate() {		
		return "const(v("+terminal+"))";
	}
	
}


class LTLLiteralMaudeTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String literalPattern="(True|False)";
	
	public LTLLiteralMaudeTranslationStrategy(String terminal) {
		super(literalPattern, terminal);
	}

	@Override
	public String translate() {		
		return terminal;
	}
	
}


class NodeVariableMaudeTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String nodeVariablePattern="([\\w-]+)\\.([\\w-]+)";
	
	public NodeVariableMaudeTranslationStrategy(String terminal) {
		super(nodeVariablePattern, terminal);
	}

	@Override
	public String translate() throws MalformedLTLExpressionException{		
		String nodeName=matcher.group(1);
		String nodeVar=matcher.group(2);
		Node n=PlexilPlan.getLastLoadedPlanNodes().get(nodeName);
		if (n==null){
			throw new MalformedLTLExpressionException("Node "+nodeName+ " wasn't defined on the loaded plexil plan.");
		}
		else{
			String absNodePath=n.getLexicalScopeVariables().get(nodeVar);
			if (absNodePath==null){
				throw new MalformedLTLExpressionException("Variable "+nodeVar+" was not defined on "+ nodeName+" node.");
			}
			else{
				return "var("+absNodePath+")";
			}
		}
	}
	
}

class ExtStatusMaudeTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String nodeExtStatePattern="([\\w-]+)\\.<(allChildrenInactive|allChildrenWaiting|allChildrenExecuting|allChildrenFinishing|allChildrenIterationEnded|allChildrenFailing|allChildrenFinished)>";
	
	private static Hashtable<String,String> childrenProcessStatusNameEquiv=new Hashtable<String, String>();
	
	static{		
		childrenProcessStatusNameEquiv.put("allChildrenInactive", "inactive");
		childrenProcessStatusNameEquiv.put("allChildrenWaiting", "waiting");
		childrenProcessStatusNameEquiv.put("allChildrenExecuting", "executing");
		childrenProcessStatusNameEquiv.put("allChildrenFinishing", "finishing");
		childrenProcessStatusNameEquiv.put("allChildrenIterationEnded", "iterationEnded");
		childrenProcessStatusNameEquiv.put("allChildrenFailing", "failing");
		childrenProcessStatusNameEquiv.put("allChildrenFinished", "finished");
	}
	
	public ExtStatusMaudeTranslationStrategy(String terminal) {
		super(nodeExtStatePattern, terminal);		
	}

	@Override
	public String translate() {		
		String nodeName=matcher.group(1);
		String state=matcher.group(2);
		String allChildState=childrenProcessStatusNameEquiv.get(state);
		
		return "allChildrenProcessesWithStatus("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+","+allChildState+")";
	}
	
}



class NodeOutcomeMaudeTranslationStrategy extends Plx2MaudeTranslationStrategy{
	
	private static final String nodeOutcome="([\\w-]+)\\.<(noOutcome|skipped|success|parentFails|invFails|preFails|postFails|fail)>";
		
	
	public NodeOutcomeMaudeTranslationStrategy(String terminal) {
		super(nodeOutcome, terminal);		
	}

	@Override
	public String translate() throws MalformedLTLExpressionException {		
		String nodeName=matcher.group(1);
		String outcome=matcher.group(2);
	
		
		if (outcome.equals("noOutcome")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",none)";
		}
		else if (outcome.equals("skipped")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",skipped)";
		}
		else if (outcome.equals("success")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",success)";
		}
		else if (outcome.equals("parentFails")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",fail(parentFail))";
		}
		else if (outcome.equals("invFails")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",fail(invariantFail))";
		}		
		else if (outcome.equals("preFails")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",fail(preconditionFail))";
		}		
		else if (outcome.equals("postFails")){
			return "outcome("+PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().get(nodeName)+",fail(postconditionFail))";
		}		
		else{
			throw new MalformedLTLExpressionException("Invalid outcome value:"+outcome);
		}

	}
	
}
