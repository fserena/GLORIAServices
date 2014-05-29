package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.log.action.ActionException;

public class DuplicateExperimentException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateExperimentException(String experiment) {
		super("duplicate experiment");
		this.getAction().put("experiment", experiment);
	}
	
	public DuplicateExperimentException() {
		super();
	}
}
