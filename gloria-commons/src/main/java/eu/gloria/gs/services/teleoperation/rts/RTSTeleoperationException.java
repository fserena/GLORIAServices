package eu.gloria.gs.services.teleoperation.rts;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class RTSTeleoperationException extends TeleoperationException {

	/**
	 * 
	 */
	public RTSTeleoperationException(Action action) {
		super(action);
	}

	public RTSTeleoperationException() {
		super();
	}
	
	public RTSTeleoperationException(String cause) {
		super(cause);
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
