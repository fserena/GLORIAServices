package eu.gloria.gs.services.teleoperation.fw;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class FilterWheelTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public FilterWheelTeleoperationException(Action action) {
		super(action);
	}

	public FilterWheelTeleoperationException(String message) {
		super(message);
	}
	
	public FilterWheelTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
