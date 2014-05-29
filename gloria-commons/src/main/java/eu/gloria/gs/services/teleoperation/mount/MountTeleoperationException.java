package eu.gloria.gs.services.teleoperation.mount;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class MountTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public MountTeleoperationException(Action action) {
		super(action);
	}

	public MountTeleoperationException(String message) {
		super(message);
	}
	
	public MountTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
