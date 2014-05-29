package eu.gloria.gs.services.repository.user;

import eu.gloria.gs.services.log.action.ActionException;

public class InvalidUserException extends ActionException {

	public InvalidUserException(String user) {
		super("invalid user");
		this.getAction().put("user", user);
	}

	public InvalidUserException() {
		super("invalid user");
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
