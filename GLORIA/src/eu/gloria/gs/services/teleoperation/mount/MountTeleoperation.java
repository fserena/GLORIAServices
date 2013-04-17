package eu.gloria.gs.services.teleoperation.mount;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.mount.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveEastOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveNorthOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveSouthOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveWestOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.ParkOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetSlewRateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetTrackingOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetTrackingRateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SlewToObjectOperation;

public class MountTeleoperation extends AbstractTeleoperation implements
		MountTeleoperationInterface {

	private void processOperationException(String message, String rt,
			String mount, String operation) {
		try {
			this.logAction(this.getClientUsername(), "Error while trying to "
					+ operation + " of '" + mount + "' " + " of '" + rt + "': "
					+ message);
		} catch (ActionLogException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public MountState getState(String rt, String mount)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		try {
			GetStateOperation operation = new GetStateOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (MountState) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"get state");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveNorth(String rt, String mount)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		try {
			MoveNorthOperation operation = new MoveNorthOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"move north");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveSouth(String rt, String mount)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		try {
			MoveSouthOperation operation = new MoveSouthOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"move south");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveEast(String rt, String mount)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		try {
			MoveEastOperation operation = new MoveEastOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"move east");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveWest(String rt, String mount)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		try {
			MoveWestOperation operation = new MoveWestOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"move west");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setSlewRate(String rt, String mount, String rate)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(rate);

		try {
			SetSlewRateOperation operation = new SetSlewRateOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"set slew rate");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void slewToObject(String rt, String mount, String object)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(object);

		try {
			SlewToObjectOperation operation = new SlewToObjectOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"slew to object");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void park(String rt, String mount)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		try {
			ParkOperation operation = new ParkOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount, "park");
			throw new MountTeleoperationException(e.getMessage());
		}

	}

	@Override
	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(rate);

		try {
			SetTrackingRateOperation operation = new SetTrackingRateOperation(
					args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"set tracking rate");
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setTracking(String rt, String mount, boolean mode)
			throws MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(mode);

		try {
			SetTrackingOperation operation = new SetTrackingOperation(args);

			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, mount,
					"set tracking mode");
			throw new MountTeleoperationException(e.getMessage());
		}
	}
}
