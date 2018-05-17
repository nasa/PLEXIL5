package org.nianet.plexil.luvintegration;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.SynchronousMaudeRuntimeEngine;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;
import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.ext.PlexilScriptException;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.model.ProcessTreeBuilder;

public class Plexil5RunUE {

	private static final int _STANDARD_UE_MESSAGE_END = 4;
	private static final int _DEFAULT_LUV_SERVER_PORT = 9787;

	static String[] standardlines=new String[]{
		"<PlanInfo><ViewerBlocks>false</ViewerBlocks></PlanInfo>",
		"<PlexilPlan><Node NodeType=\"NodeList\" FileName=\"/home/hcadavid/PLEXIL5Preview/examples/Exchange.ple\" LineNo=\"2\" ColNo=\"1\"><NodeId>Exchange</NodeId><Permissions></Permissions><Priority>100000</Priority><VariableDeclarations><DeclareVariable><Name>x</Name><Type>Integer</Type><IntegerValue>0</IntegerValue></DeclareVariable><DeclareVariable><Name>y</Name><Type>Integer</Type><IntegerValue>1</IntegerValue></DeclareVariable></VariableDeclarations><NodeBody><NodeList><Node NodeType=\"Assignment\" FileName=\"/home/hcadavid/PLEXIL5Preview/examples/Exchange.ple\" LineNo=\"10\" ColNo=\"17\"><NodeId>X</NodeId><Permissions></Permissions><Priority>100000</Priority><NodeBody><Assignment><IntegerVariable>x</IntegerVariable><RHS><IntegerVariable>y</IntegerVariable></RHS></Assignment></NodeBody></Node><Node NodeType=\"Assignment\" FileName=\"/home/hcadavid/PLEXIL5Preview/examples/Exchange.ple\" LineNo=\"14\" ColNo=\"17\"><NodeId>Y</NodeId><Permissions></Permissions><Priority>100000</Priority><NodeBody><Assignment><IntegerVariable>y</IntegerVariable><RHS><IntegerVariable>x</IntegerVariable></RHS></Assignment></NodeBody></Node></NodeList></NodeBody></Node></PlexilPlan>"};

	
	
	public static void main(String[] args) throws UnknownHostException, IOException, MaudeRuntimeConfigurationException, ParseException, PlexilPlanLoadException, InterruptedException, MaudeOutputSynchronizationException, PlexilScriptException {
		Socket kkSocket = new Socket("localhost", _DEFAULT_LUV_SERVER_PORT);
		PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
		
		if (args[0]==null){
			throw new RuntimeException("PLEXIL5 UE - Missing command arguments: <planFile> <scriptFile>");
		}
		
		File plxPlanFile=new File(args[0]);
		File xmlScriptFile=new File(args[1]);
		
		if (!plxPlanFile.exists()){
			throw new RuntimeException("PLEXIL5 UE - Invalid command argument:"+args[0]+" doesn't exists.");
		}
		if (!xmlScriptFile.exists()){
			throw new RuntimeException("PLEXIL5 UE - Invalid command argument:"+args[1]+" doesn't exists.");
		}
		
		String filename = plxPlanFile.getName();
        String basename = filename.substring(0,filename.length()-4);
		
        String xmlContent=readXMLData(plxPlanFile.getAbsolutePath());

        PlexilPlan pp=PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLX(new File(xmlContent));                	                  	                  	                  	                  	                  	  

        String maudeFileName=plxPlanFile.getAbsoluteFile().getParent()+"/"+basename+".maude";

        BufferedWriter bw=new BufferedWriter(new FileWriter(maudeFileName));                	                  	 

        
        //TODO create and send context given by command line
        PLEXILScript plexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(xmlScriptFile.getAbsolutePath());
        
        bw.write(pp.generateMaudeModule(plexilScript));

        bw.close();


		//PrintWriter out=new PrintWriter(System.out);
				
		out.write(standardlines[0]);
		out.write(_STANDARD_UE_MESSAGE_END);
		
		
		out.write(xmlContent);
		out.write(_STANDARD_UE_MESSAGE_END);
				
		SynchronousMaudeRuntimeEngine eng=new SynchronousMaudeRuntimeEngine("plexilite",maudeFileName,basename);
		
		List<String> ancestorNodeNames=new LinkedList<String>();
		
		String ns=null;
		do{
			//ns=eng.microStep();
			
			System.out.println(ns);
			
			if (!ns.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
				ProcessNode root=ProcessTreeBuilder.createStateTree(Plexilite.parseState(ns));							
				sendNodeStates(out,root,ancestorNodeNames);
			}
			
			Thread.sleep(500);
						
		} while (!ns.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT));

		
		out.close();
		
