package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;


public interface TempSensorInterface {

	public double getTemperature() throws TeleoperationException;
	public boolean isOnAlarm() throws TeleoperationException;
}
