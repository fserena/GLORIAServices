package eu.gloria.gs.services.teleoperation.scam;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationException;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.scam.operations.GetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetContrastOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetGainOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetImageURLOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetContrastOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetGainOperation;

public class SCamTeleoperation extends AbstractTeleoperation implements
		SCamTeleoperationInterface {

	@Override
	public String getImageURL(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetImageURLOperation operation = null;

		try {
			operation = new GetImageURLOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getImageURL/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			String url = (String) returns.getReturns().get(0);

			this.processSuccess(rt, scam, "getImageURL", null, url);

			return url;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setExposureTime(String rt, String scam, double value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetExposureTimeOperation operation = null;

		try {
			operation = new SetExposureTimeOperation(args);

		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setExposureTime/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, "setExposure",
					new Object[] { value }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public double getExposureTime(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetExposureTimeOperation operation = null;

		try {
			operation = new GetExposureTimeOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getExposureTime/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double exposure = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, scam, "getExposure", null, exposure);

			return exposure;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setBrightness(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetBrightnessOperation operation = null;

		try {
			operation = new SetBrightnessOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setBrightness/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, "setBrightness",
					new Object[] { value }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getBrightness(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetBrightnessOperation operation = null;

		try {
			operation = new GetBrightnessOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getBrightness/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long brightness = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, scam, "getBrightness", null, brightness);

			return brightness;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setContrast(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetContrastOperation operation = null;

		try {
			operation = new SetContrastOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setContrast/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, "setContrast",
					new Object[] { value }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getContrast(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetContrastOperation operation = null;

		try {
			operation = new GetContrastOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getContrast/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long contrast = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, scam, "getContrast", null, contrast);

			return contrast;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setGain(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetGainOperation operation = null;

		try {
			operation = new SetGainOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setGain/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, "setGain", new Object[] { value },
					null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getGain(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetGainOperation operation = null;

		try {
			operation = new GetGainOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getGain/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long gain = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, scam, "getContrast", null, gain);

			return gain;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public SCamState getState(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getState/Bad args", rt);

			throw new SCamTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			SCamState state = (SCamState) returns.getReturns().get(0);

			this.processSuccess(rt, scam, "getState", null, state.name());

			return state;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new SCamTeleoperationException(e.getMessage());
		}
	}
}
