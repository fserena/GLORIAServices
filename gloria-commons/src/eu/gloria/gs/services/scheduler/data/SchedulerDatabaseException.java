package eu.gloria.gs.services.scheduler.data;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;


public class SchedulerDatabaseException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchedulerDatabaseException() {
		super(new LogAction());
	}
	
}
