package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.scam.SCamState;


public interface SCamInteface {

	public void setExposureTime(double value) throws TeleoperationException;

	public double getExposureTime() throws TeleoperationException;

	public void setContrast(long value) throws TeleoperationException;

	public long getContrast() throws TeleoperationException;

	public void setBrightness(long value) throws TeleoperationException;

	public long getBrightness() throws TeleoperationException;

	public void setGain(long value) throws TeleoperationException;

	public long getGain() throws TeleoperationException;

	public String getImageURL() throws TeleoperationException;

	public SCamState getState() throws TeleoperationException;

}
