package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.scam.SCamState;
import eu.gloria.rti.client.RTSException;

public interface SCamInteface {

	public void setExposureTime(double value) throws RTSException;

	public double getExposureTime() throws RTSException;

	public void setContrast(long value) throws RTSException;

	public long getContrast() throws RTSException;

	public void setBrightness(long value) throws RTSException;

	public long getBrightness() throws RTSException;

	public void setGain(long value) throws RTSException;

	public long getGain() throws RTSException;

	public String getImageURL() throws RTSException;

	public SCamState getState() throws RTSException;

}
