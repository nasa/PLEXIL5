package org.nianet.plexil.maude2java.modelchecking;

import org.nianet.plexil.logging.LoggerBuilder;
import org.nianet.plexil.maude2java.MaudeIOSyncrhonizationMonitor;
import org.nianet.plexil.maude2java.MaudeOutputSynchronizationException;
import org.nianet.plexil.maude2java.MaudeRuntimeException;
import org.nianet.plexil.maude2java.ShellSession;
import org.nianet.plexil.maude2java.SynchronousMaudeSrewOutputObserver;
import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;


public class SynchronousMaudeModelCheckingEngine {

	public static final String EMPTY_RESULT = "empty";
	private String plexiliteModule;
	private String plexilProgramFile;
	private String plexilProgramName;	
	private String plexilModelChecker;
	private ModelCheckingShellSession ss;
	private SynchronousMaudeModelCheckingOutputObserver sobserver;
	private MaudeModelCheckingErrorObserver errObserver;
		
	
	public SynchronousMaudeModelCheckingEngine(String plexiliteModule,
			String plexilProgramFile,String plexilProgramName,
			String plexilModelChecker) throws MaudeRuntimeConfigurationException {		
		super();
		
		this.plexiliteModule = plexiliteModule;
		this.plexilProgramFile = plexilProgramFile;
		this.plexilProgramName = plexilProgramName;
		this.plexilModelChecker = plexilModelChecker;
		
		ss=new ModelCheckingShellSession();
		sobserver=new SynchronousMaudeModelCheckingOutputObserver();
		ss.addOutputObserver(sobserver);
		//ss.addErrorObserver(MaudeErrorObserver.getInstance());
		errObserver=new MaudeModelCheckingErrorObserver();
		ss.addErrorObserver(errObserver);
		ss.startShell();
		ss.sendInput("load "+this.plexiliteModule);
		ss.sendInput("load "+this.plexilProgramFile);

	}
	
	public SynchronousMaudeModelCheckingEngine(String plexiliteModule,
        String plexilProgramFile,String plexilProgramName) 
	throws MaudeRuntimeConfigurationException {
	  this(plexiliteModule,plexilProgramFile,plexilProgramName,"model-check-lite");
	}
	
	/**
	 * 
	 * 
	 * red <plexilModelChecker>(compile(PlanEnv,Plan), genInvFormula(compile(PlanEnv,Plan))) .

red <plexilModelChecker>(compile(PlanEnv,Plan), genPreFormula(compile(PlanEnv,Plan))) .

red <plexilModelChecker>(compile(PlanEnv,Plan), genPostFormula(compile(PlanEnv,Plan))) .

	 */

	
	/**
	 * red <plexilModelChecker>(compile(PlanEnv,Plan), genInvFormula(compile(PlanEnv,Plan))) .
	 */
	public String checkInvFormula() throws MaudeOutputSynchronizationException, MaudeRuntimeException{
			

		String input="red "+plexilModelChecker+"(compile("+plexilProgramName+"Env,"+plexilProgramName+"),genInvFormula(compile("+plexilProgramName+"Env,"+plexilProgramName+"))) .";		

		ss.sendInput(input);
				
		//wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.modelCheckingMonitor) {
			try {
				MaudeIOSyncrhonizationMonitor.modelCheckingMonitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException("Java-MaudeShell syncrhonization interrupted.",e);
			}
		}
		
		//if there is no new output available, semaphore was activated by an error observer
		if (sobserver.isNewOutputAvailable()){
			return sobserver.getLastestOutput();	
		}
		else{
			throw new MaudeRuntimeException("Error reported by Maude's runtime:"+errObserver.getLastError());
		}
		
		
	}
	


	public String checkPreFormula() throws MaudeOutputSynchronizationException, MaudeRuntimeException{
		

		String input="red "+plexilModelChecker+"(compile("+plexilProgramName+"Env,"+plexilProgramName+"),genPreFormula(compile("+plexilProgramName+"Env,"+plexilProgramName+"))) .";

		ss.sendInput(input);
				
		//wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.modelCheckingMonitor) {
			try {
				MaudeIOSyncrhonizationMonitor.modelCheckingMonitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException("Java-MaudeShell syncrhonization interrupted.",e);
			}
		}
		
		//if there is no new output available, semaphore was activated by an error observer
		if (sobserver.isNewOutputAvailable()){
			return sobserver.getLastestOutput();	
		}
		else{
			throw new MaudeRuntimeException("Error reported by Maude's runtime:"+errObserver.getLastError());
		}
		
		
	}
	

	public String checkPostFormula() throws MaudeOutputSynchronizationException, MaudeRuntimeException{
		

		String input="red "+plexilModelChecker+"(compile("+plexilProgramName+"Env,"+plexilProgramName+"),genPostFormula(compile("+plexilProgramName+"Env,"+plexilProgramName+"))) .";

		ss.sendInput(input);
				
		//wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.modelCheckingMonitor) {
			try {
				MaudeIOSyncrhonizationMonitor.modelCheckingMonitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException("Java-MaudeShell syncrhonization interrupted.",e);
			}
		}
		
		//if there is no new output available, semaphore was activated by an error observer
		if (sobserver.isNewOutputAvailable()){
			return sobserver.getLastestOutput();	
		}
		else{
			throw new MaudeRuntimeException("Error reported by Maude's runtime:"+errObserver.getLastError());
		}
		
		
	}
	
	
	
	
	public String checkProperty(String property) throws MaudeOutputSynchronizationException, MaudeRuntimeException{
					
		//sample prop. formula: [](status('SafeDrive,finished) -> status('Loop . 'SafeDrive, executing))		

		String input="red "+plexilModelChecker+"(compile("+plexilProgramName+"Env,"+plexilProgramName+"),"+property+") .";

		LoggerBuilder.getLogger().debugLog("MC:"+input);
		
		ss.sendInput(input);
				
		//wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.modelCheckingMonitor) {
			try {
				MaudeIOSyncrhonizationMonitor.modelCheckingMonitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException("Java-MaudeShell syncrhonization interrupted.",e);
			}
		}
		
		//if there is no new output available, semaphore was activated by an error observer
		if (sobserver.isNewOutputAvailable()){
			return sobserver.getLastestOutput();	
		}
		else{
			throw new MaudeRuntimeException("Error reported by Maude's runtime:"+errObserver.getLastError());
		}
		
		
	}
	
	


}
