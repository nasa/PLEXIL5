package org.nianet.plexil.maude2java.modelchecking;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimeProperties;
import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimePropertiesFactory;


/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class ModelCheckingShellSession {

		
	private static Process shellProcess;
	BufferedReader sdoutbr,sderrbr;	
	PrintWriter stdin;
	MaudeModelCheckingSdoutMonitorThread outMonitor;
	ModelCheckingSderrReaderMonitorThread errMonitor;
	
	
	private List<Observer> observers;
	private List<Observer> errObservers;
	

	public static String inUseMaudePath=null;
	
	public ModelCheckingShellSession(){
		observers=new LinkedList<Observer>();	
		errObservers=new LinkedList<Observer>();
	}
	
	
	public void addErrorObserver(Observer o){
		errObservers.add(o);
	}
	
	public void addOutputObserver(Observer o){
		observers.add(o);
	}
	
	public List<Observer> getObservers(){
		return observers;
	}
	
	public List<Observer> getErrObservers(){
		return errObservers;
	}
	
	
	/**
	 * 
	 * @return shell's welcome message
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws MaudeRuntimeConfigurationException 
	 */
	public void startShell() throws MaudeRuntimeConfigurationException{
		
		OSShellRuntimeProperties osProps=OSShellRuntimePropertiesFactory.getInstance().buildOSShellRuntimeProperties();
						
		inUseMaudePath=osProps.getCommandPath();
		
		String command=osProps.getAvailableOSShellCommand()+" "+osProps.getCommandPath();						
		
		try {
			if (shellProcess!=null) shellProcess.destroy();
			shellProcess = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			throw new MaudeRuntimeConfigurationException("Error trying to start maude-shell interaction.",e); 
		} 
		
		sdoutbr=new BufferedReader(new InputStreamReader(shellProcess.getInputStream()));
		sderrbr=new BufferedReader(new InputStreamReader(shellProcess.getErrorStream()));
		stdin=new PrintWriter(shellProcess.getOutputStream());
		
		outMonitor=new MaudeModelCheckingSdoutMonitorThread(this,sdoutbr,"out");
		errMonitor=new ModelCheckingSderrReaderMonitorThread(this,sderrbr,"err");		
				
		outMonitor.start();		
		errMonitor.start();
	}
	
	
	public synchronized void sendInput(String input) {		
		stdin.write(input+"\n");
		stdin.flush();
	}
	
	/**
	 * 
	 * @return true if the shell is ready to receive requests. o/w returns false.
	 */
	public boolean shellReady(){
		return outMonitor.isShellStarted();
	}
	
	
}