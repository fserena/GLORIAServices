package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class NoSuchExperimentException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5066534085480189421L;

	public NoSuchExperimentException(String experiment) {
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("name", experiment);
	}

	public NoSuchExperimentException(LogAction action) {
		super(action);
	}
	
	public NoSuchExperimentException() {
		super();
	}
}
