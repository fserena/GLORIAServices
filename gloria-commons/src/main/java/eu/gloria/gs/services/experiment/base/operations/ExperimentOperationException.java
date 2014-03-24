package eu.gloria.gs.services.experiment.base.operations;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ExperimentOperationException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public ExperimentOperationException(Action action) {
		super(action);
	}

	public ExperimentOperationException() {
		super();
	}

	public ExperimentOperationException(String name, String cause) {
		super(new Action());

		this.getAction().put("name", name);
		this.getAction().put("cause", cause);
	}

}
