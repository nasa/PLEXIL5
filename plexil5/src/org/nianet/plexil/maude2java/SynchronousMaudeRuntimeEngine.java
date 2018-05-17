package org.nianet.plexil.maude2java;

import org.nianet.plexil.maude2java.mauderuntime.MaudeRuntimeConfigurationException;

public class SynchronousMaudeRuntimeEngine {

	public static final String EMPTY_RESULT = "[EMPTY_OUTPUT]";
	private String plexiliteModule;
	private String plexilProgramFile;
	private String plexilProgramName;
	private int currentMicroStep;
	private int currentMacroStep;
	private ShellSession ss;
	private SynchronousMaudeSrewOutputObserver sobserver;
	private String currentOutput;

	private MaudeRewritingErrorObserver errObserver;

	private boolean quiescenceMode;

	private String commandSequence;
	// constants for commands
	private final String MICRO = "m";
	private final String MACRO = "M";
	private final String UPDATE = "U";
	private final String QUIESCENCE = "q";
	private final String EXECUTE = "E";

	public SynchronousMaudeRuntimeEngine(String plexiliteModule,
			String plexilProgramFile, String plexilProgramName)
			throws MaudeRuntimeConfigurationException {
		super();

		quiescenceMode = false;
		currentMicroStep = -1;
		currentMacroStep = -1;
		this.plexiliteModule = plexiliteModule;
		this.plexilProgramFile = plexilProgramFile;
		this.plexilProgramName = plexilProgramName;
		this.commandSequence = "";

		ss = new ShellSession();
		sobserver = new SynchronousMaudeSrewOutputObserver();
		errObserver = new MaudeRewritingErrorObserver();
		ss.addOutputObserver(sobserver);
		ss.addErrorObserver(errObserver);
		ss.startShell();
		ss.sendInput("load " + this.plexiliteModule);
		ss.sendInput("load " + this.plexilProgramFile);
	}

