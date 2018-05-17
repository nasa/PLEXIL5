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
public class SderrReaderMonitorThread extends Thread{
	
	private BufferedReader reader;
	
	private ShellSession shell;
	public ObservableBufferAdapter<OutputBlock> outputBuffer;
	
	public boolean endProcess=false;


	public SderrReaderMonitorThread(ShellSession shell, BufferedReader reader,
			String name) {
		
		super();
		this.outputBuffer=new ObservableBufferAdapter<OutputBlock>(new LinkedList<OutputBlock>());
		this.shell = shell;
		this.reader = reader;
		
		for (Observer o:this.shell.getErrObservers()){
			outputBuffer.addObserver(o);
		}
	
	}
		
	
	public void run(){		

		boolean onOutBlockBuilding=false;

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
				else if (!reader.ready() && onOutBlockBuilding){
					onOutBlockBuilding=false;					
					outputBuffer.add(out);									
				}
				
				if (reader.ready() && onOutBlockBuilding){
					out.addLine(reader.readLine());
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void endProcess() {
		this.endProcess = true;
	}


	public LinkedList<OutputBlock> getOutputBuffer() {
		return outputBuffer;
	}
	
	
	
	
}
