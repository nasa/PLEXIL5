package org.nianet.plexil.plexilxml2maude.test;

import java.io.File;

import org.nianet.plexil.plexilxml2maude.PlexilPlanLoadException;
import org.nianet.plexil.plexilxml2maude.PlexilXMLUnmarshaller;
import org.nianet.plexil.plexilxml2maude.jaxbmodel.PlexilPlan;

public class Test {

	public static void main(String[] args) throws Exception {
//		PlexilPlan pp=PlexilXMLUnmarshaller.getInstance().getPlexilPlanFromPLE("samplePlexilScripts/TestNode.ple",null);
//		
//		System.out.println(pp.getClass());
//		
//		System.out.println(pp.generateMaudeModule(null));

		
		String[] saxonArgs = new String[4];
		
		saxonArgs[0] = "-o";
		saxonArgs[1] = "/tmp/out2.plx";
		saxonArgs[2] = "/home/hector/NIA/plexil-dec-2010/plexil/examples/DriveToSchool.plx";
		saxonArgs[3] = "/home/hector/workspace/PLEXIL5/schemas/translate-plexil-plexil5.xsl";
		net.sf.saxon.Transform.main(saxonArgs);
		
	}
	
}