	public String previousStep() throws MaudeOutputSynchronizationException,
			MaudeRuntimeException {

		// if (currentMicroStep > 0 || quiescenceMode) {
		//
		// if (quiescenceMode) {
		// quiescenceMode = false;
		// } else {
		// currentMicroStep--;
		// }
		//
		// if (currentMicroStep == 0) {
		// ss.sendInput("srew compile(" + plexilProgramName + "Env,"
		// + plexilProgramName + ") using idle .");
		// } else {
		// ss.sendInput("srew compile(" + plexilProgramName + "Env,"
		// + plexilProgramName + ") using "
		// + createMicroStepStrategy(currentMicroStep));
		// }
		//
		// // wait for response
		// synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
		// try {
		// MaudeIOSyncrhonizationMonitor.monitor.wait();
		// } catch (InterruptedException e) {
		// throw new MaudeOutputSynchronizationException(
		// "Java-MaudeShell syncrhonization interrupted.", e);
		// }
		// }
		//
		// if (sobserver.isNewOutputAvailable()) {
		// currentOutput = sobserver.getLastestOutput();
		// return filterOutput(currentOutput);
		// } else {
		// throw new MaudeRuntimeException(
		// "Error reported by Maude's runtime:"
		// + errObserver.getLastError());
		// }
		//
		// } else {
		// return EMPTY_RESULT;
		// }

		this.backtrackCommand();

		ss.sendInput("srew compile(" + plexilProgramName + "Env,"
				+ plexilProgramName + ") using "
				+ getCommandStrategy(commandSequence) + " .");

		// wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			try {
				MaudeIOSyncrhonizationMonitor.monitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException(
						"Java-MaudeShell syncrhonization interrupted.", e);
			}
		}

		if (sobserver.isNewOutputAvailable()) {
			currentOutput = sobserver.getLastestOutput();
			return filterOutput(currentOutput);
		} else {
			throw new MaudeRuntimeException(
					"Error reported by Maude's runtime:"
							+ errObserver.getLastError());
		}
	}

	public String nullStep() throws MaudeOutputSynchronizationException,
			MaudeRuntimeException {

		ss.sendInput("srew compile(" + plexilProgramName + "Env,"
				+ plexilProgramName + ") using " + getCommandStrategy("")
				+ " .");

		// wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			try {
				MaudeIOSyncrhonizationMonitor.monitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException(
						"Java-MaudeShell syncrhonization interrupted.", e);
			}
		}

		if (sobserver.isNewOutputAvailable()) {
			currentOutput = sobserver.getLastestOutput();
			return filterOutput(currentOutput);
		} else {
			throw new MaudeRuntimeException(
					"Error reported by Maude's runtime:"
							+ errObserver.getLastError());
		}

	}

	public String quiescenseStep() throws MaudeRuntimeException,
			MaudeOutputSynchronizationException {
		String temp = this.commandSequence + this.QUIESCENCE;

		ss.sendInput("srew compile(" + plexilProgramName + "Env,"
				+ plexilProgramName + ") using " + getCommandStrategy(temp)
				+ " .");

		quiescenceMode = true;
		// wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			try {
				MaudeIOSyncrhonizationMonitor.monitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException(
						"Java-MaudeShell syncrhonization interrupted.", e);
			}
		}

		if (sobserver.isNewOutputAvailable()) {
			currentOutput = sobserver.getLastestOutput();
			this.commandSequence = temp;
			return filterOutput(currentOutput);
		} else {
			throw new MaudeRuntimeException(
					"Error reported by Maude's runtime:"
							+ errObserver.getLastError());
		}
	}

	public String macroStep() throws MaudeRuntimeException,
			MaudeOutputSynchronizationException {

		String command;
		currentMacroStep++;
		
		String temp = this.commandSequence + this.UPDATE;
		//String temp = this.commandSequence + this.MACRO;
		// if (currentMacroStep==0){
		// command="srew compile("+plexilProgramName+"Env,"+plexilProgramName+") using micro ! ; macro ; micro ! .";
		// }
		// else{
		ss.sendInput("srew compile(" + plexilProgramName + "Env,"
				+ plexilProgramName + ") using " + getCommandStrategy(temp)
				+ " .");
		// }

		// ss.sendInput(command);

		// wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			try {
				MaudeIOSyncrhonizationMonitor.monitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException(
						"Java-MaudeShell syncrhonization interrupted.", e);
			}
		}

		if (sobserver.isNewOutputAvailable()) {
			currentOutput = sobserver.getLastestOutput();
			this.commandSequence = temp;
			return filterOutput(currentOutput);
		} else {
			throw new MaudeRuntimeException(
					"Error reported by Maude's runtime:"
							+ errObserver.getLastError());
		}

	}

	public String executeStep() throws MaudeRuntimeException,
			MaudeOutputSynchronizationException {
		// ss.sendInput("srew compile("+plexilProgramName+"Env,"+plexilProgramName+") using (micro ! ; macro) ! .");
		String temp = this.commandSequence + this.EXECUTE;
		ss.sendInput("srew compile(" + plexilProgramName + "Env,"
				+ plexilProgramName + ") using " + getCommandStrategy(temp)
				+ " .");

		// wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			try {
				MaudeIOSyncrhonizationMonitor.monitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException(
						"Java-MaudeShell syncrhonization interrupted.", e);
			}
		}

		if (sobserver.isNewOutputAvailable()) {
			currentOutput = sobserver.getLastestOutput();
			this.commandSequence = temp;
			return filterOutput(currentOutput);
		} else {
			throw new MaudeRuntimeException(
					"Error reported by Maude's runtime:"
							+ errObserver.getLastError());
		}
	}

	public String microStep() throws MaudeRuntimeException,
			MaudeOutputSynchronizationException {

		// if (quiescenceMode){
		//
		// return EMPTY_RESULT;
		// }
		//
		// currentMicroStep++;
		//
		// if (currentMicroStep==0){
		// ss.sendInput("srew compile("+plexilProgramName+"Env,"+plexilProgramName+") using idle .");
		// }
		// else{
		// ss.sendInput("srew compile("+plexilProgramName+"Env,"+plexilProgramName+") using "+createMicroStepStrategy(currentMicroStep));
		// }
		String temp = this.commandSequence + this.MICRO;
		ss.sendInput("srew compile(" + plexilProgramName + "Env,"
				+ plexilProgramName + ") using " + getCommandStrategy(temp)
				+ " .");

		// wait for response
		synchronized (MaudeIOSyncrhonizationMonitor.monitor) {
			try {
				MaudeIOSyncrhonizationMonitor.monitor.wait();
			} catch (InterruptedException e) {
				throw new MaudeOutputSynchronizationException(
						"Java-MaudeShell syncrhonization interrupted.", e);
			}
		}

		if (sobserver.isNewOutputAvailable()) {
			currentOutput = sobserver.getLastestOutput();
			if (currentOutput.equals(EMPTY_RESULT))
				currentMicroStep--;
			this.commandSequence = temp;

			return filterOutput(currentOutput);
		} else {
			throw new MaudeRuntimeException(
					"Error reported by Maude's runtime:"
							+ errObserver.getLastError());
		}

	}

