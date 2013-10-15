package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;


public interface BarometerInterface {

	public double getPressure() throws TeleoperationException;
}
