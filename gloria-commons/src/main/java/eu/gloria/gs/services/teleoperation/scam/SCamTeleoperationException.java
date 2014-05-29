package eu.gloria.gs.services.teleoperation.scam;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class SCamTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public SCamTeleoperationException(Action action) {
		super(action);
	}

	public SCamTeleoperationException(String message) {
		super(message);
	}
	
	public SCamTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
