package org.nianet.plexil.stateviewer.view;
/* ================================================
				JSwingPad Text Editor
			       100 % Pure Java
       		  Created by: Puneet Wadhwa
             Email:puneet_tech@yahoo.com
			 Website:www.puneetwadhwa.com
   =================================================
*/
/**
 * ADAPTED BY HECTOR FABIO CADAVID
 * hector.cadavid@escuelaing.edu.co
 */
//Import the required packages
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class JSwingPad extends JDialog implements ActionListener,WindowListener
{
	JMenuBar mbar;			  //Used for creating a Menu Bar
	JMenu file,help,edit,colors;	   //Used for creating Menus
	JMenuItem m1,m2,m3,m4,m5,m6,m7,m8,m9,cut,paste,copy,delete,about; //Creates MenuItems
	Font fn=new Font("Courier",0,14);
	JTextArea ta=new JTextArea(5,4);
    String cuttext="",txtmsg="";
	int getcaretpos=0,setcaretpos=0;
	FileDialog fd1,fd2; //Created a Windows like File Dialog Box
	JToolBar tb=new JToolBar();

	private String currentFile=null;

	JSwingPad(String fileToOpen)       //Creating a constructor of our own class
	{
		this.setModal(true);
		if (fileToOpen!=null){
			try
			{
				currentFile=fileToOpen;
				FileInputStream fis=new FileInputStream(fileToOpen);
				//creates an input stream
				//streams are like passages for data

				int bytelength=fis.available();
				//calculates the file size

				for (int bytecount=0;bytecount<bytelength;bytecount++)
				{
					//this loop runs till end of file
					char fch=(char)fis.read();
					txtmsg=txtmsg+fch;
				}
				ta.setText(txtmsg);
			}
			catch(Exception ioe)
			{
				JOptionPane.showMessageDialog(this, "Error reading file:"+ioe.getMessage());
			}

		}
		
		
		setTitle("Text Editor");
		fd2=new FileDialog(this,"Save As..",FileDialog.SAVE);
		fd1=new FileDialog(this,"Open..",FileDialog.LOAD);
		
		mbar=new JMenuBar();
		//m1=new JMenuItem("New");
		m2=new JMenuItem("Save");
		//m3=new JMenuItem("Open");
		//m4=new JMenuItem("Print");
		m5=new JMenuItem("Exit"); //Here the menu items were added to the menubar

		m5.addActionListener(this);
		//m4.addActionListener(this);
		//m3.addActionListener(this);
		m2.addActionListener(this);
		//m1.addActionListener(this);
							//Here the Mouse Event listeners are added


		file=new JMenu("File");
		//help=new JMenu("Help");
		about=new JMenuItem("About");
		
		//file.add(m1);
		file.add(m2);
		//file.add(m3);
		//file.add(m4);
		file.addSeparator();
		file.add(m5);

		m6=new JMenuItem("Red");
		m7=new JMenuItem("Blue");
		m8=new JMenuItem("Green");
		m6.addActionListener(this);
		m7.addActionListener(this);
		m8.addActionListener(this);

 		edit=new JMenu("Edit");
		colors=new JMenu("Colors");

		cut=new JMenuItem("Cut");
		paste=new JMenuItem("Paste");
		copy=new JMenuItem("Copy");
		delete=new JMenuItem("Delete");

		cut.addActionListener(this);
		paste.addActionListener(this);
		copy.addActionListener(this);
		delete.addActionListener(this);
		about.addActionListener(this);

		colors.add(m6);
		colors.add(m7);
		colors.add(m8);
		
		edit.add(colors);
		edit.addSeparator();
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.add(delete);
		
		//help.add(about);

		mbar.add(file);
		mbar.add(edit);
		//mbar.add(help);
		
		ta.setFont(fn);
		
		JScrollPane jsp=new JScrollPane();
		jsp.getViewport().add(ta);
		
		this.add(jsp);

		//c.add(tb,"North");
		setJMenuBar(mbar);

		addWindowListener(this);
	}

	public void windowClosing(WindowEvent we)
	{
			this.dispose();
	}

//Following are some blank methods overriden because we implemented WindowEvent interface



	public void windowIconified(WindowEvent we)
	{
	}

	public void windowDeiconified(WindowEvent we)
	{
	}

	public void windowClosed(WindowEvent we)
	{
	}

	public void windowDeactivated(WindowEvent we)
	{
	}

	public void windowActivated(WindowEvent we)
	{
	}

	public void windowOpened(WindowEvent we)
	{
	}

	//Method called when action event occurs

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()==m5))
		{
			this.dispose();
		}
		
		if ((e.getSource()==copy))
		{
			cuttext=ta.getSelectedText();
			//Basically we are storing the selected text into a variable
		}

		if ((e.getSource()==cut))
		{
			cuttext=ta.getSelectedText();
			int cutindex=ta.getText().indexOf(cuttext);
			ta.replaceRange("",cutindex,cutindex+cuttext.length());
			//We store the cut text into a var. and replace it with a blank string
		}

		if ((e.getSource()==delete))
		{
			cuttext=ta.getSelectedText();
			int cutindex= ta.getText().indexOf(cuttext);
			ta.replaceRange("",cutindex,cutindex+cuttext.length());
			//We replace selected text with a blank string
		}

		if ((e.getSource()==paste) )
		{	
			getcaretpos=ta.getCaretPosition();
			ta.insert(cuttext,getcaretpos);
			//Here we insert copied or cut text at the Cursor posn.
			//Warning: This method has now been deprecated, but has been tested to work with JDK 1.2
		}
		
		if (e.getSource()==about)
		{
			 JFrame jf=new JFrame();
			 jf.setBounds(220,130,248,291);
			 //to set the size and location of the frame
			 Container c=jf.getContentPane();
			 //in swing the components are always added to the content pane
			 c.setLayout(new FlowLayout());
			 //sets the layout of container as flow layout
			
			 ImageIcon ic=new ImageIcon("jswingpadc.gif");
			 //Basically used for the Splash Screen
			 JLabel jl=new JLabel(ic);
			 c.add(jl);
			 //creates and shows a frame containing a label (with image inside)
		}									   
		if ((e.getSource()==m1))
		{
			ta.setText("");
		}
		
		if ((e.getSource()==m3))
		{
			txtmsg="";
			fd1.setVisible(true);
			String filename=fd1.getFile();
			String dirname=fd1.getDirectory();
			File openfile=new File(dirname,filename);
			currentFile=openfile.getAbsolutePath();
			//creates a file object for a file obtd. from dialog box

			try
			{
				FileInputStream fis=new FileInputStream(openfile);
				//creates an input stream
				//streams are like passages for data

				int bytelength=fis.available();
				//calculates the file size

				for (int bytecount=0;bytecount<bytelength;bytecount++)
				{
					//this loop runs till end of file
					char fch=(char)fis.read();
					txtmsg=txtmsg+fch;
				}
				ta.setText(txtmsg);
			}
			catch(Exception ioe)
			{
				JOptionPane.showMessageDialog(this, "Error reading file:"+ioe.getMessage());
			}
		}
		//the following block of code changes the background
		//color of the text area as per user's choice

		if (e.getSource()==m6)
		{
			ta.setBackground(new Color(255,204,255));
			ta.setForeground(new Color(0,0,102));
		}
		if (e.getSource()==m7)
		{
			ta.setBackground(new Color(153,204,255));
			ta.setForeground(Color.black);
		}
		if (e.getSource()==m8)
		{
			ta.setBackground(new Color(204,255,204));
			ta.setForeground(Color.black);
		}
		if ((e.getSource()==m2))
		{
			try
			{
				//fd2.setVisible(true);
				BufferedWriter bw=new BufferedWriter(new FileWriter(currentFile));
				bw.write(ta.getText());
				bw.close();
			}
			catch(Exception ioe)
			{
				JOptionPane.showMessageDialog(this, "Error reading file:"+ioe.getMessage());
			}
		}
	}
	
	
	public static void main(String[] args) {
		JSwingPad p=new JSwingPad(null);
		p.setSize(100,200);
		p.setVisible(true);
	}
	
}