package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class NoSuchParameterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public NoSuchParameterException(String name) {
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("name", name);
	}

	public NoSuchParameterException(LogAction action) {
		super(action);
	}
	
	public NoSuchParameterException() {
		super();
	}
}
