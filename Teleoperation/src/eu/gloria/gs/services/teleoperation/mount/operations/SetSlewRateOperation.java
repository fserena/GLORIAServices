package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Mount;

public class SetSlewRateOperation extends MountOperation {

	private String rate;

	public SetSlewRateOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.rate = (String) args.getArguments().get(2);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws RTSException {
		mount.setSlewRate(this.rate);

		returns.setMessage("Set slew rate operation executed: "
				+ this.getServer() + "," + this.getMountName() + ", " + rate);
	}
}
