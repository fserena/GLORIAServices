package eu.gloria.gs.services.teleoperation.dome;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class DomeTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public DomeTeleoperationException(LogAction action) {
		super(action);
	}

	public DomeTeleoperationException(String cause) {
		super(new LogAction());

		this.getAction().put("cause", cause);
	}
	
	public DomeTeleoperationException() {
		super();
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
