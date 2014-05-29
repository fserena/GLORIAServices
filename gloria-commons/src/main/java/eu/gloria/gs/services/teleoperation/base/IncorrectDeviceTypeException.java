package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.Action;

public class IncorrectDeviceTypeException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public IncorrectDeviceTypeException(String name, String type) {
		super();

		Action action = this.getAction();

		action.put("device", name);
		action.put("type", type);
	}

	public IncorrectDeviceTypeException() {
		super();
	}

}
