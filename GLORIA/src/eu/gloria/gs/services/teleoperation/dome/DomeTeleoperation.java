package eu.gloria.gs.services.teleoperation.dome;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationException;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.dome.operations.CloseOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.GetAzimuthOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.IsTrackingEnabledOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.OpenOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.ParkOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.SetTrackingOperation;

public class DomeTeleoperation extends AbstractTeleoperation implements
		DomeTeleoperationInterface {

	@Override
	public DomeOpeningState getState(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getDomeState/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			DomeOpeningState state = (DomeOpeningState) returns.getReturns()
					.get(0);

			this.processSuccess(rt, dome, "getState", null, state.name());

			return state;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setTracking(String rt, String dome, boolean mode)
			throws DeviceOperationFailedException, DomeTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);
		args.getArguments().add(mode);

		SetTrackingOperation operation = null;

		try {
			operation = new SetTrackingOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setTracking/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, dome, "setTracking", new Object[] { mode },
					null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public boolean isTrackingEnabled(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		IsTrackingEnabledOperation operation = null;

		try {
			operation = new IsTrackingEnabledOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/isTrackingEnabled/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			boolean mode = (Boolean) returns.getReturns().get(0);

			this.processSuccess(rt, dome, "isTrackingEnabled", null, mode);

			return mode;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public double getAzimuth(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		GetAzimuthOperation operation = null;

		try {
			operation = new GetAzimuthOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getAzimuth/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double azimuth = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, dome, "getAzimuth", null, azimuth);

			return azimuth;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void open(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		OpenOperation operation = null;

		try {
			operation = new OpenOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/openDome/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, dome, "open", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void close(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		CloseOperation operation = null;

		try {
			operation = new CloseOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/closeDome/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, dome, "close", null, null);

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void park(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		ParkOperation operation = null;

		try {
			operation = new ParkOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/parkDome/Bad args", rt);

			throw new DomeTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, dome, "park", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new DomeTeleoperationException(e.getMessage());
		}
	}
}