		System.exit(0);
	}
	
	
	private static String readXMLData(String path) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(new File(path)));
		
		String text="";
		
		String line=null;
		
		while ((line=br.readLine())!=null){
			text+=line;
		}
		br.close();
		return text;
	}
	                                  
	
	/**
	 * 
	 * @param w
	 * @param plxFile
	 * @throws MaudeRuntimeConfigurationException
	 * @throws ParseException
	 * @throws IOException 
	 */
	private static void sendNodeStates(Writer w, ProcessNode root, List<String> ancestorNodesNames) throws MaudeRuntimeConfigurationException, ParseException, IOException{
		
		//TODO read PLX file, and generate maude file
		
		String nodePathInfo="";
		
		for (String parent:ancestorNodesNames){
			nodePathInfo+="<NodeId>"+parent+"</NodeId>";
		}
		
		String updateInfo=	"<NodeStateUpdate><NodeState>"+root.getAttributes().get("status: ").toUpperCase()+
							"</NodeState><NodeOutcome>"+root.getAttributes().get("outcome: ").toUpperCase()+
							"</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>"+bLiteralToBit(root.getAttributes().get("skip: "))+
							"</SkipCondition><StartCondition>"+bLiteralToBit(root.getAttributes().get("startc: "))+
							"</StartCondition><EndCondition>"+bLiteralToBit(root.getAttributes().get("endc: "))+"</EndCondition>"+
							"<InvariantCondition>"+bLiteralToBit(root.getAttributes().get("inv: "))+"</InvariantCondition>"+
							"<PreCondition>"+bLiteralToBit(root.getAttributes().get("pre: "))+"</PreCondition><PostCondition>"+bLiteralToBit(root.getAttributes().get("post: "))+"+" +
							"</PostCondition><RepeatCondition>"+bLiteralToBit(root.getAttributes().get("repeatc: "))+
							"</RepeatCondition><AncestorInvariantCondition>UNKNOWN</AncestorInvariantCondition>"+
							"<VariableValues>"+getVariableValues(root)+"</VariableValues>" +
							"<AncestorEndCondition>UNKNOWN</AncestorEndCondition>"+
							"<ParentExecutingCondition>UNKNOWN</ParentExecutingCondition>" +
							"<AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition>" +
							"<AbortCompleteCondition>UNKNOWN</AbortCompleteCondition>" +
							"<ParentWaitingCondition>UNKNWON</ParentWaitingCondition>" +
							"<ParentFinishedCondition>UNKNOWN</ParentFinishedCondition>" +
							"<CommandHandleReceivedCondition>UNKNWON</CommandHandleReceivedCondition>" +							
							"</Conditions><NodePath>"+nodePathInfo+
							"<NodeId>"+root.getName()+"</NodeId>"+
							"</NodePath></NodeStateUpdate>";				
		
		w.write(updateInfo+"\n");
		w.write(_STANDARD_UE_MESSAGE_END);
		//TODO only generate states for changed childs
		Set<ProcessNode> childs=root.getChildTasks();
		
		List<String> ancestorNodeNamesClone=new LinkedList<String>(ancestorNodesNames);
		
		ancestorNodeNamesClone.add(root.getName());
		
		for (ProcessNode pn:childs){
			sendNodeStates(w, pn,ancestorNodeNamesClone);
		}
				
	}
	
	private static String bLiteralToBit(String val){
	
		if (val.equals("true")){
			return "1";
		}
		else if (val.equals("false")){
			return "0";
		}
		else{
			return "UNKNOWN";
		}
	}
	
	private static String getVariableValues(ProcessNode root){
		String out="";
		Set<ProcessNode> memoryNodes=root.getMemoryNodes();
		for (ProcessNode pn:memoryNodes){
			Hashtable<String, String> mvars=pn.getAttributes();
			out+=pn.getName()+": "+ mvars.get("actVal: ")+" ["+mvars.get("initVal: ")+"] ,\n";
		}
		
		if (out.equals("")) return "N/A";
		
		return out;
	}

	
	/**
	 *  Set<ProcessNode> memoryNodes=task.getMemoryNodes();
      Font oldfont = g.getFont();
      g.setFont(new Font(oldfont.getFontName(),Font.BOLD,oldfont.getSize()));
      for (ProcessNode pn:memoryNodes){
        Hashtable<String, String> mvars=pn.getAttributes();
        g.drawString(pn.getName()+": "+ mvars.get("actVal: ")+
            " ["+mvars.get("initVal: ")+"]", 10, ycoord);
        ycoord+=20;
      }     
	 */
