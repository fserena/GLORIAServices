package eu.gloria.gs.services.repository.user.data.dbservices;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class UserRepositoryAdapterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserRepositoryAdapterException(LogAction action) {
		super(action);
	}
	
}
