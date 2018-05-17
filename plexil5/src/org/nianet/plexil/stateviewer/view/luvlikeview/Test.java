package org.nianet.plexil.stateviewer.view.luvlikeview;


import gov.nasa.luv.Constants;

import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.nianet.plexil.PlexilState;
import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.MaudeRuntimeException;
import org.nianet.plexil.maude2java.SynchronousMaudeRuntimeEngine;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;
import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.ext.PlexilScriptException;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.model.ProcessTreeBuilder;

import tools.treetable.JTreeTable;

//import tools.treetable.JTreeTable;

public class Test {

	
	static JTreeTable tt;
	
	public static void main(String[] args) throws PlexilPlanLoadException, IOException, MaudeOutputSynchronizationException, MaudeRuntimeException, ParseException, MaudeRuntimeConfigurationException, ProxyTreeException, PlexilScriptException {
		

		PlexilPlan.resetPlexilPlansInfo();
		PlexilPlan pp=PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE("/home/hector/workspace/PLEXIL-4451/examples/DriveToTarget.ple","/home/hector/temp/DriveToTarget.plx");
		
	    String maudePlanPath="/home/hector/workspace/PLEXIL-4451/examples/DriveToTarget.maude";

		BufferedWriter bw=new BufferedWriter(new FileWriter(maudePlanPath));                	                  	 

		bw.write(pp.generateMaudeModule(null/*script*/));

		bw.close();


		SynchronousMaudeRuntimeEngine eng=new SynchronousMaudeRuntimeEngine("plexilite",maudePlanPath,"DriveToTarget");	
		String ns=eng.nullStep();                                        

		PlexilState state = Plexilite.parseState(ns);
		
		ProcessNode root=ProcessTreeBuilder.createStateTree(state);		
		
		/**
		 * ==============0
		 */
		Hashtable<String, ProcessNode> nodesMap=new Hashtable<String, ProcessNode>();
		
		
		
		ProxyTree proxyRoot=  ProxyTreeBuilder.buildProxyTree(nodesMap, root);
		
		printProxyRoot(proxyRoot, 0);
		//MainFrame mf=new MainFrame(new JTreeTable(new Plexil5TreeTableModel(proxyRoot)));
		
		
		tt=new JTreeTable(new Plexil5TreeTableModel(proxyRoot)); 
		
		
		Plexil5LuvLikePlanTreeComponent comp=new Plexil5LuvLikePlanTreeComponent(proxyRoot);
		
		MainFrame mf=new MainFrame(comp);
				
		mf.setVisible(true);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("=============");
		
		printProxyRoot(proxyRoot, 0);
		

		
		ns=eng.microStep();
		ns=eng.microStep();
		ns=eng.microStep();
		ns=eng.microStep();



		state = Plexilite.parseState(ns);
		
		ProcessNode root2=ProcessTreeBuilder.createStateTree(state);		

		comp.updateTree(root2);
		
		System.out.println("2:");
		printProxyRoot(proxyRoot, 0);
		//printRoot(root2, 0);
		
		mf.refresh();
		
		
	}
	
	public static void printProxyRoot(ProxyTree pr, int tab){
		for (int i=0;i<tab;i++){
			System.out.print("\t");
		}
		System.out.println(pr.getRealNode().getName()+":"+pr.getRealNode().getAttributes().get("status: ").trim().toLowerCase());
		
		List<ProxyTree> childs=pr.getChilds();
		
		for (ProxyTree ptn:childs){
			printProxyRoot(ptn, tab+1);
		}
	}

	
	public static void printRoot(ProcessNode pr, int tab){
		for (int i=0;i<tab;i++){
			System.out.print("\t");
		}
		System.out.println(pr.getName()+":"+pr.getAttributes().get("status: ").trim().toLowerCase());
		
		Set<ProcessNode> childs=pr.getChildTasks();
		
		for (ProcessNode ptn:childs){
			printRoot(ptn, tab+1);
		}
	}

	
	
}


class MainFrame extends JFrame{
	
	
	MainFrame(JTreeTable c){
		this.getContentPane().add(c);
		this.setSize(500,500);
		this.setVisible(true);
		c.setShowGrid(true);
		c.setIntercellSpacing(new Dimension(1, 1));
	}
	
	public void refresh(){
		this.getContentPane().getComponents()[0].repaint();
	}
}