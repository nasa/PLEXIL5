package org.nianet.plexil.maude2java.modelchecking.ltlassistant;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.tree.Tree;
import org.nianet.plexil.logging.LoggerBuilder;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.DeclareVariable;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.Node;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.TypeValues;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.LTLTreeGenerator;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.MalformedLTLExpressionException;

public class LTLACTextField extends ACTextField{

	//STATUS :  (~( ' ' | '\t' | '\r' | '\n' | '(' | ')' ))*('.')('inactive'|'waiting'|'executing'|'finishing'|'iterationEnded'|'failing'|'finished');
	//this pattern must correspond to the STATUS pattern on LTLExpression.g ANTLR grammar file.
	//TODO unify with LTLTreeGenerator info.
	public static final String[] nodeStates=new String[]{"inactive","waiting","executing","finishing","iterationEnded","failing","finished"};
	public static final String[] nodeConditions=new String[]{"end","inv","pre","start","post","repeat"};
	public static final String[] extNodeStates=new String[]{"allChildrenInactive","allChildrenWaiting","allChildrenExecuting","allChildrenFinishing","allChildrenIterationEnded","allChildrenFailing","allChildrenFinished"};
	public static final String[] nodeOutcomes=new String[]{"noOutcome","skipped","success","parentFails","invFails","preFails","postFails","fail"};
	
	
	private AutocompleteEntry[] autocompleteEntries;
	
	
	public LTLACTextField(){		
		
		Set<String> symbols=UnicodeLogicalSymbolsMap.availableSymbolNames();
		
		List<AutocompleteEntry> optionsList=new LinkedList<AutocompleteEntry>();
		
		Set<String> nodeNames=PlexilPlan.getLastLoadedPlanNodesAbsNamesMap().keySet();
		
		for (String nname:nodeNames){			
			optionsList.add(new AutocompleteEntry(nname+".", nname, "Node"));							
		}
		Collections.sort(optionsList,new Comparator<AutocompleteEntry>(){

			@Override
			public int compare(AutocompleteEntry o1, AutocompleteEntry o2) {
				return o1.getTextToShow().compareTo(o2.getTextToShow());
			}
			
		});

		for (String s:symbols){
			char specialChar=UnicodeLogicalSymbolsMap.getSymbol(s);
			optionsList.add(0,new AutocompleteEntry(""+specialChar, specialChar+"\t\t\t\t\t\t"+UnicodeLogicalSymbolsMap.getSymbolDescription(s), "Special character"));
		}
		
		autocompleteEntries=optionsList.toArray(new AutocompleteEntry[]{});
	}
	
	@Override
	public AutocompleteEntry[] getAutocompleteOptions() {
		
		return autocompleteEntries;
	}

	@Override
	public List<Character> getAllowedChars() {
		LinkedList<Character> lc=new LinkedList<Character>();
		lc.add('(');
		lc.add(')');
		return lc;
	}

