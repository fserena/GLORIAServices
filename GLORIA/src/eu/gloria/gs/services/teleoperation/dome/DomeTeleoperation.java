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

	public DomeTeleoperation() {
		this.createLogger(DomeTeleoperation.class);
	}
	
	@Override
	public DomeOpeningState getState(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {

		String operationName = "get state";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			DomeOpeningState state = (DomeOpeningState) returns.getReturns()
					.get(0);

			this.processSuccess(rt, dome, operationName, args.getArguments(),
					state);

			return state;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setTracking(String rt, String dome, boolean mode)
			throws DeviceOperationFailedException, DomeTeleoperationException {

		String operationName = "set tracking";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);
		args.getArguments().add(mode);

		SetTrackingOperation operation = null;

		try {
			operation = new SetTrackingOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, dome, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isTrackingEnabled(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		
		String operationName = "tracking enabled";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		IsTrackingEnabledOperation operation = null;

		try {
			operation = new IsTrackingEnabledOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			boolean mode = (Boolean) returns.getReturns().get(0);

			this.processSuccess(rt, dome, operationName, args.getArguments(),
					mode);

			return mode;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getAzimuth(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		
		String operationName = "get azimuth";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		GetAzimuthOperation operation = null;

		try {
			operation = new GetAzimuthOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double azimuth = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, dome, operationName, args.getArguments(),
					azimuth);

			return azimuth;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void open(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		
		String operationName = "open";
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		OpenOperation operation = null;

		try {
			operation = new OpenOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, dome, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void close(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		
		String operationName = "close";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		CloseOperation operation = null;

		try {
			operation = new CloseOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, dome, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void park(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		
		String operationName = "park";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		ParkOperation operation = null;

		try {
			operation = new ParkOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, dome, operationName, args.getArguments());

			throw new DomeTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, dome, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, dome, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, dome, operationName, args.getArguments());
			throw new DomeTeleoperationException(e.getAction());
		}
	}
}
