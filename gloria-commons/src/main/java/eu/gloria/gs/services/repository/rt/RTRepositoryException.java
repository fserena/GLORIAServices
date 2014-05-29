package eu.gloria.gs.services.repository.rt;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class RTRepositoryException extends ActionException {

	/**
	 * 
	 */
	public RTRepositoryException(Action action) {
		super(action);
	}

	public RTRepositoryException() {
		super();
	}
	
	public RTRepositoryException(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
