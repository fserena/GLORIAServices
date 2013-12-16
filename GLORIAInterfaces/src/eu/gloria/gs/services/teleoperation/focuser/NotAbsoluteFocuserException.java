package eu.gloria.gs.services.teleoperation.focuser;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class NotAbsoluteFocuserException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public NotAbsoluteFocuserException(String focuser)
	{
		super(new LogAction());

		LogAction action = this.getAction();

		action.put("focuser", focuser);		
	}
}
