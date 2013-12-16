package eu.gloria.gs.services.scheduler;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class SchedulerException extends ActionException {

	/**
	 * 
	 */
	public SchedulerException() {
		super();
	}

	public SchedulerException(LogAction action) {
		super(action);
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
