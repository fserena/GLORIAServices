package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ExperimentParameterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;
	
	public ExperimentParameterException(Action action) {
		super(action);
	}
	
	public ExperimentParameterException() {
		super();
	}

	public ExperimentParameterException(String name, String message) {
		super(message);
		this.getAction().put("parameter", name);
	}

}
