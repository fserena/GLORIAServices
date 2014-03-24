package eu.gloria.gs.services.teleoperation.ccd;

import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetBiningXOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetBiningYOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetContrastOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetGainOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetGammaOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetImageURLOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetBiningXOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetBiningYOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetContrastOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetGainOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetGammaOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.StartContinueModeOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.StartExposureOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.StopContinueModeOperation;

public class CCDTeleoperation extends AbstractTeleoperation implements
		CCDTeleoperationInterface {

	private ImageRepositoryInterface imageRepository;

	public CCDTeleoperation() {
		super(CCDTeleoperation.class.getSimpleName());
	}

	@Override
	public long getBiningX(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetBiningXOperation.class,
					rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getBiningY(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetBiningYOperation.class,
					rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getBrightness(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetBrightnessOperation.class,
					rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getContrast(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetContrastOperation.class,
					rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getExposureTime(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(
					GetExposureTimeOperation.class, rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getGain(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetGainOperation.class, rt,
					ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getGamma(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetGammaOperation.class, rt,
					ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public String getImageURL(String rt, String ccd, String imageId,
			ImageExtensionFormat format) throws ImageNotAvailableException,
			CCDTeleoperationException {

		try {
			String url = (String) this.invokeOperation(
					GetImageURLOperation.class, rt, ccd, imageId, format)
					.get(0);

			return url;
		} catch (ImageNotAvailableException e) {
			this.processWarning(rt, e);
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(rt, e);
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public CCDState getState(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (CCDState) this.invokeGetOperation(GetStateOperation.class,
					rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setBiningX(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		try {
			this.invokeSetOperation(SetBiningXOperation.class, rt, ccd, value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setBinningY(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(SetBiningYOperation.class, rt, ccd, value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setBrightness(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(SetBrightnessOperation.class, rt, ccd,
					value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setContrast(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(SetContrastOperation.class, rt, ccd, value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setExposureTime(String rt, String ccd, double value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(SetExposureTimeOperation.class, rt, ccd,
					value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setGain(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(SetGainOperation.class, rt, ccd, value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setGamma(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(SetGammaOperation.class, rt, ccd, value);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	public void setImageRepository(ImageRepositoryInterface repository) {
		this.imageRepository = repository;
	}

	@Override
	public String startContinueMode(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			return (String) this.invokeGetOperation(
					StartContinueModeOperation.class, rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public String startExposure(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		try {
			String id = (String) this.invokeGetOperation(
					StartExposureOperation.class, rt, ccd);

			ImageTargetData target = new ImageTargetData();
			target.setDec(null);
			target.setRa(null);
			target.setObject(null);

			double exposure = this.getExposureTime(rt, ccd);

			imageRepository.saveImage(this.getClientUsername(), rt, ccd, id,
					target, exposure);

			return id;
		} catch (TeleoperationException | ImageRepositoryException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}

	@Override
	public void stopContinueMode(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		try {
			this.invokeSetOperation(StopContinueModeOperation.class, rt, ccd);
		} catch (TeleoperationException e) {
			throw new CCDTeleoperationException(e.getAction());
		}
	}
}
