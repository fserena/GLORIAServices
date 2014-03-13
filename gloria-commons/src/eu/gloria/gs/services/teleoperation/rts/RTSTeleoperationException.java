package eu.gloria.gs.services.teleoperation.rts;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class RTSTeleoperationException extends TeleoperationException {

	/**
	 * 
	 */
	public RTSTeleoperationException(LogAction action) {
		super(action);
	}

	public RTSTeleoperationException() {
		super();
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
