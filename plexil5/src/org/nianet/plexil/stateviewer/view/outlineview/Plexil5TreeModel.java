package org.nianet.plexil.stateviewer.view.outlineview;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTree;

public class Plexil5TreeModel implements TreeModel{

	ProxyTree tree;
	
	
	
	public Plexil5TreeModel(ProxyTree tree) {
		super();
		this.tree = tree;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getChild(Object parent, int index) {
		int varCount=((ProxyTree)parent).getRealNode().getMemoryNodes().size();		
		if (index<varCount){			
			List<ProcessNode> vlist = new ArrayList<ProcessNode>(((ProxyTree)parent).getRealNode().getMemoryNodes());
			ProcessNode varNode=vlist.get(index);

			return new NodeVariableRef((ProxyTree)parent, varNode.getName(), varNode.getAttributes().get("initVal: ") , varNode.getAttributes().get("actVal: "));
			
			//return "VAR:"+varNode.getName();			
		}
		else{
			return ((ProxyTree)parent).getChilds().get(index-varCount);
		}		
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof ProxyTree){
			int varCount=((ProxyTree)parent).getRealNode().getMemoryNodes().size();
			int childCount=((ProxyTree)parent).getChilds().size();
			return varCount+childCount;			
		}
		else{
			return 0;
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
				
		if (child instanceof NodeVariableRef){
			return new ArrayList<ProcessNode>(((ProxyTree)parent).getRealNode().getMemoryNodes()).indexOf(((NodeVariableRef)child).getVarName());
		}
		else{
			int varCount=((ProxyTree)parent).getRealNode().getMemoryNodes().size();
			ProxyTree p=(ProxyTree)parent;
			List<ProxyTree> cl=p.getChilds();
			return cl.indexOf(child)+varCount;
		}		
		
	}

	@Override
	public Object getRoot() {
		return tree;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof NodeVariableRef){
			return true;
		}
		else{
			return (((ProxyTree)node).childCount()==0 && ((ProxyTree)node).getRealNode().getMemoryNodes().size()==0);
		}
		 
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	
	
}
