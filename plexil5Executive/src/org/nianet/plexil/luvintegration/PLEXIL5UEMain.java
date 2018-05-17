package org.nianet.plexil.luvintegration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.MaudeRuntimeException;
import org.nianet.plexil.maude2java.SynchronousMaudeRuntimeEngine;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;
import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.model.ProcessTreeBuilder;
import org.nianet.plexil.stateviewer.view.ModelCheckingMainFrame;


/**
 * 
 * @author Hector Fabio Cadavid Rengifo, hector.cadavid@escuelaing.edu.co
 *         Escuela Colombiana de Ingenieria.
 *
 */
public class PLEXIL5UEMain {

	private static final String _DEFAULT_HOST = "localhost";
	private static final int _STANDARD_UE_MESSAGE_END = 4;
	private static final int _DEFAULT_LUV_PORT=65400;
	
	private static String currentBaseName=null;
	private static String currentMaudePlanPath=null;
	
	static String[] standardOutput=new String[]{
		"<PlanInfo><ViewerBlocks>false</ViewerBlocks></PlanInfo>",
		""
	};
	
	public static void main(String[] args) throws IOException, PlexilPlanLoadException, MaudeRuntimeConfigurationException, MaudeOutputSynchronizationException, ParseException, InterruptedException, MaudeRuntimeException {

		
		
		
		System.out.println("PLEXIL5 Executive.");

		//CHECK PLEXIL PLAN AND PLEXIL SCRIPT PARAMETERS
		if (args.length==0){
			throw new RuntimeException("Missing arguments: PLEXIL5UE.jar <PlexilPlanInPLXFormat> [<Plexil5Script>] [-p<LUV port>]");
		}
		
		PlexilPlan.resetPlexilPlansInfo();

		File plxPlanFile=new File(args[0]);
		
		PLEXILScript plexilScript=null;
		
		if (args.length>1 && !args[1].contains("-p")){
			File plexilScriptFile=new File(args[1]);
			plexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(plexilScriptFile.getAbsolutePath());    											
		}

		BufferedWriter out;

		//CHECK LUV'S PORT PARAMETER
		if (args.length>1 && args[1].contains("-p")){					
			try{
				int port=Integer.parseInt(args[1].substring(2,args[1].length()));
				//out=new PrintWriter(new Socket(_DEFAULT_HOST,port).getOutputStream(),true);
				out=new BufferedWriter(new OutputStreamWriter(new Socket(_DEFAULT_HOST,port).getOutputStream()));
			}
			catch(NumberFormatException e){
				throw new RuntimeException("Wrong argument format (-p<PORT>): LUV port must be an integer.");
			}			
		}
		else if (args.length>2 && args[2].contains("-p")){
			try{
				int port=Integer.parseInt(args[2].substring(2,args[2].length()));
				//out=new PrintWriter(new Socket(_DEFAULT_HOST,port).getOutputStream(),true);
				out=new BufferedWriter(new OutputStreamWriter(new Socket(_DEFAULT_HOST,port).getOutputStream()));
			}
			catch(NumberFormatException e){
				throw new RuntimeException("Wrong argument format (-p<PORT>): LUV port must be an integer.");
			}						
		}
		else{
			//out=new PrintWriter(new Socket(_DEFAULT_HOST,_DEFAULT_LUV_PORT).getOutputStream(),true);
			out=new BufferedWriter(new OutputStreamWriter(new Socket(_DEFAULT_HOST,_DEFAULT_LUV_PORT).getOutputStream()));

		}
		


		String xmlFilename = new File(args[0]).getName();

		String basename = xmlFilename.substring(0,xmlFilename.length()-4);

		String maudeFileName=plxPlanFile.getAbsoluteFile().getParent()+"/"+basename+".maude";

		PlexilPlan pp=PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLX(plxPlanFile);
		
		currentBaseName=basename;
		currentMaudePlanPath=maudeFileName;
		
		
		out.write(standardOutput[0]);
		out.write(_STANDARD_UE_MESSAGE_END);

		
		String xmlCorePlexilPlan=readXMLData(plxPlanFile.getAbsolutePath()+"c");
		
		out.write(xmlCorePlexilPlan);
		out.write(_STANDARD_UE_MESSAGE_END);

		BufferedWriter bw=new BufferedWriter(new FileWriter(maudeFileName));                	                  	 

		bw.write(pp.generateMaudeModule(plexilScript));		                 	  

		bw.close();

		SynchronousMaudeRuntimeEngine eng=new SynchronousMaudeRuntimeEngine("plexilite",maudeFileName,basename);

		String ns;                                        

		List<String> ancestorNodeNames=new LinkedList<String>();

		out.flush();
				
		do{			
			ns=eng.macroStep();												

			if (!ns.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
				ProcessNode root=ProcessTreeBuilder.createStateTree(Plexilite.parseState(ns));							
				sendNodeStates(out,root,ancestorNodeNames);				
			}
			Thread.sleep(50);
			
		} while (!ns.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT));

		
		//SEND EOF
		out.write(-1);
		out.close();

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
							"<PreCondition>"+bLiteralToBit(root.getAttributes().get("pre: "))+"</PreCondition><PostCondition>"+bLiteralToBit(root.getAttributes().get("post: "))+
							"</PostCondition><RepeatCondition>"+bLiteralToBit(root.getAttributes().get("repeatc: "))+
							"</RepeatCondition><AncestorInvariantCondition>UNKNOWN</AncestorInvariantCondition>"+
							"<VariableValues>"+getVariableValues(root)+"</VariableValues>" +
							"<AncestorEndCondition>UNKNOWN</AncestorEndCondition>"+
							"<ParentExecutingCondition>UNKNOWN</ParentExecutingCondition>" +
							"<AllChildrenWaitingOrFinishedCondition>UNKNOWN</AllChildrenWaitingOrFinishedCondition>" +
							"<AbortCompleteCondition>UNKNOWN</AbortCompleteCondition>" +
							"<ParentWaitingCondition>UNKNOWN</ParentWaitingCondition>" +
							"<ParentFinishedCondition>UNKNOWN</ParentFinishedCondition>" +
							"<CommandHandleReceivedCondition>UNKNWON</CommandHandleReceivedCondition>" +							
							"</Conditions><NodePath>"+nodePathInfo+
							"<NodeId>"+root.getName()+"</NodeId>"+
							"</NodePath></NodeStateUpdate>";				
		
		//System.out.println(updateInfo);
		
		w.write(updateInfo+"\n");
		w.write(4);
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
}
