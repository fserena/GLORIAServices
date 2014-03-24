package eu.gloria.gs.services.repository.user;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class InvalidUserException extends ActionException {

	/**
	 * 
	 */
	public InvalidUserException(Action action) {
		super(action);
	}

	public InvalidUserException(String cause) {
		super();

		this.getAction().put("cause", cause);
	}

	public InvalidUserException() {
		super();
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