//	static String[] testlines=new String[]{
//	"<PlanInfo><ViewerBlocks>false</ViewerBlocks></PlanInfo>",
//	"<PlexilPlan><Node NodeType=\"NodeList\" FileName=\"/home/hcadavid/PLEXIL5Preview/examples/Exchange.ple\" LineNo=\"2\" ColNo=\"1\"><NodeId>Exchange</NodeId><Permissions></Permissions><Priority>100000</Priority><VariableDeclarations><DeclareVariable><Name>x</Name><Type>Integer</Type><IntegerValue>0</IntegerValue></DeclareVariable><DeclareVariable><Name>y</Name><Type>Integer</Type><IntegerValue>1</IntegerValue></DeclareVariable></VariableDeclarations><NodeBody><NodeList><Node NodeType=\"Assignment\" FileName=\"/home/hcadavid/PLEXIL5Preview/examples/Exchange.ple\" LineNo=\"10\" ColNo=\"17\"><NodeId>X</NodeId><Permissions></Permissions><Priority>100000</Priority><NodeBody><Assignment><IntegerVariable>x</IntegerVariable><RHS><IntegerVariable>y</IntegerVariable></RHS></Assignment></NodeBody></Node><Node NodeType=\"Assignment\" FileName=\"/home/hcadavid/PLEXIL5Preview/examples/Exchange.ple\" LineNo=\"14\" ColNo=\"17\"><NodeId>Y</NodeId><Permissions></Permissions><Priority>100000</Priority><NodeBody><Assignment><IntegerVariable>y</IntegerVariable><RHS><IntegerVariable>x</IntegerVariable></RHS></Assignment></NodeBody></Node></NodeList></NodeBody></Node></PlexilPlan>",
//	"<NodeStateUpdate><NodeState>WAITING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>1</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>0</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>EXECUTING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>0</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>1</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>0</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>WAITING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>UNKNOWN</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>X</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>WAITING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>UNKNOWN</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>Y</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>EXECUTING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>UNKNOWN</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>UNKNOWN</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>X</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>EXECUTING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>UNKNOWN</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>UNKNOWN</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>Y</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>ITERATION_ENDED</NodeState><NodeOutcome>SUCCESS</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>UNKNOWN</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>X</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>ITERATION_ENDED</NodeState><NodeOutcome>SUCCESS</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>UNKNOWN</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>Y</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>FINISHED</NodeState><NodeOutcome>SUCCESS</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>UNKNOWN</AncestorInvariantCondition><AncestorEndCondition>UNKNOWN</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>X</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>FINISHED</NodeState><NodeOutcome>SUCCESS</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>UNKNOWN</AncestorInvariantCondition><AncestorEndCondition>UNKNOWN</AncestorEndCondition><ParentExecutingCondition>UNKNOWN</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>UNKNOWN</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId><NodeId>Y</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>FINISHING</NodeState><NodeOutcome>UNKNOWN</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>1</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>1</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>0</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>ITERATION_ENDED</NodeState><NodeOutcome>SUCCESS</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>1</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>0</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId></NodePath></NodeStateUpdate>",
//	"<NodeStateUpdate><NodeState>WAITING</NodeState><NodeOutcome>SUCCESS</NodeOutcome><NodeFailureType>UNKNOWN</NodeFailureType><Conditions><SkipCondition>0</SkipCondition><StartCondition>1</StartCondition><EndCondition>UNKNOWN</EndCondition><InvariantCondition>1</InvariantCondition><PreCondition>1</PreCondition><PostCondition>1</PostCondition><RepeatCondition>0</RepeatCondition><AncestorInvariantCondition>1</AncestorInvariantCondition><AncestorEndCondition>0</AncestorEndCondition><ParentExecutingCondition>1</ParentExecutingCondition><AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition><AbortCompleteCondition>UNKNOWN</AbortCompleteCondition><ParentWaitingCondition>0</ParentWaitingCondition><ParentFinishedCondition>0</ParentFinishedCondition><CommandHandleReceivedCondition>1</CommandHandleReceivedCondition></Conditions><NodePath><NodeId>Exchange</NodeId></NodePath></NodeStateUpdate>"};
	
}