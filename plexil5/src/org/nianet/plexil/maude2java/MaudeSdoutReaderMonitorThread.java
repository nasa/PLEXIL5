package org.nianet.plexil.maude2java;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observer;


/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class MaudeSdoutReaderMonitorThread extends Thread{
	
	private BufferedReader reader;
	
	private ShellSession shell;
	public ObservableBufferAdapter<OutputBlock> outputBuffer;
	private boolean shellStarted=false;
	
	public boolean endProcess=false;


	public MaudeSdoutReaderMonitorThread(ShellSession shell, BufferedReader reader,
			String name) {
		
		super();
		this.outputBuffer=new ObservableBufferAdapter<OutputBlock>(new LinkedList<OutputBlock>());
		this.shell = shell;
		this.reader = reader;
		
		for (Observer o:this.shell.getObservers()){
			outputBuffer.addObserver(o);
		}
	
	}
		
	
	public void run(){		

		boolean onOutBlockBuilding=false;

		OutputBlock out=null;
		
		//verify shell's startup
		while (!shellStarted){
			try {
				if (reader.ready()){
					OutputDescription outd=readLineWithPromptDetection(reader);
					if (outd.getOutput().indexOf("Maude>")==0){
						shellStarted=true;
					}
				}
				Thread.sleep(1);
			} catch (IOException e) {
				throw new RuntimeException("Error on maude's shell output monitor.",e);
			} catch (InterruptedException e){
				throw new RuntimeException("Error on maude's shell output monitor.",e);
			}
		}
		
		while (true){			
				
			try {				
				
				if (reader.ready()){				
					
					OutputDescription outd=readLineWithPromptDetection(reader);
										
					
					if (onOutBlockBuilding){
						if (outd.getOutput().indexOf("No more solutions.")==0){		
							onOutBlockBuilding=false;
							outputBuffer.add(out);
						}
						else{
							out.addLine(outd.getOutput());							
						}
					}					
					
					if (!onOutBlockBuilding && outd.getOutput().indexOf("srewrite in ")==0){
						onOutBlockBuilding=true;
						out=new OutputBlock();
					}
				}
				//sleep 1ms to avoid CPU overload when idle
				Thread.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException("Error on maude's shell output monitor.",e);
			} catch (IOException e) {
				throw new RuntimeException("Error on maude's shell output monitor.",e);
			}
				
		}
	}

	private OutputDescription readLineWithPromptDetection(BufferedReader r) throws IOException{
		String s="";
		char lastChar=' ';
		
		while (r.ready() && lastChar!='\n'){
			lastChar=(char)r.read();
			s+=lastChar;			
		}
		
		return new OutputDescription(s.replaceAll("\n", ""),lastChar=='\n');
	}
	
	public void endProcess() {
		this.endProcess = true;
	}


	public LinkedList<OutputBlock> getOutputBuffer() {
		return outputBuffer;
	}


	public boolean isShellStarted() {
		return shellStarted;
	}



	
}

