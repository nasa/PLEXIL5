package org.nianet.plexil.maude2java.modelchecking.ltlassistant;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

public abstract class ACTextField extends JEditorPane implements KeyListener{

	private static final int _SPACE_KEY = 32;
	private static final int _CTRL_KEY = 17;
	boolean ctrlPressed=false;
	private JPopupMenu autoCompletePopUp=null;
	String capturedString; 		


	public ACTextField(){
		addKeyListener(this);
		this.setFont(new Font("Dialog", Font.PLAIN, 15));
		this.requestFocus();
		this.grabFocus();
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {		
		
		
		if (e.getKeyCode()==_CTRL_KEY){
			ctrlPressed=true;
		}
		else if (e.getKeyCode()==_SPACE_KEY  && ctrlPressed){
			Point caretScreenLoc=this.getCaret().getMagicCaretPosition();
			Point loc;
			if (caretScreenLoc==null){
				loc=getLocationOnScreen();
			}
			else{
				loc=caretScreenLoc;
			}
			
			if (this.getCaretPosition()>0 && (
					this.getText().charAt(this.getCaretPosition()-1)!=' ' && 
					this.getText().charAt(this.getCaretPosition()-1)!='(')&&
					this.getText().charAt(this.getCaretPosition()-1)!=')'){				
				showContextBasedAutocompleteOptions(loc.x, loc.y);
			}
			else{
				showElementsSuggestionPopUp(loc.x,loc.y, getAutocompleteOptions());	
			}
			
			
			ctrlPressed=false;
			
		}				
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
		
		/*if (e.getKeyCode()!=56 && e.getKeyCode()!=57 && !ctrlPressed){				
			if (!getAllowedChars().contains(e.getKeyChar())){
				int pos=this.getCaretPosition();
				
				this.setText(removeCharAt(this.getText(), pos-1));
			}
		}*/
		ctrlPressed=false;
		updateCapturedString();		
		inputEventAction();
		updateFormat();
	}

	protected void updateFormat(){
		if (!validInput()){
			this.setForeground(Color.RED);
		}
		else{
			this.setForeground(Color.GREEN);
		}		
	}
	
	public static String removeCharAt(String s, int pos) {
	    return s.substring(0,pos)+s.substring(pos+1);
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
				
	}		
	
	
	protected abstract AutocompleteEntry[] getOptionsByContext(String partialToken);
	
	
	void showContextBasedAutocompleteOptions(int x, int y){
		
	    autoCompletePopUp=new JPopupMenu();
	    int caretPos=this.getCaretPosition();	    	    
	    
	    String leftText=this.getText().substring(0,this.getCaretPosition());
	    String contextText;
	    int lastRP=leftText.lastIndexOf(")");
	    int lastLP=leftText.lastIndexOf("(");
	    int lastSpace=leftText.lastIndexOf(" ");
	    
	
	    if (lastRP>lastLP && lastRP>lastSpace){
	    	contextText=leftText.substring(lastRP+1,caretPos);
	    }
	    else if (lastLP>lastRP && lastLP>lastSpace){
	    	contextText=leftText.substring(lastLP+1,caretPos);
	    }
	    else if (lastSpace>lastLP && lastSpace>lastRP){
	    	contextText=leftText.substring(lastSpace+1,caretPos);
	    }
	    else{
	    	contextText=leftText;
	    }

	    AutocompleteEntry[] options=getOptionsByContext(contextText);
	    	    
	    JList list = new JList(options);
	    
	    
	    list.setFont(new Font("Dialog", Font.PLAIN, 20));
	    
	    JScrollPane scrollPane = new JScrollPane(list);
	    	    
	    autoCompletePopUp.add(scrollPane);	    	    
			    
	    autoCompletePopUp.requestFocus();
		autoCompletePopUp.grabFocus();
		
		list.addListSelectionListener(new SuggestionSelectionListener(this, options, autoCompletePopUp,caretPos));
		autoCompletePopUp.show(this,x,y);
		
	}
	
	
	void showElementsSuggestionPopUp(int x, int y, AutocompleteEntry[] options){
	    
	    autoCompletePopUp=new JPopupMenu("Suggestions...");
	    JList list = new JList(options);
	    
	    list.setFont(new Font("Dialog", Font.PLAIN, 20));
	    
	    JScrollPane scrollPane = new JScrollPane(list);
	    
	    
	    autoCompletePopUp.add(scrollPane);	    	    
			    
	    autoCompletePopUp.requestFocus();
		autoCompletePopUp.grabFocus();
		
		int caretPos=this.getCaretPosition();
		
		list.addListSelectionListener(new SuggestionSelectionListener(this, options, autoCompletePopUp,caretPos));
		autoCompletePopUp.show(this,x,y);
	    
	}

	protected void updateCapturedString(){
		capturedString=this.getText();
	}
	
	public String getCapturedString() {
		return capturedString;
	}

	
	public abstract AutocompleteEntry[] getAutocompleteOptions();
	
	public abstract List<Character> getAllowedChars();
	
	public abstract void inputEventAction();
	
	public abstract boolean validInput();
		
	
}

class SuggestionSelectionListener implements ListSelectionListener{

	ACTextField comp;
	AutocompleteEntry options[];
	JPopupMenu emergentMenu;
	int caretPos;
	
	
	public SuggestionSelectionListener(ACTextField comp, AutocompleteEntry[] options, JPopupMenu emergentMenu, int caretPos){
		this.comp=comp;
		this.options=options;
		this.emergentMenu=emergentMenu;
		this.caretPos=caretPos;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {		
		emergentMenu.setVisible(false);
		comp.setText(insertAt(comp.getText(), caretPos, options[e.getLastIndex()].getTextToInsert()));
		comp.setCaretPosition(caretPos+options[e.getLastIndex()].getTextToInsert().length());
		comp.updateCapturedString();
		comp.inputEventAction();
		comp.updateFormat();
	}
		

	public static String insertAt(String s, int pos, String c) {		
		return new StringBuffer(s).insert(pos, c).toString();
	}


	
}
