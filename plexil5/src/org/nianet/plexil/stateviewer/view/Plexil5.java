/*
 * PlexilExecutionStateViewer.java
 *
 * Created on 29 de septiembre de 2008, 04:00 PM
 */

package org.nianet.plexil.stateviewer.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.nianet.plexil.Context;
import org.nianet.plexil.Contexts;
import org.nianet.plexil.PlexilState;
import org.nianet.plexil.Variable;
import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.MaudeRuntimeException;
import org.nianet.plexil.maude2java.SynchronousMaudeRuntimeEngine;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimePropertiesFactory;
import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;
import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.ext.PlexilScriptException;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.model.ProcessTreeBuilder;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeBuilder;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeException;
import org.nianet.plexil.stateviewer.view.outlineview.Plexil5OutlineTableTree;

/**
 *
 * @author  Hector Fabio Cadavid Rengifo
 */
public class Plexil5 extends JFrame {

	public static final boolean LUV_VIEW_MODE=true;
	
	private static final int _CONTEXT_PANEL_WIDTH = 200;

	private static final long serialVersionUID = 2867593096243998746L;

	private static ProcessNode currentProcessTree=null;

	private static PLEXILScript currentPlexilScript=null;

	private static Plexil5 instance;

	private static String currentPlexilPlan=null;

	private static String currentPlexilScriptFilePath=null;

	private static String currentBaseName=null;
	
	private static String currentMaudePlan=null;
	
	private static String scriptFile=null;
	
	private static String plexilFile=null;

	private static boolean lastOutputWithEmptyContext=false;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

        int i,lasti;
        i=lasti=0;
                       
