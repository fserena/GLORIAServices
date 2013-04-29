package eu.gloria.gs.services.teleoperation.focuser;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

@WebService(name = "FocuserTeleoperationInterface", targetNamespace = "http://focuser.teleoperation.services.gs.gloria.eu/")
public interface FocuserTeleoperationInterface {

	public long getPosition(String rt, String focuser)
			throws TeleoperationException;

	public void moveAbsolute(String rt, String focuser, long position)
			throws TeleoperationException;

	public void moveRelative(String rt, String focuser, long steps)
			throws TeleoperationException;

}
