package eu.gloria.gs.services.repository.user;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class UserRepositoryException extends ActionException {

	/**
	 * 
	 */
	public UserRepositoryException(LogAction action) {
		super(action);
	}

	public UserRepositoryException(String cause) {
		super();

		this.getAction().put("cause", cause);
	}

	public UserRepositoryException() {
		super();
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
