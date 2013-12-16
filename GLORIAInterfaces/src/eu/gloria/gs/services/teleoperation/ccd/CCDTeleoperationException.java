package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class CCDTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public CCDTeleoperationException(LogAction action)
	{
		super(action);
	}
	
	public CCDTeleoperationException(String cause)
	{
		super(new LogAction());
		
		this.getAction().put("cause", cause);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
