package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class InvalidExperimentModelException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidExperimentModelException(Action action) {
		super(action);
	}
	
	public InvalidExperimentModelException(String name, String cause) {
		super(new Action());
		
		this.getAction().put("name", name);
		this.getAction().put("cause", cause);
	}
	
	public InvalidExperimentModelException() {
		super();
	}
	
}
