package org.nianet.plexil.stateviewer.view;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import org.antlr.runtime.tree.Tree;
import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.MaudeRuntimeException;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.maude2java.modelchecking.MaudeCounterexampleParsingException;
import org.nianet.plexil.maude2java.modelchecking.MaudeModelCheckingCounterExample;
import org.nianet.plexil.maude2java.modelchecking.SynchronousMaudeModelCheckingEngine;
import org.nianet.plexil.maude2java.modelchecking.ltlassistant.LTLACTextField;
import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.Node;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript;
import org.nianet.plexil.stateviewer.model.ProcessNode;
import org.nianet.plexil.stateviewer.view.luvlikeview.Plexil5LuvLikePlanTreeComponent;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeBuilder;
import org.nianet.plexil.stateviewer.view.luvlikeview.ProxyTreeException;
import org.nianet.plexil.stateviewer.view.outlineview.Plexil5OutlineTableTree;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.LTLTreeGenerator;
import org.nianet.plexil5.maude2java.modelchecking.ltlparser.MalformedLTLExpressionException;


public class ModelCheckingMainFrame extends javax.swing.JFrame {

	private JPanel topPanel;
	private JButton checkButton;
	private JButton checkInvariantsButton;
	private JButton checkPreconditionsButton;
	private JButton checkPostconditionsButton;	
	private JScrollPane outputJspContainer;
	private JButton closeButton;
	private JButton nextStepButton;
	private JButton resetButton;
	private JPanel bottomPanel;
	private int checkType = -1; // -1: None, 0: Property, 1: Inv, 2: Pre, 3: Post
	
	/*ComboBox replaced by the LTLassistant
	 * private JComboBox propertyComboBox;
	 * 
	 * */
	
	private LTLACTextField propertyLTLField;
	
	
	private String currentProperty;


	private Enumeration<ProcessNode> currentCounterExampleIterator=null;
	private MaudeModelCheckingCounterExample currentCounterExample=null;
	private JFrame self=this;

	File plexilPlan;
	String currentBaseName;
	String currentPlexilPlan;
	String selectedModule;
	String selectedProgram;
	String maudePlanPath=null;
	String baseName;
	PLEXILScript currentPlexilScript;



	public ModelCheckingMainFrame(String baseName,String maudePlanPath) {
		super("MODEL CHECKING:"+baseName);
		initGUI();
		//this.plexilPlan=plexilPlan;
		//this.currentPlexilScript=currentPlexilScript;
		this.baseName=baseName;
		this.maudePlanPath=maudePlanPath;
	}
	
	void displayMaudeOutput(String maudeOutput) throws MaudeCounterexampleParsingException {
	  if (!maudeOutput.contains("{") && maudeOutput.contains("true")){
	    outputJspContainer.getViewport().add(viewModelCheckingResult(true));
	  }     
	  else{ 
	    outputJspContainer.getViewport().add(viewModelCheckingResult(false));
	    // **** CESAR COMMENTED THIS
	    //currentCounterExample=new MaudeModelCheckingCounterExample(maudeOutput);
	    //currentCounterExampleIterator=currentCounterExample.stepsIterator();
	    //if (currentCounterExampleIterator.hasMoreElements()){
	    //  outputJspContainer.getViewport().add(viewModelCheckingResult(false));
	    //}
	    //else{
	    //  JOptionPane.showMessageDialog(null, "No steps available on counterexample:"+maudeOutput, "Error", JOptionPane.ERROR_MESSAGE);
	    //}
	  }
	}

