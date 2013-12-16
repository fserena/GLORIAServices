package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class DuplicateExperimentException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateExperimentException(String experiment) {
		super(new LogAction());

		LogAction action = this.getAction();
		action.put("name", experiment);
	}
}
