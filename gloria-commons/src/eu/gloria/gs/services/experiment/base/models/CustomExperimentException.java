package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class CustomExperimentException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomExperimentException() {
		super(new LogAction());
	}
	
	public CustomExperimentException(LogAction action) {
		super(action);
	}
	
	public CustomExperimentException(String cause) {
		super(new LogAction());
		
		this.getAction().put("cause", cause);
	}
	
}
