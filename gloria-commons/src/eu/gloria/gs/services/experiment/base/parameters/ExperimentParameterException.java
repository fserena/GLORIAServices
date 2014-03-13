package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class ExperimentParameterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;
	
	public ExperimentParameterException(LogAction action) {
		super(action);
	}
	
	public ExperimentParameterException() {
		super();
	}

	public ExperimentParameterException(String name, String cause) {
		super(new LogAction());
		
		this.getAction().put("name", name);
		this.getAction().put("cause", cause);
	}

}
