package org.nianet.plexil.maude2java.modelchecking;

import java.io.StringReader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nianet.plexil.PlexilState;
import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.model.ProcessTreeBuilder;

public class MaudeModelCheckingCounterExample {

	private static final char _COUNTEREXAMPLE_LIST_SEPARATOR = ((char)2);
	private static final String _PLEXIL_STATE_SEPARATOR = ""+((char)1);
	private LinkedList<ProcessNode> initialSteps;
	private LinkedList<ProcessNode> endLoopStates;
	
	public MaudeModelCheckingCounterExample(String maudeCounterExampleSpec) throws MaudeCounterexampleParsingException{
		super();
		initialSteps=new LinkedList<ProcessNode>();
		endLoopStates=new LinkedList<ProcessNode>();
		
		
		Pattern pattern = Pattern.compile("(,('(macro|micro)|deadlock)\\}\\s\\{)|(,('(macro|micro)|deadlock)\\})", Pattern.CASE_INSENSITIVE);
		String output=maudeCounterExampleSpec;
		output=output.substring(output.indexOf("(")+1,output.lastIndexOf(")"));
		output=output.replaceAll("\n", "");
		
		Matcher matcher=pattern.matcher(output);
		output=matcher.replaceAll(_PLEXIL_STATE_SEPARATOR);		
		
		output=output.replaceAll(_PLEXIL_STATE_SEPARATOR+",",""+_COUNTEREXAMPLE_LIST_SEPARATOR);
		
		StringTokenizer st=new StringTokenizer(output,""+_COUNTEREXAMPLE_LIST_SEPARATOR);
		
		if (st.countTokens()!=2) throw new MaudeCounterexampleParsingException("Error while parsing Maude's counterexample: expected two lists, found:"+st.countTokens());
		
		String maudeCEInitialStatesList=st.nextToken().trim().substring(1);
		String maudeCELoopStatesList=st.nextToken().trim().substring(1);
		
		StringTokenizer listTokenizer=new StringTokenizer(maudeCEInitialStatesList,_PLEXIL_STATE_SEPARATOR);
		
		while (listTokenizer.hasMoreElements()){			
			parseAndAddState(initialSteps, listTokenizer.nextToken());
		}

		listTokenizer=new StringTokenizer(maudeCELoopStatesList,_PLEXIL_STATE_SEPARATOR);

		while (listTokenizer.hasMoreElements()){			
			parseAndAddState(endLoopStates, listTokenizer.nextToken());
		}				
		
	}
	
	private void parseAndAddState(LinkedList<ProcessNode> list,String maudeState) throws MaudeCounterexampleParsingException{
		Plexilite stateParser=new Plexilite(new StringReader(maudeState));
		PlexilState ps;
		try {
			ps = stateParser.state();
		} catch (ParseException e) {
			throw new MaudeCounterexampleParsingException("Error while parsing Maude's counterexample: state parsing exception:"+e.getMessage(),e);
		}
		ProcessNode root=ProcessTreeBuilder.createStateTree(ps);
		list.add(root);		
	}
	

	public boolean infiniteStepsSet(){
		return endLoopStates.size()>1;
	}
	
	public Enumeration<ProcessNode> stepsIterator(){
		
		return new Enumeration<ProcessNode>() {
			int stepIndex=0;
			boolean onEndLoop=false; 

			@Override
			public boolean hasMoreElements() {				
				if (onEndLoop && endLoopStates.size()==1 && stepIndex==0) return false;
				else return true;
			}

			@Override
			public ProcessNode nextElement() {
				if (stepIndex==initialSteps.size() && !onEndLoop){
					onEndLoop=true;
					stepIndex=0;					
				}
				if (onEndLoop){
					ProcessNode nextStep=endLoopStates.get(stepIndex);
					stepIndex=++stepIndex%endLoopStates.size();
					return nextStep;
				}
				else{
					ProcessNode nextStep=initialSteps.get(stepIndex);
					stepIndex++;
					return nextStep;
				}
			}	

			
		};
		
		
	}
	
}
