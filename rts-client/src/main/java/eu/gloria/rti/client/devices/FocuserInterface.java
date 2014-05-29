package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.Range;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public interface FocuserInterface {

	public void moveAbsolute(long position) throws TeleoperationException;

	public void moveRelative(long steps) throws TeleoperationException;

	public long getPosition() throws TeleoperationException;
	
	public Range getRange() throws TeleoperationException;
}

