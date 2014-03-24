package eu.gloria.gs.services.teleoperation.focuser;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class FocuserTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public FocuserTeleoperationException(Action action) {
		super(action);
	}

	public FocuserTeleoperationException(String cause) {
		super(new Action());

		this.getAction().put("cause", cause);
	}
	
	public FocuserTeleoperationException() {
		super();
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
