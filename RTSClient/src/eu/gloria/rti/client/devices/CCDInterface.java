package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.ccd.CCDState;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.rti.client.RTSException;

public interface CCDInterface {

	public void setExposureTime(double value) throws RTSException;

	public double getExposureTime() throws RTSException;

	public void setAutoExposure(boolean mode) throws RTSException;

	public boolean getAutoExposure() throws RTSException;

	public void setContrast(long value) throws RTSException;

	public long getContrast() throws RTSException;

	public void setBrightness(long value) throws RTSException;

	public long getBrightness() throws RTSException;

	public void setGain(long value) throws RTSException;

	public long getGain() throws RTSException;

	public void setAutoGain(boolean mode) throws RTSException;

	public boolean getAutoGain() throws RTSException;

	public String startExposure() throws RTSException;

	public String startContinueMode() throws RTSException;

	public void stopContinueMode() throws RTSException;

	public String getImageURL(String id, ImageExtensionFormat format) throws RTSException;

	public CCDState getState() throws RTSException;

}
