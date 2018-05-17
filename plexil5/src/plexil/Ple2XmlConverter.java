/* Copyright (c) 2006-2008, Universities Space Research Association (USRA).
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Universities Space Research Association nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY USRA ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL USRA BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package plexil;

import java.util.*;
import java.io.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import net.n3.nanoxml.*;

/**
 * Creation date: (1/26/2004 12:07:04 PM)
 * @author: Andrew Bachmann
 * Adapted by Hector Cadavid R. (hector.cadavid@escuelaing.edu.co) to be integrated on
 * PLEXIL5 Runtime Environment
 */
public class Ple2XmlConverter
{
	public static final String version = new String("PlexilParser version 0.3");

	public static boolean quiet = false;
	public static FileWriter debugWriter = null;

	private Ple2XmlConverter() 
	{

	}


	private static File defaultOutputFile(String fullname)
	{
		File infile = new File(fullname);
		// replace extension with .plx
		String inname = infile.getName();
		String outname = inname.substring(0, inname.lastIndexOf('.')) + ".plx";
		return new File(infile.getParent(), outname);
	}



	public static final File generateIncludeFileName(final String parent, final String filename) 
	{
		File returnFile = new File("");
		// handle the case of the parent dir being empty. 
		if (parent != "")
		{
			returnFile = new File(parent, filename);
		}
		else
		{
			returnFile = new File(filename);
		}
		if (returnFile.canRead()) 
		{
			// file exists, no need to serach the search path. 
			return returnFile;
		} 
		else 
		{
			// search for file on search path. 
			// retrive search path.
			//Object[] customSearchPath = ModelAccessor.getCustomPath();
			Object[] customSearchPath = new Object[0];
			// if search path exists.
			if (customSearchPath.length > 0 ) 
			{
				//System.out.print("File " + returnFile.toString() + " was not found. ");
				//System.out.println("Searching for it in the nddl_path... ");
				// create a version of the filename with all path info removed.
				String filenameWithoutPath = "";
				int lastDirSlash = filename.lastIndexOf('/');
				if (lastDirSlash != -1) 
				{
					filenameWithoutPath = 
						filename.substring(lastDirSlash + 1, filename.length());
				}
				File candidateFile = new File("");
				boolean found = false;
				for (int i = 0; i < customSearchPath.length; i++) 
				{
					String pathEntry = (String) customSearchPath[i];
					// check if file exists with path info from include statement. 
					candidateFile = new File(pathEntry, filename);
					//System.out.println("checking for " + candidateFile);
					if (candidateFile.canRead()) 
					{
						found = true;
						break;
					}
					// check if file exists without path info from include statement.
					candidateFile = new File(pathEntry, filenameWithoutPath);
					// System.out.println("checking for " + candidateFile);
					if (candidateFile.canRead()) {
						found = true;
						break;
					}
				} // end examine each path. 
				if (found) 
				{
					//System.out.println("File " + candidateFile.toString() + " found in search path.");
					return candidateFile;
				}
				else 
				{
					//System.out.println("The file was not found in the nddl_path.");
				}
			}
		}
		return returnFile; // default behaviour - let caller handle case when the file is not found 
	}

	public static void load(IXMLElement xml, String filename) throws XMLTranslationException
	{
		if (!quiet) 
		{
			//System.out.println("  " + filename);
		}
		File file = generateIncludeFileName("", filename); 
		if (!file.canRead()) 
		{
			throw new XMLTranslationException("unable to read file: " + filename);
		}
		PlexilParser parser;
		try {
			parser = PlexilParser.parse(file, debugWriter);
		} catch (RecognitionException e) {
			throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
		} catch (TokenStreamException e) {
			throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
		} catch (FileNotFoundException e) {
			throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
 		}
		if (parser.getState().printErrorCount()) 
		{
			// be sure to properly close debug writer if bailing out early
			if (debugWriter != null)
				try {
					debugWriter.close();
				} catch (IOException e) {
					throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
				}
				throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.");
		}
		if (debugWriter != null)
		{
			antlr.collections.AST parseAST = parser.getAST();
			if (parseAST != null)
			{
				try {
					debugWriter.write(parseAST.toStringTree());
				} catch (IOException e) {
					throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
				}
			}
			else
			{
				try {
					debugWriter.write("Parser AST is null!");
				} catch (IOException e) {
					throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
				}
			}
			try {
				debugWriter.write('\n');
			} catch (IOException e) {
				throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
			}
		}
		PlexilTreeParser treeParser = new PlexilTreeParser(parser.getState());
		try {
			treeParser.plexilPlan(parser.getAST(), xml);
		} catch (RecognitionException e) {
			throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
		}
		if (treeParser.getState().printErrorCount()) 
		{
			// be sure to properly close debug writer if bailing out early
			if (debugWriter != null)
				try {
					debugWriter.close();
				} catch (IOException e) {
					throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.",e);
				}
				throw new XMLTranslationException("PLE to XML Converter: Error while parsing PLE file.");
		}
	}

	
	/**
	 * 
	 * @param pleFilePath
	 * @return
	 * @throws Exception
	 */
	public static String getXMLFromPLE(String pleFilePath) throws Ple2XMLProcessingException
	{    	
		
		try
		{
		

			IXMLElement xml = new XMLElement("PlexilPlan");
			
			
			//load input file
			load(xml,pleFilePath);
			
			// write out XML

			StringWriter writer=new StringWriter();
			
			XMLWriter xmlWriter = new XMLWriter(writer);
			checkXML(xml);
			xmlWriter.write(xml,true,2,true);

			return writer.getBuffer().toString();
		} 
		catch (Exception e) 
		{
			throw new Ple2XMLProcessingException("Error processing plexil PLE file:",e);
		}
	}

	private static void checkXML(IXMLElement xml)
	{

		for (int i=0; i < xml.getChildrenCount(); i++)
			checkXML(xml.getChildAtIndex(i));
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(Ple2XmlConverter.getXMLFromPLE("/home/hcadavid/nia/plexil-dic-2010/plexil/examples/DriveToSchool.ple"));
		} catch (Ple2XMLProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
