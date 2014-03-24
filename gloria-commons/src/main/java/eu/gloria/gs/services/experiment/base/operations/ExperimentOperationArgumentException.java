package eu.gloria.gs.services.experiment.base.operations;

import eu.gloria.gs.services.log.action.Action;

public class ExperimentOperationArgumentException extends ExperimentOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public ExperimentOperationArgumentException(Action action) {
		super(action);
	}
	
	public ExperimentOperationArgumentException(String name, String arg, String cause) {
		super(new Action());
		
		this.getAction().put("name", name);
		this.getAction().put("arg", arg);
		this.getAction().put("cause", cause);
	}

}
