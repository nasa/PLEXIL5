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
public class SdoutReaderMonitorThread extends Thread{
	
	private BufferedReader reader;
	
	private ShellSession shell;
	public ObservableBufferAdapter<OutputBlock> outputBuffer;
	
	public boolean endProcess=false;


	private SdoutReaderMonitorThread(ShellSession shell, BufferedReader reader,
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

		boolean onOutBlockBuilding=true;

		OutputBlock out=null;
		
		while (true){
			try {	
				//State machine to build output block's:
				//ready: ready to read next output's line
				//out: on output block building process
				//ready,!out -> ready, out
				//!ready,out -> !ready,!out
				//ready,out -> ready,out
				//ready,out -> !ready,out
				//!ready,!out -> stay in loop
							
				if (reader.ready() && !onOutBlockBuilding){
					onOutBlockBuilding=true;
					out=new OutputBlock();					
				}
				else if ((!reader.ready() && onOutBlockBuilding)){
					onOutBlockBuilding=false;	
					if (out!=null && out.countLines()>0) outputBuffer.add(out);	
					
				}
				
				
				if (reader.ready() && onOutBlockBuilding){
					OutputDescription outd=readLineWithPromptDetection(reader);
					
					//avoid to print Maude's prompt
					if (outd.hasLineFeed){
						out.addLine(outd.getOutput());						
					}
				}

				if (!reader.ready() && !onOutBlockBuilding){
					try {
						//sleep 1ms to avoid CPU overload when idle
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (IOException e1) {
				e1.printStackTrace();
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
	
	
	
	
}

