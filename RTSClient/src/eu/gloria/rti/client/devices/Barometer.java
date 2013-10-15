package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class Barometer extends DeviceHandler implements BarometerInterface {

	private String barometer;

	public Barometer(RTSHandler rts, String bar) throws TeleoperationException {

		super(rts);
		this.barometer = bar;
	}

	/* (non-Javadoc)
	 * @see eu.gloria.rti.client.devices.BarometerInterface#getPressure()
	 */
	@Override
	public double getPressure() throws TeleoperationException {
		return rts.getPressure(barometer);
	}
}
