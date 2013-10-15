package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public interface RHSensorInterface {

	public double getRelativeHumidity() throws TeleoperationException;

}
