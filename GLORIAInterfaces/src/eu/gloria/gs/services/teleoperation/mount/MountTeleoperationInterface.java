package eu.gloria.gs.services.teleoperation.mount;

import javax.jws.WebService;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;

@WebService(name = "MountTeleoperationInterface", targetNamespace = "http://mount.teleoperation.services.gs.gloria.eu/")
public interface MountTeleoperationInterface {

	public MountState getState(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void moveNorth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void moveSouth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void moveEast(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void moveWest(String rt, String mountF)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void setSlewRate(String rt, String mount, String rate)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void setTracking(String rt, String mount, boolean mode)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void slewToObject(String rt, String mount, String object)
			throws DeviceOperationFailedException, MountTeleoperationException;

	public void park(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException;
}
