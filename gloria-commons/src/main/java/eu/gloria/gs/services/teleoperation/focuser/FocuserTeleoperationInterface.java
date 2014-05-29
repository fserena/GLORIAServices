package eu.gloria.gs.services.teleoperation.focuser;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.Range;

@WebService(name = "FocuserTeleoperationInterface", targetNamespace = "http://focuser.teleoperation.services.gs.gloria.eu/")
public interface FocuserTeleoperationInterface {

	public long getPosition(String rt, String focuser)
			throws DeviceOperationFailedException, FocuserTeleoperationException;
	
	public Range getRange(String rt, String focuser)
			throws DeviceOperationFailedException, FocuserTeleoperationException;

	public void moveAbsolute(String rt, String focuser, long position)
			throws DeviceOperationFailedException, FocuserTeleoperationException;

	public void moveRelative(String rt, String focuser, long steps)
			throws DeviceOperationFailedException, FocuserTeleoperationException;

}
