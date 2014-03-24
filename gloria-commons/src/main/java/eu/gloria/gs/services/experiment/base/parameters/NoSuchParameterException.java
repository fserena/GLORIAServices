package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class NoSuchParameterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public NoSuchParameterException(String name) {
		super(new Action());
		
		Action action = this.getAction();
		action.put("name", name);
	}

	public NoSuchParameterException(Action action) {
		super(action);
	}
	
	public NoSuchParameterException() {
		super();
	}
}
