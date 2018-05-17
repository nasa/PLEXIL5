package org.nianet.plexil.maude2java.modelchecking.ltlassistant;

public class AutocompleteEntry {

	private String textToInsert;
	private String textToShow;
	private String toolTipText;
	
	public AutocompleteEntry(String textToInsert, String textToShow,
			String toolTipText) {
		super();
		this.textToInsert = textToInsert;
		this.textToShow = textToShow;
		this.toolTipText = toolTipText;
	}
	public String getTextToInsert() {
		return textToInsert;
	}
	public void setTextToInsert(String textToInsert) {
		this.textToInsert = textToInsert;
	}
	public String getTextToShow() {
		return textToShow;
	}
	public void setTextToShow(String textToShow) {
		this.textToShow = textToShow;
	}
	public String getToolTipText() {
		return toolTipText;
	}
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
	
	public String toString(){
		return textToShow;
	}
	
	
}
