package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class RHSensor extends DeviceHandler implements RHSensorInterface {

	private String rhSensor;

	public RHSensor(RTSHandler rts, String rh) throws TeleoperationException {

		super(rts);
		this.rhSensor = rh;
	}

	/* (non-Javadoc)
	 * @see eu.gloria.rti.client.devices.RHSensorInterface#getRelativeHumidity()
	 */
	@Override
	public double getRelativeHumidity() throws TeleoperationException {
		return rts.getRelativeHumidity(rhSensor);
	}
	
}
