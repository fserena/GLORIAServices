package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Mount;

public class SetTrackingOperation extends MountOperation {

	private boolean mode;

	public SetTrackingOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.mode = (boolean) args.getArguments().get(2);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws RTSException {
		mount.setTracking(this.mode);

		returns.setMessage("Set tracking mode operation executed: "
				+ this.getServer() + "," + this.getMountName() + ", " + mode);
	}
}
