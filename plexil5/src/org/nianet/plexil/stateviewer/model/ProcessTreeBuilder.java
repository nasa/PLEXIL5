package org.nianet.plexil.stateviewer.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.nianet.plexil.PlexilProcess;
import org.nianet.plexil.ProcessType;
import org.nianet.plexil.PlexilState;
import org.nianet.plexil.logging.LoggerBuilder;

public class ProcessTreeBuilder {

	
	public static ProcessNode createStateTree(PlexilState state){
				
		Iterator<PlexilProcess> it=state.getConfiguration().iterator();
		
		Hashtable<String, ProcessNode> nodesMap=new Hashtable<String, ProcessNode>();
		
		ProcessNode root=null;
		
		while (it.hasNext()){
			PlexilProcess proc=it.next();
			
			
			
			Hashtable<String, String> atts=new Hashtable<String, String>();
			Enumeration<String> vars=proc.keys();
			while (vars.hasMoreElements()){
				String key=vars.nextElement();
				atts.put(key, proc.getAttribute(key));
			}
			
			if (proc.getType()!=ProcessType.monitor){
				ProcessNode node=null;
				if (proc.getName().contains(".")){
					node=new ProcessNode(proc.getName().substring(0,proc.getName().indexOf(".")),proc.getName(),atts);	
				}
				else{
					node=new ProcessNode(proc.getName(),proc.getName(),atts);
					root=node;
				}			
				node.setProcessType(proc.getType());
				
				nodesMap.put(proc.getName(), node);		
			}
					
		}
		
		Enumeration<ProcessNode> nodes=nodesMap.elements();
		
		while (nodes.hasMoreElements()){
			ProcessNode node=nodes.nextElement();
			

			//if not the root node
			if (node.getAbsName().contains(".")){
				ProcessNode parentNode=nodesMap.get(node.getAbsName().substring(node.getAbsName().indexOf(".")+1));
				if (node.getProcessType().equals(ProcessType.memory)){
					parentNode.addMemoryNode(node);
				}
				else{
					
					if (parentNode==null){
						LoggerBuilder.getLogger().debugLog("Orphan node in view: "+node.getName()+","+node.getProcessType());
						
						
					}
					else{
						parentNode.addChild(node);	
					}
					
				}
			}
		}
		
		return root;
		
		
		/*Hashtable<String,ProcessNode> elementsMap=new Hashtable<String, ProcessNode>();
		
		String treeSegment=null;
		
		ProcessNode lastNode=null;
		Iterator<PlexilProcess> it=state.getConfiguration().iterator();
		
		while (it.hasNext()){
			lastNode=null;
			ProcessNode currentNode=null;

			PlexilProcess proc=it.next();
			
			treeSegment=proc.getName();			
			StringTokenizer st=new StringTokenizer(treeSegment,".");
			
			while (st.hasMoreTokens()){
				String token=st.nextToken();
				token=token.replaceAll("'","").trim();
				
				if (!token.equals("null")){

					//if already exists the node on the tree, retrieve it
					if (elementsMap.containsKey(token)){
						currentNode=elementsMap.get(token);
					}
					else{
						Hashtable<String, String> atts=new Hashtable<String, String>();
						Enumeration<String> vars=proc.keys();
						while (vars.hasMoreElements()){
							String key=vars.nextElement();
							atts.put(key, proc.getAttribute(key));
						}
						
						currentNode=new ProcessNode(token,token,atts);

						currentNode.setProcessType(proc.getType());
						elementsMap.put(token, currentNode);
					}
					
					//add the previous node as a child of the new one
					if (lastNode!=null){

						if (!lastNode.getProcessType().equals(ProcessType.memory)){
							
							currentNode.addChild(lastNode);	
						}
						else{

							currentNode.addMemoryNode(lastNode);
						}
						
					}
					lastNode=currentNode;
				}
			}
			
		}
		
		//return root
		return lastNode;
		//return null;*/
	}
	
}
