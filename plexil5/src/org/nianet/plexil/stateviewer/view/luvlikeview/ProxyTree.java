package org.nianet.plexil.stateviewer.view.luvlikeview;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.nianet.plexil.stateviewer.model.ProcessNode;


public class ProxyTree {

	Hashtable<String, ProcessNode> realNodesMap;
	
	private List<ProxyTree> childs;
	
	private String realNodeId;

	public ProxyTree(String realNodeId, ProcessNode realNode, Hashtable<String, ProcessNode> realNodesMap) {
		super();
		
		this.realNodeId = realNodeId;
		childs=new LinkedList<ProxyTree>();
		this.realNodesMap=realNodesMap;
		realNodesMap.put(realNodeId, realNode);
	}
	
	public void addChild(ProxyTree node){
		childs.add(node);
	}
	
	public int childCount(){
		return childs.size();
	}

	public ProcessNode getRealNode(){
		return realNodesMap.get(realNodeId);
	}

	public List<ProxyTree> getChilds() {
		return childs;
	}
	
	public String toString(){
		return this.getRealNode().getName();
	}

	public Hashtable<String, ProcessNode> getRealNodesMap() {
		return realNodesMap;
	}
	
	
	
}
