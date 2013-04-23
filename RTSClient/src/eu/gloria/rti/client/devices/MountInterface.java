package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.mount.MountState;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;

public interface MountInterface {

	public void moveNorth() throws RTSException;

	public void moveSouth() throws RTSException;

	public void moveEast() throws RTSException;

	public void moveWest() throws RTSException;

	public void setSlewRate(String rate) throws RTSException;

	public void setTrackingRate(TrackingRate rate) throws RTSException;

	public void setTracking(boolean mode) throws RTSException;

	public void slewToObject(String object) throws RTSException;

	public MountState getState() throws RTSException;

	public void park() throws RTSException;
}
