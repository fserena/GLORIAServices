package eu.gloria.gs.services.teleoperation.scam;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.rts.RTSResolver;
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

	private void processException(String message, String rt) {
		try {
			this.logAction(this.getClientUsername(), "'" + rt + "' error: "
					+ message);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getImageURL(String rt, String scam)
			throws TeleoperationException {

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
			return (String) returns.getReturns().get(0);

		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void setExposureTime(String rt, String scam, double value)
			throws TeleoperationException {
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

		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public double getExposureTime(String rt, String scam)
			throws TeleoperationException {
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
			return (Double) returns.getReturns().get(0);

		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void setBrightness(String rt, String scam, long value)
			throws TeleoperationException {
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

		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public long getBrightness(String rt, String scam)
			throws SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		try {
			GetBrightnessOperation operation = new GetBrightnessOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, scam,
					"brightness");
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setContrast(String rt, String scam, long value)
			throws SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		try {
			SetContrastOperation operation = new SetContrastOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processAttributeModificationException(e.getMessage(), rt,
					scam, "contrast");
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getContrast(String rt, String scam)
			throws SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		try {
			GetContrastOperation operation = new GetContrastOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, scam,
					"contrast");
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setGain(String rt, String scam, long value)
			throws SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);
		args.getArguments().add(value);

		try {
			SetGainOperation operation = new SetGainOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processAttributeModificationException(e.getMessage(), rt,
					scam, "gain");
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public long getGain(String rt, String scam)
			throws SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		try {
			GetGainOperation operation = new GetGainOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, scam,
					"gain");
			throw new SCamTeleoperationException(e.getMessage());
		}
	}

	@Override
	public SCamState getState(String rt, String scam)
			throws SCamTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(scam);

		try {
			GetStateOperation operation = new GetStateOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (SCamState) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processAttributeReadingException(e.getMessage(), rt, scam,
					"get state");
			throw new SCamTeleoperationException(e.getMessage());
		}
	}
}
