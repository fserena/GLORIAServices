package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class TempSensor extends DeviceHandler implements TempSensorInterface {

	private String tempSensor;

	public TempSensor(RTSHandler rts, String temp) throws TeleoperationException {

		super(rts);
		this.tempSensor = temp;
	}

	/* (non-Javadoc)
	 * @see eu.gloria.rti.client.devices.BarometerInterface#getPressure()
	 */
	@Override
	public double getTemperature() throws TeleoperationException {
		return rts.getTemperature(tempSensor);
	}

	@Override
	public boolean isOnAlarm() throws TeleoperationException {
		return rts.isTemperatureSensorInAlarm(tempSensor);
	}
}
