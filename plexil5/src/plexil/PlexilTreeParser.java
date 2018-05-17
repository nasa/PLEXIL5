// $ANTLR 2.7.6 (20060405): "plexil.tree.g" -> "PlexilTreeParser.java"$

package plexil;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Vector;

import net.n3.nanoxml.*;

import plexil.PlexilASTNode;
import plexil.PlexilDataType;
import plexil.PlexilGlobalDeclaration;
import plexil.PlexilNameType;


import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


public class PlexilTreeParser extends antlr.TreeParser       implements PlexilTreeParserTokenTypes
 {

    static final int PLEXIL_MAJOR_RELEASE = 1;
    static final int PLEXIL_MINOR_RELEASE = 0;
    static final int PLEXIL_PARSER_RELEASE = 0;

    PlexilParserState state = null;
    PlexilNodeContext context = PlexilGlobalContext.getGlobalContext();
    NumberFormat numberFormat = NumberFormat.getInstance();

    public PlexilTreeParser(PlexilParserState inState)
    {
        super();
        state = inState;
    }

    // For the convenience of the plan editor ONLY!!

    public void setContext(PlexilNodeContext ctxt)
    {
        context = ctxt;
    }

    public PlexilParserState getState() 
    {
        return state;
    }

    protected Boolean hasChild(IXMLElement xnode, String name) 
    {
		if (!xnode.hasChildren())
		  return false;
        java.util.Enumeration<IXMLElement> children = xnode.enumerateChildren();
        while (children.hasMoreElements()) {
		    IXMLElement child = children.nextElement();
			if (name.equals(child.getName()))
			    return true;
		}
		return false;
    }

	protected void copyPosition(IXMLElement object, AST n) throws ClassCastException {
		if(!(n instanceof PlexilASTNode))
			throw new ClassCastException("cannot copy position from AST!");
		PlexilASTNode node = (PlexilASTNode) n;
		if ((node!=null) && (object!=null)) {
			if (node.getLine()>=0) {
				object.setAttribute(PlexilXmlStrings.x_line,Integer.toString(node.getLine()));
			}
			if (node.getColumn()>=0) {
			  object.setAttribute(PlexilXmlStrings.x_column,Integer.toString(node.getColumn()));
			}
		}
	}

	protected void copyFilename(IXMLElement object, AST n) throws ClassCastException 
    {
		if(!(n instanceof PlexilASTNode))
			throw new ClassCastException("cannot copy filename from AST!");
		PlexilASTNode node = (PlexilASTNode) n;
		if ((node!=null) && (object!=null)) {
			if (node.getColumn()>=0) {
			  object.setAttribute(PlexilXmlStrings.x_filename,node.getFilename());
			}
		}
    }

    private SemanticException createSemanticException(String message, 
                                                      antlr.collections.AST location)
    {
      PlexilASTNode plexLoc = null;
      if (location != null)
        {
          plexLoc = (PlexilASTNode) location;
        }
      if (plexLoc == null)
        return new SemanticException(message);
      else
        return new SemanticException(message,
                                     plexLoc.getFilename(),
                                     plexLoc.getLine(),
                                     plexLoc.getColumn());
    }

    private int parseInteger(AST the_int)
      throws SemanticException
    {
      int result = 0;
      try
      {
        result = numberFormat.parse(the_int.getText()).intValue();
      }
      catch (java.text.ParseException e)
      {
        throw createSemanticException("Internal error: \""
                                      + the_int.getText()
                                      + "\" is not parsable as an integer",
                                      the_int);
      }
      return result;
    }

  /*
    Methods involving state
  */

  public void reportError(RecognitionException ex)
  {
    state.error(ex);
  }
  public void reportWarning(RecognitionException ex)
  {
    state.warn(ex);
  }

public PlexilTreeParser() {
	tokenNames = _tokenNames;
}

	public final void plexilPlan(AST _t,
		IXMLElement plexilPlan
	) throws RecognitionException {
		
		AST plexilPlan_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t2 = _t;
			AST tmp1_AST_in = (AST)_t;
			match(_t,PLEXIL);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case GLOBAL_DECLARATIONS:
			{
				globalDeclarations(_t,plexilPlan);
				_t = _retTree;
				break;
			}
			case 3:
			case NODE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			_loop5:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NODE)) {
					action(_t,plexilPlan);
					_t = _retTree;
				}
				else {
					break _loop5;
				}
				
			} while (true);
			}
			_t = __t2;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				plexilPlan.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
				if (state.isExtendedPlexil())
				{
				plexilPlan.setAttribute("xsi:noNamespaceSchemaLocation",
							       "http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/extended-plexil.xsd");
				}
				else
				{
				plexilPlan.setAttribute("xsi:noNamespaceSchemaLocation",
							       "http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/core-plexil.xsd");
				}
				plexilPlan.setAttribute("FileName", 
				((PlexilASTNode) plexilPlan_AST_in).getFilename());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void globalDeclarations(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST globalDeclarations_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement globals = new XMLElement("GlobalDeclarations");
		
		try {      // for error handling
			AST __t7 = _t;
			AST tmp2_AST_in = (AST)_t;
			match(_t,GLOBAL_DECLARATIONS);
			_t = _t.getFirstChild();
			{
			int _cnt9=0;
			_loop9:
			do {
				if (_t==null) _t=ASTNULL;
				if (((_t.getType() >= COMMAND_KYWD && _t.getType() <= LIBRARY_NODE_KYWD))) {
					globalDeclaration(_t,globals);
					_t = _retTree;
				}
				else {
					if ( _cnt9>=1 ) { break _loop9; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt9++;
			} while (true);
			}
			_t = __t7;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				// extract declarations from global context, 
				// output in correct order
				Vector commandDecls = new Vector();
				Vector lookupDecls = new Vector();
				PlexilGlobalContext.getGlobalContext().getGlobalDeclarations(commandDecls,
				lookupDecls);
				for (Iterator commandIt = commandDecls.iterator(); commandIt.hasNext(); )
				{
				// process one command declaration
				IXMLElement commandXml = 
				((PlexilGlobalDeclaration) commandIt.next()).getXml();
				globals.addChild(commandXml);
				}
				for (Iterator lookupIt = lookupDecls.iterator(); lookupIt.hasNext(); )
				{
				// process one lookup declaration
				IXMLElement lookupXml = 
				((PlexilGlobalDeclaration) lookupIt.next()).getXml();
				globals.addChild(lookupXml);
				}
				
				parent.addChild(globals); 
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void action(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST action_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST id = null;
		
		XMLElement xnode = null;
		XMLElement xmlResourceList = new XMLElement("ResourceList");
		
		
		try {      // for error handling
			AST __t45 = _t;
			AST tmp3_AST_in = (AST)_t;
			match(_t,NODE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				
				xnode = new XMLElement(action_AST_in.getText());
				parent.addChild(xnode);
				// push node context
				context = ((NodeASTNode) action_AST_in).getContext();
				// get file,, line, col # from token
				xnode.setAttribute("FileName", ((PlexilASTNode) action_AST_in).getFilename());
				xnode.setAttribute("LineNo", (new Integer(action_AST_in.getLine())).toString());
				xnode.setAttribute("ColNo", (new Integer(action_AST_in.getColumn())).toString());
				
				if (action_AST_in.getText() == "Node")
				{
				// initialize node type attribute to empty
				xnode.setAttribute("NodeType", "Empty");
				}
				
			}
			AST __t46 = _t;
			AST tmp4_AST_in = (AST)_t;
			match(_t,NODE_ID);
			_t = _t.getFirstChild();
			id = (AST)_t;
			match(_t,NodeName);
			_t = _t.getNextSibling();
			_t = __t46;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
					IXMLElement nodeId = new XMLElement("NodeId");
					xnode.addChild(nodeId);
					nodeId.setContent(id.getText());
				
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COMMENT_KYWD:
			{
				comment(_t,xnode);
				_t = _retTree;
				break;
			}
			case 3:
			case COMMAND_KYWD:
			case RESOURCE_KYWD:
			case RESOURCE_PRIORITY_KYWD:
			case PRIORITY_KYWD:
			case PERMISSIONS_KYWD:
			case START_CONDITION_KYWD:
			case REPEAT_CONDITION_KYWD:
			case SKIP_CONDITION_KYWD:
			case PRE_CONDITION_KYWD:
			case POST_CONDITION_KYWD:
			case INVARIANT_CONDITION_KYWD:
			case END_CONDITION_KYWD:
			case IN_KYWD:
			case IN_OUT_KYWD:
			case NODE_LIST_KYWD:
			case UPDATE_KYWD:
			case REQUEST_KYWD:
			case LIBRARY_CALL_KYWD:
			case SEQUENCE_KYWD:
			case CONCURRENCE_KYWD:
			case UNCHECKED_SEQUENCE_KYWD:
			case TRY_KYWD:
			case IF_KYWD:
			case WHILE_KYWD:
			case ON_COMMAND_KYWD:
			case ON_MESSAGE_KYWD:
			case FOR_KYWD:
			case VARIABLE_DECLARATION:
			case ARRAY_ASSIGNMENT:
			case BOOLEAN_ASSIGNMENT:
			case INTEGER_ASSIGNMENT:
			case REAL_ASSIGNMENT:
			case STRING_ASSIGNMENT:
			case TIME_ASSIGNMENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			_loop49:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==IN_KYWD||_t.getType()==IN_OUT_KYWD||_t.getType()==VARIABLE_DECLARATION)) {
					nodeDeclaration(_t,xnode);
					_t = _retTree;
				}
				else {
					break _loop49;
				}
				
			} while (true);
			}
			{
			_loop51:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_0.member(_t.getType()))) {
					nodeAttribute(_t,xnode, xmlResourceList);
					_t = _retTree;
				}
				else {
					break _loop51;
				}
				
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COMMAND_KYWD:
			case NODE_LIST_KYWD:
			case UPDATE_KYWD:
			case REQUEST_KYWD:
			case LIBRARY_CALL_KYWD:
			case SEQUENCE_KYWD:
			case CONCURRENCE_KYWD:
			case UNCHECKED_SEQUENCE_KYWD:
			case TRY_KYWD:
			case IF_KYWD:
			case WHILE_KYWD:
			case ON_COMMAND_KYWD:
			case ON_MESSAGE_KYWD:
			case FOR_KYWD:
			case ARRAY_ASSIGNMENT:
			case BOOLEAN_ASSIGNMENT:
			case INTEGER_ASSIGNMENT:
			case REAL_ASSIGNMENT:
			case STRING_ASSIGNMENT:
			case TIME_ASSIGNMENT:
			{
				actionBody(_t,xnode, xmlResourceList);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t45;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				// Add all variable declarations to node
				Vector localVars = new Vector();
				Vector inVars = new Vector();
				Vector inOutVars = new Vector();
				context.getNodeVariables(localVars, inVars, inOutVars);
				
				if (!localVars.isEmpty()) {
				// System.out.println("Adding " + localVars.size() + " variable declarations to node " + #id.getText());
				IXMLElement varDeclXml = new XMLElement("VariableDeclarations");
				xnode.insertChild(varDeclXml, 1);
				for (Iterator it = localVars.iterator(); it.hasNext(); ) {
				PlexilVariableName var = (PlexilVariableName) it.next();
				// System.out.println("Making variable declaration for variable " + var.getName());
				IXMLElement xdecl = var.makeVariableDeclarationElement();
				varDeclXml.addChild(xdecl);
				}
				}
				
				if (!inVars.isEmpty() || !inOutVars.isEmpty())
				{
				IXMLElement intfXml = xnode.getFirstChildNamed("Interface");
				if (intfXml == null)
					 {
					   intfXml = new XMLElement("Interface");
					   xnode.insertChild(intfXml, 0);
					 }
				if (!inVars.isEmpty())
					 {                
					   IXMLElement inXml = new XMLElement("In");                    
					   for (Iterator it = inVars.iterator(); it.hasNext(); )
					     {
					       IXMLElement xdecl =
						 ((PlexilVariableName) it.next()).makeVariableDeclarationElement();
					       inXml.addChild(xdecl);
					     }
					   intfXml.addChild(inXml);
					 }
				if (!inOutVars.isEmpty())
					 {                
					   IXMLElement inOutXml = new XMLElement("InOut");
					   for (Iterator it = inOutVars.iterator(); it.hasNext(); )
					     {
					       IXMLElement xdecl =
						 ((PlexilVariableName) it.next()).makeVariableDeclarationElement();
					       inOutXml.addChild(xdecl);
					     }
					   intfXml.addChild(inOutXml);
					 }
				}
				
				// pop out to parent context on exit
				context = context.getParentContext();
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void globalDeclaration(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST globalDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COMMAND_KYWD:
			{
				commandDeclaration(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			{
				lookupDeclaration(_t,parent);
				_t = _retTree;
				break;
			}
			case LIBRARY_NODE_KYWD:
			{
				libraryNodeDeclaration(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void commandDeclaration(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST commandDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST cn = null;
		AST ret = null;
		AST parms = null;
		IXMLElement decl = new XMLElement("CommandDeclaration");
		
		try {      // for error handling
			AST __t12 = _t;
			AST tmp5_AST_in = (AST)_t;
			match(_t,COMMAND_KYWD);
			_t = _t.getFirstChild();
			cn = _t==ASTNULL ? null : (AST)_t;
			nameLiteral(_t,decl);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case RETURNS_KYWD:
			{
				ret = _t==ASTNULL ? null : (AST)_t;
				returnsSpec(_t,decl);
				_t = _retTree;
				break;
			}
			case 3:
			case PARAMETER_DECLARATIONS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PARAMETER_DECLARATIONS:
			{
				parms = _t==ASTNULL ? null : (AST)_t;
				paramsSpec(_t,decl);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t12;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void lookupDeclaration(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST lookupDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST sn = null;
		AST ret = null;
		AST parms = null;
		IXMLElement decl = new XMLElement("StateDeclaration");
		
		try {      // for error handling
			AST __t16 = _t;
			AST tmp6_AST_in = (AST)_t;
			match(_t,LOOKUP_KYWD);
			_t = _t.getFirstChild();
			sn = _t==ASTNULL ? null : (AST)_t;
			nameLiteral(_t,decl);
			_t = _retTree;
			ret = _t==ASTNULL ? null : (AST)_t;
			returnsSpec(_t,decl);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PARAMETER_DECLARATIONS:
			{
				parms = _t==ASTNULL ? null : (AST)_t;
				paramsSpec(_t,decl);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t16;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void libraryNodeDeclaration(AST _t) throws RecognitionException {
		
		AST libraryNodeDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t36 = _t;
			AST tmp7_AST_in = (AST)_t;
			match(_t,LIBRARY_NODE_KYWD);
			_t = _t.getFirstChild();
			AST tmp8_AST_in = (AST)_t;
			match(_t,NodeName);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PARAMETER_DECLARATIONS:
			{
				libraryInterfaceSpec(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t36;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nameLiteral(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nameLiteral_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		IXMLElement sn = new XMLElement("Name");
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				IXMLElement sv = new XMLElement("StringValue");
				sv.setContent(n.getText());
				sn.addChild(sv);
				parent.addChild(sn);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void returnsSpec(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST returnsSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t27 = _t;
			AST tmp9_AST_in = (AST)_t;
			match(_t,RETURNS_KYWD);
			_t = _t.getFirstChild();
			{
			_loop29:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==RETURN_VALUE)) {
					returnSpec(_t,parent);
					_t = _retTree;
				}
				else {
					break _loop29;
				}
				
			} while (true);
			}
			_t = __t27;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void paramsSpec(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST paramsSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t20 = _t;
			AST tmp10_AST_in = (AST)_t;
			match(_t,PARAMETER_DECLARATIONS);
			_t = _t.getFirstChild();
			{
			_loop22:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PARAMETER)) {
					paramSpec(_t,parent);
					_t = _retTree;
				}
				else {
					break _loop22;
				}
				
			} while (true);
			}
			_t = __t20;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void paramSpec(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST paramSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST tn = null;
		AST pn = null;
		
		try {      // for error handling
			AST __t24 = _t;
			AST tmp11_AST_in = (AST)_t;
			match(_t,PARAMETER);
			_t = _t.getFirstChild();
			tn = _t==ASTNULL ? null : (AST)_t;
			typeName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NCName:
			{
				pn = _t==ASTNULL ? null : (AST)_t;
				paramName(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t24;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				IXMLElement paramXml = new XMLElement("Parameter");
				paramXml.setAttribute("Type", tn.getText());
				if (pn != null)
				paramXml.setContent(pn.getText());
				parent.addChild(paramXml);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void typeName(AST _t) throws RecognitionException {
		
		AST typeName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case BLOB_KYWD:
			{
				AST tmp12_AST_in = (AST)_t;
				match(_t,BLOB_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case BOOLEAN_KYWD:
			{
				AST tmp13_AST_in = (AST)_t;
				match(_t,BOOLEAN_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case INTEGER_KYWD:
			{
				AST tmp14_AST_in = (AST)_t;
				match(_t,INTEGER_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case REAL_KYWD:
			{
				AST tmp15_AST_in = (AST)_t;
				match(_t,REAL_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case STRING_KYWD:
			{
				AST tmp16_AST_in = (AST)_t;
				match(_t,STRING_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case TIME_KYWD:
			{
				AST tmp17_AST_in = (AST)_t;
				match(_t,TIME_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NO_TYPE:
			{
				AST tmp18_AST_in = (AST)_t;
				match(_t,NO_TYPE);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void paramName(AST _t) throws RecognitionException {
		
		AST paramName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST tmp19_AST_in = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void returnSpec(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST returnSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST tn = null;
		AST pn = null;
		
		try {      // for error handling
			AST __t31 = _t;
			AST tmp20_AST_in = (AST)_t;
			match(_t,RETURN_VALUE);
			_t = _t.getFirstChild();
			tn = _t==ASTNULL ? null : (AST)_t;
			typeName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NCName:
			{
				pn = _t==ASTNULL ? null : (AST)_t;
				paramName(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t31;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				IXMLElement rtnXml = new XMLElement("Return");
				rtnXml.setAttribute("Type", tn.getText());
				if (pn != null)
				rtnXml.setContent(pn.getText());
				parent.addChild(rtnXml);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void libraryInterfaceSpec(AST _t) throws RecognitionException {
		
		AST libraryInterfaceSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t39 = _t;
			AST tmp21_AST_in = (AST)_t;
			match(_t,PARAMETER_DECLARATIONS);
			_t = _t.getFirstChild();
			{
			_loop41:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PARAMETER)) {
					libraryParamSpec(_t);
					_t = _retTree;
				}
				else {
					break _loop41;
				}
				
			} while (true);
			}
			_t = __t39;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void libraryParamSpec(AST _t) throws RecognitionException {
		
		AST libraryParamSpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t43 = _t;
			AST tmp22_AST_in = (AST)_t;
			match(_t,PARAMETER);
			_t = _t.getFirstChild();
			typeName(_t);
			_t = _retTree;
			paramName(_t);
			_t = _retTree;
			_t = __t43;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void comment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST comment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST str = null;
		IXMLElement cmnt = new XMLElement("Comment");
		
		try {      // for error handling
			AST __t55 = _t;
			AST tmp23_AST_in = (AST)_t;
			match(_t,COMMENT_KYWD);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				parent.addChild(cmnt);
			}
			str = (AST)_t;
			match(_t,STRING);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				cmnt.setContent(str.getText());
			}
			_t = __t55;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeDeclaration(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST nodeDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IN_KYWD:
			case IN_OUT_KYWD:
			{
				interfaceDeclaration(_t);
				_t = _retTree;
				break;
			}
			case VARIABLE_DECLARATION:
			{
				variableDeclaration(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeAttribute(AST _t,
		IXMLElement node, XMLElement xmlResourceList
	) throws RecognitionException {
		
		AST nodeAttribute_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case START_CONDITION_KYWD:
			{
				startCondition(_t,node);
				_t = _retTree;
				break;
			}
			case REPEAT_CONDITION_KYWD:
			{
				repeatCondition(_t,node);
				_t = _retTree;
				break;
			}
			case SKIP_CONDITION_KYWD:
			{
				skipCondition(_t,node);
				_t = _retTree;
				break;
			}
			case PRE_CONDITION_KYWD:
			{
				preCondition(_t,node);
				_t = _retTree;
				break;
			}
			case POST_CONDITION_KYWD:
			{
				postCondition(_t,node);
				_t = _retTree;
				break;
			}
			case INVARIANT_CONDITION_KYWD:
			{
				invariantCondition(_t,node);
				_t = _retTree;
				break;
			}
			case END_CONDITION_KYWD:
			{
				endCondition(_t,node);
				_t = _retTree;
				break;
			}
			case PRIORITY_KYWD:
			{
				priority(_t,node);
				_t = _retTree;
				break;
			}
			case RESOURCE_KYWD:
			{
				resource(_t,node, xmlResourceList);
				_t = _retTree;
				break;
			}
			case RESOURCE_PRIORITY_KYWD:
			{
				resourcePriority(_t);
				_t = _retTree;
				break;
			}
			case PERMISSIONS_KYWD:
			{
				permissions(_t,node);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void actionBody(AST _t,
		IXMLElement xaction, IXMLElement xmlResourceList
	) throws RecognitionException {
		
		AST actionBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COMMAND_KYWD:
			case NODE_LIST_KYWD:
			case UPDATE_KYWD:
			case REQUEST_KYWD:
			case LIBRARY_CALL_KYWD:
			case ARRAY_ASSIGNMENT:
			case BOOLEAN_ASSIGNMENT:
			case INTEGER_ASSIGNMENT:
			case REAL_ASSIGNMENT:
			case STRING_ASSIGNMENT:
			case TIME_ASSIGNMENT:
			{
				nodeBody(_t,xaction, xmlResourceList);
				_t = _retTree;
				break;
			}
			case SEQUENCE_KYWD:
			{
				sequence(_t,xaction);
				_t = _retTree;
				break;
			}
			case CONCURRENCE_KYWD:
			{
				concurrence(_t,xaction);
				_t = _retTree;
				break;
			}
			case UNCHECKED_SEQUENCE_KYWD:
			{
				uncheckedSequence(_t,xaction);
				_t = _retTree;
				break;
			}
			case TRY_KYWD:
			{
				tryBody(_t,xaction);
				_t = _retTree;
				break;
			}
			case IF_KYWD:
			{
				ifBody(_t,xaction);
				_t = _retTree;
				break;
			}
			case WHILE_KYWD:
			{
				whileBody(_t,xaction);
				_t = _retTree;
				break;
			}
			case FOR_KYWD:
			{
				forBody(_t,xaction);
				_t = _retTree;
				break;
			}
			case ON_COMMAND_KYWD:
			{
				onCommandBody(_t,xaction);
				_t = _retTree;
				break;
			}
			case ON_MESSAGE_KYWD:
			{
				onMessageBody(_t,xaction);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void extendedPlexilActionKywd(AST _t) throws RecognitionException {
		
		AST extendedPlexilActionKywd_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CONCURRENCE_KYWD:
			{
				AST tmp24_AST_in = (AST)_t;
				match(_t,CONCURRENCE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case ON_COMMAND_KYWD:
			{
				AST tmp25_AST_in = (AST)_t;
				match(_t,ON_COMMAND_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case SEQUENCE_KYWD:
			{
				AST tmp26_AST_in = (AST)_t;
				match(_t,SEQUENCE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case TRY_KYWD:
			{
				AST tmp27_AST_in = (AST)_t;
				match(_t,TRY_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case UNCHECKED_SEQUENCE_KYWD:
			{
				AST tmp28_AST_in = (AST)_t;
				match(_t,UNCHECKED_SEQUENCE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void interfaceDeclaration(AST _t) throws RecognitionException {
		
		AST interfaceDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IN_KYWD:
			{
				in(_t);
				_t = _retTree;
				break;
			}
			case IN_OUT_KYWD:
			{
				inOut(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void variableDeclaration(AST _t) throws RecognitionException {
		
		AST variableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t99 = _t;
			AST tmp29_AST_in = (AST)_t;
			match(_t,VARIABLE_DECLARATION);
			_t = _t.getFirstChild();
			{
			_loop101:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case BOOLEAN_KYWD:
				{
					booleanVariableDeclaration(_t);
					_t = _retTree;
					break;
				}
				case INTEGER_KYWD:
				{
					integerVariableDeclaration(_t);
					_t = _retTree;
					break;
				}
				case REAL_KYWD:
				{
					realVariableDeclaration(_t);
					_t = _retTree;
					break;
				}
				case STRING_KYWD:
				{
					stringVariableDeclaration(_t);
					_t = _retTree;
					break;
				}
				case BLOB_KYWD:
				{
					blobVariableDeclaration(_t);
					_t = _retTree;
					break;
				}
				case TIME_KYWD:
				{
					timeVariableDeclaration(_t);
					_t = _retTree;
					break;
				}
				case BOOLEAN_ARRAY:
				{
					booleanArrayDeclaration(_t);
					_t = _retTree;
					break;
				}
				case INTEGER_ARRAY:
				{
					integerArrayDeclaration(_t);
					_t = _retTree;
					break;
				}
				case REAL_ARRAY:
				{
					realArrayDeclaration(_t);
					_t = _retTree;
					break;
				}
				case STRING_ARRAY:
				{
					stringArrayDeclaration(_t);
					_t = _retTree;
					break;
				}
				case BLOB_ARRAY:
				{
					blobArrayDeclaration(_t);
					_t = _retTree;
					break;
				}
				case TIME_ARRAY:
				{
					timeArrayDeclaration(_t);
					_t = _retTree;
					break;
				}
				default:
				{
					break _loop101;
				}
				}
			} while (true);
			}
			_t = __t99;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void startCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST startCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST sc = null;
		AST be = null;
		IXMLElement xsc = new XMLElement("StartCondition");
		
		try {      // for error handling
			AST __t59 = _t;
			sc = _t==ASTNULL ? null :(AST)_t;
			match(_t,START_CONDITION_KYWD);
			_t = _t.getFirstChild();
			be = _t==ASTNULL ? null : (AST)_t;
			booleanExpression(_t,xsc);
			_t = _retTree;
			_t = __t59;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "StartCondition")) {
				throw createSemanticException("Error: \""
					                                 + sc.getText()
													 + "\" attribute is redundant",
													 sc);
				}
				node.addChild(xsc);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void repeatCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST repeatCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST rc = null;
		IXMLElement xrc = new XMLElement("RepeatCondition");
		
		try {      // for error handling
			AST __t61 = _t;
			rc = _t==ASTNULL ? null :(AST)_t;
			match(_t,REPEAT_CONDITION_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xrc);
			_t = _retTree;
			_t = __t61;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "RepeatCondition")) {
				throw createSemanticException("Error: \""
					                                 + rc.getText()
													 + "\" attribute is redundant",
													 rc);
				}
				node.addChild(xrc);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void skipCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST skipCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST sc = null;
		IXMLElement xsc = new XMLElement("SkipCondition");
		
		try {      // for error handling
			AST __t63 = _t;
			sc = _t==ASTNULL ? null :(AST)_t;
			match(_t,SKIP_CONDITION_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xsc);
			_t = _retTree;
			_t = __t63;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "SkipCondition")) {
				throw createSemanticException("Error: \""
					                                 + sc.getText()
													 + "\" attribute is redundant",
													 sc);
				}
				node.addChild(xsc);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void preCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST preCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST pc = null;
		IXMLElement xpc = new XMLElement("PreCondition");
		
		try {      // for error handling
			AST __t65 = _t;
			pc = _t==ASTNULL ? null :(AST)_t;
			match(_t,PRE_CONDITION_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xpc);
			_t = _retTree;
			_t = __t65;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "PreCondition")) {
				throw createSemanticException("Error: \""
					                                 + pc.getText()
													 + "\" attribute is redundant",
													 pc);
				}
				node.addChild(xpc);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void postCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST postCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST pc = null;
		IXMLElement xpc = new XMLElement("PostCondition");
		
		try {      // for error handling
			AST __t67 = _t;
			pc = _t==ASTNULL ? null :(AST)_t;
			match(_t,POST_CONDITION_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xpc);
			_t = _retTree;
			_t = __t67;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "PostCondition")) {
				throw createSemanticException("Error: \""
					                                 + pc.getText()
													 + "\" attribute is redundant",
													 pc);
				}
				node.addChild(xpc);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void invariantCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST invariantCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST ic = null;
		IXMLElement xic = new XMLElement("InvariantCondition");
		
		try {      // for error handling
			AST __t69 = _t;
			ic = _t==ASTNULL ? null :(AST)_t;
			match(_t,INVARIANT_CONDITION_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xic);
			_t = _retTree;
			_t = __t69;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "InvariantCondition")) {
				throw createSemanticException("Error: \""
					                                 + ic.getText()
													 + "\" attribute is redundant",
													 ic);
				}
				node.addChild(xic);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void endCondition(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST endCondition_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST ec = null;
		IXMLElement xec = new XMLElement("EndCondition");
		
		try {      // for error handling
			AST __t71 = _t;
			ec = _t==ASTNULL ? null :(AST)_t;
			match(_t,END_CONDITION_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xec);
			_t = _retTree;
			_t = __t71;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "EndCondition")) {
				throw createSemanticException("Error: \""
					                                 + ec.getText()
													 + "\" attribute is redundant",
													 ec);
				}
				node.addChild(xec);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void priority(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST priority_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST pr = null;
		AST i = null;
		IXMLElement xprio = new XMLElement("Priority");
		
		try {      // for error handling
			AST __t83 = _t;
			pr = _t==ASTNULL ? null :(AST)_t;
			match(_t,PRIORITY_KYWD);
			_t = _t.getFirstChild();
			i = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			_t = __t83;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "Priority")) {
				throw createSemanticException("Error: \""
					                                 + pr.getText()
													 + "\" attribute is redundant",
													 pr);
				} 
				xprio.setContent(i.getText());
				node.addChild(xprio);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void resource(AST _t,
		IXMLElement node, XMLElement resourceList
	) throws RecognitionException {
		
		AST resource_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xmlResource = new XMLElement("Resource");
		IXMLElement xmlName = new XMLElement("ResourceName");
		IXMLElement xmlPrio = new XMLElement("ResourcePriority");
		IXMLElement xmlLowerBound = new XMLElement("ResourceLowerBound");
		IXMLElement xmlUpperBound = new XMLElement("ResourceUpperBound");
		IXMLElement xmlReleaseAtTerm = new XMLElement("ResourceReleaseAtTermination");
		
		
		try {      // for error handling
			AST __t75 = _t;
			AST tmp30_AST_in = (AST)_t;
			match(_t,RESOURCE_KYWD);
			_t = _t.getFirstChild();
			stringExpression(_t,xmlName);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				xmlResource.addChild(xmlName);
			}
			{
			_loop81:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case PRIORITY_KYWD:
				{
					{
					AST tmp31_AST_in = (AST)_t;
					match(_t,PRIORITY_KYWD);
					_t = _t.getNextSibling();
					numericExpression(_t,xmlPrio);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						xmlResource.addChild(xmlPrio);
					}
					}
					break;
				}
				case LOWER_BOUND_KYWD:
				{
					{
					AST tmp32_AST_in = (AST)_t;
					match(_t,LOWER_BOUND_KYWD);
					_t = _t.getNextSibling();
					numericExpression(_t,xmlLowerBound);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						xmlResource.addChild(xmlLowerBound);
					}
					}
					break;
				}
				case UPPER_BOUND_KYWD:
				{
					{
					AST tmp33_AST_in = (AST)_t;
					match(_t,UPPER_BOUND_KYWD);
					_t = _t.getNextSibling();
					numericExpression(_t,xmlUpperBound);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						xmlResource.addChild(xmlUpperBound);
					}
					}
					break;
				}
				case RELEASE_AT_TERM_KYWD:
				{
					{
					AST tmp34_AST_in = (AST)_t;
					match(_t,RELEASE_AT_TERM_KYWD);
					_t = _t.getNextSibling();
					booleanExpression(_t,xmlReleaseAtTerm);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						xmlResource.addChild(xmlReleaseAtTerm);
					}
					}
					break;
				}
				default:
				{
					break _loop81;
				}
				}
			} while (true);
			}
			_t = __t75;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				resourceList.addChild(xmlResource);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void resourcePriority(AST _t) throws RecognitionException {
		
		AST resourcePriority_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xrp = new XMLElement("ResourcePriority");
		
		try {      // for error handling
			AST __t73 = _t;
			AST tmp35_AST_in = (AST)_t;
			match(_t,RESOURCE_PRIORITY_KYWD);
			_t = _t.getFirstChild();
			numericExpression(_t,xrp);
			_t = _retTree;
			_t = __t73;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				context.setResourcePriorityXML(xrp);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void permissions(AST _t,
		IXMLElement node
	) throws RecognitionException {
		
		AST permissions_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST per = null;
		AST s = null;
		IXMLElement xperm = new XMLElement("Permissions");
		
		try {      // for error handling
			AST __t86 = _t;
			per = _t==ASTNULL ? null :(AST)_t;
			match(_t,PERMISSIONS_KYWD);
			_t = _t.getFirstChild();
			s = (AST)_t;
			match(_t,STRING);
			_t = _t.getNextSibling();
			_t = __t86;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (hasChild(node, "Permissions")) {
				throw createSemanticException("Error: \""
					                                 + per.getText()
													 + "\" attribute is redundant",
													 per);
				}
				xperm.setContent(s.getText());
				node.addChild(xperm);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanExpression(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case OR_KYWD:
			{
				or(_t,parent);
				_t = _retTree;
				break;
			}
			case AND_KYWD:
			{
				and(_t,parent);
				_t = _retTree;
				break;
			}
			case BANG:
			{
				not(_t,parent);
				_t = _retTree;
				break;
			}
			case GREATER:
			{
				gt(_t,parent);
				_t = _retTree;
				break;
			}
			case GEQ:
			{
				ge(_t,parent);
				_t = _retTree;
				break;
			}
			case LESS:
			{
				lt(_t,parent);
				_t = _retTree;
				break;
			}
			case LEQ:
			{
				le(_t,parent);
				_t = _retTree;
				break;
			}
			case BOOLEAN_COMPARISON:
			case NODE_STATE_COMPARISON:
			case NODE_OUTCOME_COMPARISON:
			case NODE_COMMAND_HANDLE_COMPARISON:
			case NODE_FAILURE_COMPARISON:
			case NUMERIC_COMPARISON:
			case STRING_COMPARISON:
			case TIME_COMPARISON:
			{
				comparison(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case TRUE_KYWD:
			case FALSE_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			case IS_KNOWN_KYWD:
			case MESSAGE_RECEIVED_KYWD:
			case INT:
			case VARIABLE:
			case ARRAY_REF:
			case NODE_STATE_PREDICATE:
			{
				booleanTerm(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void numericExpression(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST numericExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			case SQRT_KYWD:
			case ABS_KYWD:
			case INT:
			case DOUBLE:
			case VARIABLE:
			case ARRAY_REF:
			case NODE_TIMEPOINT_VALUE:
			case LPAREN:
			{
				numericTerm(_t,parent);
				_t = _retTree;
				break;
			}
			case PLUS:
			{
				add(_t,parent);
				_t = _retTree;
				break;
			}
			case MINUS:
			{
				sub(_t,parent);
				_t = _retTree;
				break;
			}
			case ASTERISK:
			{
				mul(_t,parent);
				_t = _retTree;
				break;
			}
			case SLASH:
			{
				div(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringExpression(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST sv = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CONCAT:
			{
				stringConcatenation(_t,parent);
				_t = _retTree;
				break;
			}
			case STRING:
			{
				sv = _t==ASTNULL ? null : (AST)_t;
				stringValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode) sv).getXmlElement());
				}
				break;
			}
			case VARIABLE:
			{
				stringVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				stringArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			{
				lookupExpr(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nonNegativeInteger(AST _t) throws RecognitionException {
		
		AST nonNegativeInteger_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST tmp36_AST_in = (AST)_t;
			match(_t,INT);
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void in(AST _t) throws RecognitionException {
		
		AST in_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST tn = null;
		
		try {      // for error handling
			AST __t89 = _t;
			AST tmp37_AST_in = (AST)_t;
			match(_t,IN_KYWD);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case BOOLEAN_KYWD:
			case INTEGER_KYWD:
			case REAL_KYWD:
			case STRING_KYWD:
			case BLOB_KYWD:
			case TIME_KYWD:
			case NO_TYPE:
			{
				tn = _t==ASTNULL ? null : (AST)_t;
				typeName(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case NCName:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			_loop92:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NCName)) {
					AST tmp38_AST_in = (AST)_t;
					match(_t,NCName);
					_t = _t.getNextSibling();
				}
				else {
					break _loop92;
				}
				
			} while (true);
			}
			_t = __t89;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				AST var = in_AST_in.getFirstChild();
				PlexilDataType typ = null;
				if (tn != null)
				{
				var = var.getNextSibling(); // skip over type
				typ = PlexilDataType.findByName(tn.getText());
				if (typ == null)
				{
				throw createSemanticException("Internal error: \""
				+ tn.getText()
				+ "\" is an unknown data type in an In statement",
				tn);
				}
				}
				if (context.isLibraryNode())
				{
				if (tn == null)
				{
				throw new SemanticException("Internal error: In statement has no type in library node!");
				}
				}
				while (var != null)
				{
				PlexilVariableName varName = context.findVariable(var.getText());
				if (varName == null)
				{
				throw createSemanticException("Internal error: In variable \""
				+ var.getText()
				+ "\" not found in context",
				var);
				}
				if (varName.isLocal())
				{
				throw createSemanticException("Internal error: In variable \""
				+ var.getText()
				+ "\" is not an interface variable",
				var);
				}
				if (varName.isAssignable())
				{
				throw createSemanticException("Internal error: In variable \""
				+ var.getText()
				+ "\" is assignable",
				var);
				}
				var = var.getNextSibling();
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void inOut(AST _t) throws RecognitionException {
		
		AST inOut_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST tn = null;
		
		try {      // for error handling
			AST __t94 = _t;
			AST tmp39_AST_in = (AST)_t;
			match(_t,IN_OUT_KYWD);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case BOOLEAN_KYWD:
			case INTEGER_KYWD:
			case REAL_KYWD:
			case STRING_KYWD:
			case BLOB_KYWD:
			case TIME_KYWD:
			case NO_TYPE:
			{
				tn = _t==ASTNULL ? null : (AST)_t;
				typeName(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case NCName:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			_loop97:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NCName)) {
					AST tmp40_AST_in = (AST)_t;
					match(_t,NCName);
					_t = _t.getNextSibling();
				}
				else {
					break _loop97;
				}
				
			} while (true);
			}
			_t = __t94;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				AST var = inOut_AST_in.getFirstChild();
				PlexilDataType typ = null;
				if (tn != null)
				{
				var = var.getNextSibling(); // skip over type
				typ = PlexilDataType.findByName(tn.getText());
				if (typ == null)
				{
				throw createSemanticException("Internal error: \""
				+ tn.getText()
				+ "\" is an unknown data type in an InOut statement",
				tn);
				}
				}
				if (context.isLibraryNode())
				{
				if (tn == null)
				{
				throw new SemanticException("Internal error: InOut statement has no type in library node!");
				}
				}
				while (var != null)
				{
				PlexilVariableName varName = context.findVariable(var.getText());
				if (varName == null)
				{
				throw createSemanticException("Internal error: InOut variable \""
				+ var.getText()
				+ "\" not found in context",
				var);
				}
				if (varName.isLocal())
				{
				throw createSemanticException("Internal error: InOut variable \""
				+ var.getText()
				+ "\" is not an interface variable",
				var);
				}
				if (!varName.isAssignable())
				{
				throw createSemanticException("Internal error: InOut variable \""
				+ var.getText()
				+ "\" is not assignable",
				var);
				}
				var = var.getNextSibling();
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanVariableDeclaration(AST _t) throws RecognitionException {
		
		AST booleanVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t103 = _t;
			AST tmp41_AST_in = (AST)_t;
			match(_t,BOOLEAN_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRUE_KYWD:
			case FALSE_KYWD:
			case INT:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				booleanValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t103;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ vn.getText() + "\"",
				vn);
				if (var.getVariableType() != PlexilDataType.BOOLEAN_TYPE)
				throw createSemanticException("Internal error: Boolean variable named \""
				+ vn.getText() + "\" is not Boolean!",
				vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerVariableDeclaration(AST _t) throws RecognitionException {
		
		AST integerVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t113 = _t;
			AST tmp42_AST_in = (AST)_t;
			match(_t,INTEGER_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				integerValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t113;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ vn.getText() + "\"",
				vn);
				if (var.getVariableType() != PlexilDataType.INTEGER_TYPE)
				throw createSemanticException("Internal error: Integer variable named \""
				+ vn.getText() + "\" is not Integer!",
				vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realVariableDeclaration(AST _t) throws RecognitionException {
		
		AST realVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t134 = _t;
			AST tmp43_AST_in = (AST)_t;
			match(_t,REAL_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				realValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t134;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ vn.getText() + "\"",
				vn);
				if (var.getVariableType() != PlexilDataType.REAL_TYPE)
				throw createSemanticException("Internal error: Real variable named \""
				+ vn.getText() + "\" is not Real!",
				vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringVariableDeclaration(AST _t) throws RecognitionException {
		
		AST stringVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t144 = _t;
			AST tmp44_AST_in = (AST)_t;
			match(_t,STRING_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STRING:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				stringValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t144;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ vn.getText() + "\"",
				vn);
				if (var.getVariableType() != PlexilDataType.STRING_TYPE)
				throw createSemanticException("Internal error: String variable named \""
				+ vn.getText() + "\" is not String!",
				vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void blobVariableDeclaration(AST _t) throws RecognitionException {
		
		AST blobVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST pt = null;
		
		try {      // for error handling
			AST __t123 = _t;
			AST tmp45_AST_in = (AST)_t;
			match(_t,BLOB_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case POINTS_TO:
			{
				pt = _t==ASTNULL ? null : (AST)_t;
				pointsTo(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t123;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ vn.getText() + "\"",
				vn);
				if (var.getVariableType() != PlexilDataType.BLOB_TYPE)
				throw createSemanticException("Internal error: BLOB variable named \""
				+ vn.getText() + "\" is not BLOB!",
				vn);
				if (pt != null)
				{
				var.setInitialValue(((PlexilASTNode) pt).getXmlElement());
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeVariableDeclaration(AST _t) throws RecognitionException {
		
		AST timeVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t154 = _t;
			AST tmp46_AST_in = (AST)_t;
			match(_t,TIME_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TIME_VALUE:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				timeValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t154;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ vn.getText() + "\"",
				vn);
				if (var.getVariableType() != PlexilDataType.TIME_TYPE)
				throw createSemanticException("Internal error: Time variable named \""
				+ vn.getText() + "\" is not Time!",
				vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanArrayDeclaration(AST _t) throws RecognitionException {
		
		AST booleanArrayDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST avn = null;
		AST dim = null;
		AST aiv = null;
		
		try {      // for error handling
			AST __t106 = _t;
			AST tmp47_AST_in = (AST)_t;
			match(_t,BOOLEAN_ARRAY);
			_t = _t.getFirstChild();
			avn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			dim = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRUE_KYWD:
			case FALSE_KYWD:
			case INT:
			case BOOLEAN_ARRAY_LITERAL:
			{
				aiv = _t==ASTNULL ? null : (AST)_t;
				booleanArrayInitialValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t106;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(avn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ avn.getText() + "\"",
				avn);
				if (var.getVariableType() != PlexilDataType.BOOLEAN_ARRAY_TYPE)
				throw createSemanticException("Internal error: Boolean array variable named \""
				+ avn.getText() + "\" is not a Boolean array!",
				avn);
				if (aiv != null)
				{
				var.setInitialValue(aiv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerArrayDeclaration(AST _t) throws RecognitionException {
		
		AST integerArrayDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST avn = null;
		AST dim = null;
		AST aiv = null;
		
		try {      // for error handling
			AST __t116 = _t;
			AST tmp48_AST_in = (AST)_t;
			match(_t,INTEGER_ARRAY);
			_t = _t.getFirstChild();
			avn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			dim = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case INTEGER_ARRAY_LITERAL:
			{
				aiv = _t==ASTNULL ? null : (AST)_t;
				integerArrayInitialValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t116;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(avn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ avn.getText() + "\"",
				avn);
				if (var.getVariableType() != PlexilDataType.INTEGER_ARRAY_TYPE)
				throw createSemanticException("Internal error: Integer array variable named \""
				+ avn.getText() + "\" is not an integer array!",
				avn);
				if (aiv != null)
				{
				var.setInitialValue(aiv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realArrayDeclaration(AST _t) throws RecognitionException {
		
		AST realArrayDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST avn = null;
		AST dim = null;
		AST aiv = null;
		
		try {      // for error handling
			AST __t137 = _t;
			AST tmp49_AST_in = (AST)_t;
			match(_t,REAL_ARRAY);
			_t = _t.getFirstChild();
			avn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			dim = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			case REAL_ARRAY_LITERAL:
			{
				aiv = _t==ASTNULL ? null : (AST)_t;
				realArrayInitialValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t137;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(avn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ avn.getText() + "\"",
				avn);
				if (var.getVariableType() != PlexilDataType.REAL_ARRAY_TYPE)
				throw createSemanticException("Internal error: Real array variable named \""
				+ avn.getText() + "\" is not a real array!",
				avn);
				if (aiv != null)
				{
				var.setInitialValue(aiv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringArrayDeclaration(AST _t) throws RecognitionException {
		
		AST stringArrayDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST avn = null;
		AST dim = null;
		AST aiv = null;
		
		try {      // for error handling
			AST __t147 = _t;
			AST tmp50_AST_in = (AST)_t;
			match(_t,STRING_ARRAY);
			_t = _t.getFirstChild();
			avn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			dim = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STRING:
			case STRING_ARRAY_LITERAL:
			{
				aiv = _t==ASTNULL ? null : (AST)_t;
				stringArrayInitialValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t147;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(avn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ avn.getText() + "\"",
				avn);
				if (var.getVariableType() != PlexilDataType.STRING_ARRAY_TYPE)
				throw createSemanticException("Internal error: String array variable named \""
				+ avn.getText() + "\" is not a string array!",
				avn);
				if (aiv != null)
				{
				var.setInitialValue(aiv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void blobArrayDeclaration(AST _t) throws RecognitionException {
		
		AST blobArrayDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST avn = null;
		AST dim = null;
		AST aiv = null;
		
		try {      // for error handling
			AST __t126 = _t;
			AST tmp51_AST_in = (AST)_t;
			match(_t,BLOB_ARRAY);
			_t = _t.getFirstChild();
			avn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			dim = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case POINTS_TO:
			{
				aiv = _t==ASTNULL ? null : (AST)_t;
				pointsTo(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t126;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(avn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ avn.getText() + "\"",
				avn);
				if (var.getVariableType() != PlexilDataType.BLOB_ARRAY_TYPE)
				throw createSemanticException("Internal error: BLOB array variable named \""
				+ avn.getText() + "\" is not a BLOB array!",
				avn);
				if (aiv != null)
				{
				var.setInitialValue(((PlexilASTNode) aiv).getXmlElement());
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeArrayDeclaration(AST _t) throws RecognitionException {
		
		AST timeArrayDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST avn = null;
		AST dim = null;
		AST aiv = null;
		
		try {      // for error handling
			AST __t157 = _t;
			AST tmp52_AST_in = (AST)_t;
			match(_t,TIME_ARRAY);
			_t = _t.getFirstChild();
			avn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			dim = _t==ASTNULL ? null : (AST)_t;
			nonNegativeInteger(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TIME_ARRAY_LITERAL:
			case TIME_VALUE:
			{
				aiv = _t==ASTNULL ? null : (AST)_t;
				timeArrayInitialValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t157;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(avn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no variable named \""
				+ avn.getText() + "\"",
				avn);
				if (var.getVariableType() != PlexilDataType.TIME_ARRAY_TYPE)
				throw createSemanticException("Internal error: Time array variable named \""
				+ avn.getText() + "\" is not a Time array!",
				avn);
				if (aiv != null)
				{
				var.setInitialValue(aiv);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void variableName(AST _t) throws RecognitionException {
		
		AST variableName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST tmp53_AST_in = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanValue(AST _t) throws RecognitionException {
		
		AST booleanValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST i = null;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			{
				i = (AST)_t;
				match(_t,INT);
				_t = _t.getNextSibling();
				if (!( i.getText().equals("0") || i.getText().equals("1") ))
				  throw new SemanticException(" i.getText().equals(\"0\") || i.getText().equals(\"1\") ");
				break;
			}
			case TRUE_KYWD:
			{
				AST tmp54_AST_in = (AST)_t;
				match(_t,TRUE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case FALSE_KYWD:
			{
				AST tmp55_AST_in = (AST)_t;
				match(_t,FALSE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				((PlexilASTNode) booleanValue_AST_in).setDataType(PlexilDataType.BOOLEAN_TYPE);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanArrayInitialValue(AST _t) throws RecognitionException {
		
		AST booleanArrayInitialValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRUE_KYWD:
			case FALSE_KYWD:
			case INT:
			{
				booleanValue(_t);
				_t = _retTree;
				break;
			}
			case BOOLEAN_ARRAY_LITERAL:
			{
				AST __t109 = _t;
				AST tmp56_AST_in = (AST)_t;
				match(_t,BOOLEAN_ARRAY_LITERAL);
				_t = _t.getFirstChild();
				{
				_loop111:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==TRUE_KYWD||_t.getType()==FALSE_KYWD||_t.getType()==INT)) {
						booleanValue(_t);
						_t = _retTree;
					}
					else {
						break _loop111;
					}
					
				} while (true);
				}
				_t = __t109;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerValue(AST _t) throws RecognitionException {
		
		AST integerValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			{
			AST tmp57_AST_in = (AST)_t;
			match(_t,INT);
			_t = _t.getNextSibling();
			}
			if ( inputState.guessing==0 ) {
				((PlexilASTNode) integerValue_AST_in).setDataType(PlexilDataType.INTEGER_TYPE);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerArrayInitialValue(AST _t) throws RecognitionException {
		
		AST integerArrayInitialValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			{
				integerValue(_t);
				_t = _retTree;
				break;
			}
			case INTEGER_ARRAY_LITERAL:
			{
				AST __t119 = _t;
				AST tmp58_AST_in = (AST)_t;
				match(_t,INTEGER_ARRAY_LITERAL);
				_t = _t.getFirstChild();
				{
				_loop121:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==INT)) {
						integerValue(_t);
						_t = _retTree;
					}
					else {
						break _loop121;
					}
					
				} while (true);
				}
				_t = __t119;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void pointsTo(AST _t) throws RecognitionException {
		
		AST pointsTo_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t129 = _t;
			AST tmp59_AST_in = (AST)_t;
			match(_t,POINTS_TO);
			_t = _t.getFirstChild();
			externalStructName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			{
				pointerInitialValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t129;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void externalStructName(AST _t) throws RecognitionException {
		
		AST externalStructName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST tmp60_AST_in = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void pointerInitialValue(AST _t) throws RecognitionException {
		
		AST pointerInitialValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST tmp61_AST_in = (AST)_t;
			match(_t,INT);
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realValue(AST _t) throws RecognitionException {
		
		AST realValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOUBLE:
			{
				AST tmp62_AST_in = (AST)_t;
				match(_t,DOUBLE);
				_t = _t.getNextSibling();
				break;
			}
			case INT:
			{
				AST tmp63_AST_in = (AST)_t;
				match(_t,INT);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				((PlexilASTNode) realValue_AST_in).setDataType(PlexilDataType.REAL_TYPE);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realArrayInitialValue(AST _t) throws RecognitionException {
		
		AST realArrayInitialValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			{
				realValue(_t);
				_t = _retTree;
				break;
			}
			case REAL_ARRAY_LITERAL:
			{
				AST __t140 = _t;
				AST tmp64_AST_in = (AST)_t;
				match(_t,REAL_ARRAY_LITERAL);
				_t = _t.getFirstChild();
				{
				_loop142:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==INT||_t.getType()==DOUBLE)) {
						realValue(_t);
						_t = _retTree;
					}
					else {
						break _loop142;
					}
					
				} while (true);
				}
				_t = __t140;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringValue(AST _t) throws RecognitionException {
		
		AST stringValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			{
			AST tmp65_AST_in = (AST)_t;
			match(_t,STRING);
			_t = _t.getNextSibling();
			}
			if ( inputState.guessing==0 ) {
				((PlexilASTNode) stringValue_AST_in).setDataType(PlexilDataType.STRING_TYPE);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringArrayInitialValue(AST _t) throws RecognitionException {
		
		AST stringArrayInitialValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STRING:
			{
				stringValue(_t);
				_t = _retTree;
				break;
			}
			case STRING_ARRAY_LITERAL:
			{
				AST __t150 = _t;
				AST tmp66_AST_in = (AST)_t;
				match(_t,STRING_ARRAY_LITERAL);
				_t = _t.getFirstChild();
				{
				_loop152:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==STRING)) {
						stringValue(_t);
						_t = _retTree;
					}
					else {
						break _loop152;
					}
					
				} while (true);
				}
				_t = __t150;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeValue(AST _t) throws RecognitionException {
		
		AST timeValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t164 = _t;
			AST tmp67_AST_in = (AST)_t;
			match(_t,TIME_VALUE);
			_t = _t.getFirstChild();
			integerValue(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			{
				integerValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t164;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeArrayInitialValue(AST _t) throws RecognitionException {
		
		AST timeArrayInitialValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TIME_VALUE:
			{
				timeValue(_t);
				_t = _retTree;
				break;
			}
			case TIME_ARRAY_LITERAL:
			{
				AST __t160 = _t;
				AST tmp68_AST_in = (AST)_t;
				match(_t,TIME_ARRAY_LITERAL);
				_t = _t.getFirstChild();
				{
				_loop162:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==TIME_VALUE)) {
						timeValue(_t);
						_t = _retTree;
					}
					else {
						break _loop162;
					}
					
				} while (true);
				}
				_t = __t160;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void variable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST variable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST tmp69_AST_in = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) variable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isBooleanVariableName(n.getText()) ))
			  throw new SemanticException(" context.isBooleanVariableName(n.getText()) ");
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) booleanVariable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST integerVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isIntegerVariableName(n.getText()) ))
			  throw new SemanticException(" context.isIntegerVariableName(n.getText()) ");
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) integerVariable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void blobVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST blobVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isBlobVariableName(n.getText()) ))
			  throw new SemanticException(" context.isBlobVariableName(n.getText()) ");
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) blobVariable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST realVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isRealVariableName(n.getText()) ))
			  throw new SemanticException(" context.isRealVariableName(n.getText()) ");
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) realVariable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isStringVariableName(n.getText()) ))
			  throw new SemanticException(" context.isStringVariableName(n.getText()) ");
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) stringVariable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timeVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isTimeVariableName(n.getText()) ))
			  throw new SemanticException(" context.isTimeVariableName(n.getText()) ");
			if ( inputState.guessing==0 ) {
				
				parent.addChild(((VariableASTNode) timeVariable_AST_in).getXMLElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arrayVariable(AST _t) throws RecognitionException {
		
		AST arrayVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,VARIABLE);
			_t = _t.getNextSibling();
			if (!( context.isArrayVariableName(n.getText()) ))
			  throw new SemanticException(" context.isArrayVariableName(n.getText()) ");
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST arrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST idx = null;
		
		IXMLElement xdx = new XMLElement("Index");
		
		
		try {      // for error handling
			AST __t176 = _t;
			AST tmp70_AST_in = (AST)_t;
			match(_t,ARRAY_REF);
			_t = _t.getFirstChild();
			arrayVariable(_t);
			_t = _retTree;
			idx = _t==ASTNULL ? null : (AST)_t;
			numericExpression(_t,xdx);
			_t = _retTree;
			_t = __t176;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				IXMLElement aref = ((PlexilASTNode) arrayReference_AST_in).getXmlElement();
				aref.addChild(xdx);
				parent.addChild(aref);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanArrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanArrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			arrayReference(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerArrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST integerArrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			arrayReference(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void blobArrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST blobArrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			arrayReference(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realArrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST realArrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			arrayReference(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringArrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringArrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			arrayReference(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeArrayReference(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timeArrayReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			arrayReference(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeBody(AST _t,
		IXMLElement xnode, IXMLElement xmlResourceList
	) throws RecognitionException {
		
		AST nodeBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xNodeBody = new XMLElement("NodeBody");
		
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NODE_LIST_KYWD:
			{
				nodeList(_t,xnode, xNodeBody);
				_t = _retTree;
				break;
			}
			case COMMAND_KYWD:
			{
				command(_t,xnode, xNodeBody, xmlResourceList);
				_t = _retTree;
				break;
			}
			case ARRAY_ASSIGNMENT:
			case BOOLEAN_ASSIGNMENT:
			case INTEGER_ASSIGNMENT:
			case REAL_ASSIGNMENT:
			case STRING_ASSIGNMENT:
			case TIME_ASSIGNMENT:
			{
				assignment(_t,xnode, xNodeBody);
				_t = _retTree;
				break;
			}
			case UPDATE_KYWD:
			{
				update(_t,xnode, xNodeBody);
				_t = _retTree;
				break;
			}
			case REQUEST_KYWD:
			{
				request(_t,xnode, xNodeBody);
				_t = _retTree;
				break;
			}
			case LIBRARY_CALL_KYWD:
			{
				libraryCall(_t,xnode, xNodeBody);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				xnode.addChild(xNodeBody); 
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void sequence(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST sequence_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t269 = _t;
			AST tmp71_AST_in = (AST)_t;
			match(_t,SEQUENCE_KYWD);
			_t = _t.getFirstChild();
			{
			_loop271:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NODE)) {
					action(_t,xaction);
					_t = _retTree;
				}
				else {
					break _loop271;
				}
				
			} while (true);
			}
			_t = __t269;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void concurrence(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST concurrence_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t273 = _t;
			AST tmp72_AST_in = (AST)_t;
			match(_t,CONCURRENCE_KYWD);
			_t = _t.getFirstChild();
			{
			_loop275:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NODE)) {
					action(_t,xaction);
					_t = _retTree;
				}
				else {
					break _loop275;
				}
				
			} while (true);
			}
			_t = __t273;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void uncheckedSequence(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST uncheckedSequence_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t277 = _t;
			AST tmp73_AST_in = (AST)_t;
			match(_t,UNCHECKED_SEQUENCE_KYWD);
			_t = _t.getFirstChild();
			{
			_loop279:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NODE)) {
					action(_t,xaction);
					_t = _retTree;
				}
				else {
					break _loop279;
				}
				
			} while (true);
			}
			_t = __t277;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void tryBody(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST tryBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t281 = _t;
			AST tmp74_AST_in = (AST)_t;
			match(_t,TRY_KYWD);
			_t = _t.getFirstChild();
			{
			_loop283:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NODE)) {
					action(_t,xaction);
					_t = _retTree;
				}
				else {
					break _loop283;
				}
				
			} while (true);
			}
			_t = __t281;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void ifBody(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST ifBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xcond = new XMLElement("Condition");
		xaction.addChild(xcond);
		IXMLElement xthen = new XMLElement("Then");
		xaction.addChild(xthen);
		IXMLElement xelse = null;
		
		
		try {      // for error handling
			AST __t285 = _t;
			AST tmp75_AST_in = (AST)_t;
			match(_t,IF_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xcond);
			_t = _retTree;
			AST tmp76_AST_in = (AST)_t;
			match(_t,THEN_KYWD);
			_t = _t.getNextSibling();
			action(_t,xthen);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELSE_KYWD:
			{
				AST tmp77_AST_in = (AST)_t;
				match(_t,ELSE_KYWD);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					xelse = new XMLElement("Else");
					xaction.addChild(xelse);
					
				}
				action(_t,xelse);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t285;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whileBody(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST whileBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xcond = new XMLElement("Condition");
		xaction.addChild(xcond);
		IXMLElement actionChild = new XMLElement("Action");
		xaction.addChild(actionChild);
		
		
		try {      // for error handling
			AST __t288 = _t;
			AST tmp78_AST_in = (AST)_t;
			match(_t,WHILE_KYWD);
			_t = _t.getFirstChild();
			booleanExpression(_t,xcond);
			_t = _retTree;
			action(_t,actionChild);
			_t = _retTree;
			_t = __t288;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void forBody(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST forBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xvar = null;
		IXMLElement xcond = null;
		IXMLElement xupd = null;
		IXMLElement actionChild = null;
		
		
		try {      // for error handling
			AST __t290 = _t;
			AST tmp79_AST_in = (AST)_t;
			match(_t,FOR_KYWD);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				
				context = ((NodeASTNode) forBody_AST_in).getContext();
				xvar = new XMLElement("LoopVariable");
				
			}
			loopVariableDeclaration(_t,xvar);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				xaction.addChild(xvar);
				xcond = new XMLElement("Condition");
				
			}
			booleanExpression(_t,xcond);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				xaction.addChild(xcond);
				xupd = new XMLElement("LoopVariableUpdate");
				
			}
			numericExpression(_t,xupd);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				xaction.addChild(xupd);
				actionChild = new XMLElement("Action");
				
			}
			action(_t,actionChild);
			_t = _retTree;
			_t = __t290;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				xaction.addChild(actionChild);
				context = context.getParentContext();
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void onCommandBody(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST onCommandBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			AST __t299 = _t;
			AST tmp80_AST_in = (AST)_t;
			match(_t,ON_COMMAND_KYWD);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				
				// xaction.setValue("OnCommand"); // override Node -- shouldn't be needed
				
			}
			n = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				IXMLElement xname = new XMLElement("Name");
				IXMLElement xstring = new XMLElement("StringValue");
				xname.addChild(xstring);
				xstring.setContent(n.getText());
				xaction.addChild(xname); // schema sez command name must follow any variable decls
				
			}
			if ( inputState.guessing==0 ) {
				
				
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ON_COMMAND_PARAMS:
			{
				onCommandParams(_t);
				_t = _retTree;
				break;
			}
			case NODE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			action(_t,xaction);
			_t = _retTree;
			_t = __t299;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void onMessageBody(AST _t,
		IXMLElement xaction
	) throws RecognitionException {
		
		AST onMessageBody_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
			 IXMLElement xname = new XMLElement("Message");
			 xaction.addChild(xname);
			
		
		try {      // for error handling
			AST __t307 = _t;
			AST tmp81_AST_in = (AST)_t;
			match(_t,ON_MESSAGE_KYWD);
			_t = _t.getFirstChild();
			stringExpression(_t,xname);
			_t = _retTree;
			action(_t,xaction);
			_t = _retTree;
			_t = __t307;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeList(AST _t,
		IXMLElement node, IXMLElement nodeBody
	) throws RecognitionException {
		
		AST nodeList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nlist = new XMLElement("NodeList");
		
		try {      // for error handling
			AST __t188 = _t;
			AST tmp82_AST_in = (AST)_t;
			match(_t,NODE_LIST_KYWD);
			_t = _t.getFirstChild();
			{
			_loop190:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NODE)) {
					action(_t,nlist);
					_t = _retTree;
				}
				else {
					break _loop190;
				}
				
			} while (true);
			}
			_t = __t188;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				node.setAttribute("NodeType", "NodeList");
				nodeBody.addChild(nlist);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void command(AST _t,
		IXMLElement node, IXMLElement nodeBody, IXMLElement resourceList
	) throws RecognitionException {
		
		AST command_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement cmd = new XMLElement("Command");
		if (resourceList.getChildrenCount() > 0)
		{
		// supply default priority
		IXMLElement priority = context.getResourcePriorityXML();
		if (priority != null)
		{
		// add priority to resources
		for (int i = 0; i < resourceList.getChildrenCount(); ++i)
		{
		IXMLElement resource = resourceList.getChildAtIndex(i);
		if (resource.getFirstChildNamed("ResourcePriority") == null)
		resource.addChild(priority);
		}
		}
		cmd.addChild(resourceList);
		}
		
		
		try {      // for error handling
			{
			boolean synPredMatched195 = false;
			if (_t==null) _t=ASTNULL;
			if (((_t.getType()==COMMAND_KYWD))) {
				AST __t195 = _t;
				synPredMatched195 = true;
				inputState.guessing++;
				try {
					{
					AST __t194 = _t;
					AST tmp83_AST_in = (AST)_t;
					match(_t,COMMAND_KYWD);
					_t = _t.getFirstChild();
					AST tmp84_AST_in = (AST)_t;
					match(_t,ARRAY_REF);
					_t = _t.getNextSibling();
					AST tmp85_AST_in = (AST)_t;
					match(_t,EQUALS);
					_t = _t.getNextSibling();
					_t = __t194;
					_t = _t.getNextSibling();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched195 = false;
				}
				_t = __t195;
inputState.guessing--;
			}
			if ( synPredMatched195 ) {
				AST __t196 = _t;
				AST tmp86_AST_in = (AST)_t;
				match(_t,COMMAND_KYWD);
				_t = _t.getFirstChild();
				arrayReference(_t,cmd);
				_t = _retTree;
				AST tmp87_AST_in = (AST)_t;
				match(_t,EQUALS);
				_t = _t.getNextSibling();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NCName:
				{
					commandNameLiteral(_t,cmd);
					_t = _retTree;
					break;
				}
				case LOOKUP_KYWD:
				case LOOKUP_ON_CHANGE_KYWD:
				case LOOKUP_NOW_KYWD:
				case STRING:
				case VARIABLE:
				case ARRAY_REF:
				case CONCAT:
				{
					nameExp(_t,cmd);
					_t = _retTree;
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ARGUMENT_LIST:
				{
					argumentList(_t,cmd);
					_t = _retTree;
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t196;
				_t = _t.getNextSibling();
			}
			else {
				boolean synPredMatched201 = false;
				if (_t==null) _t=ASTNULL;
				if (((_t.getType()==COMMAND_KYWD))) {
					AST __t201 = _t;
					synPredMatched201 = true;
					inputState.guessing++;
					try {
						{
						AST __t200 = _t;
						AST tmp88_AST_in = (AST)_t;
						match(_t,COMMAND_KYWD);
						_t = _t.getFirstChild();
						AST tmp89_AST_in = (AST)_t;
						match(_t,VARIABLE);
						_t = _t.getNextSibling();
						AST tmp90_AST_in = (AST)_t;
						match(_t,EQUALS);
						_t = _t.getNextSibling();
						_t = __t200;
						_t = _t.getNextSibling();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched201 = false;
					}
					_t = __t201;
inputState.guessing--;
				}
				if ( synPredMatched201 ) {
					AST __t202 = _t;
					AST tmp91_AST_in = (AST)_t;
					match(_t,COMMAND_KYWD);
					_t = _t.getFirstChild();
					variable(_t,cmd);
					_t = _retTree;
					AST tmp92_AST_in = (AST)_t;
					match(_t,EQUALS);
					_t = _t.getNextSibling();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case NCName:
					{
						commandNameLiteral(_t,cmd);
						_t = _retTree;
						break;
					}
					case LOOKUP_KYWD:
					case LOOKUP_ON_CHANGE_KYWD:
					case LOOKUP_NOW_KYWD:
					case STRING:
					case VARIABLE:
					case ARRAY_REF:
					case CONCAT:
					{
						nameExp(_t,cmd);
						_t = _retTree;
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ARGUMENT_LIST:
					{
						argumentList(_t,cmd);
						_t = _retTree;
						break;
					}
					case 3:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					_t = __t202;
					_t = _t.getNextSibling();
				}
				else if ((_t.getType()==COMMAND_KYWD)) {
					AST __t205 = _t;
					AST tmp93_AST_in = (AST)_t;
					match(_t,COMMAND_KYWD);
					_t = _t.getFirstChild();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case NCName:
					{
						commandNameLiteral(_t,cmd);
						_t = _retTree;
						break;
					}
					case LOOKUP_KYWD:
					case LOOKUP_ON_CHANGE_KYWD:
					case LOOKUP_NOW_KYWD:
					case STRING:
					case VARIABLE:
					case ARRAY_REF:
					case CONCAT:
					{
						nameExp(_t,cmd);
						_t = _retTree;
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ARGUMENT_LIST:
					{
						argumentList(_t,cmd);
						_t = _retTree;
						break;
					}
					case 3:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					_t = __t205;
					_t = _t.getNextSibling();
				}
				else {
					throw new NoViableAltException(_t);
				}
				}
				}
				if ( inputState.guessing==0 ) {
					
					node.setAttribute("NodeType", "Command");
					nodeBody.addChild(cmd);
					
				}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					if (_t!=null) {_t = _t.getNextSibling();}
				} else {
				  throw ex;
				}
			}
			_retTree = _t;
		}
		
	public final void assignment(AST _t,
		IXMLElement node, IXMLElement nodeBody
	) throws RecognitionException {
		
		AST assignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement assign = new XMLElement("Assignment");
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case BOOLEAN_ASSIGNMENT:
			{
				booleanAssignment(_t,assign);
				_t = _retTree;
				break;
			}
			case INTEGER_ASSIGNMENT:
			{
				integerAssignment(_t,assign);
				_t = _retTree;
				break;
			}
			case REAL_ASSIGNMENT:
			{
				realAssignment(_t,assign);
				_t = _retTree;
				break;
			}
			case STRING_ASSIGNMENT:
			{
				stringAssignment(_t,assign);
				_t = _retTree;
				break;
			}
			case TIME_ASSIGNMENT:
			{
				timeAssignment(_t,assign);
				_t = _retTree;
				break;
			}
			case ARRAY_ASSIGNMENT:
			{
				arrayAssignment(_t,assign);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				node.setAttribute("NodeType", "Assignment");
				nodeBody.addChild(assign);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void update(AST _t,
		IXMLElement node, IXMLElement nodeBody
	) throws RecognitionException {
		
		AST update_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		node.setAttribute("NodeType", "Update");
		IXMLElement xupdate = new XMLElement("Update");
		nodeBody.addChild(xupdate);
		
		
		try {      // for error handling
			AST __t244 = _t;
			AST tmp94_AST_in = (AST)_t;
			match(_t,UPDATE_KYWD);
			_t = _t.getFirstChild();
			{
			_loop246:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NCName)) {
					pair(_t,xupdate);
					_t = _retTree;
				}
				else {
					break _loop246;
				}
				
			} while (true);
			}
			_t = __t244;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void request(AST _t,
		IXMLElement node, IXMLElement nodeBody
	) throws RecognitionException {
		
		AST request_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		node.setAttribute("NodeType", "Request");
		IXMLElement xrequest = new XMLElement("Request");
		nodeBody.addChild(xrequest);
		
		
		try {      // for error handling
			AST __t253 = _t;
			AST tmp95_AST_in = (AST)_t;
			match(_t,REQUEST_KYWD);
			_t = _t.getFirstChild();
			nodeNameRef(_t,xrequest);
			_t = _retTree;
			{
			_loop255:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NCName)) {
					pair(_t,xrequest);
					_t = _retTree;
				}
				else {
					break _loop255;
				}
				
			} while (true);
			}
			_t = __t253;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void libraryCall(AST _t,
		IXMLElement node, IXMLElement nodeBody
	) throws RecognitionException {
		
		AST libraryCall_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		node.setAttribute("NodeType", "LibraryNodeCall");
		IXMLElement xlibCall = new XMLElement("LibraryNodeCall");
		nodeBody.addChild(xlibCall);
		
		
		try {      // for error handling
			AST __t257 = _t;
			AST tmp96_AST_in = (AST)_t;
			match(_t,LIBRARY_CALL_KYWD);
			_t = _t.getFirstChild();
			libraryNodeIdRef(_t,xlibCall);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALIASES:
			{
				aliasSpecs(_t,xlibCall);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t257;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void commandNameLiteral(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST commandNameLiteral_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		AST n2 = null;
		IXMLElement sn = new XMLElement("Name");
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NCName:
			{
				n2 = (AST)_t;
				match(_t,NCName);
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case ARGUMENT_LIST:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				IXMLElement sv = new XMLElement("StringValue");
				String text = n.getText();
				if (n2 != null) {
					text += ":" + n2.getText();
				}
				sv.setContent(text);
				sn.addChild(sv);
				parent.addChild(sn);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nameExp(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nameExp_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST se = null;
		
		IXMLElement nm = new XMLElement("Name");
		
		
		try {      // for error handling
			se = _t==ASTNULL ? null : (AST)_t;
			stringExpression(_t,nm);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				parent.addChild(nm);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void argumentList(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST argumentList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xargs = new XMLElement("Arguments");
		parent.addChild(xargs);
		
		
		try {      // for error handling
			AST __t212 = _t;
			AST tmp97_AST_in = (AST)_t;
			match(_t,ARGUMENT_LIST);
			_t = _t.getFirstChild();
			{
			int _cnt214=0;
			_loop214:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_1.member(_t.getType()))) {
					argument(_t,xargs);
					_t = _retTree;
				}
				else {
					if ( _cnt214>=1 ) { break _loop214; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt214++;
			} while (true);
			}
			_t = __t212;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void argument(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST argument_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRUE_KYWD:
			case FALSE_KYWD:
			case INT:
			case DOUBLE:
			case STRING:
			case TIME_VALUE:
			{
				argValue(_t,parent);
				_t = _retTree;
				break;
			}
			case VARIABLE:
			{
				variable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				arrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void argValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST argValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST iv = null;
		AST rv = null;
		AST sv = null;
		AST tv = null;
		AST bv = null;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STRING:
			{
				sv = _t==ASTNULL ? null : (AST)_t;
				stringValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode) sv).getXmlElement());
				}
				break;
			}
			case TIME_VALUE:
			{
				tv = _t==ASTNULL ? null : (AST)_t;
				timeValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode) tv).getXmlElement());
				}
				break;
			}
			default:
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==INT)) {
					iv = _t==ASTNULL ? null : (AST)_t;
					integerValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) iv).getXmlElement());
					}
				}
				else if ((_t.getType()==INT||_t.getType()==DOUBLE)) {
					rv = _t==ASTNULL ? null : (AST)_t;
					realValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) rv).getXmlElement());
					}
				}
				else if ((_t.getType()==TRUE_KYWD||_t.getType()==FALSE_KYWD||_t.getType()==INT)) {
					bv = _t==ASTNULL ? null : (AST)_t;
					booleanValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) bv).getXmlElement());
					}
				}
			else {
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanAssignment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanAssignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST bRHS = null;
		
		try {      // for error handling
			AST __t221 = _t;
			AST tmp98_AST_in = (AST)_t;
			match(_t,BOOLEAN_ASSIGNMENT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARIABLE:
			{
				booleanVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				booleanArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			bRHS = _t==ASTNULL ? null : (AST)_t;
			booleanRHS(_t,parent);
			_t = _retTree;
			_t = __t221;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerAssignment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST integerAssignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t225 = _t;
			AST tmp99_AST_in = (AST)_t;
			match(_t,INTEGER_ASSIGNMENT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARIABLE:
			{
				integerVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				integerArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			numericRHS(_t,parent);
			_t = _retTree;
			_t = __t225;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realAssignment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST realAssignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t228 = _t;
			AST tmp100_AST_in = (AST)_t;
			match(_t,REAL_ASSIGNMENT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARIABLE:
			{
				realVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				realArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			numericRHS(_t,parent);
			_t = _retTree;
			_t = __t228;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringAssignment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringAssignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t232 = _t;
			AST tmp101_AST_in = (AST)_t;
			match(_t,STRING_ASSIGNMENT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARIABLE:
			{
				stringVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				stringArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			stringRHS(_t,parent);
			_t = _retTree;
			_t = __t232;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeAssignment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timeAssignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t236 = _t;
			AST tmp102_AST_in = (AST)_t;
			match(_t,TIME_ASSIGNMENT);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARIABLE:
			{
				timeVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				timeArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			timeRHS(_t,parent);
			_t = _retTree;
			_t = __t236;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arrayAssignment(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST arrayAssignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST var = null;
		
		try {      // for error handling
			AST __t240 = _t;
			AST tmp103_AST_in = (AST)_t;
			match(_t,ARRAY_ASSIGNMENT);
			_t = _t.getFirstChild();
			var = _t==ASTNULL ? null : (AST)_t;
			arrayVariable(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				IXMLElement xname = new XMLElement("ArrayVariable");
				xname.setContent(var.getText());
				parent.addChild(xname); 
				
			}
			arrayRHS(_t,parent);
			_t = _retTree;
			_t = __t240;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanRHS(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanRHS_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xbRHS = new XMLElement("BooleanRHS");
		
		try {      // for error handling
			booleanExpression(_t,xbRHS);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xbRHS);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void numericRHS(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST numericRHS_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xnRHS = new XMLElement("NumericRHS");
		
		try {      // for error handling
			numericExpression(_t,xnRHS);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xnRHS);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringRHS(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringRHS_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xsRHS = new XMLElement("StringRHS");
		
		try {      // for error handling
			stringExpression(_t,xsRHS);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xsRHS);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeRHS(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timeRHS_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xsRHS = new XMLElement("TimeRHS");
		
		try {      // for error handling
			timeExpression(_t,xsRHS);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xsRHS);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeExpression(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timeExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST tv = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TIME_VALUE:
			{
				tv = _t==ASTNULL ? null : (AST)_t;
				timeValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode)tv).getXmlElement());
				}
				break;
			}
			case VARIABLE:
			{
				timeVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			{
				lookupExpr(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arrayRHS(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST arrayRHS_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xsRHS = new XMLElement("BooleanRHS");
		
		try {      // for error handling
			arrayExpression(_t,xsRHS);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xsRHS);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arrayExpression(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST arrayExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			lookupExpr(_t,parent);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void lookupExpr(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST lookupExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LOOKUP_KYWD:
			{
				lookup(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_NOW_KYWD:
			{
				lookupNow(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_ON_CHANGE_KYWD:
			{
				lookupOnChange(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void pair(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST pair_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xpair = new XMLElement("Pair");
		parent.addChild(xpair);
		
		
		try {      // for error handling
			name(_t,xpair);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case TRUE_KYWD:
			case FALSE_KYWD:
			case INT:
			case DOUBLE:
			case STRING:
			case TIME_VALUE:
			{
				value(_t,xpair);
				_t = _retTree;
				break;
			}
			case VARIABLE:
			{
				variable(_t,xpair);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				arrayReference(_t,xpair);
				_t = _retTree;
				break;
			}
			case LOOKUP_NOW_KYWD:
			{
				lookupNow(_t,xpair);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void name(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST name_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xname = new XMLElement("Name");
		parent.addChild(xname);
		
		
		try {      // for error handling
			AST tmp104_AST_in = (AST)_t;
			match(_t,NCName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				xname.setContent(name_AST_in.getText());
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void value(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST value_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST iv = null;
		AST rv = null;
		AST bv = null;
		AST sv = null;
		AST tv = null;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STRING:
			{
				sv = _t==ASTNULL ? null : (AST)_t;
				stringValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode) sv).getXmlElement());
				}
				break;
			}
			case TIME_VALUE:
			{
				tv = _t==ASTNULL ? null : (AST)_t;
				timeValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode) tv).getXmlElement());
				}
				break;
			}
			default:
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==INT)) {
					iv = _t==ASTNULL ? null : (AST)_t;
					integerValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) iv).getXmlElement());
					}
				}
				else if ((_t.getType()==INT||_t.getType()==DOUBLE)) {
					rv = _t==ASTNULL ? null : (AST)_t;
					realValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) rv).getXmlElement());
					}
				}
				else if ((_t.getType()==TRUE_KYWD||_t.getType()==FALSE_KYWD||_t.getType()==INT)) {
					bv = _t==ASTNULL ? null : (AST)_t;
					booleanValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) bv).getXmlElement());
					}
				}
			else {
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void lookupNow(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST lookupNow_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xln = new XMLElement("LookupNow"); 
		parent.addChild(xln);
		
		
		try {      // for error handling
			AST __t427 = _t;
			AST tmp105_AST_in = (AST)_t;
			match(_t,LOOKUP_NOW_KYWD);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STATE_NAME:
			{
				stateNameLiteral(_t,xln);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			case STRING:
			case VARIABLE:
			case ARRAY_REF:
			case CONCAT:
			{
				nameExp(_t,xln);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ARGUMENT_LIST:
			{
				argumentList(_t,xln);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t427;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeNameRef(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeNameRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,NodeName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				IXMLElement nid = new XMLElement("NodeRef");
				nid.setContent(n.getText());
				parent.addChild(nid);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void libraryNodeIdRef(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST libraryNodeIdRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		IXMLElement nid = new XMLElement("NodeId");
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,NodeName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (!PlexilGlobalContext.getGlobalContext().isLibraryNodeName(n.getText()))
				throw createSemanticException("\"" + n.getText() + "\" is not the name of a declared library node",
				n);
				nid.setContent(n.getText());
				parent.addChild(nid);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void aliasSpecs(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST aliasSpecs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t260 = _t;
			AST tmp106_AST_in = (AST)_t;
			match(_t,ALIASES);
			_t = _t.getFirstChild();
			{
			_loop262:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case CONST_ALIAS:
				{
					constAlias(_t,parent);
					_t = _retTree;
					break;
				}
				case VARIABLE_ALIAS:
				{
					varAlias(_t,parent);
					_t = _retTree;
					break;
				}
				default:
				{
					break _loop262;
				}
				}
			} while (true);
			}
			_t = __t260;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void constAlias(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST constAlias_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xalias = new XMLElement("Alias");
		parent.addChild(xalias);
		
		
		try {      // for error handling
			AST __t264 = _t;
			AST tmp107_AST_in = (AST)_t;
			match(_t,CONST_ALIAS);
			_t = _t.getFirstChild();
			nodeParameter(_t,xalias);
			_t = _retTree;
			value(_t,xalias);
			_t = _retTree;
			_t = __t264;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void varAlias(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST varAlias_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		IXMLElement xalias = new XMLElement("Alias");
		parent.addChild(xalias);
		
		
		try {      // for error handling
			AST __t266 = _t;
			AST tmp108_AST_in = (AST)_t;
			match(_t,VARIABLE_ALIAS);
			_t = _t.getFirstChild();
			nodeParameter(_t,xalias);
			_t = _retTree;
			variable(_t,xalias);
			_t = _retTree;
			_t = __t266;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeParameter(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeParameter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		
		try {      // for error handling
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				
				IXMLElement xNodeParm = new XMLElement("NodeParameter");
				xNodeParm.setContent(vn.getText());
				parent.addChild(xNodeParm);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void loopVariableDeclaration(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST loopVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INTEGER_KYWD:
			{
				integerLoopVariableDeclaration(_t,parent);
				_t = _retTree;
				break;
			}
			case REAL_KYWD:
			{
				realLoopVariableDeclaration(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void integerLoopVariableDeclaration(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST integerLoopVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t293 = _t;
			AST tmp109_AST_in = (AST)_t;
			match(_t,INTEGER_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				integerValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t293;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no loop variable named \""
								    + vn.getText() + "\"",
								    vn);
				if (var.getVariableType() != PlexilDataType.INTEGER_TYPE)
				throw createSemanticException("Internal error: Integer loop variable named \""
								    + vn.getText() + "\" is not Integer!",
								    vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				parent.addChild(var.makeVariableDeclarationElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void realLoopVariableDeclaration(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST realLoopVariableDeclaration_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST vn = null;
		AST iv = null;
		
		try {      // for error handling
			AST __t296 = _t;
			AST tmp110_AST_in = (AST)_t;
			match(_t,REAL_KYWD);
			_t = _t.getFirstChild();
			vn = _t==ASTNULL ? null : (AST)_t;
			variableName(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			{
				iv = _t==ASTNULL ? null : (AST)_t;
				realValue(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t296;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				PlexilVariableName var = context.findLocalVariable(vn.getText());
				if (var == null)
				throw createSemanticException("Internal error: context contains no loop variable named \""
								    + vn.getText() + "\"",
								    vn);
				if (var.getVariableType() != PlexilDataType.REAL_TYPE)
				throw createSemanticException("Internal error: Real loop variable named \""
								    + vn.getText() + "\" is not Real!",
								    vn);
				if (iv != null)
				{
				var.setInitialValue(iv);
				}
				parent.addChild(var.makeVariableDeclarationElement());
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void onCommandParams(AST _t) throws RecognitionException {
		
		AST onCommandParams_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t302 = _t;
			AST tmp111_AST_in = (AST)_t;
			match(_t,ON_COMMAND_PARAMS);
			_t = _t.getFirstChild();
			{
			_loop304:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==VARIABLE_DECLARATION)) {
					onCommandParam(_t);
					_t = _retTree;
				}
				else {
					break _loop304;
				}
				
			} while (true);
			}
			_t = __t302;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void onCommandParam(AST _t) throws RecognitionException {
		
		AST onCommandParam_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			variableDeclaration(_t);
			_t = _retTree;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanTerm(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanTerm_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST bval = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARIABLE:
			{
				booleanVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case ARRAY_REF:
			{
				booleanArrayReference(_t,parent);
				_t = _retTree;
				break;
			}
			case TRUE_KYWD:
			case FALSE_KYWD:
			case INT:
			{
				bval = _t==ASTNULL ? null : (AST)_t;
				booleanValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					parent.addChild(((PlexilASTNode) bval).getXmlElement());
				}
				break;
			}
			case IS_KNOWN_KYWD:
			{
				isKnownExp(_t,parent);
				_t = _retTree;
				break;
			}
			case NODE_STATE_PREDICATE:
			{
				nodeStatePredicateExp(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			{
				lookupExpr(_t,parent);
				_t = _retTree;
				break;
			}
			case MESSAGE_RECEIVED_KYWD:
			{
				messageReceivedExp(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void isKnownExp(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST isKnownExp_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xisKnown = new XMLElement("IsKnown"); parent.addChild(xisKnown);
		
		try {      // for error handling
			AST __t312 = _t;
			AST tmp112_AST_in = (AST)_t;
			match(_t,IS_KNOWN_KYWD);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ARRAY_REF:
			{
				arrayReference(_t,xisKnown);
				_t = _retTree;
				break;
			}
			case VARIABLE:
			{
				variable(_t,xisKnown);
				_t = _retTree;
				break;
			}
			case NODE_STATE_VARIABLE:
			{
				nodeStateVariable(_t,xisKnown);
				_t = _retTree;
				break;
			}
			case NODE_OUTCOME_VARIABLE:
			{
				nodeOutcomeVariable(_t,xisKnown);
				_t = _retTree;
				break;
			}
			case NODE_TIMEPOINT_VALUE:
			{
				nodeTimepointValue(_t,xisKnown);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t312;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeStatePredicateExp(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeStatePredicateExp_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST nsp = null;
		
		IXMLElement nspx = new XMLElement();
		
		
		try {      // for error handling
			AST __t315 = _t;
			AST tmp113_AST_in = (AST)_t;
			match(_t,NODE_STATE_PREDICATE);
			_t = _t.getFirstChild();
			nsp = _t==ASTNULL ? null : (AST)_t;
			nodeStatePredicate(_t);
			_t = _retTree;
			nodeIdRef(_t,nspx);
			_t = _retTree;
			_t = __t315;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				nspx.setName(nsp.getText());
				parent.addChild(nspx);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void messageReceivedExp(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST messageReceivedExp_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST sv = null;
		
		IXMLElement mrx = new XMLElement("MessageReceived");
		
		
		try {      // for error handling
			AST __t318 = _t;
			AST tmp114_AST_in = (AST)_t;
			match(_t,MESSAGE_RECEIVED_KYWD);
			_t = _t.getFirstChild();
			sv = _t==ASTNULL ? null : (AST)_t;
			stringValue(_t);
			_t = _retTree;
			_t = __t318;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				mrx.addChild(((PlexilASTNode) sv).getXmlElement());
				parent.addChild(mrx);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeStateVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeStateVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nsv = new XMLElement("NodeStateVariable");
		
		try {      // for error handling
			AST __t365 = _t;
			AST tmp115_AST_in = (AST)_t;
			match(_t,NODE_STATE_VARIABLE);
			_t = _t.getFirstChild();
			nodeIdRef(_t,nsv);
			_t = _retTree;
			_t = __t365;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(nsv);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeOutcomeVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeOutcomeVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nov = new XMLElement("NodeOutcomeVariable");
		
		try {      // for error handling
			AST __t374 = _t;
			AST tmp116_AST_in = (AST)_t;
			match(_t,NODE_OUTCOME_VARIABLE);
			_t = _t.getFirstChild();
			nodeIdRef(_t,nov);
			_t = _retTree;
			_t = __t374;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(nov);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeTimepointValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeTimepointValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement ntpv = new XMLElement("NodeTimepointValue");
		
		try {      // for error handling
			AST __t388 = _t;
			AST tmp117_AST_in = (AST)_t;
			match(_t,NODE_TIMEPOINT_VALUE);
			_t = _t.getFirstChild();
			nodeIdRef(_t,ntpv);
			_t = _retTree;
			nodeStateValue(_t,ntpv);
			_t = _retTree;
			timepoint(_t,ntpv);
			_t = _retTree;
			_t = __t388;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(ntpv);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeStatePredicate(AST _t) throws RecognitionException {
		
		AST nodeStatePredicate_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NODE_EXECUTING_KYWD:
			{
				AST tmp118_AST_in = (AST)_t;
				match(_t,NODE_EXECUTING_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_FAILED_KYWD:
			{
				AST tmp119_AST_in = (AST)_t;
				match(_t,NODE_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_FINISHED_KYWD:
			{
				AST tmp120_AST_in = (AST)_t;
				match(_t,NODE_FINISHED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_INACTIVE_KYWD:
			{
				AST tmp121_AST_in = (AST)_t;
				match(_t,NODE_INACTIVE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_INVARIANT_FAILED_KYWD:
			{
				AST tmp122_AST_in = (AST)_t;
				match(_t,NODE_INVARIANT_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_ITERATION_ENDED_KYWD:
			{
				AST tmp123_AST_in = (AST)_t;
				match(_t,NODE_ITERATION_ENDED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_ITERATION_FAILED_KYWD:
			{
				AST tmp124_AST_in = (AST)_t;
				match(_t,NODE_ITERATION_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_ITERATION_SUCCEEDED_KYWD:
			{
				AST tmp125_AST_in = (AST)_t;
				match(_t,NODE_ITERATION_SUCCEEDED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_PARENT_FAILED_KYWD:
			{
				AST tmp126_AST_in = (AST)_t;
				match(_t,NODE_PARENT_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_POSTCONDITION_FAILED_KYWD:
			{
				AST tmp127_AST_in = (AST)_t;
				match(_t,NODE_POSTCONDITION_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_PRECONDITION_FAILED_KYWD:
			{
				AST tmp128_AST_in = (AST)_t;
				match(_t,NODE_PRECONDITION_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_SKIPPED_KYWD:
			{
				AST tmp129_AST_in = (AST)_t;
				match(_t,NODE_SKIPPED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_SUCCEEDED_KYWD:
			{
				AST tmp130_AST_in = (AST)_t;
				match(_t,NODE_SUCCEEDED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case NODE_WAITING_KYWD:
			{
				AST tmp131_AST_in = (AST)_t;
				match(_t,NODE_WAITING_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeIdRef(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeIdRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		IXMLElement nid = new XMLElement("NodeId");
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,NodeName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (!state.isNodeName(n.getText()))
				throw createSemanticException("\"" + n.getText() + "\" is not the name of a known node",
				n);
				nid.setContent(n.getText());
				parent.addChild(nid);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void or(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST or_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xor = new XMLElement("OR");
		
		try {      // for error handling
			AST __t321 = _t;
			AST tmp132_AST_in = (AST)_t;
			match(_t,OR_KYWD);
			_t = _t.getFirstChild();
			{
			int _cnt323=0;
			_loop323:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_2.member(_t.getType()))) {
					booleanExpression(_t,xor);
					_t = _retTree;
				}
				else {
					if ( _cnt323>=1 ) { break _loop323; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt323++;
			} while (true);
			}
			_t = __t321;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xor);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void and(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST and_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xand = new XMLElement("AND");
		
		try {      // for error handling
			AST __t325 = _t;
			AST tmp133_AST_in = (AST)_t;
			match(_t,AND_KYWD);
			_t = _t.getFirstChild();
			{
			int _cnt327=0;
			_loop327:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_2.member(_t.getType()))) {
					booleanExpression(_t,xand);
					_t = _retTree;
				}
				else {
					if ( _cnt327>=1 ) { break _loop327; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt327++;
			} while (true);
			}
			_t = __t325;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xand);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void not(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST not_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xnot = new XMLElement("NOT");
		
		try {      // for error handling
			AST __t329 = _t;
			AST tmp134_AST_in = (AST)_t;
			match(_t,BANG);
			_t = _t.getFirstChild();
			booleanExpression(_t,xnot);
			_t = _retTree;
			_t = __t329;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xnot);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void gt(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST gt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xgt = new XMLElement("GT");
		
		try {      // for error handling
			AST __t331 = _t;
			AST tmp135_AST_in = (AST)_t;
			match(_t,GREATER);
			_t = _t.getFirstChild();
			numericExpression(_t,xgt);
			_t = _retTree;
			numericExpression(_t,xgt);
			_t = _retTree;
			_t = __t331;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xgt);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void ge(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST ge_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xge = new XMLElement("GE");
		
		try {      // for error handling
			AST __t333 = _t;
			AST tmp136_AST_in = (AST)_t;
			match(_t,GEQ);
			_t = _t.getFirstChild();
			numericExpression(_t,xge);
			_t = _retTree;
			numericExpression(_t,xge);
			_t = _retTree;
			_t = __t333;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xge);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void lt(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST lt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xlt = new XMLElement("LT");
		
		try {      // for error handling
			AST __t335 = _t;
			AST tmp137_AST_in = (AST)_t;
			match(_t,LESS);
			_t = _t.getFirstChild();
			numericExpression(_t,xlt);
			_t = _retTree;
			numericExpression(_t,xlt);
			_t = _retTree;
			_t = __t335;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xlt);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void le(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST le_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xle = new XMLElement("LE");
		
		try {      // for error handling
			AST __t337 = _t;
			AST tmp138_AST_in = (AST)_t;
			match(_t,LEQ);
			_t = _t.getFirstChild();
			numericExpression(_t,xle);
			_t = _retTree;
			numericExpression(_t,xle);
			_t = _retTree;
			_t = __t337;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xle);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void comparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST comparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case BOOLEAN_COMPARISON:
			{
				booleanComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case NUMERIC_COMPARISON:
			{
				numericComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case NODE_STATE_COMPARISON:
			{
				nodeStateComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case NODE_OUTCOME_COMPARISON:
			{
				nodeOutcomeComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case NODE_COMMAND_HANDLE_COMPARISON:
			{
				nodeCommandHandleComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case NODE_FAILURE_COMPARISON:
			{
				nodeFailureComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case STRING_COMPARISON:
			{
				stringComparison(_t,parent);
				_t = _retTree;
				break;
			}
			case TIME_COMPARISON:
			{
				timeComparison(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST booleanComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQBoolean");
		
		try {      // for error handling
			AST __t340 = _t;
			AST tmp139_AST_in = (AST)_t;
			match(_t,BOOLEAN_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp140_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp141_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NEBoolean");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			booleanExpression(_t,xeq);
			_t = _retTree;
			booleanExpression(_t,xeq);
			_t = _retTree;
			_t = __t340;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void numericComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST numericComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQNumeric");
		
		try {      // for error handling
			AST __t343 = _t;
			AST tmp142_AST_in = (AST)_t;
			match(_t,NUMERIC_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp143_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp144_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NENumeric");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			numericExpression(_t,xeq);
			_t = _retTree;
			numericExpression(_t,xeq);
			_t = _retTree;
			_t = __t343;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeStateComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeStateComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQInternal");
		
		try {      // for error handling
			AST __t346 = _t;
			AST tmp145_AST_in = (AST)_t;
			match(_t,NODE_STATE_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp146_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp147_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NEInternal");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			nodeState(_t,xeq);
			_t = _retTree;
			nodeState(_t,xeq);
			_t = _retTree;
			_t = __t346;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeOutcomeComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeOutcomeComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQInternal");
		
		try {      // for error handling
			AST __t349 = _t;
			AST tmp148_AST_in = (AST)_t;
			match(_t,NODE_OUTCOME_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp149_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp150_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NEInternal");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			nodeOutcome(_t,xeq);
			_t = _retTree;
			nodeOutcome(_t,xeq);
			_t = _retTree;
			_t = __t349;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeCommandHandleComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeCommandHandleComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQInternal");
		
		try {      // for error handling
			AST __t352 = _t;
			AST tmp151_AST_in = (AST)_t;
			match(_t,NODE_COMMAND_HANDLE_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp152_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp153_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NEInternal");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			nodeCommandHandle(_t,xeq);
			_t = _retTree;
			nodeCommandHandle(_t,xeq);
			_t = _retTree;
			_t = __t352;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeFailureComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeFailureComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQInternal");
		
		try {      // for error handling
			AST __t355 = _t;
			AST tmp154_AST_in = (AST)_t;
			match(_t,NODE_FAILURE_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp155_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp156_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NEInternal");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			nodeFailure(_t,xeq);
			_t = _retTree;
			nodeFailure(_t,xeq);
			_t = _retTree;
			_t = __t355;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQString");
		
		try {      // for error handling
			AST __t358 = _t;
			AST tmp157_AST_in = (AST)_t;
			match(_t,STRING_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp158_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp159_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NEString");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			stringExpression(_t,xeq);
			_t = _retTree;
			stringExpression(_t,xeq);
			_t = _retTree;
			_t = __t358;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timeComparison(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timeComparison_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xeq = new XMLElement("EQTime");
		
		try {      // for error handling
			AST __t361 = _t;
			AST tmp160_AST_in = (AST)_t;
			match(_t,TIME_COMPARISON);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DEQUALS:
			{
				AST tmp161_AST_in = (AST)_t;
				match(_t,DEQUALS);
				_t = _t.getNextSibling();
				break;
			}
			case NEQUALS:
			{
				AST tmp162_AST_in = (AST)_t;
				match(_t,NEQUALS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					xeq.setName("NETime");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			timeExpression(_t,xeq);
			_t = _retTree;
			timeExpression(_t,xeq);
			_t = _retTree;
			_t = __t361;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xeq);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeState(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeState_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NODE_STATE_VARIABLE:
			{
				nodeStateVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case WAITING_STATE_KYWD:
			case EXECUTING_STATE_KYWD:
			case FINISHING_STATE_KYWD:
			case FAILING_STATE_KYWD:
			case FINISHED_STATE_KYWD:
			case ITERATION_ENDED_STATE_KYWD:
			case INACTIVE_STATE_KYWD:
			{
				nodeStateValue(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeOutcome(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeOutcome_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NODE_OUTCOME_VARIABLE:
			{
				nodeOutcomeVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case SUCCESS_OUTCOME_KYWD:
			case FAILURE_OUTCOME_KYWD:
			case SKIPPED_OUTCOME_KYWD:
			{
				nodeOutcomeValue(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeCommandHandle(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeCommandHandle_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NODE_COMMAND_HANDLE_VARIABLE:
			{
				nodeCommandHandleVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case COMMAND_ABORTED_KYWD:
			case COMMAND_ABORT_FAILED_KYWD:
			case COMMAND_ACCEPTED_KYWD:
			case COMMAND_DENIED_KYWD:
			case COMMAND_FAILED_KYWD:
			case COMMAND_RCVD_KYWD:
			case COMMAND_SENT_KYWD:
			case COMMAND_SUCCESS_KYWD:
			{
				nodeCommandHandleValue(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeFailure(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeFailure_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NODE_FAILURE_VARIABLE:
			{
				nodeFailureVariable(_t,parent);
				_t = _retTree;
				break;
			}
			case PRE_CONDITION_FAILED_KYWD:
			case POST_CONDITION_FAILED_KYWD:
			case INVARIANT_CONDITION_FAILED_KYWD:
			case PARENT_FAILED_KYWD:
			{
				nodeFailureValue(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeStateValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeStateValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nsv = new XMLElement("NodeStateValue");
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WAITING_STATE_KYWD:
			{
				AST tmp163_AST_in = (AST)_t;
				match(_t,WAITING_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case EXECUTING_STATE_KYWD:
			{
				AST tmp164_AST_in = (AST)_t;
				match(_t,EXECUTING_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case FINISHING_STATE_KYWD:
			{
				AST tmp165_AST_in = (AST)_t;
				match(_t,FINISHING_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case FAILING_STATE_KYWD:
			{
				AST tmp166_AST_in = (AST)_t;
				match(_t,FAILING_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case FINISHED_STATE_KYWD:
			{
				AST tmp167_AST_in = (AST)_t;
				match(_t,FINISHED_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case ITERATION_ENDED_STATE_KYWD:
			{
				AST tmp168_AST_in = (AST)_t;
				match(_t,ITERATION_ENDED_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case INACTIVE_STATE_KYWD:
			{
				AST tmp169_AST_in = (AST)_t;
				match(_t,INACTIVE_STATE_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				nsv.setContent(nodeStateValue_AST_in.getText());
				parent.addChild(nsv);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeRef(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		IXMLElement nid = new XMLElement("NodeRef");
		
		try {      // for error handling
			n = (AST)_t;
			match(_t,NodeName);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (!state.isNodeName(n.getText()))
				throw createSemanticException("\"" + n.getText() + "\" is not the name of a known node",
				n);
				nid.setContent(n.getText());
				parent.addChild(nid);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeOutcomeValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeOutcomeValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nov = new XMLElement("NodeOutcomeValue");
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SUCCESS_OUTCOME_KYWD:
			{
				AST tmp170_AST_in = (AST)_t;
				match(_t,SUCCESS_OUTCOME_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case FAILURE_OUTCOME_KYWD:
			{
				AST tmp171_AST_in = (AST)_t;
				match(_t,FAILURE_OUTCOME_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case SKIPPED_OUTCOME_KYWD:
			{
				AST tmp172_AST_in = (AST)_t;
				match(_t,SKIPPED_OUTCOME_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				nov.setContent(nodeOutcomeValue_AST_in.getText());
				parent.addChild(nov);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeCommandHandleVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeCommandHandleVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nov = new XMLElement("NodeCommandHandleVariable");
		
		try {      // for error handling
			AST __t379 = _t;
			AST tmp173_AST_in = (AST)_t;
			match(_t,NODE_COMMAND_HANDLE_VARIABLE);
			_t = _t.getFirstChild();
			nodeIdRef(_t,nov);
			_t = _retTree;
			_t = __t379;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(nov);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeCommandHandleValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeCommandHandleValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nov = new XMLElement("NodeCommandHandleValue");
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COMMAND_ABORTED_KYWD:
			{
				AST tmp174_AST_in = (AST)_t;
				match(_t,COMMAND_ABORTED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_ABORT_FAILED_KYWD:
			{
				AST tmp175_AST_in = (AST)_t;
				match(_t,COMMAND_ABORT_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_ACCEPTED_KYWD:
			{
				AST tmp176_AST_in = (AST)_t;
				match(_t,COMMAND_ACCEPTED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_DENIED_KYWD:
			{
				AST tmp177_AST_in = (AST)_t;
				match(_t,COMMAND_DENIED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_FAILED_KYWD:
			{
				AST tmp178_AST_in = (AST)_t;
				match(_t,COMMAND_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_RCVD_KYWD:
			{
				AST tmp179_AST_in = (AST)_t;
				match(_t,COMMAND_RCVD_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_SENT_KYWD:
			{
				AST tmp180_AST_in = (AST)_t;
				match(_t,COMMAND_SENT_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case COMMAND_SUCCESS_KYWD:
			{
				AST tmp181_AST_in = (AST)_t;
				match(_t,COMMAND_SUCCESS_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				nov.setContent(nodeCommandHandleValue_AST_in.getText());
				parent.addChild(nov);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeFailureVariable(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeFailureVariable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nov = new XMLElement("NodeFailureVariable");
		
		try {      // for error handling
			AST __t384 = _t;
			AST tmp182_AST_in = (AST)_t;
			match(_t,NODE_FAILURE_VARIABLE);
			_t = _t.getFirstChild();
			nodeRef(_t,nov);
			_t = _retTree;
			_t = __t384;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(nov);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nodeFailureValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST nodeFailureValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement nov = new XMLElement("NodeFailureValue");
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PRE_CONDITION_FAILED_KYWD:
			{
				AST tmp183_AST_in = (AST)_t;
				match(_t,PRE_CONDITION_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case POST_CONDITION_FAILED_KYWD:
			{
				AST tmp184_AST_in = (AST)_t;
				match(_t,POST_CONDITION_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case INVARIANT_CONDITION_FAILED_KYWD:
			{
				AST tmp185_AST_in = (AST)_t;
				match(_t,INVARIANT_CONDITION_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case PARENT_FAILED_KYWD:
			{
				AST tmp186_AST_in = (AST)_t;
				match(_t,PARENT_FAILED_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				nov.setContent(nodeFailureValue_AST_in.getText());
				parent.addChild(nov);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void timepoint(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST timepoint_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement tp = new XMLElement("Timepoint");
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case START_KYWD:
			{
				AST tmp187_AST_in = (AST)_t;
				match(_t,START_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			case END_KYWD:
			{
				AST tmp188_AST_in = (AST)_t;
				match(_t,END_KYWD);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				tp.setContent(timepoint_AST_in.getText());
				parent.addChild(tp);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void numericTerm(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST numericTerm_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST iv = null;
		AST rv = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SQRT_KYWD:
			case ABS_KYWD:
			{
				numericUnaryOperation(_t,parent);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			{
				lookupExpr(_t,parent);
				_t = _retTree;
				break;
			}
			case NODE_TIMEPOINT_VALUE:
			{
				nodeTimepointValue(_t,parent);
				_t = _retTree;
				break;
			}
			case LPAREN:
			{
				AST tmp189_AST_in = (AST)_t;
				match(_t,LPAREN);
				_t = _t.getNextSibling();
				numericExpression(_t,parent);
				_t = _retTree;
				AST tmp190_AST_in = (AST)_t;
				match(_t,RPAREN);
				_t = _t.getNextSibling();
				break;
			}
			default:
				if (_t==null) _t=ASTNULL;
				if (((_t.getType()==VARIABLE))&&( numericTerm_AST_in.getType() == VARIABLE && context.isIntegerVariableName(numericTerm_AST_in.getText()) )) {
					integerVariable(_t,parent);
					_t = _retTree;
				}
				else if (((_t.getType()==VARIABLE))&&( numericTerm_AST_in.getType() == VARIABLE && context.isRealVariableName(numericTerm_AST_in.getText()) )) {
					realVariable(_t,parent);
					_t = _retTree;
				}
				else if ((_t.getType()==INT)) {
					iv = _t==ASTNULL ? null : (AST)_t;
					integerValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) iv).getXmlElement());
					}
				}
				else if ((_t.getType()==INT||_t.getType()==DOUBLE)) {
					rv = _t==ASTNULL ? null : (AST)_t;
					realValue(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						parent.addChild(((PlexilASTNode) rv).getXmlElement());
					}
				}
				else if ((_t.getType()==ARRAY_REF)) {
					integerArrayReference(_t,parent);
					_t = _retTree;
				}
				else if ((_t.getType()==ARRAY_REF)) {
					realArrayReference(_t,parent);
					_t = _retTree;
				}
			else {
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void numericUnaryOperation(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST numericUnaryOperation_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ABS_KYWD:
			{
				absValue(_t,parent);
				_t = _retTree;
				break;
			}
			case SQRT_KYWD:
			{
				sqrt(_t,parent);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void add(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST add_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xadd = new XMLElement("ADD");
		
		try {      // for error handling
			AST __t399 = _t;
			AST tmp191_AST_in = (AST)_t;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			numericExpression(_t,xadd);
			_t = _retTree;
			{
			int _cnt401=0;
			_loop401:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_3.member(_t.getType()))) {
					numericExpression(_t,xadd);
					_t = _retTree;
				}
				else {
					if ( _cnt401>=1 ) { break _loop401; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt401++;
			} while (true);
			}
			_t = __t399;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xadd);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void sub(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST sub_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xsub = new XMLElement("SUB");
		
		try {      // for error handling
			AST __t403 = _t;
			AST tmp192_AST_in = (AST)_t;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			numericExpression(_t,xsub);
			_t = _retTree;
			numericExpression(_t,xsub);
			_t = _retTree;
			_t = __t403;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xsub);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void mul(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST mul_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xmul = new XMLElement("MUL");
		
		try {      // for error handling
			AST __t405 = _t;
			AST tmp193_AST_in = (AST)_t;
			match(_t,ASTERISK);
			_t = _t.getFirstChild();
			numericExpression(_t,xmul);
			_t = _retTree;
			{
			int _cnt407=0;
			_loop407:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_3.member(_t.getType()))) {
					numericExpression(_t,xmul);
					_t = _retTree;
				}
				else {
					if ( _cnt407>=1 ) { break _loop407; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt407++;
			} while (true);
			}
			_t = __t405;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xmul);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void div(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST div_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xdiv = new XMLElement("DIV");
		
		try {      // for error handling
			AST __t409 = _t;
			AST tmp194_AST_in = (AST)_t;
			match(_t,SLASH);
			_t = _t.getFirstChild();
			numericExpression(_t,xdiv);
			_t = _retTree;
			numericExpression(_t,xdiv);
			_t = _retTree;
			_t = __t409;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xdiv);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void absValue(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST absValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xabs = new XMLElement("ABS");
		
		try {      // for error handling
			AST __t395 = _t;
			AST tmp195_AST_in = (AST)_t;
			match(_t,ABS_KYWD);
			_t = _t.getFirstChild();
			numericExpression(_t,xabs);
			_t = _retTree;
			_t = __t395;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xabs);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void sqrt(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST sqrt_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xsqrt = new XMLElement("SQRT");
		
		try {      // for error handling
			AST __t397 = _t;
			AST tmp196_AST_in = (AST)_t;
			match(_t,SQRT_KYWD);
			_t = _t.getFirstChild();
			numericExpression(_t,xsqrt);
			_t = _retTree;
			_t = __t397;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xsqrt);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stringConcatenation(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stringConcatenation_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		IXMLElement xconcat = new XMLElement("Concat");
		
		try {      // for error handling
			AST __t416 = _t;
			AST tmp197_AST_in = (AST)_t;
			match(_t,CONCAT);
			_t = _t.getFirstChild();
			stringExpression(_t,xconcat);
			_t = _retTree;
			stringExpression(_t,xconcat);
			_t = _retTree;
			_t = __t416;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				parent.addChild(xconcat);
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void lookup(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST lookup_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST a = null;
		AST t = null;
		
		IXMLElement xl = new XMLElement("Lookup"); 
		parent.addChild(xl);
		
		
		try {      // for error handling
			AST __t422 = _t;
			AST tmp198_AST_in = (AST)_t;
			match(_t,LOOKUP_KYWD);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STATE_NAME:
			{
				stateNameLiteral(_t,xl);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			case STRING:
			case VARIABLE:
			case ARRAY_REF:
			case CONCAT:
			{
				nameExp(_t,xl);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ARGUMENT_LIST:
			{
				a = _t==ASTNULL ? null : (AST)_t;
				argumentList(_t,xl);
				_t = _retTree;
				break;
			}
			case 3:
			case INT:
			case DOUBLE:
			case VARIABLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			case VARIABLE:
			{
				t = _t==ASTNULL ? null : (AST)_t;
				tolerance(_t,xl);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t422;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				// have to scramble only if both tolerance and arglist are provided
				// if either is missing, order is fine
				if (a != null && t != null)
				{
				IXMLElement arglist = xl.getChildAtIndex(1); // 0-based index
				xl.removeChildAtIndex(1);
				xl.addChild(arglist);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void lookupOnChange(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST lookupOnChange_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST a = null;
		AST t = null;
		
		IXMLElement xloc = new XMLElement("LookupOnChange");
		parent.addChild(xloc);
		
		
		try {      // for error handling
			AST __t431 = _t;
			AST tmp199_AST_in = (AST)_t;
			match(_t,LOOKUP_ON_CHANGE_KYWD);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STATE_NAME:
			{
				stateNameLiteral(_t,xloc);
				_t = _retTree;
				break;
			}
			case LOOKUP_KYWD:
			case LOOKUP_ON_CHANGE_KYWD:
			case LOOKUP_NOW_KYWD:
			case STRING:
			case VARIABLE:
			case ARRAY_REF:
			case CONCAT:
			{
				nameExp(_t,xloc);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ARGUMENT_LIST:
			{
				a = _t==ASTNULL ? null : (AST)_t;
				argumentList(_t,xloc);
				_t = _retTree;
				break;
			}
			case 3:
			case INT:
			case DOUBLE:
			case VARIABLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			case VARIABLE:
			{
				t = _t==ASTNULL ? null : (AST)_t;
				tolerance(_t,xloc);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t431;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				// have to scramble only if both tolerance and arglist are provided
				// if either is missing, order is fine
				if (a != null && t != null)
				{
				IXMLElement arglist = xloc.getChildAtIndex(1); // 0-based index
				xloc.removeChildAtIndex(1);
				xloc.addChild(arglist);
				}
				
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void stateNameLiteral(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST stateNameLiteral_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t436 = _t;
			AST tmp200_AST_in = (AST)_t;
			match(_t,STATE_NAME);
			_t = _t.getFirstChild();
			nameLiteral(_t,parent);
			_t = _retTree;
			_t = __t436;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void tolerance(AST _t,
		IXMLElement parent
	) throws RecognitionException {
		
		AST tolerance_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST rval = null;
		IXMLElement xtol = new XMLElement("Tolerance"); parent.addChild(xtol);
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INT:
			case DOUBLE:
			{
				rval = _t==ASTNULL ? null : (AST)_t;
				realValue(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					xtol.addChild(((PlexilASTNode) rval).getXmlElement());
				}
				break;
			}
			case VARIABLE:
			{
				variable(_t,xtol);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"Comment\"",
		"\"Command\"",
		"\"Lookup\"",
		"\"LibraryNode\"",
		"\"returns\"",
		"\"Resource\"",
		"\"ResourcePriority\"",
		"\"Name\"",
		"\"UpperBound\"",
		"\"LowerBound\"",
		"\"ReleaseAtTermination\"",
		"\"Priority\"",
		"\"Permissions\"",
		"\"StartCondition\"",
		"\"RepeatCondition\"",
		"\"SkipCondition\"",
		"\"PreCondition\"",
		"\"PostCondition\"",
		"\"InvariantCondition\"",
		"\"EndCondition\"",
		"\"Start\"",
		"\"Repeat\"",
		"\"Skip\"",
		"\"Pre\"",
		"\"Post\"",
		"\"Invariant\"",
		"\"End\"",
		"\"In\"",
		"\"InOut\"",
		"\"Boolean\"",
		"\"Integer\"",
		"\"Real\"",
		"\"String\"",
		"\"BLOB\"",
		"\"Time\"",
		"\"true\"",
		"\"false\"",
		"\"NodeList\"",
		"\"Assignment\"",
		"\"Update\"",
		"\"Request\"",
		"\"LibraryCall\"",
		"\"OR\"",
		"\"AND\"",
		"\"NOT\"",
		"\"state\"",
		"\"outcome\"",
		"\"command_handle\"",
		"\"failure\"",
		"\"WAITING\"",
		"\"EXECUTING\"",
		"\"FINISHING\"",
		"\"FAILING\"",
		"\"FINISHED\"",
		"\"ITERATION_ENDED\"",
		"\"INACTIVE\"",
		"\"SUCCESS\"",
		"\"FAILURE\"",
		"\"SKIPPED\"",
		"\"COMMAND_ABORTED\"",
		"\"COMMAND_ABORT_FAILED\"",
		"\"COMMAND_ACCEPTED\"",
		"\"COMMAND_DENIED\"",
		"\"COMMAND_FAILED\"",
		"\"COMMAND_RCVD_BY_SYSTEM\"",
		"\"COMMAND_SENT_TO_SYSTEM\"",
		"\"COMMAND_SUCCESS\"",
		"\"PRE_CONDITION_FAILED\"",
		"\"POST_CONDITION_FAILED\"",
		"\"INVARIANT_CONDITION_FAILED\"",
		"\"PARENT_FAILED\"",
		"\"START\"",
		"\"END\"",
		"\"LookupOnChange\"",
		"\"LookupNow\"",
		"\"sqrt\"",
		"\"abs\"",
		"\"isKnown\"",
		"\"NodeExecuting\"",
		"\"NodeFailed\"",
		"\"NodeFinished\"",
		"\"NodeInactive\"",
		"\"NodeInvariantFailed\"",
		"\"NodeIterationEnded\"",
		"\"NodeIterationFailed\"",
		"\"NodeIterationSucceeded\"",
		"\"NodeParentFailed\"",
		"\"NodePostconditionFailed\"",
		"\"NodePreconditionFailed\"",
		"\"NodeSkipped\"",
		"\"NodeSucceeded\"",
		"\"NodeWaiting\"",
		"\"Sequence\"",
		"\"Concurrence\"",
		"\"UncheckedSequence\"",
		"\"Try\"",
		"\"If\"",
		"\"Then\"",
		"\"Else\"",
		"\"While\"",
		"\"OnCommand\"",
		"\"OnMessage\"",
		"\"For\"",
		"\"MessageReceived\"",
		"INT",
		"DOUBLE",
		"STRING",
		"NCName",
		"NodeName",
		"PLEXIL",
		"NODE",
		"NODE_ID",
		"GLOBAL_DECLARATIONS",
		"PARAMETER_DECLARATIONS",
		"PARAMETER",
		"RETURN_VALUE",
		"VARIABLE",
		"VARIABLE_DECLARATION",
		"ALIASES",
		"CONST_ALIAS",
		"VARIABLE_ALIAS",
		"ARRAY_ASSIGNMENT",
		"BOOLEAN_ASSIGNMENT",
		"INTEGER_ASSIGNMENT",
		"REAL_ASSIGNMENT",
		"STRING_ASSIGNMENT",
		"TIME_ASSIGNMENT",
		"BLOB_ARRAY",
		"BOOLEAN_ARRAY",
		"INTEGER_ARRAY",
		"REAL_ARRAY",
		"STRING_ARRAY",
		"TIME_ARRAY",
		"ARRAY_REF",
		"BOOLEAN_ARRAY_LITERAL",
		"INTEGER_ARRAY_LITERAL",
		"REAL_ARRAY_LITERAL",
		"STRING_ARRAY_LITERAL",
		"TIME_ARRAY_LITERAL",
		"BOOLEAN_COMPARISON",
		"NODE_STATE_COMPARISON",
		"NODE_OUTCOME_COMPARISON",
		"NODE_COMMAND_HANDLE_COMPARISON",
		"NODE_FAILURE_COMPARISON",
		"NUMERIC_COMPARISON",
		"STRING_COMPARISON",
		"TIME_COMPARISON",
		"LOOKUP_STATE",
		"POINTS_TO",
		"TIME_VALUE",
		"NODE_STATE_VARIABLE",
		"NODE_OUTCOME_VARIABLE",
		"NODE_COMMAND_HANDLE_VARIABLE",
		"NODE_FAILURE_VARIABLE",
		"NODE_TIMEPOINT_VALUE",
		"ARGUMENT_LIST",
		"COMMAND_NAME",
		"STATE_NAME",
		"CONCAT",
		"NODE_STATE_PREDICATE",
		"ON_COMMAND_PARAMS",
		"SEMICOLON",
		"LPAREN",
		"COMMA",
		"RPAREN",
		"COLON",
		"LBRACE",
		"RBRACE",
		"EQUALS",
		"LBRACKET",
		"RBRACKET",
		"HASHPAREN",
		"OR_ALT_KYWD",
		"AND_ALT_KYWD",
		"DEQUALS",
		"NEQUALS",
		"BANG",
		"GREATER",
		"GEQ",
		"LESS",
		"LEQ",
		"PERIOD",
		"PLUS",
		"MINUS",
		"ASTERISK",
		"SLASH",
		"BAR",
		"IDENT",
		"DCOLON",
		"ESC",
		"UNICODE_ESC",
		"NUMERIC_ESC",
		"QUAD_DIGIT",
		"OCTAL_DIGIT",
		"DIGIT",
		"HEX_DIGIT",
		"EXPONENT",
		"FLOAT_SUFFIX",
		"whitespace, such as a space, tab, or newline",
		"a single line comment, delimited by //",
		"a multiple line comment, such as /* <comment> */",
		"NO_TYPE"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 16745984L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 1649267441664L, 72180739340238848L, 33554944L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 212755499974720L, 72083982317150208L, 139611622816580096L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 64L, 72110370596184064L, 4323455918227325440L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	}
	
