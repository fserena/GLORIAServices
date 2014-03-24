package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.Action;

public class ExperimentParameterArgumentException extends ExperimentParameterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public ExperimentParameterArgumentException(Action action) {
		super(action);
	}
	
	public ExperimentParameterArgumentException(String name, String arg, String cause) {
		super(new Action());
		
		this.getAction().put("name", name);
		this.getAction().put("arg", arg);
		this.getAction().put("cause", cause);
	}

}
