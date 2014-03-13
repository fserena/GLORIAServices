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

	public SCamTeleoperation() {
		this.createLogger(SCamTeleoperation.class);
	}
	
	@Override
	public String getImageURL(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {

		String operationName = "get image url";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetImageURLOperation operation = null;

		try {
			operation = new GetImageURLOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			String url = (String) returns.getReturns().get(0);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					url);

			return url;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setExposureTime(String rt, String scam, double value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "set exposure";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetExposureTimeOperation operation = null;

		try {
			operation = new SetExposureTimeOperation(args);

		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public double getExposureTime(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "get exposure";
		
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetExposureTimeOperation operation = null;

		try {
			operation = new GetExposureTimeOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double exposure = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					exposure);

			return exposure;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setBrightness(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "set brightness";
		
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetBrightnessOperation operation = null;

		try {
			operation = new SetBrightnessOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getBrightness(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "get brightness";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetBrightnessOperation operation = null;

		try {
			operation = new GetBrightnessOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long brightness = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					brightness);

			return brightness;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setContrast(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "set contrast";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetContrastOperation operation = null;

		try {
			operation = new SetContrastOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getContrast(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "get contrast";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetContrastOperation operation = null;

		try {
			operation = new GetContrastOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long contrast = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					contrast);

			return contrast;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setGain(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "set gain";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		SetGainOperation operation = null;

		try {
			operation = new SetGainOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getGain(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "get gain";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetGainOperation operation = null;

		try {
			operation = new GetGainOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long gain = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					gain);

			return gain;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public SCamState getState(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		
		String operationName = "get state";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, scam, operationName, args.getArguments());

			throw new SCamTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			SCamState state = (SCamState) returns.getReturns().get(0);

			this.processSuccess(rt, scam, operationName, args.getArguments(),
					state);

			return state;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, scam, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, scam, operationName, args.getArguments());
			throw new SCamTeleoperationException(e.getMessage());
		}
	}
}
