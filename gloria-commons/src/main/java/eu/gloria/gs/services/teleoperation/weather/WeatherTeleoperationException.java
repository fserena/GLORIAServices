package eu.gloria.gs.services.teleoperation.weather;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class WeatherTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public WeatherTeleoperationException(Action action) {
		super(action);
	}

	public WeatherTeleoperationException(String message) {
		super(message);
	}
	
	public WeatherTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
