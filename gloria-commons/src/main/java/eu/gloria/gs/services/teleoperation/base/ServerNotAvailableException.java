package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.log.action.Action;

public class ServerNotAvailableException extends TeleoperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378992569190922738L;

	public ServerNotAvailableException(String host, String port, String error) {
		super(new Action());

		Action action = this.getAction();
		action.put("host", host);
		action.put("port", port);
		if (error == null) {
			action.put("error", "no handler");
		} else {
			action.put("error", error);
		}
	}
	

	public ServerNotAvailableException() {
		super();
	}
}
