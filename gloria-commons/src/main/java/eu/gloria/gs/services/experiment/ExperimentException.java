package eu.gloria.gs.services.experiment;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ExperimentException extends ActionException {

	/**
	 * 
	 */
	public ExperimentException(Action action) {
		super(action);
	}

	public ExperimentException() {
		super();
	}

	public ExperimentException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
