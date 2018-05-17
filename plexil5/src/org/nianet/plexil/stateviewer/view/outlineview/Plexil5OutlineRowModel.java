package org.nianet.plexil.stateviewer.view.outlineview;

import java.util.Hashtable;
import java.util.Set;

import javax.swing.JLabel;

import org.netbeans.swing.outline.RowModel;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTree;

public class Plexil5OutlineRowModel implements RowModel {

	@Override
	public Class getColumnClass(int column) {		
		if (column==2){
			return NodeVariableRef.class;
		}
		else{
			return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {		
		case 0:
			return "State";
		case 1:
			return "Outcome";
		case 2:
			return "Current value";
		case 3:
		    return "Initial value";
		default:
			return "";
		}
	}

	@Override
	public Object getValueFor(Object node, int arg1) {
		if (node instanceof ProxyTree){
			if (arg1==0){
				return ((ProxyTree)node).getRealNode().getAttributes().get("status: ").trim().toLowerCase();
			}
			else if(arg1==1){
				return ((ProxyTree)node).getRealNode().getAttributes().get("outcome: ");
			}
			else{
				return "";
			}

		}
		else if (node instanceof NodeVariableRef && arg1==2){
			return ((NodeVariableRef)node).getCurrentValue();
		} 
		else if (node instanceof NodeVariableRef && arg1==3){
            return ((NodeVariableRef)node).getInitialValue();
        }
		else{			
			return new String();	
						
		}

	}

	@Override
	public boolean isCellEditable(Object arg0, int arg1) {		
		return false;
	}

	@Override
	public void setValueFor(Object arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
