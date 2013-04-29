package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class ImageNotAvailableException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public ImageNotAvailableException(String message)
	{
		super(message);
	}
}
