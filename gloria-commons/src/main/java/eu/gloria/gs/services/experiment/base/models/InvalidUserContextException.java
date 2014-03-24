package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class InvalidUserContextException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUserContextException(Action action) {
		super(action);
	}
	
	public InvalidUserContextException() {
		super();
	}
	
	public InvalidUserContextException(String user, int rid) {
		super(new Action());
		
		Action action = this.getAction();
		action.put("user", user);
		action.put("rid", rid);
	}
	
}
