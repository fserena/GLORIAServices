package eu.gloria.gs.services.experiment.base.operations;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class ExperimentOperationException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public ExperimentOperationException(LogAction action) {
		super(action);
	}
	
	public ExperimentOperationException(String name, String cause) {
		super(new LogAction());
		
		this.getAction().put("name", name);
		this.getAction().put("cause", cause);
	}

}
