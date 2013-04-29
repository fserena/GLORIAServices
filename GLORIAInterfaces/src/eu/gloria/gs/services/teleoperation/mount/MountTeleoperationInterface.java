package eu.gloria.gs.services.teleoperation.mount;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

@WebService(name = "MountTeleoperationInterface", targetNamespace = "http://mount.teleoperation.services.gs.gloria.eu/")
public interface MountTeleoperationInterface {

	public MountState getState(String rt, String mount)
			throws TeleoperationException;

	public void moveNorth(String rt, String mount)
			throws TeleoperationException;

	public void moveSouth(String rt, String mount)
			throws TeleoperationException;

	public void moveEast(String rt, String mount)
			throws TeleoperationException;

	public void moveWest(String rt, String mountF)
			throws TeleoperationException;

	public void setSlewRate(String rt, String mount, String rate)
			throws TeleoperationException;

	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws TeleoperationException;

	public void setTracking(String rt, String mount, boolean mode)
			throws TeleoperationException;

	public void slewToObject(String rt, String mount, String object)
			throws TeleoperationException;

	public void park(String rt, String mount)
			throws TeleoperationException;
}
