package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class ImageTransferFailedException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public ImageTransferFailedException(String message)
	{
		super(message);
	}
}
