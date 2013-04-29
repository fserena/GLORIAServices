package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;

public interface DomeInterface {

	public void open() throws TeleoperationException;

	public void close() throws TeleoperationException;

	public void park() throws TeleoperationException;

	public double getAzimuth() throws TeleoperationException;

	public boolean isTrackingEnabled() throws TeleoperationException;

	public void setTracking(boolean mode) throws TeleoperationException;

	public DomeOpeningState getOpeningStatus() throws TeleoperationException;
}
