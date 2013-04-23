package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.teleoperation.base.RTSException;

public class ImageNotAvailableException extends RTSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public ImageNotAvailableException(String message)
	{
		super(message);
	}
}
