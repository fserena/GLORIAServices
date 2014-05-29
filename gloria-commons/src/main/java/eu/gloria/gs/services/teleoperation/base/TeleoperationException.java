package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public abstract class TeleoperationException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public TeleoperationException(Action action) {
		super(action);
	}

	public TeleoperationException() {
		super();
	}
	
	public TeleoperationException(String cause) {
		super(cause);
	}
}