        try{
                while(i < args.length){
                    if(args[i].equals("--help")){
                        helpMessage();                 
                    }
                    else if (args[i].equals("-s") || args[i].equals("--script")){
                    	scriptFile = args[++i];
                    } else if (args[i].equals("-p") || args[i].equals("--plexil")){
                    	plexilFile = args[++i];
                    } else if (i==args.length-1) {//allow last arg as implicit inputfile
                    	plexilFile = args[i];
                    } else if (i==lasti){//when not all of the options have been cleared, ERROR
                            throw new Exception("Illegal command line argument: "+args[i]);
                    }
                     i++;
                     lasti=i;
                   }
                
            }catch(Exception ex){
                System.out.println("ERROR: "+ex.getMessage());
                helpMessage();
            }
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				/*for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            try {
							UIManager.setLookAndFeel(info.getClassName());
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedLookAndFeelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            break;
			        }
			    }*/
				
				
				instance=new Plexil5("PLEXIL5");
				instance.setSize(1100,800);
				//sv.setExtendedState(MAXIMIZED_BOTH);
				instance.setVisible(true);
				instance.setLocationRelativeTo(null);
			}
		});
	}

	private static void helpMessage() {
        System.out.println("Usage: Plexil5 [-s, --script scriptfile] [-p, --plexil plexilfile]\n");
	}
	
	private JComponent graphicalTree;
	ProcessNode e3=null;
	LinkedList<ProcessNode> nodes=new LinkedList<ProcessNode>();

	//private JMenuItem loadMaudePlan;	
	
	private JMenuItem loadStandardPlexilButton;
	private JMenuItem loadPlexilScript;

	private JMenuItem reloadStandardPlexilButton;
	private JMenuItem reloadPlexilScript;

	private JMenuItem editPlanButton;
	private JMenuItem editScriptButton;

	private JMenuItem exitButton;
	private JMenuItem settingButton;

	private JMenuItem doModelChecking;

	private JButton resetButton;
	private JButton backButton;
	private JButton microButton;
	private JButton quiescenceButton;
	private JButton macroButton;
	private JButton executeButton;
	private JScrollPane mainPanel;

	private Container plexilContainer;
	private JFrame plexilFrame;
	private JToolBar toolBar;
	private String selectedModule;	

	private JScrollPane contextView;
	private SynchronousMaudeRuntimeEngine eng;
	private JLabel maudeVersion;
	private JLabel currentScriptLabel;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	
	private JMenu optionsMenu;
	private JMenu modelCheckingMenu;
	private JMenu helpMenu;


	/** Creates new form PlexilExecutionStateViewer */
	private Plexil5(String title) {
		super(title);
		initComponents();
		loadFromFile();
	}
	
	private void initComponents() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);

	

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		
		modelCheckingMenu = new JMenu("Model-checking");
		optionsMenu = new JMenu("Options");
		helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(modelCheckingMenu);
		
		menuBar.add(optionsMenu);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);

		toolBar = new JToolBar();

		settingButton = new JMenuItem("Select Formal Interpreter");        

		loadStandardPlexilButton = new JMenuItem("Load Plan");
		loadPlexilScript=new JMenuItem("Load Script");

		reloadStandardPlexilButton = new JMenuItem("Reload Plan");
		reloadPlexilScript=new JMenuItem("Reload Script");
		
		//loadMaudePlan=new JMenuItem("Load Maude plan");

		resetButton = new JButton("Reset");
		resetButton.setEnabled(false);

		backButton = new JButton("Undo");
		backButton.setEnabled(false);

		microButton = new JButton("Micro");
        microButton.setEnabled(false);
 
        quiescenceButton = new JButton("Quiescence");
        quiescenceButton.setEnabled(false);
        
		macroButton=new JButton("Macro");
		macroButton.setEnabled(false);
		
		executeButton=new JButton("Execution");
		executeButton.setEnabled(false);
		
		exitButton = new JMenuItem("Exit");

		mainPanel = new JScrollPane();
		
		editPlanButton = new JMenuItem("Edit Plan");
		editScriptButton = new JMenuItem("Edit Script");
		try {
			maudeVersion=new JLabel("Maude Version:"+OSShellRuntimePropertiesFactory.getCurrentMaudeProperties());
		} catch (MaudeRuntimeConfigurationException e1) {
			JOptionPane.showMessageDialog(null, e1);
		}
		currentScriptLabel=new JLabel("No script loaded.");
		currentScriptLabel.setForeground(Color.RED);

		editPlanButton.setEnabled(false);
		editScriptButton.setEnabled(false);
		loadPlexilScript.setEnabled(false);

		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		final JFileChooser finalChooser = new JFileChooser();
		finalChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		finalChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) 
					return true;
				return f.getName().startsWith("maude"); 
			}
			public String getDescription() {
				return "Maude Executable";
			}
		});

		settingButton.setFocusable(false);
		settingButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		settingButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		settingButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						new Thread(){
							public void run(){
								if (finalChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
									File file = finalChooser.getSelectedFile();                  
									String maudePath = file.getAbsolutePath();

									try {
										OSShellRuntimePropertiesFactory.updateConfigurationPropertiesMaudePath(maudePath);
										maudeVersion.setText("Maude version:"+OSShellRuntimePropertiesFactory.getCurrentMaudeProperties());
									} catch (IOException e) {
										JOptionPane.showMessageDialog(null, "Error saving Maude's path:"+e.getMessage());
									} catch (MaudeRuntimeConfigurationException e) {
										JOptionPane.showMessageDialog(null, "Error saving Maude's path:"+e.getMessage());
									}

								}
							}
						}.start();
					}
				}
		);

		optionsMenu.add(settingButton);

		final JFileChooser maudeFileChooser = new JFileChooser();
		maudeFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		maudeFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return (f.isDirectory() || f.getName().endsWith("maude"));
			}
			public String getDescription() {
				return "Maude File";
			}
		});


		final JFileChooser plexilFileChooser = new JFileChooser();
		plexilFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		plexilFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return (f.isDirectory() || f.getName().endsWith(".ple") || f.getName().endsWith(".maude"));
			}
			public String getDescription() {
				return "Plexil File";
			}
		});

		final JFileChooser plexilScriptFileChooser = new JFileChooser();
		plexilScriptFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		plexilScriptFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return (f.isDirectory() || f.getName().endsWith("xml"));
			}
			public String getDescription() {
				return "Plexil script - XML file";
			}
		});

		loadStandardPlexilButton.setFocusable(false);
		loadStandardPlexilButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		loadStandardPlexilButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener loadStandarPlexilAL=    
			new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run(){
						if (plexilFileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
							File file = plexilFileChooser.getSelectedFile();

							try{

								if (file.getName().endsWith(".maude")){
									String basename = file.getName().split(".maude")[0];    

									plexilFrame.setTitle("PLEXIL5: "+basename);
									selectedModule = file.getAbsolutePath();

									currentBaseName=basename;
									currentMaudePlan=selectedModule;

									eng=new SynchronousMaudeRuntimeEngine("plexilite",selectedModule,currentBaseName);	
									String ns=eng.nullStep();                                        

									PlexilState state = Plexilite.parseState(ns);
									updateContexts(state);

									lastOutputWithEmptyContext=state.getContexts().isEmpty();

									currentProcessTree=ProcessTreeBuilder.createStateTree(state);

									PlexilGraphicalTreeBuilder.resetCoordinatesMap();
									PlexilPlan.resetPlexilPlansInfo();

									initView();

									//doModelChecking.setEnabled(true);
									editScriptButton.setEnabled(false);
									editPlanButton.setEnabled(false);									
								}
								else{
									String filename = file.getName();
									String basename = filename.substring(0,filename.length()-4);

									plexilFrame.setTitle("PLEXIL5: "+basename);

									//when a new plan is loaded, previous script information is removed.
									currentPlexilScript=null;					  
									currentScriptLabel.setText("No script loaded.");
									currentScriptLabel.setForeground(Color.RED);
									editScriptButton.setEnabled(false);
									doModelChecking.setEnabled(true);
									editPlanButton.setEnabled(true);


									String scriptname = file.getParentFile()+"/"+basename+"-script.xml";
									File script = new File(scriptname);
									if (script.exists()) {
										currentPlexilScriptFilePath=script.getAbsolutePath();
										currentPlexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(currentPlexilScriptFilePath);                                    
										currentScriptLabel.setText("Script loaded:"+script.getAbsolutePath());
										currentScriptLabel.setForeground(Color.BLUE);
										editScriptButton.setEnabled(true);	                              
									}

									loadPlexilPlan(file);								
								}
								
                                resetButton.setEnabled(true);
                                backButton.setEnabled(true);
                                quiescenceButton.setEnabled(true);
                                microButton.setEnabled(true);
                                macroButton.setEnabled(true);
                                executeButton.setEnabled(true);

							} catch(ParseException ex){
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE); 
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (PlexilPlanLoadException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (IOException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							}
						}
					}
				}.start();
			}        			
		};

		loadStandardPlexilButton.addActionListener(loadStandarPlexilAL);
		fileMenu.add(loadStandardPlexilButton);          

		loadPlexilScript.setFocusable(false);
		loadPlexilScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		loadPlexilScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener loadScriptAL=    
			new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run(){
						if (plexilScriptFileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
							File file = plexilScriptFileChooser.getSelectedFile();

							try{
								//update plexil script object
								currentPlexilScriptFilePath=file.getAbsolutePath();
								currentPlexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(currentPlexilScriptFilePath);    								

								//reload plan
								loadPlexilPlan(new File(currentPlexilPlan));

								currentScriptLabel.setText("Script loaded:"+file.getAbsolutePath());
								currentScriptLabel.setForeground(Color.BLUE);
								editScriptButton.setEnabled(true);

							} catch (PlexilPlanLoadException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (IOException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (ParseException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} 
						}
					}
				}.start();
			}        			
		};    
		loadPlexilScript.addActionListener(loadScriptAL);
		fileMenu.add(loadPlexilScript);
		fileMenu.add(new JSeparator());

		reloadStandardPlexilButton.setFocusable(false);
		reloadStandardPlexilButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		reloadStandardPlexilButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener reloadStandarPlexilAL=    
			new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run(){
							try{
								//reload plan
								loadPlexilPlan(new File(currentPlexilPlan));
							} catch (PlexilPlanLoadException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (IOException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (ParseException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} 
					}
				}.start();
			}    			
		};

		reloadStandardPlexilButton.addActionListener(reloadStandarPlexilAL);
		fileMenu.add(reloadStandardPlexilButton);          

		reloadPlexilScript.setFocusable(false);
		reloadPlexilScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		reloadPlexilScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener reloadScriptAL=    
			new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run(){
							try{
								//update plexil script object
								currentPlexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(currentPlexilScriptFilePath);    								

								//reload plan
								loadPlexilPlan(new File(currentPlexilPlan));

							} catch (PlexilPlanLoadException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (IOException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (ParseException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} 
						}
				}.start();
			}        			
		};    
		reloadPlexilScript.addActionListener(reloadScriptAL);
		fileMenu.add(reloadPlexilScript);
		fileMenu.add(new JSeparator());


		resetButton.setFocusable(false);
		resetButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		resetButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		resetButton.addActionListener(        		
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						org.nianet.plexil.PlexilState state;
						try {
								eng.resetSteps();
                				String ns=eng.nullStep();
				                PlexilState pstate = Plexilite.parseState(ns);
				                updateContexts(pstate);
				                lastOutputWithEmptyContext=pstate.getContexts().isEmpty();
				                currentProcessTree=ProcessTreeBuilder.createStateTree(pstate);
				                PlexilGraphicalTreeBuilder.resetCoordinatesMap();
				                PlexilPlan.resetPlexilPlansInfo();
				                initView();
						
					} catch (ParseException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
						//System.out.println(stepOutput);
					} catch (MaudeOutputSynchronizationException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					} catch (MaudeRuntimeException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
					}
				}
		);

		toolBar.add(resetButton);


		backButton.setFocusable(false);
		backButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		backButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		backButton.addActionListener(        		
				new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						org.nianet.plexil.PlexilState state;
						String stepOutput=null;
						try {
							stepOutput=eng.previousStep();
							if (stepOutput.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
								JOptionPane.showMessageDialog(null, "No more steps available.", "Error.", JOptionPane.ERROR_MESSAGE);                
							}
							else{
								state = Plexilite.parseState(stepOutput);
								 

								updateContexts(state);

								currentProcessTree=ProcessTreeBuilder.createStateTree(state);	                
								
								updateView();
								/*graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
								mainPanel.getViewport().removeAll();
								mainPanel.getViewport().add(graphicalTree);*/
							}
						} catch (ParseException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
							//System.out.println(stepOutput);
						} catch (MaudeOutputSynchronizationException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						} catch (MaudeRuntimeException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
					}

				}        		
		);

		toolBar.add(backButton);

		microButton.setFocusable(false);
		microButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		microButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		microButton.addActionListener(        		
				new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						org.nianet.plexil.PlexilState state;
						String nextStepOut=null;
						try {
							nextStepOut=eng.microStep();							

							if (nextStepOut.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
								eng.backtrackCommand();
								JOptionPane.showMessageDialog(null, "No more steps available.", "Error.", JOptionPane.ERROR_MESSAGE);								
							}
							else{								
								state = Plexilite.parseState(nextStepOut);

								updateContexts(state);

								 
								currentProcessTree=ProcessTreeBuilder.createStateTree(state);			
								
								updateView();
								/*graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
								mainPanel.getViewport().removeAll();
								mainPanel.getViewport().add(graphicalTree);*/														
							}
						} catch (ParseException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							//System.out.println(nextStepOut);
							ex.printStackTrace();
						} catch (MaudeOutputSynchronizationException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();

						} catch (MaudeRuntimeException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}

				}        		
		);

		toolBar.add(microButton);


		quiescenceButton.setFocusable(false);
		quiescenceButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		quiescenceButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		quiescenceButton.addActionListener(        		
				new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						org.nianet.plexil.PlexilState state;
						String nextStepOut=null;
						try {
							nextStepOut=eng.quiescenseStep();
							if (nextStepOut.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
								eng.backtrackCommand();
								JOptionPane.showMessageDialog(null, "No more steps available.", "Error.", JOptionPane.ERROR_MESSAGE);								
							}
							else{								
								state = Plexilite.parseState(nextStepOut);
								updateContexts(state);
								 
								currentProcessTree=ProcessTreeBuilder.createStateTree(state);			
								
								updateView();
								/*graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
								mainPanel.getViewport().removeAll();
								mainPanel.getViewport().add(graphicalTree);*/														
							}
						} catch (ParseException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							//System.out.println(nextStepOut);
							ex.printStackTrace();
						} catch (MaudeOutputSynchronizationException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						} catch (MaudeRuntimeException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
					}

				}        		
		);

		//toolBar.add(quiescenceButton);


		macroButton.setFocusable(false);
		macroButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		macroButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		macroButton.addActionListener(        		
				new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						org.nianet.plexil.PlexilState state;
						String nextStepOut=null;
						try {
							
							//==================================
							if (lastOutputWithEmptyContext){
								nextStepOut=eng.quiescenseStep();								
							}
							//==================================
							else{
								nextStepOut=eng.macroStep();	
							}
							
							if (nextStepOut.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
								eng.backtrackCommand();
								JOptionPane.showMessageDialog(null, "No more steps available.", "Error.", JOptionPane.ERROR_MESSAGE);								
							}
							else{								
								state = Plexilite.parseState(nextStepOut);

								lastOutputWithEmptyContext=state.getContexts().isEmpty();
								
								
								updateContexts(state);								
								currentProcessTree=ProcessTreeBuilder.createStateTree(state);
								
								updateView();
							}
						} catch (ParseException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							//System.out.println(nextStepOut);
							ex.printStackTrace();
						} catch (MaudeOutputSynchronizationException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						} catch (MaudeRuntimeException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
					}

				}        		
		);

		toolBar.add(macroButton);


		executeButton.setFocusable(false);
		executeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		executeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener executeAL=            		
			new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				org.nianet.plexil.PlexilState state;
				String nextStepOut=null;
				try {
				// CESAR ADDED THIS (AND COMMENTED THE REST)
                  //==================================
                  if (lastOutputWithEmptyContext){
                      nextStepOut=eng.quiescenseStep();                               
                  }
                  //==================================
                  else {
                      nextStepOut=eng.macroStep();   
                  }
                  while (!nextStepOut.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)) {
                    state = Plexilite.parseState(nextStepOut);
                    lastOutputWithEmptyContext=state.getContexts().isEmpty();
                    updateContexts(state);                              
                    currentProcessTree=ProcessTreeBuilder.createStateTree(state);                        
                    updateView();
                    nextStepOut=eng.macroStep();    
                  }
                  eng.backtrackCommand();                  
 				//  nextStepOut=eng.executeStep();
				//	if (nextStepOut.equals(SynchronousMaudeRuntimeEngine.EMPTY_RESULT)){
				//		eng.backtrackCommand();
				//		JOptionPane.showMessageDialog(null, "No more steps available.", "Error.", JOptionPane.ERROR_MESSAGE);								
				//	}
				//	else{								
				//		state = Plexilite.parseState(nextStepOut);
 				//		updateContexts(state);
			    //		currentProcessTree=ProcessTreeBuilder.createStateTree(state);			
				//		updateView();
				//		/*graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
				//		mainPanel.getViewport().removeAll();
				//		mainPanel.getViewport().add(graphicalTree);*/														
				//	} 
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				} catch (MaudeOutputSynchronizationException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				} catch (MaudeRuntimeException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}

		};
		executeButton.addActionListener(executeAL);
