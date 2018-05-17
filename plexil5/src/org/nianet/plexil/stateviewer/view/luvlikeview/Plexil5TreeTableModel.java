package org.nianet.plexil.stateviewer.view.luvlikeview;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.table.TableModel;

import org.nianet.plexil.stateviewer.model.ProcessNode;

import tools.treetable.AbstractTreeTableModel;
import tools.treetable.TreeTableModel;


public class Plexil5TreeTableModel extends AbstractTreeTableModel
		implements TreeTableModel {

	
	
	public Plexil5TreeTableModel(ProxyTree root) {
		super(root);
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Name";			
		case 1:
			return "State";
		case 2:
			return "Outcome";
		case 3:
			return "Variables";
		default:
			return "";
		}
	}

	@Override
	public Object getValueAt(Object node, int arg1) {
		if (arg1==0){
			return ((ProxyTree)node).getRealNode().getName();
		}
		else if (arg1==1){
			return ((ProxyTree)node).getRealNode().getAttributes().get("status: ").trim().toLowerCase();
		}
		else if(arg1==2){
			return ((ProxyTree)node).getRealNode().getAttributes().get("outcome: ");
		}
		else{
			
			Set<ProcessNode> memoryNodes=((ProxyTree)node).getRealNode().getMemoryNodes();
			StringBuffer varInfo=new StringBuffer();
			for (ProcessNode pn:memoryNodes){
				Hashtable<String, String> mvars=pn.getAttributes();
				varInfo.append(pn.getName());
				varInfo.append(":");
				varInfo.append(mvars.get("actVal: "));
				varInfo.append("("+mvars.get("initVal: ")+").");
			}
						
			
			return varInfo.toString();
		}
	}

		
	
	
	@Override
	public Object getChild(Object parent, int index) {
		return ((ProxyTree)parent).getChilds().get(index);
		
	}

	@Override
	public int getChildCount(Object parent) {
		return ((ProxyTree)parent).getChilds().size();
	}

	@Override
	public boolean isLeaf(Object node) {
		return super.isLeaf(node); 
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		int ioc=super.getIndexOfChild(parent, child);
		return ioc;
	}

	@Override
	public Class getColumnClass(int column) {
		if (column==0) return TreeTableModel.class;
		else if (column==3){
			return JLabel.class;
		}
		else{
			return String.class;
		}
	}

	
	
	
}
