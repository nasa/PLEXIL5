package org.nianet.plexil.stateviewer.view;

import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeException;

public interface Plexil5TableTree {

	public void updateTree(ProcessNode realTree) throws ProxyTreeException;	
	
}
