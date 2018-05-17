package org.nianet.plexil.plexilxml2maude;

import java.io.File;

public class JarPathSolver {

	
	public static String getJarLocation(){
		String path=JarPathSolver.class.getResource("JarPathSolver.class").getPath();
		if (path.contains("!")){
			path=path.substring(0,path.lastIndexOf('!')).substring(5);			
			path=new File(new File(path).getParent()).getAbsolutePath();
			return path;
		}
		else{
			throw new RuntimeException("This application isn't running in a jar file.");
		}
		
	}
	
}
