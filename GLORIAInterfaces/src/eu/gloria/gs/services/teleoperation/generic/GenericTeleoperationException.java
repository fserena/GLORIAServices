package eu.gloria.gs.services.teleoperation.generic;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class GenericTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public GenericTeleoperationException(String operation, Exception e)
	{
		super(new LogAction());
		LogAction action = this.getAction();
		
		action.put("operation", operation);
		action.put("error", e.getClass().getSimpleName());
		action.put("message", e.getMessage());
	}
	
	public GenericTeleoperationException(LogAction action)
	{
		super(action);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
