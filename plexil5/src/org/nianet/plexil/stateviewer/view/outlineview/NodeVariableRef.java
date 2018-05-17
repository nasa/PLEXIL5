package org.nianet.plexil.stateviewer.view.outlineview;

import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTree;

public class NodeVariableRef {

	ProxyTree node;
	String varName;
	String initialValue;
	String currentValue;
	
	


	public NodeVariableRef(ProxyTree node, String varName, String initialValue,
			String currentValue) {
		super();
		this.node = node;
		this.varName = varName;
		this.initialValue = initialValue;
		this.currentValue = currentValue;
	}




	public ProxyTree getNode() {
		return node;
	}




	public String getVarName() {
		return varName;
	}




	public String getInitialValue() {
		return initialValue;
	}




	public String getCurrentValue() {
		return currentValue;
	}




	@Override
	public String toString() {
		return varName;
	}

	
	
}
