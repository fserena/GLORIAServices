package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.Action;

public class DeviceOperationFailedException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public DeviceOperationFailedException(String name, String type,
			String operation, String message) {
		super(message);
		Action action = this.getAction();

		action.put("operation", operation);
		action.put("device", name);
		action.put("type", type);
	}

	public DeviceOperationFailedException() {
		super();
	}
}