	private void initGUI() {
		try {			
						
			BorderLayout thisLayout = new BorderLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(thisLayout);

			bottomPanel = new JPanel();
			getContentPane().add(bottomPanel, BorderLayout.SOUTH);

			resetButton = new JButton();
			bottomPanel.add(resetButton);
			resetButton.setText("Reset Counterexample");
			resetButton.setEnabled(false);
			resetButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							currentCounterExampleIterator=currentCounterExample.stepsIterator();							
							if (currentCounterExampleIterator.hasMoreElements()){
								
								 initView();
								 /*JComponent component=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentCounterExampleIterator.nextElement());
								  outputJspContainer.getViewport().removeAll();
								  outputJspContainer.getViewport().add(component);*/
							  }
							  else{
								  JOptionPane.showMessageDialog(null, "No more steps available", "Info", JOptionPane.INFORMATION_MESSAGE);
							  }						
						}
					}
			
			);
			
			
			

			nextStepButton = new JButton();
			bottomPanel.add(nextStepButton);
			nextStepButton.setText("Show Next Step >>");
			nextStepButton.setBackground(new java.awt.Color(0,255,0));
			nextStepButton.setEnabled(false);
			nextStepButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							if (currentCounterExampleIterator.hasMoreElements()){
								
								updateView();
								  /*JComponent component=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentCounterExampleIterator.nextElement());
								  outputJspContainer.getViewport().removeAll();
								  outputJspContainer.getViewport().add(component);*/
							  }
							  else{
								  JOptionPane.showMessageDialog(null, "No more steps available", "Info", JOptionPane.INFORMATION_MESSAGE);
							  }						
						}
					}
			
			);
			

			closeButton = new JButton();
			bottomPanel.add(closeButton);
			closeButton.setText("Close");


			closeButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							self.dispose();						
						}
					}
			
			);
			

			topPanel = new JPanel();
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
			getContentPane().add(topPanel, BorderLayout.NORTH);

			
			
			propertyLTLField = new LTLACTextField();
			propertyLTLField.setToolTipText("Here, enter the property to be verified by modelchecking.");
			propertyLTLField.setPreferredSize(new Dimension(381,100));
			/*propertyComboBox = new JComboBox(ModelCheckingHistory.getHistoryArray());
			propertyComboBox.setSelectedItem("");
			propertyComboBox.setEditable(true);
			propertyComboBox.setToolTipText("Here, enter the property to be verified by modelchecking.");
			
			propertyComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					currentProperty = (String)cb.getSelectedItem();
					System.out.println("Prop:"+currentProperty);
				}
			});

			propertyComboBox.setPreferredSize(new java.awt.Dimension(381, 22));*/
			
			JPanel checkPanel=new JPanel();
			
			checkButton = new JButton();
			checkButton.setText("<-- Check");	

			
			checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.X_AXIS));
			checkPanel.add(propertyLTLField);
			//checkPanel.add(propertyComboBox);
			checkPanel.add(checkButton);

			
			topPanel.add(checkPanel);			
									
			checkButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try{
								  																
								  setCursor(new Cursor(Cursor.WAIT_CURSOR));							
								  
								  SynchronousMaudeModelCheckingEngine eng=new SynchronousMaudeModelCheckingEngine("plexilite", maudePlanPath, baseName);
								  
								  //change
								  
								  if (propertyLTLField.validInput()){
									  try {
										  Tree expRoot=LTLTreeGenerator.parseAndGetTree(propertyLTLField.getCapturedString());
										  currentProperty=LTLTreeGenerator.generateMaudeExpression(expRoot);
										  String maudeOutput=eng.checkProperty(currentProperty);
                                          checkType = 0;
										  displayMaudeOutput(maudeOutput);
									      /* if (!ModelCheckingHistory.getHistory().contains(currentProperty)){
									        ModelCheckingHistory.addElement(currentProperty);                                   
									        propertyComboBox.addItem(currentProperty);                                      
									       */
									  } catch (MalformedLTLExpressionException e1) {
										  e1.printStackTrace();
									  }
								  }								  								  
							}
							catch(MaudeRuntimeException ex){
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							}  catch (MaudeCounterexampleParsingException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} finally {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							}
						}
					}	
				);

			
			JPanel predefinedChecks=new JPanel();
			predefinedChecks.setLayout(new BoxLayout(predefinedChecks, BoxLayout.LINE_AXIS));
			topPanel.add(predefinedChecks);
			
			checkInvariantsButton = new JButton();
			predefinedChecks.add(checkInvariantsButton);
			checkInvariantsButton.setText("Check Invariants");	
						
			checkInvariantsButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try{
								  setCursor(new Cursor(Cursor.WAIT_CURSOR));							
								  SynchronousMaudeModelCheckingEngine eng=new SynchronousMaudeModelCheckingEngine("plexilite", maudePlanPath, baseName);								  
								  String maudeOutput=eng.checkInvFormula();						  
                                  checkType = 1;
								  displayMaudeOutput(maudeOutput);								
							}
							catch(MaudeRuntimeException ex){
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();							
							} catch (MaudeCounterexampleParsingException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} finally {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							}
						}
					}	
				);

			

			checkPreconditionsButton = new JButton();
			predefinedChecks.add(checkPreconditionsButton);
			checkPreconditionsButton.setText("Check Preconditions");			
			
			
			checkPreconditionsButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try{
								  setCursor(new Cursor(Cursor.WAIT_CURSOR));	
								  SynchronousMaudeModelCheckingEngine eng=new SynchronousMaudeModelCheckingEngine("plexilite", maudePlanPath, baseName);				
								  String maudeOutput=eng.checkPreFormula();
                                  checkType = 2;
								  displayMaudeOutput(maudeOutput);
							}
							catch(MaudeRuntimeException ex){
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();							
							} catch (MaudeCounterexampleParsingException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} finally {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							}
						}
					}	
				);

			checkPostconditionsButton = new JButton();
			predefinedChecks.add(checkPostconditionsButton);
			checkPostconditionsButton.setText("Check Postconditions");					
						
			checkPostconditionsButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try{
								  setCursor(new Cursor(Cursor.WAIT_CURSOR));	   
								  SynchronousMaudeModelCheckingEngine eng=new SynchronousMaudeModelCheckingEngine("plexilite", maudePlanPath, baseName);			
								  String maudeOutput=eng.checkPostFormula();
								  checkType = 3;
								  displayMaudeOutput(maudeOutput);
							}
							catch(MaudeRuntimeException ex){
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeOutputSynchronizationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} catch (MaudeRuntimeConfigurationException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();							
							} catch (MaudeCounterexampleParsingException ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							} finally {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							}
						}
					}	
				);

			outputJspContainer = new JScrollPane();
			getContentPane().add(outputJspContainer, BorderLayout.CENTER);

			pack();
			this.setSize(800, 571);
		} catch (Exception e) {
			//add your error handling code here
			e.printStackTrace();
		}
	}

	
	
	private void updateView(){
		if (!Plexil5.LUV_VIEW_MODE){
			JComponent graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentCounterExampleIterator.nextElement());
			outputJspContainer.getViewport().removeAll();
			outputJspContainer.getViewport().add(graphicalTree);																	
		}
		else{
			try {
				//((Plexil5LuvLikePlanTreeComponent)outputJspContainer.getViewport().getComponent(0)).updateTree(currentCounterExampleIterator.nextElement());
				((Plexil5TableTree)outputJspContainer.getViewport().getComponent(0)).updateTree(currentCounterExampleIterator.nextElement());
			} catch (ProxyTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	private JPanel viewModelCheckingResult(boolean mcresult){
		
		
		
		JPanel panel=new JPanel();
		
		panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
		
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));				
		
		if (mcresult){
			panel.add(new JLabel("Output: TRUE"));	
		}
		else{
			panel.add(new JLabel("Output: FALSE"));	
			JButton viewButton=new JButton("View counter-example...");						
			
			panel.add(viewButton);
			
			viewButton.addActionListener(
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
                          // **** CESAR ADDEDED THIS
					      try {
					        SynchronousMaudeModelCheckingEngine eng=new SynchronousMaudeModelCheckingEngine("plexilite", maudePlanPath, baseName, "model-check");
					        String maudeOutput = "";
					        switch (checkType) {
					        case 0: maudeOutput=eng.checkProperty(currentProperty);
					          break;
					        case 1: maudeOutput=eng.checkInvFormula();
					          break;
					        case 2: maudeOutput=eng.checkPreFormula();
					          break;
					        case 3: maudeOutput=eng.checkPostFormula();
					          break;
					        }
					        currentCounterExample=new MaudeModelCheckingCounterExample(maudeOutput);
					        currentCounterExampleIterator=currentCounterExample.stepsIterator();
					        if (currentCounterExampleIterator.hasMoreElements()){
					          outputJspContainer.getViewport().add(viewModelCheckingResult(false));
					        }
					        else{
					          JOptionPane.showMessageDialog(null, "No steps available on counterexample:"+maudeOutput, "Error", JOptionPane.ERROR_MESSAGE);
	                        }
					        initView();
					        nextStepButton.setEnabled(true);
                            resetButton.setEnabled(true);
					      } catch (MaudeRuntimeConfigurationException ex) {
					        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					        ex.printStackTrace();
					      } catch (MaudeOutputSynchronizationException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                          } catch (MaudeRuntimeException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                          } catch (MaudeCounterexampleParsingException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                          }
						}
					}		
			);			
		}
		
		return panel;
	}
	
	private void initView() {
		if (!Plexil5.LUV_VIEW_MODE){
			JComponent graphicalTree=new PlexilGraphicalTreeBuilder().buildGraphicalTaskTree(currentCounterExampleIterator.nextElement());
			outputJspContainer.getViewport().removeAll();
			outputJspContainer.getViewport().add(graphicalTree);																	
		}
		else{
			//A reference to this local map will be kept on ProxyTree's nodes.
			Hashtable<String, ProcessNode> realNodesMap=new Hashtable<String, ProcessNode>();			
			//Plexil5LuvLikePlanTreeComponent luvTree=new Plexil5LuvLikePlanTreeComponent(ProxyTreeBuilder.buildProxyTree(realNodesMap, currentCounterExampleIterator.nextElement()));
			Plexil5OutlineTableTree luvTree=new Plexil5OutlineTableTree(ProxyTreeBuilder.buildProxyTree(realNodesMap, currentCounterExampleIterator.nextElement()));
			outputJspContainer.getViewport().removeAll();
			outputJspContainer.getViewport().add(luvTree);			
		}
	}

	
}

class ModelCheckingHistory{
	
	private static Set<String> history=new LinkedHashSet<String>();
	
	public static void addElement(String e){
		history.add(e);
	}
	
	private ModelCheckingHistory(){}

	public static Set<String> getHistory() {
		return history;
	}
	
	public static String[] getHistoryArray(){
		String out[]=new String[history.size()];
		int i=0;
		for (String s:history){			
			out[i]=s;
			i++;
		}
		return out;
	}
}