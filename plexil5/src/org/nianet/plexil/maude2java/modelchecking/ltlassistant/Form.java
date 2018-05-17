package org.nianet.plexil.maude2java.modelchecking.ltlassistant;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Form extends JFrame{

	public static void main(String[] args) {
		Form f=new Form();
		f.setVisible(true);
	}
	
	public Form(){
		this.setSize(1024,500);
		this.setLayout(null);
		
		ACTextField tf=new LTLACTextField();
		tf.setBounds(10, 10, 800, 50);		
		this.getContentPane().add(tf);
		
		JCheckBox validExp=new JCheckBox();
		validExp.setSelected(false);
		validExp.setBounds(810,10,50,50);
		validExp.setEnabled(false);
		this.getContentPane().add(validExp);				
		
		this.setVisible(true);
	}
	
}
