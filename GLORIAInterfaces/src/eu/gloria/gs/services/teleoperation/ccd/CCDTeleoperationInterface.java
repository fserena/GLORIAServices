package eu.gloria.gs.services.teleoperation.ccd;

import javax.jws.WebService;

@WebService(name = "CCDTeleoperationInterface", targetNamespace = "http://ccd.teleoperation.services.gs.gloria.eu/")
public interface CCDTeleoperationInterface {

	public String getImageURL(String rt, String ccd, String imageId, ImageExtensionFormat format)
			throws CCDTeleoperationException;

	public void setExposureTime(String rt, String ccd, double value)
			throws CCDTeleoperationException;

	public double getExposureTime(String rt, String ccd)
			throws CCDTeleoperationException;

	public void setBrightness(String rt, String ccd, long value)
			throws CCDTeleoperationException;

	public long getBrightness(String rt, String ccd)
			throws CCDTeleoperationException;

	public void setContrast(String rt, String ccd, long value)
			throws CCDTeleoperationException;

	public long getContrast(String rt, String ccd)
			throws CCDTeleoperationException;

	public void setGain(String rt, String ccd, long value)
			throws CCDTeleoperationException;

	public long getGain(String rt, String ccd) throws CCDTeleoperationException;

	public CCDState getState(String rt, String ccd)
			throws CCDTeleoperationException;

	public String startExposure(String rt, String ccd)
			throws CCDTeleoperationException;

	public String startContinueMode(String rt, String ccd)
			throws CCDTeleoperationException;

	public void stopContinueMode(String rt, String ccd)
			throws CCDTeleoperationException;

}