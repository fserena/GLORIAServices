package eu.gloria.gs.services.teleoperation.ccd;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.Range;

@WebService(name = "CCDTeleoperationInterface", targetNamespace = "http://ccd.teleoperation.services.gs.gloria.eu/")
public interface CCDTeleoperationInterface {

	public String getImageURL(String rt, String ccd, String imageId,
			ImageExtensionFormat format) throws ImageNotAvailableException,
			CCDTeleoperationException;

	public void setExposureTime(String rt, String ccd, double value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public double getExposureTime(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public boolean gainIsModifiable(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public boolean gammaIsModifiable(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public Range getExposureRange(String rt, String ccd, String object)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void setBrightness(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public long getBrightness(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void setContrast(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public long getContrast(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void setGain(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public long getGain(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void setBiningX(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public long getBiningX(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void setBiningY(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public long getBiningY(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void setGamma(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public long getGamma(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public CCDState getState(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public String startExposure(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public String startContinueMode(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

	public void stopContinueMode(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException;

}