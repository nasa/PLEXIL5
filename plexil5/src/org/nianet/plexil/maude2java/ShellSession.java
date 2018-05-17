package org.nianet.plexil.maude2java;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;
import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimeProperties;
import org.nianet.plexil.maude2java.mauderuntime.OSShellRuntimePropertiesFactory;
import org.nianet.plexil.maude2java.observers.DefaultOutputObserver;
import org.nianet.plexil.maude2java.observers.Observer2;
import org.nianet.plexil.stateviewer.view.Plexil5;


/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class ShellSession {

	
	private static final String noAdviseParameter="-no-advise";
	private static Process shellProcess;
	BufferedReader sdoutbr,sderrbr;	
	PrintWriter stdin;
	MaudeSdoutReaderMonitorThread outMonitor;
	SderrReaderMonitorThread errMonitor;
	
	
	private List<Observer> observers;
	private List<Observer> errObservers;
	

	public static String inUseMaudePath=null;
	
	public ShellSession(){
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
	
	
//	public static void main(String[] args) throws IOException, InterruptedException, MaudeRuntimeConfigurationException {
//		ShellSession ss=new ShellSession();
//		ss.addOutputObserver(new DefaultOutputObserver());
//		ss.addOutputObserver(new Observer2());
//		ss.addErrorObserver(new Observer2());
//		ss.startShell();
//				
//		while (true){
//			
//			Scanner scan = new Scanner(System.in);
//			String input=scan.nextLine();    
//	
//			ss.sendInput(input);
//
//		}
//			
//		
//	}
	
	
	
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
				
						
		
		outMonitor=new MaudeSdoutReaderMonitorThread(this,sdoutbr,"out");
		errMonitor=new SderrReaderMonitorThread(this,sderrbr,"err");		
		
		
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