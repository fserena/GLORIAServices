package eu.gloria.gs.services.teleoperation.fw;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class FilterWheelTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public FilterWheelTeleoperationException(LogAction action) {
		super(action);
	}

	public FilterWheelTeleoperationException(String cause) {
		super(new LogAction());

		this.getAction().put("cause", cause);
	}
	
	public FilterWheelTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
