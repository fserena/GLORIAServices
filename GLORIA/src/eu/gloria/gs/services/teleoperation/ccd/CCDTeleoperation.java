package eu.gloria.gs.services.teleoperation.ccd;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetContrastOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetGainOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetImageURLOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetContrastOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.SetGainOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.StartContinueModeOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.StartExposureOperation;
import eu.gloria.gs.services.teleoperation.ccd.operations.StopContinueModeOperation;

public class CCDTeleoperation extends AbstractTeleoperation implements
		CCDTeleoperationInterface {

	private ImageRepositoryInterface imageRepository;

	private void processAttributeModificationException(String message,
			String rt, String ccd, String attribute) {
		try {
			this.logAction(this.getClientUsername(), "Error modifying the '"
					+ ccd + "' " + attribute + " of '" + rt + "': " + message);
		} catch (ActionLogException e1) {
			e1.printStackTrace();
		}
	}

	private void processAttributeReadingException(String message, String rt,
			String ccd, String attribute) {
		try {
			this.logAction(this.getClientUsername(), "Error reading the '"
					+ ccd + "' " + attribute + " of '" + rt + "': " + message);
		} catch (ActionLogException e1) {
			e1.printStackTrace();
		}
	}

	public void setImageRepository(ImageRepositoryInterface repository) {
		this.imageRepository = repository;
	}

	@Override
	public String getImageURL(String rt, String ccd, String imageId,
			ImageExtensionFormat format) throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(imageId);
		args.getArguments().add(format);

		try {
			GetImageURLOperation operation = new GetImageURLOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			String url = (String) returns.getReturns().get(0);

			/*
			 * if (url != null) { imageRepository.setUrlByRTLocalId(rt, imageId,
			 * url); }
			 */

			return url;

		} catch (Exception e) {

			if (e.getMessage() != null && !e.getMessage().contains("yet")) {
				this.processAttributeReadingException(e.getMessage(), rt, ccd
						+ "/" + imageId, "image URL");
			}
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setExposureTime(String rt, String ccd, double value)
			throws CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		try {
			SetExposureTimeOperation operation = new SetExposureTimeOperation(
					args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processAttributeModificationException(e.getMessage(), rt, ccd,
					"exposure");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public double getExposureTime(String rt, String ccd)
			throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			GetExposureTimeOperation operation = new GetExposureTimeOperation(
					args);

			OperationReturn returns = this.executeOperation(operation);
			return (Double) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, ccd,
					"exposure");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setBrightness(String rt, String ccd, long value)
			throws CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		try {
			SetBrightnessOperation operation = new SetBrightnessOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processAttributeModificationException(e.getMessage(), rt, ccd,
					"brightness");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getBrightness(String rt, String ccd)
			throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			GetBrightnessOperation operation = new GetBrightnessOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, ccd,
					"brightness");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setContrast(String rt, String ccd, long value)
			throws CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		try {
			SetContrastOperation operation = new SetContrastOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processAttributeModificationException(e.getMessage(), rt, ccd,
					"contrast");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getContrast(String rt, String ccd)
			throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			GetContrastOperation operation = new GetContrastOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, ccd,
					"contrast");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setGain(String rt, String ccd, long value)
			throws CCDTeleoperationException {
		OperationArgs args = new OperationArgs();
		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);
		args.getArguments().add(value);

		try {
			SetGainOperation operation = new SetGainOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processAttributeModificationException(e.getMessage(), rt, ccd,
					"gain");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getGain(String rt, String ccd) throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			GetGainOperation operation = new GetGainOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, ccd,
					"gain");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public CCDState getState(String rt, String ccd)
			throws CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			GetStateOperation operation = new GetStateOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (CCDState) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, ccd,
					"state");
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public String startExposure(String rt, String ccd)
			throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			StartExposureOperation operation = new StartExposureOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			String imageId = (String) returns.getReturns().get(0);

			imageRepository.saveImage(this.getClientUsername(), rt, ccd,
					imageId);

			return imageId;

		} catch (Exception e) {
			try {
				this.logAction(this.getClientUsername(),
						"Error starting exposure of the '" + ccd + "' of '"
								+ rt + "': " + e.getMessage());
			} catch (ActionLogException e1) {
				e1.printStackTrace();
			}
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public String startContinueMode(String rt, String ccd)
			throws CCDTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			StartContinueModeOperation operation = new StartContinueModeOperation(
					args);

			OperationReturn returns = this.executeOperation(operation);
			return (String) returns.getReturns().get(0);

		} catch (Exception e) {
			try {
				this.logAction(this.getClientUsername(),
						"Error starting continue mode of the '" + ccd
								+ "' of '" + rt + "': " + e.getMessage());
			} catch (ActionLogException e1) {
				e1.printStackTrace();
			}
			throw new CCDTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void stopContinueMode(String rt, String ccd)
			throws CCDTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(ccd);

		try {
			StopContinueModeOperation operation = new StopContinueModeOperation(
					args);

			this.executeOperation(operation);

		} catch (Exception e) {
			try {
				this.logAction(this.getClientUsername(),
						"Error stopping exposure of the '" + ccd + "' of '"
								+ rt + "': " + e.getMessage());
			} catch (ActionLogException e1) {
				e1.printStackTrace();
			}
			throw new CCDTeleoperationException(e.getMessage());
		}
	}
}
