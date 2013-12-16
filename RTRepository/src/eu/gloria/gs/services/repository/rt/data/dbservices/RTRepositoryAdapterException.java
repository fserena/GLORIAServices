package eu.gloria.gs.services.repository.rt.data.dbservices;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class RTRepositoryAdapterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RTRepositoryAdapterException(LogAction action) {
		super(action);
	}
	
	public RTRepositoryAdapterException(String cause) {
		super(new LogAction());
		
		this.getAction().put("cause", cause);
	}
	
}
