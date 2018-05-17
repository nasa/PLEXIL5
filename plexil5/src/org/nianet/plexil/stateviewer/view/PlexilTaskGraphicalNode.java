package org.nianet.plexil.stateviewer.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.jgraph.graph.DefaultGraphCell;
import org.nianet.plexil.Util;
import org.nianet.plexil.stateviewer.model.ProcessNode;

public class PlexilTaskGraphicalNode extends JInternalFrame implements Observer {
	private static final String _SHOW_DETAILS_MESSAGE = "{see more...}";


	private static final String _HIDE_DETAILS_MESSAGE = "^";


	private static final int _NODE_WIDTH = 150;


	private static JFrame statusPanel;

	private static final long serialVersionUID = -8754477937368318640L;
	private DefaultGraphCell _graphCell = null;
	private ProcessNode task;
	private static Hashtable<String, Color> stateColors;
	private ProcessNode tasksTree;
	private TaskRep stateView;
	
	private JButton expandButton;
	private boolean compressedView=true;
	
	private int compressedHeight=0;
	
	
	//track expanded/compressed views changes
	private static Hashtable<String, Boolean> nodesCompressedViewStates=new Hashtable<String, Boolean>();
	
	public static void resetNodesViewInfo(){
		nodesCompressedViewStates=new Hashtable<String, Boolean>();
	}
	
	public boolean isCompressedView() {
		return compressedView;
	}


	static {
		stateColors = new Hashtable<String, Color>();
		stateColors.put("inactive", Color.LIGHT_GRAY);
		stateColors.put("waiting", Color.YELLOW);
		stateColors.put("executing", Color.GREEN);
		stateColors.put("finishing", Color.WHITE);
		stateColors.put("iterationended", Color.ORANGE);
		stateColors.put("failing", Color.RED);
		stateColors.put("finished", new Color(165, 211, 237));
	}



	public PlexilTaskGraphicalNode(ProcessNode tsk,ProcessNode tasksTree) {
		
		
		this.task = tsk;
		this.tasksTree=tasksTree;
		//this.setTitle(task.getName() + "::" + task.getProcessType());
		tsk.addObserver(this);
		setResizable(true);
		setFrameIcon(null);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		JScrollPane sp = new JScrollPane();
		
		stateView=new TaskRep(task);
		
		sp.getViewport().add(stateView);
		mainPanel.add(sp, BorderLayout.CENTER);

		expandButton=new JButton(_SHOW_DETAILS_MESSAGE);
		expandButton.setSize(_NODE_WIDTH,80);
		String status    = task.getAttributes().get("status: ").trim().toLowerCase();
		expandButton.setBackground(stateColors.get(status));
		
		String outcome = task.getAttributes().get("outcome: ");		
		if (outcome != null && outcome.endsWith("Fail")) {			
			expandButton.setBackground(stateColors.get("failing"));
		}
		
		
		expandButton.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if (compressedView){
							setSize(_NODE_WIDTH,compressedHeight+150);
							compressedView=false;
							expandButton.setText(_HIDE_DETAILS_MESSAGE);
							nodesCompressedViewStates.put(task.getName(), false);
						}
						else{
							setSize(_NODE_WIDTH,compressedHeight);
							expandButton.setText(_SHOW_DETAILS_MESSAGE);
							compressedView=true;
							nodesCompressedViewStates.put(task.getName(), true);
						}
					}
					
				}
		);
		
		JLabel title=new JLabel(task.getName() + "::" + task.getProcessType());
		JPanel titlePanel=new JPanel();
		titlePanel.setBackground(stateColors.get(status));
		titlePanel.add(title);
		mainPanel.add(titlePanel,BorderLayout.NORTH);
		mainPanel.add(expandButton,BorderLayout.SOUTH);
		this.setResizable(true);
		
		//mainPanel.add(createTaskToolBar(),BorderLayout.EAST);

		getContentPane().add(mainPanel);

		pack();
						
				
		setVisible(true);
		
		compressedHeight=(int)expandButton.getSize().getHeight();
	
		if (nodesCompressedViewStates.get(task.getName())==null || nodesCompressedViewStates.get(task.getName())==false){			
			setSize(_NODE_WIDTH, compressedHeight+150);
			compressedView=false;
			expandButton.setText(_HIDE_DETAILS_MESSAGE);
		}
		else{
			setSize(_NODE_WIDTH, compressedHeight);
			compressedView=true;
			expandButton.setText(_SHOW_DETAILS_MESSAGE);
		}
		
	}


	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}

	class TaskRep extends JComponent{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ProcessNode tsk;


		public TaskRep(ProcessNode tsk) {
			super();
			this.tsk = tsk;
			
		}

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);
			Set<String> vars = tsk.getAttributes().keySet();
			String status    = task.getAttributes().get("status: ");

			if (status != null) {
				status = status.trim().toLowerCase();
				Color oldColor = g.getColor();
				g.setColor(stateColors.get(status));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(oldColor);
			}

			String outcome = task.getAttributes().get("outcome: ");

			if (outcome != null && outcome.endsWith("Fail")) {
				Color oldColor = g.getColor();
				g.setColor(stateColors.get("failing"));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(oldColor);
			}

			int ycoord = 20;

			Set<ProcessNode> memoryNodes=task.getMemoryNodes();
			Font oldfont = g.getFont();
			g.setFont(new Font(oldfont.getFontName(),Font.BOLD,oldfont.getSize()));
			for (ProcessNode pn:memoryNodes){
				Hashtable<String, String> mvars=pn.getAttributes();
				g.drawString(pn.getName()+": "+ mvars.get("actVal: ")+
						" ["+mvars.get("initVal: ")+"]", 10, ycoord);
				ycoord+=20;
			}     
			g.setFont(oldfont);

			ycoord -= 10;
			g.drawLine(10,ycoord,100,ycoord);
			ycoord += 20;

			for (String vname : vars) {
				if (!Util.isCondition(vname) &&
						!vname.equals("priority: ")) {
					g.drawString(vname+tsk.getAttributes().get(vname),
							10, ycoord);
					ycoord += 20;
				}
			}

			ycoord -= 10;
			g.drawLine(10,ycoord,100,ycoord);
			ycoord += 20;

			for (String vname : vars) {
				if (Util.isCondition(vname)) {
					g.drawString(vname+"["+tsk.getAttributes().get(vname)+"]",
							10, ycoord);
					ycoord += 20;
				}
			}
			setPreferredSize(new Dimension(100, ycoord + 20));
		}

	}

	public String toString() {
		return ("");
	}

	public void setGraphCell(DefaultGraphCell graphCell) {
		_graphCell = graphCell;
	}

	public DefaultGraphCell getGraphCell() {
		return (_graphCell);
	}

	public ProcessNode getTask() {
		return task;
	}
	

}

