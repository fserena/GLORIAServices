package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class ImageURLNotAvailableException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public ImageURLNotAvailableException(Action action)
	{
		super(action);
	}
	
	public ImageURLNotAvailableException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
