package org.nianet.plexil.maude2java.modelchecking;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.nianet.plexil.PlexilState;
import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.MaudeRuntimeException;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimePropertiesFactory;
import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.model.ProcessTreeBuilder;
import org.nianet.plexil.stateviewer.view.PlexilGraphicalTreeBuilder;

public class Test {

	public static void main(String[] args) throws MaudeRuntimeConfigurationException, MaudeOutputSynchronizationException, IOException, MaudeRuntimeException, ParseException, MaudeCounterexampleParsingException {

				OSShellRuntimePropertiesFactory.updateConfigurationPropertiesMaudePath("/home/fmatnia/nia/hcadavid-workspace/PLEXIL5-JUL-2010/maude/maude.linux");
		//		
				SynchronousMaudeModelCheckingEngine eng=new SynchronousMaudeModelCheckingEngine("plexilite", "/home/fmatnia/nia/hcadavid-workspace/PLEXIL5-JUL-2010/examples/SafeDrive.maude", "SafeDrive");
		//		
				System.out.println(eng.checkProperty("[](status('SafeDrive,finished) -> status('Loop . 'SafeDrive, executing))"));


		//Plexilite parser = new Plexilite(System.in);
		//System.out.println(">");
//		
			//PlexilState state=parser.state();
			//System.out.println(state);
//			ProcessNode root=ProcessTreeBuilder.createStateTree(state);
//			JComponent component=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(root);
//			
//			//System.out.println(component);
//			JFrame jf=new JFrame();
//			jf.getContentPane().add(component);
//			jf.setSize(1000,1000);
//			jf.setVisible(true);
			
//		InputStreamReader iir=new InputStreamReader(System.in);
//		BufferedReader br=new BufferedReader(iir);
//		String input="";
//		String line;
//		while (!(line=br.readLine()).equals("eof")){
//			input=input+line+"\n";				
//		}
//		//System.out.println(input);
//		
//		
//		MaudeModelCheckingCounterExample ce=new MaudeModelCheckingCounterExample(input);
//	
//		System.out.println("INFINITE?:"+ce.infiniteStepsSet());
//		
//		Enumeration<ProcessNode> its=ce.stepsIterator();
//		
//		
//		
//		while (its.hasMoreElements()){
//			System.out.println(its.nextElement());
//		}
//		
//		System.out.println("done!");
		
//			Pattern pattern = Pattern.compile("(,('(macro|micro)|deadlock)\\}\\s\\{)|(,('(macro|micro)|deadlock)\\})", Pattern.CASE_INSENSITIVE);
//
//			//String s="{\nXXXXXXXX,'micro} {\nYYYYY,'macro},\n{ZZZZZZ,'micro}";
//			//Matcher matcher = pattern.matcher("{XXXXXXXX,'micro} {YYYYY,'macro},\n{ZZZZZZ,'micro} {QQQQQQQ,'macro}");
//			
//			//String output="result ModelCheckResult: counterexample({\nxXXXXXXXXXXXx\n,'micro} {\nyYYYYYYYYYYy\n,'micro} {\nzZZZZZZZZZZz\n,'micro} {\nwWWWWWWWWWWw\n,'macro}, {\nzZZZZZZZZZZz\n,'micro} \n{\nrRRRRRRRRRRr\n,deadlock})";
//			String output=input;
//			output=output.substring(output.indexOf("(")+1,output.lastIndexOf(")"));
//			output=output.replaceAll("\n", "");
//			System.out.println(output);
//			
//			Matcher matcher=pattern.matcher(output);
//			output=matcher.replaceAll(""+((char)1));
//			
//			System.out.println(output);
//			
//			output=output.replaceAll(((char)1)+",",""+((char)2));
//			
//			System.out.println(output);
//			
//			StringTokenizer st=new StringTokenizer(output,""+((char)2));
//			
//			System.out.println(st.countTokens());
//			System.out.println(st.nextToken().trim().substring(1));
//			System.out.println(st.nextToken().trim().substring(1));
			
			

			//System.out.println(s.replaceAll("micro", "$"));
			//System.out.println(s.indexOf("'micro}"));
			


	}

}
