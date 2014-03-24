package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class WindSensor extends DeviceHandler implements WindSensorInterface {

	private String windSensor;

	public WindSensor(RTSHandler rts, String wind)
			throws TeleoperationException {

		super(rts);
		this.windSensor = wind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.rti.client.devices.WindSensorInterface#getWindSpeed()
	 */
	@Override
	public double getWindSpeed() throws TeleoperationException {
		return rts.getWindSpeed(windSensor);
	}

	@Override
	public boolean isOnAlarm() throws TeleoperationException {
		return rts.isWindSensorInAlarm(windSensor);
	}
}
