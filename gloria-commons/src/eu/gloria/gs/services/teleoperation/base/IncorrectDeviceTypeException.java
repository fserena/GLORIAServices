package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.LogAction;

public class IncorrectDeviceTypeException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public IncorrectDeviceTypeException(String type) {
		super(new LogAction());

		LogAction action = this.getAction();

		action.put("type", type);		
	}
	
	public IncorrectDeviceTypeException() {
		super();
	}
	
}
