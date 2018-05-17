package org.nianet.plexil.stateviewer.view.outlineview;

import java.awt.Color;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.RenderDataProvider;
import org.nianet.plexil.logging.LoggerBuilder;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.view.Plexil5TableTree;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTree;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeBuilder;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeException;

public class Plexil5OutlineTableTree extends Outline implements Plexil5TableTree{

	

	private static Color lastUsedColor;
	
	Hashtable<String,ProcessNode> realNodesMap;

	static Hashtable<String, Color> stateColors;
	
	static {
		stateColors = new Hashtable<String, Color>();
		stateColors.put("inactive", Color.GRAY);
		stateColors.put("waiting", Color.BLUE);
		stateColors.put("executing", Color.GREEN);
		stateColors.put("finishing", Color.YELLOW);
		stateColors.put("iterationended", Color.ORANGE);
		stateColors.put("failing", new Color(165, 211, 237));
		stateColors.put("finished", Color.RED);
	}
	
	public Plexil5OutlineTableTree(ProxyTree root) {	
		
		super();
		this.setBackground(Color.lightGray);
		
		this.setModel(DefaultOutlineModel.createOutlineModel(
	            new Plexil5TreeModel(root), new Plexil5OutlineRowModel(), true, "Name"));
		
		realNodesMap=root.getRealNodesMap();
		
		this.setDefaultRenderer(String.class, 
				new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(
					JTable table, 
					Object value, 
					boolean isSelected, 
					boolean hasFocus, 
					int row, 
					int column) {



				Component component = super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);

				String label = (String)value;

				if (column==1) {
					setForeground(stateColors.get(label));	
				}
				else if (column==2){
					setForeground(Color.BLACK);
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

				return component;
			}

		}

		);
		
		
		this.setDefaultRenderer(NodeVariableRef.class, 
				new DefaultTableCellRenderer(){

					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						// TODO Auto-generated method stub
						return new JLabel(value.toString());

					}
			
		}
		
		);
		
		this.setRenderDataProvider(new Plexil5DataRenderer());
	}
	
	public void updateTree(ProcessNode realTree) throws ProxyTreeException{
		ProxyTreeBuilder.updateRealNodesMap(realNodesMap, realTree);
		this.repaint();
	}
	
}

class Plexil5DataRenderer implements RenderDataProvider{

	@Override
	public Color getBackground(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getForeground(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Icon getIcon(Object obj) {
		if (obj instanceof ProxyTree){			
			return new ImageIcon(streamToByteArray(Plexil5TreeModel.class.getResourceAsStream("images.jpg")));
		}
		else if (obj instanceof NodeVariableRef){			
			return new ImageIcon(streamToByteArray(Plexil5TreeModel.class.getResourceAsStream("var.png")));
		}
		else{
			return new ImageIcon(streamToByteArray(Plexil5TreeModel.class.getResourceAsStream("empty.jpg")));	
		}		
	}

	@Override
	public String getTooltipText(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHtmlDisplayName(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static byte[] streamToByteArray(InputStream is){
		int thisLine;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((thisLine = is.read()) != -1) {
			    bos.write(thisLine);
			}
		} catch (IOException e) {
			LoggerBuilder.getLogger().errorLog(e.getMessage());
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
}