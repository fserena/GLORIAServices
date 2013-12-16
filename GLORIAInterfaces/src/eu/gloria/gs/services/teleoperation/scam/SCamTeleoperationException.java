package eu.gloria.gs.services.teleoperation.scam;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class SCamTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public SCamTeleoperationException(LogAction action) {
		super(action);
	}

	public SCamTeleoperationException(String cause) {
		super(new LogAction());

		this.getAction().put("cause", cause);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
