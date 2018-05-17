package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.NodeOutcomeVariable;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.NodeStateVariable;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;

public abstract class StateOutcomeCheckingExpression implements BooleanExpression {

	public String getStateVariable(){
		
		if (isStateCheckingExpression()){
			Object o=getNodeState().get(0);
			Object o2=getNodeState().get(1);
			if (o instanceof NodeStateVariable){
				return PlexilPlan.getNodeAbsoluteName(((NodeStateVariable)o).getNodeId());
			}
			else if (o2 instanceof NodeStateVariable){
				return PlexilPlan.getNodeAbsoluteName(((NodeStateVariable)o2).getNodeId());
			}		
			else{
				throw new RuntimeException("Unsupported datatype for state checking operations:"+o.getClass());
			}			
		}
		else if (isOutcomeCheckingExpression()){
			Object o=getNodeOutcome().get(0);
			Object o2=getNodeOutcome().get(1);
			if (o instanceof NodeOutcomeVariable){
				return PlexilPlan.getNodeAbsoluteName(((NodeOutcomeVariable)o).getNodeId());
			}
			else if (o2 instanceof NodeOutcomeVariable){
				return PlexilPlan.getNodeAbsoluteName(((NodeOutcomeVariable)o2).getNodeId());
			}		
			else{
				throw new RuntimeException("Unsupported datatype for state checking operations:"+o.getClass());
			}						
		}
		else
		{
			throw new RuntimeException("The given element "+this.getClass()+":"+this.toString()+" isn't neither an outcome or state checking expression.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public String getVariableValue(){
		if (isStateCheckingExpression()){
			Object o=getNodeState().get(0);
			Object o2=getNodeState().get(1);
			if (o instanceof JAXBElement){
				return ((JAXBElement)o).getValue().toString().toLowerCase();
			}
			else if (o2 instanceof JAXBElement){
				return ((JAXBElement)o2).getValue().toString().toLowerCase();
			}
			else{
				throw new RuntimeException("Unsupported datatype for state checking operations:"+o.getClass());
			}
		}
		else if(isOutcomeCheckingExpression()){
			Object o=getNodeOutcome().get(0);
			Object o2=getNodeOutcome().get(1);
			if (o instanceof JAXBElement){
				return ((JAXBElement)o).getValue().toString().toLowerCase();
			}
			else if (o2 instanceof JAXBElement){
				return ((JAXBElement)o2).getValue().toString().toLowerCase();
			}
			else{
				throw new RuntimeException("Unsupported datatype for state checking operations:"+o.getClass());
			}			
		}
		else{
			throw new RuntimeException("The given element "+this.getClass()+":"+this.toString()+" isn't neither an outcome or state checking expression.");
		}
		
	}
	
	public abstract List<Object> getNodeState();
	
	public abstract List<Object> getNodeOutcome();
	
	public boolean isOutcomeCheckingExpression(){
		return getNodeOutcome().size()>0;
	}
	
	public boolean isStateCheckingExpression(){
		return getNodeState().size()>0;
	}
	
}
