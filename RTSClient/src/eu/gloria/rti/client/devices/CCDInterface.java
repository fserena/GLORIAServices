package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDState;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;

public interface CCDInterface {

	public void setExposureTime(double value) throws TeleoperationException;

	public double getExposureTime() throws TeleoperationException;

	public void setAutoExposure(boolean mode) throws TeleoperationException;

	public boolean getAutoExposure() throws TeleoperationException;

	public void setContrast(long value) throws TeleoperationException;

	public long getContrast() throws TeleoperationException;

	public void setBrightness(long value) throws TeleoperationException;

	public long getBrightness() throws TeleoperationException;

	public void setGain(long value) throws TeleoperationException;

	public long getGain() throws TeleoperationException;

	public void setAutoGain(boolean mode) throws TeleoperationException;

	public boolean getAutoGain() throws TeleoperationException;

	public String startExposure() throws TeleoperationException;

	public String startContinueMode() throws TeleoperationException;

	public void stopContinueMode() throws TeleoperationException;

	public String getImageURL(String id, ImageExtensionFormat format) throws TeleoperationException;

	public CCDState getState() throws TeleoperationException;

}
