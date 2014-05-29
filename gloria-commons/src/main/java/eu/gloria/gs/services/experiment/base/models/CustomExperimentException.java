package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class CustomExperimentException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomExperimentException() {
		super(new Action());
	}
	
	public CustomExperimentException(Action action) {
		super(action);
	}
	
	public CustomExperimentException(String cause) {
		super(new Action());
		
		this.getAction().put("cause", cause);
	}
	
}
