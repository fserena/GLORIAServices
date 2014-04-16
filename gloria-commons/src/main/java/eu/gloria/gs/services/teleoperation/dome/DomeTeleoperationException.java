package eu.gloria.gs.services.teleoperation.dome;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class DomeTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public DomeTeleoperationException(Action action) {
		super(action);
	}

	public DomeTeleoperationException(String message) {
		super(message);
	}
	
	public DomeTeleoperationException() {
		super();
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
