package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class NoSuchExperimentException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5066534085480189421L;

	public NoSuchExperimentException(String experiment) {
		super();

		Action action = this.getAction();
		action.put("experiment", experiment);
	}

	public NoSuchExperimentException() {
		super();
	}
}
