package eu.gloria.gs.services.repository.image.data;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;


public class ImageDatabaseException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageDatabaseException() {
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("cause", "db error");
	}
	
	public ImageDatabaseException(LogAction action) {
		super(action);		
	}
	
}
