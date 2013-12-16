package eu.gloria.gs.services.teleoperation.focuser;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class FocuserTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public FocuserTeleoperationException(LogAction action) {
		super(action);
	}

	public FocuserTeleoperationException(String cause) {
		super(new LogAction());

		this.getAction().put("cause", cause);
	}
	
	public FocuserTeleoperationException() {
		super();
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
