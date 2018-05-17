package org.nianet.plexil.stateviewer.model;

import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Set;

import org.nianet.plexil.ProcessType;

public class ProcessNode extends Observable{

	private String name;
	private String absName;
	private Set<ProcessNode> childTasks;
	private ExecutionState executionState;
	private ExecutionOutcome executionOutcome;
	private Hashtable<String,String> attributes;
	private ProcessType procType;
	private Set<ProcessNode> memoryNodes;
	private boolean hiddenChilds=false;

	
	public ProcessNode(String name, String absName, Hashtable<String, String> attributes) {
		super();
		this.absName=absName;
		executionState=ExecutionState.Inactive;
		executionOutcome=ExecutionOutcome.None;
		this.attributes=attributes;
		this.name = name;
		childTasks=new LinkedHashSet<ProcessNode>();
		memoryNodes=new LinkedHashSet<ProcessNode>();
	}
	
	public void addChild(ProcessNode t){
		childTasks.add(t);
	}

	public String getName() {
		return name;
	}

	
	
	public String getAbsName() {
		return absName;
	}

	public Set<ProcessNode> getChildTasks() {
		return childTasks;
	}

	public void setName(String name) {
		this.name = name;
		this.setChanged();
		notifyObservers();
	}
	
	public int getWidth(){
		if (isLeaf()){
			return 1;
		}
		else{
			int w=0;
			for (ProcessNode c:childTasks){
				w+=c.getWidth();
			}
			return w;
		}
	}
	
	public boolean isLeaf(){
		return childTasks.size()==0;
	}

	public ExecutionState getExecutionState() {
		return executionState;
	}

	public void setExecutionState(ExecutionState executionState) {
		this.setChanged();
		notifyObservers();
		this.executionState = executionState;
	}

	public ExecutionOutcome getExecutionOutcome() {
		return executionOutcome;
	}

	public void setExecutionOutcome(ExecutionOutcome executionOutcome) {
		this.setChanged();
		notifyObservers();
		this.executionOutcome = executionOutcome;
	}

	@Override
	public boolean equals(Object obj) {
		return ((ProcessNode)obj).getName().equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public Hashtable<String, String> getAttributes() {
		return attributes;
	}	
	
	public void setAttributes(Hashtable<String, String> attributes) {
		this.attributes = attributes;
	}

	public ProcessType getProcessType() {
		return procType;
	}

	public void setProcessType(ProcessType procType) {
		this.procType = procType;
	}

	public void addMemoryNode(ProcessNode proc){
		memoryNodes.add(proc);
	}
	
	public Set<ProcessNode> getMemoryNodes() {
		return memoryNodes;	
	}

	public boolean isHiddenChilds() {
		return hiddenChilds;
	}

	public void setHiddenChilds(boolean hiddenChilds) {
		this.hiddenChilds = hiddenChilds;
	}

	public boolean initialLocationAssigned() {
		// TODO Auto-generated method stub
		return false;
	}	

	public boolean containsChildByName(String name){
		if (this.name.equals(name)){
			return true;
		}
		else{
			for (ProcessNode pn:this.getChildTasks()){
				if (pn.containsChildByName(name)) return true;
			}
			for (ProcessNode pn:this.getMemoryNodes()){
				if (pn.containsChildByName(name)) return true;
			}
			return false;
		}
	}
	
}
