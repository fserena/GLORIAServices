package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public interface WindSensorInterface {

	public double getWindSpeed() throws TeleoperationException;
}
