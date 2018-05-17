package org.nianet.plexil.stateviewer.view.luvlikeview;

import gov.nasa.luv.Constants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.view.Plexil5TableTree;

import tools.treetable.JTreeTable;



public class Plexil5LuvLikePlanTreeComponent extends JTreeTable implements Plexil5TableTree {

	private static Color lastUsedColor;
	
	Hashtable<String,ProcessNode> realNodesMap;

	static Hashtable<String, Color> stateColors;
	
	static {
		stateColors = new Hashtable<String, Color>();
		stateColors.put("inactive", Color.LIGHT_GRAY);
		stateColors.put("waiting", Color.BLUE);
		stateColors.put("executing", Color.GREEN);
		stateColors.put("finishing", Color.YELLOW);
		stateColors.put("iterationended", Color.ORANGE);
		stateColors.put("failing", Color.RED);
		stateColors.put("finished", new Color(165, 211, 237));
	}
	
	public Plexil5LuvLikePlanTreeComponent(ProxyTree root) {	
		super(new Plexil5TreeTableModel(root));
		realNodesMap=root.getRealNodesMap();
		
		this.setShowGrid(true);
		this.setIntercellSpacing(new Dimension(1, 1));
		
		this.getTree().setCellRenderer(new DefaultTreeCellRenderer()
		{
			public Component getTreeCellRendererComponent(
					JTree newTree,
					Object value,
					boolean isSelected,
					boolean expanded,
					boolean leaf,
					int row,
					boolean hasFocus) 
			{
				Component component = super.getTreeCellRendererComponent(
						newTree, value, isSelected, expanded, leaf, row, hasFocus);

				setIcon(Constants.getIcon(Constants.NODELIST_ICO_NAME));


				return component;
			}
		});
				
		setDefaultRenderer(String.class, 
				new DefaultTableCellRenderer(){
			
		            public Component getTableCellRendererComponent(
		                    JTable table, 
		                    Object value, 
		                    boolean isSelected, 
		                    boolean hasFocus, 
		                    int row, 
		                    int column)
		                 {
		                    Component component = super.getTableCellRendererComponent(
		                       table, value, isSelected, hasFocus, row, column);
		                    
		                    String label = (String)value;
		                    
		                    if (column==1) {
		                    	setForeground(stateColors.get(label));	
		                    }
		                    if (value instanceof String && ((String)value).equals("failure")){
		                    	lastUsedColor=this.getForeground();
		                    	setForeground(Color.RED);		                    	
		                    }
		                    else{
		                    	if (lastUsedColor!=null){
		                    		setForeground(lastUsedColor);
		                    	}
		                    }
		                    
		                    //setBackground(isSelected ? table.getSelectionBackground() : getRowColor(row));  
		
		                    return component;
		                 }
			
				}
		
		);

		

	}
	
	public void updateTree(ProcessNode realTree) throws ProxyTreeException{
		ProxyTreeBuilder.updateRealNodesMap(realNodesMap, realTree);
		this.repaint();
	}
	
		
	

}
