package eu.gloria.gs.services.repository.rt;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class RTRepositoryException extends ActionException {

	/**
	 * 
	 */
	public RTRepositoryException(LogAction action) {
		super(action);
	}

	public RTRepositoryException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
