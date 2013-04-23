package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Mount;

public class MoveNorthOperation extends MountOperation {

	public MoveNorthOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws RTSException {

		mount.moveNorth();

		returns.setMessage("Move north operation executed: " + this.getServer()
				+ ", " + this.getMountName());

	}
}
