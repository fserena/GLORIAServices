package eu.gloria.gs.services.teleoperation.weather;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;

public class WeatherTeleoperationException extends RTSTeleoperationException {

	/**
	 * 
	 */
	public WeatherTeleoperationException(LogAction action) {
		super(action);
	}

	public WeatherTeleoperationException(String cause) {
		super(new LogAction());

		this.getAction().put("cause", cause);
	}
	
	public WeatherTeleoperationException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