//		addMenuItem(runMenu,"Execute>>",executeAL);
		toolBar.add(executeButton);


		editPlanButton.setFocusable(false);
		editPlanButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		editPlanButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener editPlanAL=        		
			new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (currentPlexilPlan!=null){

					try{

						JSwingPad editPad=new JSwingPad(currentPlexilPlan);
						editPad.setSize(500,500);
						editPad.setVisible(true);
						loadPlexilPlan(new File(currentPlexilPlan));

					}
					catch (ParseException ex){
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();  
					} 
					catch (PlexilPlanLoadException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (MaudeRuntimeConfigurationException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (MaudeOutputSynchronizationException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (MaudeRuntimeException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}


				}

			}

		};
		editPlanButton.addActionListener(editPlanAL);
		fileMenu.add(editPlanButton);

		editScriptButton.setFocusable(false);
		editScriptButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		editScriptButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ActionListener editScriptAL=        		
			new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (currentPlexilPlan!=null){

					try{


						JSwingPad editPad=new JSwingPad(currentPlexilScriptFilePath);
						editPad.setSize(500,500);
						editPad.setVisible(true);

						//reload script
						currentPlexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(currentPlexilScriptFilePath);

						loadPlexilPlan(new File(currentPlexilPlan));

						currentScriptLabel.setText("Script loaded:"+currentPlexilScriptFilePath);
						currentScriptLabel.setForeground(Color.BLUE);
						editScriptButton.setEnabled(true);					  

					}
					catch (ParseException ex){
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();  
					} 
					catch (PlexilPlanLoadException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (MaudeRuntimeConfigurationException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (MaudeOutputSynchronizationException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
						ex.printStackTrace();
					} catch (MaudeRuntimeException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}


				}


			}

		};
		editScriptButton.addActionListener(editScriptAL);
		fileMenu.add(editScriptButton);
		fileMenu.add(new JSeparator());


		ActionListener exitAL=               
			new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		exitButton.addActionListener(exitAL);
		fileMenu.add(exitButton);

		
		
		doModelChecking = new JMenuItem("Check Property");
		doModelChecking.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {												    
						
						ModelCheckingMainFrame inst = new ModelCheckingMainFrame(currentBaseName,currentMaudePlan);
						inst.setLocationRelativeTo(null);
						inst.setVisible(true);
					}
				});

			}
		});
		modelCheckingMenu.add(doModelChecking);
		doModelChecking.setEnabled(false);



		plexilContainer = getContentPane();
		plexilContainer.setLayout(new BorderLayout());
		plexilContainer.add(mainPanel,BorderLayout.CENTER);

		JPanel bottomBar=new JPanel();
		bottomBar.setLayout(new BorderLayout());

		bottomBar.add(maudeVersion,BorderLayout.SOUTH);
		bottomBar.add(currentScriptLabel,BorderLayout.CENTER);
		bottomBar.add(toolBar,BorderLayout.NORTH);

		plexilContainer.add(bottomBar,BorderLayout.SOUTH);
		plexilFrame = this;


		contextView=new JScrollPane();
		contextView.setPreferredSize(new Dimension(_CONTEXT_PANEL_WIDTH,(int)contextView.getPreferredSize().getHeight()));
		plexilContainer.add(contextView,BorderLayout.EAST);

		//contextView.getViewport().add(new JTextArea("CURRENT CONTEXT:\n"));
		updateContexts(null);

		pack();


	}


	private void updateContexts(PlexilState state){

		JPanel rootPanel=new JPanel();
		rootPanel.setLayout(new BorderLayout());

		JPanel contextPanel=new JPanel();
		
		GridBagLayout layout=new GridBagLayout();
		
		GridBagConstraints c=new GridBagConstraints();
		
		contextPanel.setLayout(layout);
		
		//c.fill = GridBagConstraints.HORIZONTAL;
				
		
		c.gridx=0;
		c.gridy=0;
		
		
		JLabel envLabel=new JLabel("ENVIRONMENT");
		Font font = envLabel.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize()+2);
		envLabel.setFont(boldFont);
		
		contextPanel.add(envLabel,c);
		c.gridy++;
		
		if (state!=null){
			
			Context currentContext=state.getContext();
			
			contextPanel.add(createContextDataTable(currentContext),c);
			c.gridy++;
			
			Contexts ctxs=state.getContexts();
			
			for (int i=0;i<ctxs.size();i++){
				contextPanel.add(new JLabel("    "),c);
				c.gridy++;
				Context ctx=ctxs.get(i);			
				contextPanel.add(createContextDataTable(ctx),c);
				c.gridy++;				
			}

			
			rootPanel.add(contextPanel,BorderLayout.NORTH);
			contextView.getViewport().add(rootPanel);
			
			
		}
		
		
	}


	private JTable createContextDataTable(Context cnt){
		
		Object[][] data=new Object[cnt.keys().size()][2];
		Set<String> ctsKeys=cnt.keys();
		
		int i=0;
		for (String key:ctsKeys){			
			Variable var=cnt.get(key);
			data[i][0]=var.getName();
			try{
				data[i][1]= new DecimalFormat("#.##").format(Float.parseFloat(var.getValue()));	
			}
			catch (NumberFormatException e){
				data[i][1]= var.getValue();
			}
						
			i++;
		}
		JTable t=new JTable(new ContextDataModel(data,new Object[]{"Var. name","Value"}));
		t.getColumnModel().getColumn(0).setPreferredWidth((int)Math.round(_CONTEXT_PANEL_WIDTH*0.75));		
		return t;
		
	}
	


	/**
	 * This method Change UI's state according with the given PLEXIL script. New state corresponds to given plan's 1st micro-step.
	 * @param file - Plexil plan in standard plexil (PLE) format
	 * @throws PlexilPlanLoadException
	 * @throws IOException
	 * @throws MaudeRuntimeConfigurationException
	 * @throws MaudeOutputSynchronizationException
	 * @throws ParseException
	 * @throws MaudeRuntimeException 
	 */
	public void loadPlexilPlan(File file) throws PlexilPlanLoadException, IOException, MaudeRuntimeConfigurationException, MaudeOutputSynchronizationException, ParseException, MaudeRuntimeException{

		String filename = file.getName();
		String basename = filename.substring(0,filename.length()-4);
		currentBaseName=basename;


		currentPlexilPlan=file.getAbsolutePath();

		PlexilPlan.resetPlexilPlansInfo();
		PlexilPlan pp=PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE(file.getAbsolutePath(),file.getAbsoluteFile().getParent()+"/"+basename+".plx");

		//System.out.println(pp.generateMaudeModule(currentPlexilScript));

		currentMaudePlan=file.getAbsoluteFile().getParent()+"/"+basename+".maude";
		
		BufferedWriter bw=new BufferedWriter(new FileWriter(currentMaudePlan));                	                  	 
		try {
			bw.write(pp.generateMaudeModule(currentPlexilScript));
		} catch (PlexilScriptException e) {
			throw new PlexilPlanLoadException("Error on plexil plan load caused by a wrong Plexil script:"+e.getMessage(),e);
		}
		bw.close();

		//TODO fix windows incompatibility problem
		selectedModule = file.getAbsolutePath();

		eng=new SynchronousMaudeRuntimeEngine("plexilite",currentMaudePlan,currentBaseName);	
		String ns=eng.nullStep();                                        

		PlexilState state = Plexilite.parseState(ns);		
		updateContexts(state);
		lastOutputWithEmptyContext=state.getContexts().isEmpty();
		
		currentProcessTree=ProcessTreeBuilder.createStateTree(state);
		PlexilGraphicalTreeBuilder.resetCoordinatesMap();
		PlexilPlan.resetPlexilPlansInfo();

		initView();
		/*graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
		mainPanel.getViewport().add(graphicalTree);*/

		//on new plexil plan load, reset current script
		loadPlexilScript.setEnabled(true);

	}

	private void loadFromFile() {
	if (plexilFile!=null) {
		File pfile = new File(plexilFile);
		if (scriptFile==null) {
			String pfbase = pfile.getName().split("\\.(?=[^\\.]+$)")[0];
			scriptFile = pfile.getParentFile()+"/"+pfbase+"-script.xml";
		}
		File sfile = new File(scriptFile);
		try {
			if (sfile.exists()) {
			  currentPlexilScriptFilePath=sfile.getAbsolutePath();
			  currentPlexilScript=PlexilXMLUnmarshaller.getInstance().getPlexilScriptFromXML(currentPlexilScriptFilePath);                                    
			  currentScriptLabel.setText("Script loaded:"+sfile.getAbsolutePath());
			  currentScriptLabel.setForeground(Color.BLUE);
			  editScriptButton.setEnabled(true);	                              
			}
			loadPlexilPlan(new File(plexilFile));
            		resetButton.setEnabled(true);
            		backButton.setEnabled(true);
            		quiescenceButton.setEnabled(true);
            		microButton.setEnabled(true);
            		macroButton.setEnabled(true);
            		executeButton.setEnabled(true);
            editPlanButton.setEnabled(true);
		}
	    catch (ParseException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();  
	    } 
	    catch (PlexilPlanLoadException ex) {
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
	            ex.printStackTrace();
	    } catch (IOException ex) {
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
	            ex.printStackTrace();
	    } catch (MaudeRuntimeConfigurationException ex) {
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
	            ex.printStackTrace();
	    } catch (MaudeOutputSynchronizationException ex) {
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);                      
	            ex.printStackTrace();
	    } catch (MaudeRuntimeException ex) {
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	    }
	}
	}

	
	private void addMenuItem(JMenu menu, String title, ActionListener al){
		JMenuItem mitem=new JMenuItem(title);
		mitem.addActionListener(al);
		menu.add(mitem);
	}

	private void addMenuItem(JMenu menu, JMenuItem menuItem){	  
		menu.add(menuItem);
	}
	
	private void updateView(){
		if (!LUV_VIEW_MODE){
			graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
			mainPanel.getViewport().removeAll();
			mainPanel.getViewport().add(graphicalTree);																	
		}
		else{
			try {
				if (mainPanel.getViewport().getComponentCount()>0)
					((Plexil5TableTree)mainPanel.getViewport().getComponent(0)).updateTree(currentProcessTree);
			} catch (ProxyTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	private void initView(){
		if (!LUV_VIEW_MODE){
			graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentProcessTree);
			mainPanel.getViewport().removeAll();
			mainPanel.getViewport().add(graphicalTree);																	
		}
		else{
			//A reference to this local map will be kept on ProxyTree's nodes.
			Hashtable<String, ProcessNode> realNodesMap=new Hashtable<String, ProcessNode>();			
			//Plexil5LuvLikePlanTreeComponent luvTree=new Plexil5LuvLikePlanTreeComponent(ProxyTreeBuilder.buildProxyTree(realNodesMap, currentProcessTree));
			Plexil5OutlineTableTree luvTree=new Plexil5OutlineTableTree(ProxyTreeBuilder.buildProxyTree(realNodesMap, currentProcessTree));
			mainPanel.getViewport().removeAll();
			mainPanel.getViewport().add(luvTree);			
		}
	}
	
}

class ContextDataModel extends DefaultTableModel{

	
	public ContextDataModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
		
}
