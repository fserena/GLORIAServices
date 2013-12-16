package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public abstract class TeleoperationException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public TeleoperationException(LogAction action) {
		super(action);
	}
}
