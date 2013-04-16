package eu.gloria.rti.client.devices;

import eu.gloria.rti.client.RTSException;

public interface FocuserInterface {

	public void moveAbsolute(long position) throws RTSException;

	public void moveRelative(long steps) throws RTSException;

	public long getPosition() throws RTSException;
}
