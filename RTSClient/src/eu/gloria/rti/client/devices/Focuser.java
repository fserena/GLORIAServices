package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.RTSHandler;

public class Focuser extends DeviceHandler implements FocuserInterface {

	private RTSHandler rts;
	private String focuser;

	public Focuser(RTSHandler rts, String focuser) throws RTSException {

		this.rts = rts;
		this.focuser = focuser;
	}

	@Override
	public void moveAbsolute(long position) throws RTSException {
		rts.absoluteFocuserMove(this.focuser, position);
	}

	@Override
	public void moveRelative(long steps) throws RTSException {
		rts.relativeFocuserMove(this.focuser, steps);
	}

	@Override
	public long getPosition() throws RTSException {
		return rts.getFocuserAbsolutePosition(this.focuser);
	}
}
