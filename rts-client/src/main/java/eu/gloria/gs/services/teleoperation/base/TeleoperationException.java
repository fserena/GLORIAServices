package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class TeleoperationException extends ActionException {

	/**
	 * 
	 */
	public TeleoperationException(Action action) {
		super(action);
	}

	public TeleoperationException(String cause) {
		super(cause);
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
