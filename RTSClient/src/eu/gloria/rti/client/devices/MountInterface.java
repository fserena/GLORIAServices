package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountState;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;

public interface MountInterface {

	public void moveNorth() throws TeleoperationException;

	public void moveSouth() throws TeleoperationException;

	public void moveEast() throws TeleoperationException;

	public void moveWest() throws TeleoperationException;

	public void setSlewRate(String rate) throws TeleoperationException;

	public void setTrackingRate(TrackingRate rate) throws TeleoperationException;

	public void setTracking(boolean mode) throws TeleoperationException;

	public void slewToObject(String object) throws TeleoperationException;

	public MountState getState() throws TeleoperationException;

	public void park() throws TeleoperationException;
}