	@Override
	protected void updateCapturedString() {
		
		int caretPos=this.getCaretPosition();
		
		this.setText(this.getText().replaceAll("/\\\\", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLAND)));
		this.setText(this.getText().replaceAll("\\[\\]", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLGLOBALLY)));
		this.setText(this.getText().replaceAll("\\<\\>", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLFINALLY)));
		this.setText(this.getText().replaceAll("->", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLIMPLICATION)));
		this.setText(this.getText().replaceAll(" \\\\/ ", " "+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLOR)+" "));
		//this.setText(this.getText().replaceAll("\\^", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLAND)));
		this.setText(this.getText().replaceAll("<=>", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLEQUIVALENT)));
		this.setText(this.getText().replaceAll("\\(\\)", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLNEXT)));
		this.setText(this.getText().replaceAll(" T ", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLTRUE)));
		this.setText(this.getText().replaceAll(" F ", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLFALSE)));		
		this.setText(this.getText().replaceAll("~", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLNOT)));
		this.setText(this.getText().replaceAll(" R ", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLRELEASE)));
		this.setText(this.getText().replaceAll(" U ", ""+UnicodeLogicalSymbolsMap.getSymbol(UnicodeLogicalSymbolsMap._LTLUNTIL)));
		
		/*
		 * Equivalence:<=>
			Eventually:<>
			Always: []
			Implication(LTL): =>
			Or: \/
			And: /\
			Next: () unless as Radu said, it becomes difficult to parse, in that case
			the letter O
			true and false: T, F
			Negation: ~
		    Release and Until: R and U.
		 */
		
		try{
			this.setCaretPosition(caretPos);	
		}
		catch(IllegalArgumentException e){
			//this exception is not handled
		}
		
		
		
		Collection<Character> uchars=UnicodeLogicalSymbolsMap.availableSymbols();
		capturedString=this.getText();
		for (Character c:uchars){
			//avoid >= and <= wrong replacement
			if (c=='='){
				capturedString=capturedString.replaceAll("([^><])=", "$1\\$"+UnicodeLogicalSymbolsMap.getSymbolName(c)+"\\$");
			}
			else{
				capturedString=capturedString.replaceAll(""+c, "\\$"+UnicodeLogicalSymbolsMap.getSymbolName(c)+"\\$");	
			}
		}		
		
	}

	@Override
	public void inputEventAction() {
		
		
	}

	@Override
	public boolean validInput() {
		try {
			Tree root=LTLTreeGenerator.parseAndGetTree(getCapturedString());
			LoggerBuilder.getLogger().debugLog("VALID LTL:"+LTLTreeGenerator.generateMaudeExpression(root));

			return true;
		} catch (MalformedLTLExpressionException e) {
			LoggerBuilder.getLogger().debugLog("INVALID LTL:"+getCapturedString());
					
			return false;
		}
	}

	@Override
	protected AutocompleteEntry[] getOptionsByContext(String partialToken) {
		
		Set<String> nodenames=PlexilPlan.getLastLoadedPlanNodes().keySet();
				
		//if a partial token is contained in a node name

		//TODO
		//if a partial token IS a node name with a "."
		Node n=PlexilPlan.getLastLoadedPlanNodes().get(partialToken.substring(0, partialToken.length()-1));
		
		if (partialToken.endsWith(".") && n!=null){
			List<DeclareVariable> vars=n.getVariables();
			List<AutocompleteEntry> lent=new LinkedList<AutocompleteEntry>();
			
			//node variables
			for (DeclareVariable dv:vars){
				if (dv.getType().compareTo(TypeValues.BOOLEAN)==0){
					lent.add(new AutocompleteEntry(dv.getName(), partialToken+dv.getName()+"(logical)", "Node variable"));	
				}
				else if (dv.getType().compareTo(TypeValues.INTEGER)==0 || dv.getType().compareTo(TypeValues.REAL)==0 ){
					lent.add(new AutocompleteEntry(dv.getName(), partialToken+dv.getName()+"(numerical)", "Node variable"));
				}
				
			}

			//node states
			for (int i=0;i<nodeStates.length;i++){
				lent.add(new AutocompleteEntry("<"+nodeStates[i]+">", partialToken+"<"+nodeStates[i]+">", "Node status"));
			}
			
			//node ext. states
			for (int i=0;i<extNodeStates.length;i++){
				lent.add(new AutocompleteEntry("<"+extNodeStates[i]+">", partialToken+"<"+extNodeStates[i]+">", "Node status"));
			}
			
			//node outcomes
			for (int i=0;i<nodeOutcomes.length;i++){
				lent.add(new AutocompleteEntry("<"+nodeOutcomes[i]+">", partialToken+"<"+nodeOutcomes[i]+">", "Node outcome"));
			}			
			
			
			//node conditions
			for (int i=0;i<nodeConditions.length;i++){
				lent.add(new AutocompleteEntry("<"+nodeConditions[i]+">", partialToken+"<"+nodeConditions[i]+">", "Node condition"));
			}
			
			return lent.toArray(new AutocompleteEntry[]{});
		}
		else{
			return new AutocompleteEntry[]{};
		}
				
	}

	
	
	
	
}
