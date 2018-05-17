// $ANTLR 2.7.6 (20060405): "plexil.g" -> "PlexilLexer.java"$

package plexil;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;


import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class PlexilLexer extends antlr.CharScanner implements PlexilTokenTypes, TokenStream
 {

  static final int PLEXIL_MAJOR_RELEASE = 1;
  static final int PLEXIL_MINOR_RELEASE = 0;
  static final int PLEXIL_LEXER_RELEASE = 0;
  public String toString()
  {
    return "PlexilLexer "+
           PLEXIL_MAJOR_RELEASE+"."+
           PLEXIL_MINOR_RELEASE+"."+
           PLEXIL_LEXER_RELEASE;
  }
public PlexilLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public PlexilLexer(Reader in) {
	this(new CharBuffer(in));
}
public PlexilLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public PlexilLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
	literals.put(new ANTLRHashString("BLOB", this), new Integer(37));
	literals.put(new ANTLRHashString("InOut", this), new Integer(32));
	literals.put(new ANTLRHashString("outcome", this), new Integer(50));
	literals.put(new ANTLRHashString("POST_CONDITION_FAILED", this), new Integer(72));
	literals.put(new ANTLRHashString("NodeSucceeded", this), new Integer(94));
	literals.put(new ANTLRHashString("For", this), new Integer(106));
	literals.put(new ANTLRHashString("Update", this), new Integer(43));
	literals.put(new ANTLRHashString("OR", this), new Integer(46));
	literals.put(new ANTLRHashString("NodeFinished", this), new Integer(84));
	literals.put(new ANTLRHashString("Concurrence", this), new Integer(97));
	literals.put(new ANTLRHashString("Invariant", this), new Integer(29));
	literals.put(new ANTLRHashString("END", this), new Integer(76));
	literals.put(new ANTLRHashString("LookupNow", this), new Integer(78));
	literals.put(new ANTLRHashString("WAITING", this), new Integer(53));
	literals.put(new ANTLRHashString("Boolean", this), new Integer(33));
	literals.put(new ANTLRHashString("ResourcePriority", this), new Integer(10));
	literals.put(new ANTLRHashString("Request", this), new Integer(44));
	literals.put(new ANTLRHashString("Priority", this), new Integer(15));
	literals.put(new ANTLRHashString("Comment", this), new Integer(4));
	literals.put(new ANTLRHashString("Command", this), new Integer(5));
	literals.put(new ANTLRHashString("NodeInactive", this), new Integer(85));
	literals.put(new ANTLRHashString("Try", this), new Integer(99));
	literals.put(new ANTLRHashString("Repeat", this), new Integer(25));
	literals.put(new ANTLRHashString("COMMAND_RCVD_BY_SYSTEM", this), new Integer(68));
	literals.put(new ANTLRHashString("OnMessage", this), new Integer(105));
	literals.put(new ANTLRHashString("SkipCondition", this), new Integer(19));
	literals.put(new ANTLRHashString("Assignment", this), new Integer(42));
	literals.put(new ANTLRHashString("NodeParentFailed", this), new Integer(90));
	literals.put(new ANTLRHashString("START", this), new Integer(75));
	literals.put(new ANTLRHashString("Skip", this), new Integer(26));
	literals.put(new ANTLRHashString("command_handle", this), new Integer(51));
	literals.put(new ANTLRHashString("COMMAND_SUCCESS", this), new Integer(70));
	literals.put(new ANTLRHashString("state", this), new Integer(49));
	literals.put(new ANTLRHashString("Else", this), new Integer(102));
	literals.put(new ANTLRHashString("Time", this), new Integer(38));
	literals.put(new ANTLRHashString("COMMAND_FAILED", this), new Integer(67));
	literals.put(new ANTLRHashString("failure", this), new Integer(52));
	literals.put(new ANTLRHashString("Integer", this), new Integer(34));
	literals.put(new ANTLRHashString("FINISHING", this), new Integer(55));
	literals.put(new ANTLRHashString("ReleaseAtTermination", this), new Integer(14));
	literals.put(new ANTLRHashString("Pre", this), new Integer(27));
	literals.put(new ANTLRHashString("NodeSkipped", this), new Integer(93));
	literals.put(new ANTLRHashString("In", this), new Integer(31));
	literals.put(new ANTLRHashString("COMMAND_ACCEPTED", this), new Integer(65));
	literals.put(new ANTLRHashString("While", this), new Integer(103));
	literals.put(new ANTLRHashString("NodeList", this), new Integer(41));
	literals.put(new ANTLRHashString("AND", this), new Integer(47));
	literals.put(new ANTLRHashString("MessageReceived", this), new Integer(107));
	literals.put(new ANTLRHashString("Post", this), new Integer(28));
	literals.put(new ANTLRHashString("COMMAND_DENIED", this), new Integer(66));
	literals.put(new ANTLRHashString("COMMAND_SENT_TO_SYSTEM", this), new Integer(69));
	literals.put(new ANTLRHashString("Name", this), new Integer(11));
	literals.put(new ANTLRHashString("NodePostconditionFailed", this), new Integer(91));
	literals.put(new ANTLRHashString("false", this), new Integer(40));
	literals.put(new ANTLRHashString("Sequence", this), new Integer(96));
	literals.put(new ANTLRHashString("PostCondition", this), new Integer(21));
	literals.put(new ANTLRHashString("NodeIterationEnded", this), new Integer(87));
	literals.put(new ANTLRHashString("true", this), new Integer(39));
	literals.put(new ANTLRHashString("PRE_CONDITION_FAILED", this), new Integer(71));
	literals.put(new ANTLRHashString("LibraryNode", this), new Integer(7));
	literals.put(new ANTLRHashString("abs", this), new Integer(80));
	literals.put(new ANTLRHashString("UpperBound", this), new Integer(12));
	literals.put(new ANTLRHashString("Real", this), new Integer(35));
	literals.put(new ANTLRHashString("NodeFailed", this), new Integer(83));
	literals.put(new ANTLRHashString("sqrt", this), new Integer(79));
	literals.put(new ANTLRHashString("NodePreconditionFailed", this), new Integer(92));
	literals.put(new ANTLRHashString("NOT", this), new Integer(48));
	literals.put(new ANTLRHashString("ITERATION_ENDED", this), new Integer(58));
	literals.put(new ANTLRHashString("NodeWaiting", this), new Integer(95));
	literals.put(new ANTLRHashString("InvariantCondition", this), new Integer(22));
	literals.put(new ANTLRHashString("COMMAND_ABORT_FAILED", this), new Integer(64));
	literals.put(new ANTLRHashString("Permissions", this), new Integer(16));
	literals.put(new ANTLRHashString("NodeIterationFailed", this), new Integer(88));
	literals.put(new ANTLRHashString("Resource", this), new Integer(9));
	literals.put(new ANTLRHashString("PreCondition", this), new Integer(20));
	literals.put(new ANTLRHashString("FAILURE", this), new Integer(61));
	literals.put(new ANTLRHashString("RepeatCondition", this), new Integer(18));
	literals.put(new ANTLRHashString("End", this), new Integer(30));
	literals.put(new ANTLRHashString("FAILING", this), new Integer(56));
	literals.put(new ANTLRHashString("NodeIterationSucceeded", this), new Integer(89));
	literals.put(new ANTLRHashString("String", this), new Integer(36));
	literals.put(new ANTLRHashString("If", this), new Integer(100));
	literals.put(new ANTLRHashString("Lookup", this), new Integer(6));
	literals.put(new ANTLRHashString("NodeExecuting", this), new Integer(82));
	literals.put(new ANTLRHashString("COMMAND_ABORTED", this), new Integer(63));
	literals.put(new ANTLRHashString("Start", this), new Integer(24));
	literals.put(new ANTLRHashString("LookupOnChange", this), new Integer(77));
	literals.put(new ANTLRHashString("EXECUTING", this), new Integer(54));
	literals.put(new ANTLRHashString("LibraryCall", this), new Integer(45));
	literals.put(new ANTLRHashString("isKnown", this), new Integer(81));
	literals.put(new ANTLRHashString("FINISHED", this), new Integer(57));
	literals.put(new ANTLRHashString("PARENT_FAILED", this), new Integer(74));
	literals.put(new ANTLRHashString("Then", this), new Integer(101));
	literals.put(new ANTLRHashString("NodeInvariantFailed", this), new Integer(86));
	literals.put(new ANTLRHashString("INVARIANT_CONDITION_FAILED", this), new Integer(73));
	literals.put(new ANTLRHashString("EndCondition", this), new Integer(23));
	literals.put(new ANTLRHashString("returns", this), new Integer(8));
	literals.put(new ANTLRHashString("StartCondition", this), new Integer(17));
	literals.put(new ANTLRHashString("SKIPPED", this), new Integer(62));
	literals.put(new ANTLRHashString("SUCCESS", this), new Integer(60));
	literals.put(new ANTLRHashString("LowerBound", this), new Integer(13));
	literals.put(new ANTLRHashString("UncheckedSequence", this), new Integer(98));
	literals.put(new ANTLRHashString("OnCommand", this), new Integer(104));
	literals.put(new ANTLRHashString("INACTIVE", this), new Integer(59));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '[':
				{
					mLBRACKET(true);
					theRetToken=_returnToken;
					break;
				}
				case ']':
				{
					mRBRACKET(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLBRACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRBRACE(true);
					theRetToken=_returnToken;
					break;
				}
				case '(':
				{
					mLPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '&':
				{
					mAND_ALT_KYWD(true);
					theRetToken=_returnToken;
					break;
				}
				case '*':
				{
					mASTERISK(true);
					theRetToken=_returnToken;
					break;
				}
				case '#':
				{
					mHASHPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMICOLON(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':  case '\'':
				{
					mSTRING(true);
					theRetToken=_returnToken;
					break;
				}
				case '\\':
				{
					mESC(true);
					theRetToken=_returnToken;
					break;
				}
				case 'A':  case 'B':  case 'C':  case 'D':
				case 'E':  case 'F':  case 'G':  case 'H':
				case 'I':  case 'J':  case 'K':  case 'L':
				case 'M':  case 'N':  case 'O':  case 'P':
				case 'Q':  case 'R':  case 'S':  case 'T':
				case 'U':  case 'V':  case 'W':  case 'X':
				case 'Y':  case 'Z':  case '_':  case 'a':
				case 'b':  case 'c':  case 'd':  case 'e':
				case 'f':  case 'g':  case 'h':  case 'i':
				case 'j':  case 'k':  case 'l':  case 'm':
				case 'n':  case 'o':  case 'p':  case 'q':
				case 'r':  case 's':  case 't':  case 'u':
				case 'v':  case 'w':  case 'x':  case 'y':
				case 'z':
				{
					mIDENT(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\n':  case '\u000c':  case '\r':
				case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='|') && (LA(2)=='|')) {
						mOR_ALT_KYWD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='=')) {
						mLEQ(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='=')) {
						mGEQ(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)==':') && (LA(2)==':')) {
						mDCOLON(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='=')) {
						mDEQUALS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (LA(2)=='=')) {
						mNEQUALS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='/')) {
						mSL_COMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='*')) {
						mML_COMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (true)) {
						mBAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (true)) {
						mLESS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (true)) {
						mGREATER(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)==':') && (true)) {
						mCOLON(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (true)) {
						mEQUALS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (true)) {
						mSLASH(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='.') && (true)) {
						mPERIOD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (true)) {
						mBANG(true);
						theRetToken=_returnToken;
					}
					else if ((_tokenSet_0.member(LA(1))) && (true)) {
						mINT(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mLBRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LBRACKET;
		int _saveIndex;
		
		match('[');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRBRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RBRACKET;
		int _saveIndex;
		
		match(']');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLBRACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LBRACE;
		int _saveIndex;
		
		match('{');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRBRACE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RBRACE;
		int _saveIndex;
		
		match('}');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		match('(');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		match(')');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAR;
		int _saveIndex;
		
		match('|');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOR_ALT_KYWD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OR_ALT_KYWD;
		int _saveIndex;
		
		match("||");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mAND_ALT_KYWD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = AND_ALT_KYWD;
		int _saveIndex;
		
		match("&&");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLESS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LESS;
		int _saveIndex;
		
		match('<');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGREATER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GREATER;
		int _saveIndex;
		
		match('>');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LEQ;
		int _saveIndex;
		
		match("<=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GEQ;
		int _saveIndex;
		
		match(">=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLON;
		int _saveIndex;
		
		match(':');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DCOLON;
		int _saveIndex;
		
		match("::");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDEQUALS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DEQUALS;
		int _saveIndex;
		
		match("==");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNEQUALS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NEQUALS;
		int _saveIndex;
		
		match("!=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEQUALS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EQUALS;
		int _saveIndex;
		
		match('=');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASTERISK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASTERISK;
		int _saveIndex;
		
		match('*');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSLASH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SLASH;
		int _saveIndex;
		
		match('/');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPERIOD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PERIOD;
		int _saveIndex;
		
		match('.');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mHASHPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HASHPAREN;
		int _saveIndex;
		
		match("#(");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMICOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMICOLON;
		int _saveIndex;
		
		match(';');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		match(',');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBANG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BANG;
		int _saveIndex;
		
		match('!');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING;
		int _saveIndex;
		
		switch ( LA(1)) {
		case '"':
		{
			match('"');
			{
			_loop416:
			do {
				switch ( LA(1)) {
				case '\\':
				{
					mESC(false);
					break;
				}
				case '\u0003':  case '\u0004':  case '\u0005':  case '\u0006':
				case '\u0007':  case '\u0008':  case '\t':  case '\n':
				case '\u000b':  case '\u000c':  case '\r':  case '\u000e':
				case '\u000f':  case '\u0010':  case '\u0011':  case '\u0012':
				case '\u0013':  case '\u0014':  case '\u0015':  case '\u0016':
				case '\u0017':  case '\u0018':  case '\u0019':  case '\u001a':
				case '\u001b':  case '\u001c':  case '\u001d':  case '\u001e':
				case '\u001f':  case ' ':  case '!':  case '#':
				case '$':  case '%':  case '&':  case '\'':
				case '(':  case ')':  case '*':  case '+':
				case ',':  case '-':  case '.':  case '/':
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':  case ':':  case ';':
				case '<':  case '=':  case '>':  case '?':
				case '@':  case 'A':  case 'B':  case 'C':
				case 'D':  case 'E':  case 'F':  case 'G':
				case 'H':  case 'I':  case 'J':  case 'K':
				case 'L':  case 'M':  case 'N':  case 'O':
				case 'P':  case 'Q':  case 'R':  case 'S':
				case 'T':  case 'U':  case 'V':  case 'W':
				case 'X':  case 'Y':  case 'Z':  case '[':
				case ']':  case '^':  case '_':  case '`':
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':  case '{':  case '|':
				case '}':  case '~':  case '\u007f':
				{
					{
					match(_tokenSet_1);
					}
					break;
				}
				default:
				{
					break _loop416;
				}
				}
			} while (true);
			}
			match('"');
			break;
		}
		case '\'':
		{
			match('\'');
			{
			_loop419:
			do {
				switch ( LA(1)) {
				case '\\':
				{
					mESC(false);
					break;
				}
				case '\u0003':  case '\u0004':  case '\u0005':  case '\u0006':
				case '\u0007':  case '\u0008':  case '\t':  case '\n':
				case '\u000b':  case '\u000c':  case '\r':  case '\u000e':
				case '\u000f':  case '\u0010':  case '\u0011':  case '\u0012':
				case '\u0013':  case '\u0014':  case '\u0015':  case '\u0016':
				case '\u0017':  case '\u0018':  case '\u0019':  case '\u001a':
				case '\u001b':  case '\u001c':  case '\u001d':  case '\u001e':
				case '\u001f':  case ' ':  case '!':  case '"':
				case '#':  case '$':  case '%':  case '&':
				case '(':  case ')':  case '*':  case '+':
				case ',':  case '-':  case '.':  case '/':
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':  case ':':  case ';':
				case '<':  case '=':  case '>':  case '?':
				case '@':  case 'A':  case 'B':  case 'C':
				case 'D':  case 'E':  case 'F':  case 'G':
				case 'H':  case 'I':  case 'J':  case 'K':
				case 'L':  case 'M':  case 'N':  case 'O':
				case 'P':  case 'Q':  case 'R':  case 'S':
				case 'T':  case 'U':  case 'V':  case 'W':
				case 'X':  case 'Y':  case 'Z':  case '[':
				case ']':  case '^':  case '_':  case '`':
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':  case '{':  case '|':
				case '}':  case '~':  case '\u007f':
				{
					{
					match(_tokenSet_2);
					}
					break;
				}
				default:
				{
					break _loop419;
				}
				}
			} while (true);
			}
			match('\'');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		match('\\');
		{
		switch ( LA(1)) {
		case 'n':
		{
			match('n');
			break;
		}
		case 't':
		{
			match('t');
			break;
		}
		case 'b':
		{
			match('b');
			break;
		}
		case 'f':
		{
			match('f');
			break;
		}
		case '"':
		{
			match('"');
			break;
		}
		case '\'':
		{
			match('\'');
			break;
		}
		case '\\':
		{
			match('\\');
			break;
		}
		case 'u':
		{
			mUNICODE_ESC(false);
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		{
			mNUMERIC_ESC(false);
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mUNICODE_ESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = UNICODE_ESC;
		int _saveIndex;
		
		match('u');
		mHEX_DIGIT(false);
		mHEX_DIGIT(false);
		mHEX_DIGIT(false);
		mHEX_DIGIT(false);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mNUMERIC_ESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NUMERIC_ESC;
		int _saveIndex;
		
		if (((LA(1) >= '0' && LA(1) <= '3')) && (true)) {
			mQUAD_DIGIT(false);
			{
			if (((LA(1) >= '0' && LA(1) <= '7')) && (true)) {
				mOCTAL_DIGIT(false);
				{
				if (((LA(1) >= '0' && LA(1) <= '7')) && (true)) {
					mOCTAL_DIGIT(false);
				}
				else {
				}
				
				}
			}
			else {
			}
			
			}
		}
		else if (((LA(1) >= '0' && LA(1) <= '7')) && (true)) {
			mOCTAL_DIGIT(false);
			{
			if (((LA(1) >= '0' && LA(1) <= '7')) && (true)) {
				mOCTAL_DIGIT(false);
			}
			else {
			}
			
			}
		}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX_DIGIT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			mDIGIT(false);
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':
		{
			matchRange('A','F');
			break;
		}
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':
		{
			matchRange('a','f');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mQUAD_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = QUAD_DIGIT;
		int _saveIndex;
		
		{
		matchRange('0','3');
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mOCTAL_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCTAL_DIGIT;
		int _saveIndex;
		
		{
		matchRange('0','7');
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGIT;
		int _saveIndex;
		
		{
		matchRange('0','9');
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS;
		int _saveIndex;
		
		match('+');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS;
		int _saveIndex;
		
		match('-');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mEXPONENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EXPONENT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'e':
		{
			match('e');
			break;
		}
		case 'E':
		{
			match('E');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		switch ( LA(1)) {
		case '+':
		{
			mPLUS(false);
			break;
		}
		case '-':
		{
			mMINUS(false);
			break;
		}
		case '0':  case '1':  case '2':  case '3':
		case '4':  case '5':  case '6':  case '7':
		case '8':  case '9':
		{
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		int _cnt441=0;
		_loop441:
		do {
			if (((LA(1) >= '0' && LA(1) <= '9'))) {
				mDIGIT(false);
			}
			else {
				if ( _cnt441>=1 ) { break _loop441; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt441++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mFLOAT_SUFFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = FLOAT_SUFFIX;
		int _saveIndex;
		
		switch ( LA(1)) {
		case 'f':
		{
			match('f');
			break;
		}
		case 'F':
		{
			match('F');
			break;
		}
		case 'd':
		{
			match('d');
			break;
		}
		case 'D':
		{
			match('D');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INT;
		int _saveIndex;
		int base = 0;
		
		switch ( LA(1)) {
		case '.':
		{
			{
			mPERIOD(false);
			{
			int _cnt467=0;
			_loop467:
			do {
				if (((LA(1) >= '0' && LA(1) <= '9'))) {
					mDIGIT(false);
				}
				else {
					if ( _cnt467>=1 ) { break _loop467; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt467++;
			} while (true);
			}
			{
			if ((LA(1)=='E'||LA(1)=='e')) {
				mEXPONENT(false);
			}
			else {
			}
			
			}
			if ( inputState.guessing==0 ) {
				_ttype = DOUBLE; base = 10;
			}
			}
			break;
		}
		case '+':  case '-':
		{
			{
			{
			switch ( LA(1)) {
			case '+':
			{
				match('+');
				if ( inputState.guessing==0 ) {
					_ttype = PLUS;
				}
				break;
			}
			case '-':
			{
				match('-');
				if ( inputState.guessing==0 ) {
					_ttype = MINUS;
				}
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			if ((_tokenSet_3.member(LA(1)))) {
				{
				switch ( LA(1)) {
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':
				{
					{
					int _cnt481=0;
					_loop481:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							mDIGIT(false);
						}
						else {
							if ( _cnt481>=1 ) { break _loop481; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt481++;
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						_ttype = INT;
					}
					{
					if ((LA(1)=='.')) {
						mPERIOD(false);
						{
						_loop484:
						do {
							if (((LA(1) >= '0' && LA(1) <= '9'))) {
								mDIGIT(false);
							}
							else {
								break _loop484;
							}
							
						} while (true);
						}
						if ( inputState.guessing==0 ) {
							_ttype = DOUBLE;
						}
					}
					else {
					}
					
					}
					{
					if ((LA(1)=='E'||LA(1)=='e')) {
						mEXPONENT(false);
						if ( inputState.guessing==0 ) {
							_ttype = DOUBLE;
						}
					}
					else {
					}
					
					}
					break;
				}
				case '.':
				{
					{
					mPERIOD(false);
					{
					int _cnt488=0;
					_loop488:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							mDIGIT(false);
						}
						else {
							if ( _cnt488>=1 ) { break _loop488; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt488++;
					} while (true);
					}
					{
					if ((LA(1)=='E'||LA(1)=='e')) {
						mEXPONENT(false);
					}
					else {
					}
					
					}
					if ( inputState.guessing==0 ) {
						_ttype = DOUBLE;
					}
					}
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					base = 10;
				}
			}
			else {
			}
			
			}
			}
			break;
		}
		default:
			boolean synPredMatched445 = false;
			if (((LA(1)=='0') && (true))) {
				int _m445 = mark();
				synPredMatched445 = true;
				inputState.guessing++;
				try {
					{
					_saveIndex=text.length();
					match('0');
					text.setLength(_saveIndex);
					mDIGIT(false);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched445 = false;
				}
				rewind(_m445);
inputState.guessing--;
			}
			if ( synPredMatched445 ) {
				{
				match('0');
				if ( inputState.guessing==0 ) {
					base = 10;
				}
				{
				switch ( LA(1)) {
				case 'X':  case 'x':
				{
					{
					{
					switch ( LA(1)) {
					case 'x':
					{
						match('x');
						break;
					}
					case 'X':
					{
						match('X');
						if ( inputState.guessing==0 ) {
							base = 16;
						}
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					{
					int _cnt451=0;
					_loop451:
					do {
						if ((_tokenSet_4.member(LA(1)))) {
							mHEX_DIGIT(false);
						}
						else {
							if ( _cnt451>=1 ) { break _loop451; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt451++;
					} while (true);
					}
					}
					break;
				}
				case 'O':  case 'o':
				{
					{
					{
					switch ( LA(1)) {
					case 'o':
					{
						match('o');
						break;
					}
					case 'O':
					{
						match('O');
						if ( inputState.guessing==0 ) {
							base = 8;
						}
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					{
					int _cnt455=0;
					_loop455:
					do {
						if (((LA(1) >= '0' && LA(1) <= '7'))) {
							mOCTAL_DIGIT(false);
						}
						else {
							if ( _cnt455>=1 ) { break _loop455; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt455++;
					} while (true);
					}
					}
					break;
				}
				case 'B':  case 'b':
				{
					{
					{
					switch ( LA(1)) {
					case 'b':
					{
						match('b');
						break;
					}
					case 'B':
					{
						match('B');
						if ( inputState.guessing==0 ) {
							base = 2;
						}
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					{
					int _cnt459=0;
					_loop459:
					do {
						switch ( LA(1)) {
						case '0':
						{
							match('0');
							break;
						}
						case '1':
						{
							match('1');
							break;
						}
						default:
						{
							if ( _cnt459>=1 ) { break _loop459; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						}
						_cnt459++;
					} while (true);
					}
					}
					break;
				}
				case '.':
				{
					{
					mPERIOD(false);
					if ( inputState.guessing==0 ) {
						_ttype = DOUBLE;
					}
					{
					_loop462:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							mDIGIT(false);
						}
						else {
							break _loop462;
						}
						
					} while (true);
					}
					{
					if ((LA(1)=='E'||LA(1)=='e')) {
						mEXPONENT(false);
					}
					else {
					}
					
					}
					}
					break;
				}
				case 'E':  case 'e':
				{
					{
					mEXPONENT(false);
					if ( inputState.guessing==0 ) {
						_ttype = DOUBLE;
					}
					}
					break;
				}
				default:
					{
					}
				}
				}
				}
			}
			else if (((LA(1) >= '0' && LA(1) <= '9')) && (true)) {
				{
				{
				int _cnt471=0;
				_loop471:
				do {
					if (((LA(1) >= '0' && LA(1) <= '9'))) {
						mDIGIT(false);
					}
					else {
						if ( _cnt471>=1 ) { break _loop471; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt471++;
				} while (true);
				}
				{
				if ((LA(1)=='.')) {
					mPERIOD(false);
					{
					_loop474:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							mDIGIT(false);
						}
						else {
							break _loop474;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						_ttype = DOUBLE;
					}
				}
				else {
				}
				
				}
				{
				if ((LA(1)=='E'||LA(1)=='e')) {
					mEXPONENT(false);
					if ( inputState.guessing==0 ) {
						_ttype = DOUBLE;
					}
				}
				else {
				}
				
				}
				if ( inputState.guessing==0 ) {
					base = 10;
				}
				}
			}
		else {
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mIDENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENT;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case 'a':  case 'b':  case 'c':  case 'd':
		case 'e':  case 'f':  case 'g':  case 'h':
		case 'i':  case 'j':  case 'k':  case 'l':
		case 'm':  case 'n':  case 'o':  case 'p':
		case 'q':  case 'r':  case 's':  case 't':
		case 'u':  case 'v':  case 'w':  case 'x':
		case 'y':  case 'z':
		{
			matchRange('a','z');
			break;
		}
		case 'A':  case 'B':  case 'C':  case 'D':
		case 'E':  case 'F':  case 'G':  case 'H':
		case 'I':  case 'J':  case 'K':  case 'L':
		case 'M':  case 'N':  case 'O':  case 'P':
		case 'Q':  case 'R':  case 'S':  case 'T':
		case 'U':  case 'V':  case 'W':  case 'X':
		case 'Y':  case 'Z':
		{
			matchRange('A','Z');
			break;
		}
		case '_':
		{
			match('_');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		{
		_loop493:
		do {
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			default:
			{
				break _loop493;
			}
			}
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '\t':
		{
			match('\t');
			break;
		}
		case '\u000c':
		{
			match('\f');
			break;
		}
		case '\n':  case '\r':
		{
			{
			if ((LA(1)=='\r') && (LA(2)=='\n')) {
				match("\r\n");
			}
			else if ((LA(1)=='\r') && (true)) {
				match('\r');
			}
			else if ((LA(1)=='\n')) {
				match('\n');
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			if ( inputState.guessing==0 ) {
				newline();
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_COMMENT;
		int _saveIndex;
		
		match("//");
		{
		_loop500:
		do {
			if ((_tokenSet_5.member(LA(1)))) {
				{
				match(_tokenSet_5);
				}
			}
			else {
				break _loop500;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case '\n':
		{
			match('\n');
			break;
		}
		case '\r':
		{
			match('\r');
			{
			if ((LA(1)=='\n')) {
				match('\n');
			}
			else {
			}
			
			}
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			_ttype = Token.SKIP; newline();
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ML_COMMENT;
		int _saveIndex;
		
		match("/*");
		{
		_loop506:
		do {
			switch ( LA(1)) {
			case '\n':
			{
				match('\n');
				if ( inputState.guessing==0 ) {
					newline();
				}
				break;
			}
			case '\u0003':  case '\u0004':  case '\u0005':  case '\u0006':
			case '\u0007':  case '\u0008':  case '\t':  case '\u000b':
			case '\u000c':  case '\u000e':  case '\u000f':  case '\u0010':
			case '\u0011':  case '\u0012':  case '\u0013':  case '\u0014':
			case '\u0015':  case '\u0016':  case '\u0017':  case '\u0018':
			case '\u0019':  case '\u001a':  case '\u001b':  case '\u001c':
			case '\u001d':  case '\u001e':  case '\u001f':  case ' ':
			case '!':  case '"':  case '#':  case '$':
			case '%':  case '&':  case '\'':  case '(':
			case ')':  case '+':  case ',':  case '-':
			case '.':  case '/':  case '0':  case '1':
			case '2':  case '3':  case '4':  case '5':
			case '6':  case '7':  case '8':  case '9':
			case ':':  case ';':  case '<':  case '=':
			case '>':  case '?':  case '@':  case 'A':
			case 'B':  case 'C':  case 'D':  case 'E':
			case 'F':  case 'G':  case 'H':  case 'I':
			case 'J':  case 'K':  case 'L':  case 'M':
			case 'N':  case 'O':  case 'P':  case 'Q':
			case 'R':  case 'S':  case 'T':  case 'U':
			case 'V':  case 'W':  case 'X':  case 'Y':
			case 'Z':  case '[':  case '\\':  case ']':
			case '^':  case '_':  case '`':  case 'a':
			case 'b':  case 'c':  case 'd':  case 'e':
			case 'f':  case 'g':  case 'h':  case 'i':
			case 'j':  case 'k':  case 'l':  case 'm':
			case 'n':  case 'o':  case 'p':  case 'q':
			case 'r':  case 's':  case 't':  case 'u':
			case 'v':  case 'w':  case 'x':  case 'y':
			case 'z':  case '{':  case '|':  case '}':
			case '~':  case '\u007f':
			{
				{
				match(_tokenSet_6);
				}
				break;
			}
			default:
				if (((LA(1)=='*') && ((LA(2) >= '\u0003' && LA(2) <= '\u007f')))&&( LA(2)!='/' )) {
					match('*');
				}
				else if ((LA(1)=='\r') && (LA(2)=='\n')) {
					match("\r\n");
					if ( inputState.guessing==0 ) {
						newline();
					}
				}
				else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\u007f'))) {
					match('\r');
					if ( inputState.guessing==0 ) {
						newline();
					}
				}
			else {
				break _loop506;
			}
			}
		} while (true);
		}
		match("*/");
		if ( inputState.guessing==0 ) {
			_ttype = Token.SKIP;
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 288063250384289792L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { -17179869192L, -268435457L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { -549755813896L, -268435457L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 288019269919178752L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 287948901175001088L, 541165879422L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { -9224L, -1L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { -4398046520328L, -1L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	
	}
