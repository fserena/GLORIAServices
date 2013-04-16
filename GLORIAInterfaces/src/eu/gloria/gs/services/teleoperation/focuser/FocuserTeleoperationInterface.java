package eu.gloria.gs.services.teleoperation.focuser;

import javax.jws.WebService;

@WebService(name = "FocuserTeleoperationInterface", targetNamespace = "http://focuser.teleoperation.services.gs.gloria.eu/")
public interface FocuserTeleoperationInterface {

	public long getPosition(String rt, String focuser)
			throws FocuserTeleoperationException;

	public void moveAbsolute(String rt, String focuser, long position)
			throws FocuserTeleoperationException;

	public void moveRelative(String rt, String focuser, long steps)
			throws FocuserTeleoperationException;

}
