package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.rti.client.RTSException;

public interface DomeInterface {

	public void open() throws RTSException;

	public void close() throws RTSException;

	public void park() throws RTSException;

	public double getAzimuth() throws RTSException;

	public boolean isTrackingEnabled() throws RTSException;

	public void setTracking(boolean mode) throws RTSException;

	public DomeOpeningState getOpeningStatus() throws RTSException;
}
