package org.nianet.plexil.maude2java.mauderuntime;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 * Adapted from:
 * http://forum.java.sun.com/thread.jspa?threadID=5150783&tstart=90
 * by Srini_Kandula
 */

public class LocalClassInspector {
	
	
	
	
	public static List<String> loadClassesFromDir(File classpathElement, String prefix) {
		List<String> allEntries = new Vector<String>();  //this will contain all entries in this directory
		//these are all the class files directly in this directory
		List<String> classEntries = Arrays.asList(classpathElement.list(new CLASSFilter()));  
		//add the prefix to all entries
		if(prefix != null){
			for (int i = 0, c = classEntries.size(); i < c; i++) {
				String entry=(prefix + "." + classEntries.get(i));
				//remove initial dot and class extension
				entry=entry.substring(1,entry.length()).substring(0,entry.length()-7);
				allEntries.add(entry);
			}
		}
		//now get a list of all directories within the current directory
		List<File> allSubdirectories = Arrays.asList(classpathElement.listFiles(new DirectoryFilter()));
		//loop through all of them and load their classes, adding this directory to the prefix list.
		for(int i = 0, c = allSubdirectories.size(); i < c; i++){
			allEntries.addAll(loadClassesFromDir(allSubdirectories.get(i), (prefix == null ? "" : prefix + "." ) + allSubdirectories.get(i).getName()));
		}
		return allEntries;
	}

	
	public static List<String> loadClassesFromLocalRootDir(String prefix) {
		String path=null;
		
		path=LocalClassInspector.class.getResource("LocalClassInspector.class").getPath().replace("LocalClassInspector.class","");
		int pkgLenght=LocalClassInspector.class.getPackage().getName().length()+1;
		path=path.substring(0,path.length()-pkgLenght);
		File classpathElement=new File(path);
		List<String> allEntries = new Vector<String>();  //this will contain all entries in this directory
		//these are all the class files directly in this directory
		List<String> classEntries = Arrays.asList(classpathElement.list(new CLASSFilter()));  
		//add the prefix to all entries
		if(prefix != null){
			for (int i = 0, c = classEntries.size(); i < c; i++) {
				String entry=(prefix + "." + classEntries.get(i));
				//remove initial dot and class extension
				entry=entry.substring(1,entry.length()).substring(0,entry.length()-7);
				allEntries.add(entry);
			}
		}
		//now get a list of all directories within the current directory
		List<File> allSubdirectories = Arrays.asList(classpathElement.listFiles(new DirectoryFilter()));
		//loop through all of them and load their classes, adding this directory to the prefix list.
		for(int i = 0, c = allSubdirectories.size(); i < c; i++){
			allEntries.addAll(loadClassesFromDir(allSubdirectories.get(i), (prefix == null ? "" : prefix + "." ) + allSubdirectories.get(i).getName()));
		}
		return allEntries;
	}

	@SuppressWarnings("unchecked")
	public static List<String> loadClassesFromLocalRootDir(String prefix,Class superclass) {
                        
            List<String> str=loadClassesFromLocalRootDir(prefix);
            LinkedList<String> res=new LinkedList<String>();
            
            for (String c:str){
                Class cl=null;
                try {
                    cl = Class.forName(c);
                    Class sc=cl.getSuperclass();
                    
                    if (sc==superclass){                            
                    	res.add(c);
                    }
                    
                } catch (ClassNotFoundException ex) {
                   throw new RuntimeException("Error loading available classes...",ex);
                }
            }
            
            return res;
        }
	
	
	static class DirectoryFilter implements FileFilter {
		public boolean accept(File dir) {
			return dir.isDirectory();
		}
	}
	
	static class CLASSFilter implements FilenameFilter{
		public boolean accept(File dir, String name) {
			return (name.endsWith(".class"));
		}
		
	}
	
	
}