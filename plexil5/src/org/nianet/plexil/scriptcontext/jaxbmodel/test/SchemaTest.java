package org.nianet.plexil.scriptcontext.jaxbmodel.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript.Step;
import org.nianet.plexil.scriptcontext.jaxbmodel.generated.PLEXILScript.Step.State;

public class SchemaTest {

	public static void main(String[] args) throws JAXBException, MalformedURLException, FileNotFoundException {
		JAXBContext jc = null;
		Unmarshaller u = null;

		jc = JAXBContext.newInstance("org.nianet.plexil.scriptcontext.jaxbmodel.generated");

		u = jc.createUnmarshaller();
		
		PLEXILScript ps=(PLEXILScript)u.unmarshal(new FileInputStream("/Users/hcadavid/NIA/Plexil5/trunk/examples/plexil5/DriveToTarget-script.xml"));
		//PlexilPlan pp=(PlexilPlan)u.unmarshal(new FileInputStream("/home/hcadavid/nia/PLEXIL5-JUL-2010/examples/SafeDrive.plx"));
		
		
		System.out.println(ps.getStep().get(0).getState().get(0).getRandomValue().getEnumeration().size());

		Float f=new Float(2.0);
		System.out.println((int)f.floatValue()==f);
		
	}
	
	public int floatToInt(float f) throws NumberFormatException{
		if ((int)f==f){
			return (int)f;
		}
		else throw new NumberFormatException(f+" cannot be converted to int without precision loss");
	}
	
}
