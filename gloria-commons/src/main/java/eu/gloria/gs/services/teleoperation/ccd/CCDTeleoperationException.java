package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class CCDTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public CCDTeleoperationException(Action action)
	{
		super(action);
	}
	
	public CCDTeleoperationException() {
		super();
	}
	
	public CCDTeleoperationException(String message)
	{
		super(message);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
