package eu.gloria.gs.services.teleoperation.generic;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class GenericTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public GenericTeleoperationException(String operation, String whileStr, Exception e) {
		super(e.getMessage());
		Action action = this.getAction();

		action.put("operation", operation);
		action.put("while", whileStr);
		action.put("error", e.getClass().getSimpleName());
	}
	
	public GenericTeleoperationException(String operation, Exception e) {
		super(e.getMessage());
		Action action = this.getAction();

		action.put("operation", operation);
		action.put("error", e.getClass().getSimpleName());
	}

	public GenericTeleoperationException(Action action) {
		super(action);
	}

	public GenericTeleoperationException() {
		super();
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
