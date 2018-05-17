package org.nianet.plexil.plexilxml2maude.jaxbmodel.ext;

import java.util.Map;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.AND;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.EQBoolean;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.EQInternal;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.EQNumeric;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.EQString;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.GE;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.GT;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.IsKnown;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.LE;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.LT;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.LookupNow;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.LookupOnChange;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.NEBoolean;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.NEInternal;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.NENumeric;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.NEString;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.NOT;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.OR;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.XOR;

public abstract class PlexilNodeCondition {

	public String getExpression(Map<String,String> variablesContext){
		return PlexilXMLConditionsPrettyPrinterGenerator.getBooleanExpressionPrettyPrinter(this, variablesContext);
	}
	public abstract OR getOR() ;
	public abstract XOR getXOR() ;
	public abstract AND getAND() ;
	public abstract NOT getNOT() ;
	public abstract IsKnown getIsKnown() ;
	public abstract GT getGT() ;
	public abstract GE getGE() ;
	public abstract LT getLT() ;
	public abstract LE getLE() ;
	public abstract EQBoolean getEQBoolean() ;
	public abstract EQNumeric getEQNumeric() ;
	public abstract EQInternal getEQInternal() ;
	public abstract EQString getEQString() ;
	public abstract NEBoolean getNEBoolean() ;
	public abstract NENumeric getNENumeric() ;
	public abstract NEInternal getNEInternal() ;
	public abstract NEString getNEString() ;
	public abstract String getBooleanVariable() ;
	public abstract Boolean isBooleanValue() ;
	public abstract LookupOnChange getLookupOnChange() ;
    public abstract LookupNow getLookupNow();
	
		
	public BooleanExpression getCondition(){
		if (getOR()!=null){
			return getOR();
		}
		else if (getXOR()!=null){
			return getXOR();
		}
		else if (getAND()!=null){
			return getAND();
		}
		else if (getNOT()!=null){
			return getNOT();
		}
		else if (getIsKnown()!=null){
			return getXOR();
		}
		else if (getGT()!=null){
			return getGT();
		}
		else if (getGE()!=null){
			return getGE();
		}
		else if (getLT()!=null){
			return getLT();
		}
		else if (getLE()!=null){
			return getLE();
		}
		else if (getEQBoolean()!=null){
			return getEQBoolean();
		}
		else if (getEQNumeric()!=null){
			return getEQNumeric();
		}
		else if (getEQInternal()!=null){
			return getEQInternal();
		}
		else if (getEQString()!=null){
			return getEQString();
		}
		else if (getNEBoolean()!=null){
			return getNEBoolean();
		}
		else if (getNENumeric()!=null){
			return getNENumeric();
		}
		else if (getNEInternal()!=null){
			return getNEInternal();
		}
		else if (getNEString()!=null){
			return getNEString();
		}
		else if (getLookupOnChange()!=null){
			return getLookupOnChange();
		}
		else if (isBooleanValue()!=null){
			return new BooleanLiteral(isBooleanValue());
		}
		else if (getLookupNow()!=null){
			return getLookupNow();
		}
		else if (getBooleanVariable()!=null){
			return new BooleanVariable(getBooleanVariable());
		}
		else{
			return null;
		}

	
	}
	
}

