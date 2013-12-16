package eu.gloria.gs.services.teleoperation.mount;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class MountTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public MountTeleoperationException(LogAction action) {
		super(action);
	}

	public MountTeleoperationException(String cause) {
		super(new LogAction());

		this.getAction().put("cause", cause);
	}
	
	public MountTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