//	private static String createMicroStepStrategy(int step) {
//		StringBuffer s = new StringBuffer("micro ");
//
//		for (int i = 0; i < step - 1; i++) {
//			s.append("; micro ");
//		}
//		s.append(".");
//		return s.toString();
//	}
//
//	private static String createMacroStepStrategy(int step) {
//		StringBuffer s = new StringBuffer("micro ! ; macro ");
//
//		for (int i = 0; i < step; i++) {
//			s.append("; micro ! ; macro ");
//		}
//		s.append(".");
//		return s.toString();
//	}
//
//	private static String createMacroStepStrategy2(int step) {
//		StringBuffer s = new StringBuffer("micro ! ; macro ");
//
//		for (int i = 0; i < step; i++) {
//			s.append("; micro ! ; macro ");
//		}
//		s.append("; micro ! ");
//		s.append(".");
//		return s.toString();
//	}

	public String getCurrentOutput() {
		return currentOutput;
	}

	private static String filterOutput(String out) {
		if (out.contains("{"))
			return out.substring(out.indexOf("{")).trim();
		else {
			return EMPTY_RESULT;
		}
	}

	public void decreaseCurrentStep() {
		currentMicroStep--;
	}

	public void increaseCurrentStep() {
		currentMicroStep++;
	}

	public int getCurrentStep() {
		return currentMicroStep;
	}

	private String getCommand(String name) {
		String r = null;
		if (name.equals("")) {
			r = "idle ";
		} else if (name.equalsIgnoreCase(this.MICRO)) {
			r = " micro ";
		} else if (name.equalsIgnoreCase(this.MACRO)) {
			r = " macro ; micro ! ";
		} else if (name.equalsIgnoreCase(this.QUIESCENCE)) {
			r = " micro ! ";
		} else if (name.equalsIgnoreCase(this.UPDATE)) {
			r = " micro ! ; macro ";
		} else if (name.equalsIgnoreCase(this.EXECUTE)) {
			r = " micro! ; (macro ; micro !) ! ";
		}
		return r;
	}

	private String getCommandStrategy(String sequence) {
		StringBuffer sb = new StringBuffer("");
		String temp = new String(sequence);
		while (temp.length() > 0) {
			String temp2 = temp.substring(0, 1);
			temp = temp.substring(1);
			sb.append(getCommand(temp2) + " ; ");
		}
		sb.append(getCommand(""));

		return sb.toString();

	}

	public void backtrackCommand() {
		if (commandSequence.length() > 0) {
			commandSequence = commandSequence.substring(0,
					commandSequence.length() - 1);
		}
	}

    public String resetSteps() throws MaudeOutputSynchronizationException, MaudeRuntimeException 
	{
		quiescenceMode = false;
		currentMicroStep = -1;
		currentMacroStep = -1;
		commandSequence = "";
		return EMPTY_RESULT;

/*
		String prevCommand = EMPTY_RESULT;
		String firstCommand = EMPTY_RESULT;
			do {
				firstCommand = prevCommand;
				prevCommand = previousStep();
			} while (!(prevCommand.equals(EMPTY_RESULT)));
		return firstCommand;
*/
	}
}
