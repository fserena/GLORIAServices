package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.LogAction;

public class DeviceNotAvailableException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public DeviceNotAvailableException(String name, String type, String message)
	{
		super(new LogAction());
		
		LogAction action = this.getAction();
		
		action.put("name", name);
		action.put("type", type);
		action.put("message", message);
	}
	
	public DeviceNotAvailableException() {
		super();
	}
}
