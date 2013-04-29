package eu.gloria.gs.services.teleoperation.ccd;

import javax.jws.WebService;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

@WebService(name = "CCDTeleoperationInterface", targetNamespace = "http://ccd.teleoperation.services.gs.gloria.eu/")
public interface CCDTeleoperationInterface {

	public String getImageURL(String rt, String ccd, String imageId, ImageExtensionFormat format)
			throws TeleoperationException;

	public void setExposureTime(String rt, String ccd, double value)
			throws TeleoperationException;

	public double getExposureTime(String rt, String ccd)
			throws TeleoperationException;

	public void setBrightness(String rt, String ccd, long value)
			throws TeleoperationException;

	public long getBrightness(String rt, String ccd)
			throws TeleoperationException;

	public void setContrast(String rt, String ccd, long value)
			throws TeleoperationException;

	public long getContrast(String rt, String ccd)
			throws TeleoperationException;

	public void setGain(String rt, String ccd, long value)
			throws TeleoperationException;

	public long getGain(String rt, String ccd) throws TeleoperationException;

	public CCDState getState(String rt, String ccd)
			throws TeleoperationException;

	public String startExposure(String rt, String ccd)
			throws TeleoperationException;

	public String startContinueMode(String rt, String ccd)
			throws TeleoperationException;

	public void stopContinueMode(String rt, String ccd)
			throws TeleoperationException;

}