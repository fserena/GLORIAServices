package eu.gloria.gs.services.teleoperation.ccd;

import java.util.ArrayList;

import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
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

	public void setImageRepository(ImageRepositoryInterface repository) {
		this.imageRepository = repository;
	}

	@Override
	public String getImageURL(String rt, String ccd, String imageId,
			ImageExtensionFormat format) throws ImageNotAvailableException,
			CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(imageId);
		args.getArguments().add(format);

		GetImageURLOperation operation = null;

		try {
			operation = new GetImageURLOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getImageURL/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			String url = (String) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getImageURL",
					new Object[] { imageId }, url.substring(0, 15) + "...");

			return url;

		} catch (ImageNotAvailableException e) {
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);

			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setExposureTime(String rt, String ccd, double value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetExposureTimeOperation operation = null;

		try {
			operation = new SetExposureTimeOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setExposureTime/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setExposure", new Object[] { value },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public double getExposureTime(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetExposureTimeOperation operation = null;

		try {
			operation = new GetExposureTimeOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getExposureTime/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double exposure = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getExposure", null, exposure);

			return exposure;
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setBrightness(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetBrightnessOperation operation = null;

		try {
			operation = new SetBrightnessOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setBrightness/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setBrightness",
					new Object[] { value }, null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getBrightness(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetBrightnessOperation operation = null;

		try {
			operation = new GetBrightnessOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getBrightness/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long brightness = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getBrightness", null, brightness);

			return brightness;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setContrast(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetContrastOperation operation = null;

		try {
			operation = new SetContrastOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setContrast/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setContrast", new Object[] { value },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getContrast(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetContrastOperation operation = null;

		try {
			operation = new GetContrastOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getBrightness/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long contrast = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getContrast", null, contrast);

			return contrast;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setGain(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetGainOperation operation = null;

		try {
			operation = new SetGainOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setGain/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setGain", new Object[] { value },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getGain(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetGainOperation operation = null;

		try {
			operation = new GetGainOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getGain/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long gain = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getGain", null, gain);

			return gain;
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public CCDState getState(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getState/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {

			OperationReturn returns = this.executeOperation(operation);
			return (CCDState) returns.getReturns().get(0);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public String startExposure(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		StartExposureOperation operation = null;

		try {
			operation = new StartExposureOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/startExposure/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			String imageId = (String) returns.getReturns().get(0);

			ImageTargetData target = new ImageTargetData();
			target.setDec(null);
			target.setRa(null);
			target.setObject(null);
			
			imageRepository.saveImage(this.getClientUsername(), rt, ccd,
					imageId, target);

			this.processSuccess(rt, ccd, "startExposure", null, imageId);

			return imageId;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException | ImageRepositoryException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public String startContinueMode(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		StartContinueModeOperation operation = null;

		try {
			operation = new StartContinueModeOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/startContinueMode/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {

			OperationReturn returns = this.executeOperation(operation);
			String imageId = (String) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "startContinueMode", null, imageId);

			return imageId;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void stopContinueMode(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		StopContinueModeOperation operation = null;

		try {
			operation = new StopContinueModeOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/stopContinueMode/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "stopContinueMode", null, null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setGamma(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetGammaOperation operation = null;

		try {
			operation = new SetGammaOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setGamma/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setGamma", new Object[] { value },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getGamma(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetGammaOperation operation = null;

		try {
			operation = new GetGammaOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getGamma/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long gain = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getGamma", null, gain);

			return gain;
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface#setBiningX
	 * (java.lang.String, java.lang.String, long)
	 */
	@Override
	public void setBinningX(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetBiningXOperation operation = null;

		try {
			operation = new SetBiningXOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setBiningX/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setBiningX", new Object[] { value },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface#getBiningX
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public long getBiningX(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetBiningXOperation operation = null;

		try {
			operation = new GetBiningXOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getBiningX/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long gain = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getBiningX", null, gain);

			return gain;
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface#setBiningY
	 * (java.lang.String, java.lang.String, long)
	 */
	@Override
	public void setBinningY(String rt, String ccd, long value)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		SetBiningYOperation operation = null;

		try {
			operation = new SetBiningYOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setBiningY/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, ccd, "setBiningY", new Object[] { value },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface#getBiningY
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public long getBiningY(String rt, String ccd)
			throws DeviceOperationFailedException, CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		GetBiningYOperation operation = null;

		try {
			operation = new GetBiningYOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getBiningY/Bad args", rt);

			throw new CCDTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long gain = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, ccd, "getBiningY", null, gain);

			return gain;
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new CCDTeleoperationException(e.getMessage());
		}
	}
}
