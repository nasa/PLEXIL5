package org.nianet.plexil.stateviewer.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.nianet.plexil.stateviewer.model.ProcessNode;


/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class PlexilGraphicalTreeBuilder extends JFrame {

	private static Map<String,Point> nodeCoordinates=new Hashtable<String, Point>();
	
	private static final int defaultNodesLenght=300;
	
	public static void resetCoordinatesMap(){
		nodeCoordinates=new Hashtable<String, Point>();
		PlexilTaskGraphicalNode.resetNodesViewInfo();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JDesktopPane _desktopPane = null;

	private FrameComponentListener _fcl = null;

	private DefaultGraphModel _graph = null;

	private JPanel _canvas = null;

	private ComponentListener _cl = null;
		
	private ProcessNode treeRoot=null;	
	
	private static ProcessNode rootx=null;

	public JComponent buildGraphicalTaskTree(ProcessNode treeRoot) {
		
		this.treeRoot=treeRoot;
		_fcl = new FrameComponentListener();
		_cl = new CompListener();
		_graph = new DefaultGraphModel();
		JPanel mainPanel = new JPanel(new BorderLayout());
		_desktopPane = new JDesktopPane();
		_desktopPane.addComponentListener(_cl);
		_canvas = new JPanel(new BorderLayout());
		JGraph graphComp = new JGraph(_graph);
		_canvas.add(graphComp, BorderLayout.CENTER);
		_desktopPane.add(_canvas, JLayeredPane.FRAME_CONTENT_LAYER);
		mainPanel.add(_desktopPane, BorderLayout.CENTER);
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		mainPanel.add(toolBar, BorderLayout.NORTH);

		JScrollPane sp = new JScrollPane();
		mainPanel.setPreferredSize(new Dimension(10000, 10000));
		sp.getViewport().add(mainPanel);		
		
		//define node's initial locations

		if (nodeCoordinates.size()==0){
			nodeCoordinates.put(treeRoot.getName(), new Point(10,100));
			setNodeInitialLocations(this.treeRoot, 10, 100, nodeCoordinates);
		}
		PlexilTaskGraphicalNode groot=addRoot(this.treeRoot,nodeCoordinates.get(this.treeRoot.getName()).x,nodeCoordinates.get(this.treeRoot.getName()).y);
					
		createTree(treeRoot, groot);

		return sp;
		
	}
	
	private void setNodeInitialLocations(ProcessNode root,int x, int y,Map<String,Point> coordinatesMap){
		double childAngle=0;
		Set<ProcessNode> childs=root.getChildTasks();
		
		if (childs.size()>0){
			double angleStep=180/childs.size();
			
			for (ProcessNode t:childs){
				long childX=x+Math.abs(Math.round((defaultNodesLenght*Math.cos(childAngle*(180/Math.PI)))));
				long childY=y+Math.abs(Math.round((defaultNodesLenght*Math.sin(childAngle*(180/Math.PI)))));
				
				coordinatesMap.put(t.getName(), new Point((int)childX,(int)childY));

				setNodeInitialLocations(t, (int)childX, (int)childY,coordinatesMap);
				
				childAngle+=angleStep;
			}			
		}
		
	}
	
	private void createTree(ProcessNode root,PlexilTaskGraphicalNode groot){
		
		for (ProcessNode t:root.getChildTasks()){
			if (!root.isHiddenChilds()){
				PlexilTaskGraphicalNode gnode=addChild(groot, t, nodeCoordinates.get(t.getName()).x,nodeCoordinates.get(t.getName()).y );
				createTree(t, gnode);				
			}
		}
	
	}


	private PlexilTaskGraphicalNode addRoot(ProcessNode tsk,int x,int y) {
		PlexilTaskGraphicalNode internalFrame = new PlexilTaskGraphicalNode(tsk,treeRoot);
		// Add to graph model
		DefaultGraphCell insertCell = new DefaultGraphCell(internalFrame);
		internalFrame.setGraphCell(insertCell);
		Object insertCells[] = new Object[] { insertCell };
		_graph.insert(insertCells, null, null, null, null);
		internalFrame.addComponentListener(_fcl);
		_desktopPane.add(internalFrame);
		internalFrame.setLocation(x, y);
		try {
			internalFrame.setSelected(true);
		} catch (PropertyVetoException pve) {
		}
		return internalFrame;

	}

	@SuppressWarnings("unchecked")
	private PlexilTaskGraphicalNode addChild(PlexilTaskGraphicalNode root,
			ProcessNode child, int x, int y) {
		PlexilTaskGraphicalNode internalFrame = new PlexilTaskGraphicalNode(child,treeRoot);

		DefaultGraphCell insertCell = new DefaultGraphCell(internalFrame);
		internalFrame.setGraphCell(insertCell);
		Object insertCells[] = new Object[] { insertCell };
		_graph.insert(insertCells, null, null, null, null);
		DefaultGraphCell parentCell = root.getGraphCell();
		DefaultPort parentPort = new DefaultPort();
		parentCell.add(parentPort);
		DefaultPort childPort = new DefaultPort();
		insertCell.add(childPort);
		DefaultEdge edge = new DefaultEdge();
		HashMap map = new HashMap();
		Map atts = new Hashtable();
		GraphConstants.setLineEnd(atts, GraphConstants.ARROW_CLASSIC);
		GraphConstants.setEndFill(atts, true);
		map.put(edge, atts);
		ConnectionSet cs = new ConnectionSet(edge, parentPort, childPort);
		Object insertEdges[] = new Object[] { edge };
		_graph.insert(insertEdges, map, cs, null, null);
		internalFrame.addComponentListener(_fcl);

		// location
		internalFrame.setLocation(x, y);

		_desktopPane.add(internalFrame);
		try {
			internalFrame.setSelected(true);
		} catch (PropertyVetoException pve) {
			pve.printStackTrace();
		}
		
		return internalFrame;
	}


	private class CompListener extends ComponentAdapter {
		public void componentResized(ComponentEvent ce) {
			_canvas.setSize(_desktopPane.getSize());
			_canvas.updateUI();
		}
	}

	private class FrameComponentListener extends ComponentAdapter {
		@SuppressWarnings("unchecked")
		public void componentResized(ComponentEvent ce) {
			HashMap map = new HashMap();
			Map atts = new Hashtable();
			PlexilTaskGraphicalNode frame = (PlexilTaskGraphicalNode) ce
					.getComponent();
			GraphConstants.setBounds(atts, frame.getBounds());
			map.put(frame.getGraphCell(), atts);
			_graph.edit(map, null, null, null);
		}

		@SuppressWarnings("unchecked")
		public void componentMoved(ComponentEvent ce) {			
			HashMap map = new HashMap();
			Map atts = new Hashtable();
			PlexilTaskGraphicalNode frame = (PlexilTaskGraphicalNode) ce
					.getComponent();
			
			//update node location
			nodeCoordinates.put(frame.getTask().getName(),frame.getLocation());
			
			GraphConstants.setBounds(atts, frame.getBounds());
			map.put(frame.getGraphCell(), atts);
			_graph.edit(map, null, null, null);
		}
	}


}