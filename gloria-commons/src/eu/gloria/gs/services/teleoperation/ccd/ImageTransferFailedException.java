package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public class ImageTransferFailedException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public ImageTransferFailedException(String camera, String id) {
		super(new LogAction());

		LogAction action = this.getAction();

		action.put("camera", camera);
		action.put("id", id);
	}

	public ImageTransferFailedException() {
		super();
	}
}
