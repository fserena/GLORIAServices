package eu.gloria.gs.services.teleoperation.focuser;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class NotAbsoluteFocuserException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public NotAbsoluteFocuserException(String focuser)
	{
		super(new Action());

		Action action = this.getAction();

		action.put("focuser", focuser);		
	}
	
	public NotAbsoluteFocuserException() {
		super();
	}
}
