package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class TeleoperationException extends ActionException {

	/**
	 * 
	 */
	public TeleoperationException(LogAction action)
	{
		super(action);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
