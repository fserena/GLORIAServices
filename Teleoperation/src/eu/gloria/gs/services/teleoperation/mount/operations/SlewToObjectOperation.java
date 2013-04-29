package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Mount;

public class SlewToObjectOperation extends MountOperation {

	private String object;

	public SlewToObjectOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.object = (String) args.getArguments().get(2);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws TeleoperationException {
		mount.slewToObject(this.object);

		returns.setMessage("Slew to object operation executed: "
				+ this.getServer() + "," + this.getMountName() + ", " + object);
	}
}
