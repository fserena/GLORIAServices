package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.Action;

public class IncorrectDeviceTypeException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public IncorrectDeviceTypeException(String type) {
		super(new Action());

		Action action = this.getAction();

		action.put("type", type);		
	}
	
	public IncorrectDeviceTypeException() {
		super();
	}
	
}
