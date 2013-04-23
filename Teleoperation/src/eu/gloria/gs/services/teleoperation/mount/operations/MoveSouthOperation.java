package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Mount;

public class MoveSouthOperation extends MountOperation {

	public MoveSouthOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws RTSException {
		mount.moveSouth();

		returns.setMessage("Move south operation executed: " + this.getServer()
				+ ", " + this.getMountName());

	}
}
