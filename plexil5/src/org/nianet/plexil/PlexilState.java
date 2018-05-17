package org.nianet.plexil;



/**
 * @author Camilo Rocha
 * @version 0.1
 */
public class PlexilState {
   
	private static final PlexilState instance = new PlexilState();
	
	private Contexts contexts;
	
	private Context context;
	
	private Configuration configuration;
	
	private Trace trace;
	
	private ActionSet actions;
	
	private PlexilState(){
		
	}
	
	public static PlexilState getInstance(Contexts contexts, Context context, Configuration configuration, ActionSet actions, Trace trace){
		instance.setActions(actions);
		instance.setConfiguration(configuration);
		instance.setContext(context);
		instance.setContexts(contexts);
		instance.setTrace(trace);
		return instance;
	}

	public void setContexts(Contexts contexts) {
		this.contexts = contexts;
	}

	public Contexts getContexts() {
		return contexts;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setActions(ActionSet actions) {
		this.actions = actions;
	}

	public ActionSet getActions() {
		return actions;
	}
}
