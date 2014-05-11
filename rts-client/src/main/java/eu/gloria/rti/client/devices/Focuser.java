package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.Range;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class Focuser extends DeviceHandler implements FocuserInterface {

	private String focuser;

	public Focuser(RTSHandler rts, String focuser) throws TeleoperationException {

		super(rts);
		this.focuser = focuser;
	}

	@Override
	public void moveAbsolute(long position) throws TeleoperationException {
		rts.absoluteFocuserMove(this.focuser, position);
	}

	@Override
	public void moveRelative(long steps) throws TeleoperationException {
		rts.relativeFocuserMove(this.focuser, steps);
	}

	@Override
	public long getPosition() throws TeleoperationException {
		return rts.getFocuserAbsolutePosition(this.focuser);
	}

	@Override
	public Range getRange() throws TeleoperationException {
		return rts.getFocuserRange(this.focuser);
	}
}
