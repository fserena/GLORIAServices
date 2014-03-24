package eu.gloria.gs.services.scheduler.data;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;


public class ScheduleNotFoundException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScheduleNotFoundException(Action action) {
		super(action);
	}
	
}
