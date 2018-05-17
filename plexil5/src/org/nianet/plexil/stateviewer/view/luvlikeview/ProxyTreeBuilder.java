package org.nianet.plexil.stateviewer.view.luvlikeview;

import java.util.Hashtable;
import java.util.Set;

import org.nianet.plexil.stateviewer.model.ProcessNode;

public class ProxyTreeBuilder {

	public static ProxyTree buildProxyTree(Hashtable<String, ProcessNode> realNodesMap, ProcessNode root){
		ProxyTree pn=new ProxyTree(root.getName(), root, realNodesMap);
		fillProxyTree(pn, realNodesMap);
		return pn;
	}
	
	private static void fillProxyTree(ProxyTree root,Hashtable<String, ProcessNode> realNodesMap){
			Set<ProcessNode> rchilds=root.getRealNode().getChildTasks();			
			
			for (ProcessNode pn:rchilds){				
				ProxyTree ptn=new ProxyTree(pn.getName(), pn, realNodesMap);
				root.addChild(ptn);
				fillProxyTree(ptn, realNodesMap);
			}

	}
	
	public static void updateRealNodesMap(Hashtable<String, ProcessNode> realNodesMap, ProcessNode root) throws ProxyTreeException{
		
		if (realNodesMap.containsKey(root.getName())){
			realNodesMap.put(root.getName(), root);
			Set<ProcessNode> childs=root.getChildTasks();
			for (ProcessNode pn:childs){
				updateRealNodesMap(realNodesMap, pn);
			}
		}
		else{
			throw new ProxyTreeException("The node "+root.getName()+" couldn't be updated. It doesnt exists.");
		}
	}
	
}
