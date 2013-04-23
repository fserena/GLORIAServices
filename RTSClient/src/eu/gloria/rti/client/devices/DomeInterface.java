package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;

public interface DomeInterface {

	public void open() throws RTSException;

	public void close() throws RTSException;

	public void park() throws RTSException;

	public double getAzimuth() throws RTSException;

	public boolean isTrackingEnabled() throws RTSException;

	public void setTracking(boolean mode) throws RTSException;

	public DomeOpeningState getOpeningStatus() throws RTSException;
}
