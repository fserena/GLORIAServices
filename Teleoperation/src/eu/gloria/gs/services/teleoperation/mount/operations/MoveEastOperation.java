package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Mount;

public class MoveEastOperation extends MountOperation {

	public MoveEastOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws TeleoperationException {
		mount.moveEast();

		returns.setMessage("Move east operation executed: " + this.getServer()
				+ ", " + this.getMountName());

	}
}
