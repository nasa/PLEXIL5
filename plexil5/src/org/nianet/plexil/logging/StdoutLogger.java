package org.nianet.plexil.logging;

public class StdoutLogger implements Logger {

	@Override
	public void debugLog(String msg) {
		System.out.println("[DEBUG] "+msg);

	}

	@Override
	public void errorLog(String msg) {
		System.out.println("[ERROR] "+msg);

	}

	@Override
	public void fatalLog(String msg) {
		System.out.println("[FATAL] "+msg);

	}

	@Override
	public void infoLog(String msg) {
		System.out.println("[INFO] "+msg);

	}

	@Override
	public void warningLog(String msg) {
		System.out.println("[WARNING] "+msg);

	}

}
