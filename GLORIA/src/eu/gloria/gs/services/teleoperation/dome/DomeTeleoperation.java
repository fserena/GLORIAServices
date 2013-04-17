package eu.gloria.gs.services.teleoperation.dome;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
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

	private void processOperationException(String message, String rt,
			String dome, String operation) {
		try {
			this.logAction(this.getClientUsername(), "Error while trying to "
					+ operation + " of '" + dome + "' " + " of '"
					+ rt + "': " + message);
		} catch (ActionLogException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public DomeOpeningState getState(String rt, String dome)
			throws DomeTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		try {
			GetStateOperation operation = new GetStateOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (DomeOpeningState) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome,
					"get state");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setTracking(String rt, String dome, boolean mode)
			throws DomeTeleoperationException {

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);
		args.getArguments().add(mode);

		try {
			SetTrackingOperation operation = new SetTrackingOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome,
					"set tracking mode");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public boolean isTrackingEnabled(String rt, String dome)
			throws DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		try {
			IsTrackingEnabledOperation operation = new IsTrackingEnabledOperation(
					args);

			OperationReturn returns = this.executeOperation(operation);
			return (Boolean) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome,
					"get tracking enable");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public double getAzimuth(String rt, String dome)
			throws DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		try {
			GetAzimuthOperation operation = new GetAzimuthOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Double) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome,
					"get azimuth");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void open(String rt, String dome) throws DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		try {
			OpenOperation operation = new OpenOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome, "open");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void close(String rt, String dome) throws DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		try {
			CloseOperation operation = new CloseOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome, "close");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void park(String rt, String dome) throws DomeTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());

		args.getArguments().add(rt);
		args.getArguments().add(dome);

		try {
			ParkOperation operation = new ParkOperation(args);

			this.executeOperation(operation);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, dome, "park");
			throw new DomeTeleoperationException(e.getMessage());
		}
	}
}
