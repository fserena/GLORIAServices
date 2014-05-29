package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Mount;

public class SlewToCoordinatesOperation extends MountOperation {

	private double ra;
	private double dec;

	public SlewToCoordinatesOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 3) {
			this.ra = (Double) args.getArguments().get(2);
			this.dec = (Double) args.getArguments().get(3);
		}
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws TeleoperationException {
		mount.slewToCoordinates(ra, dec);
	}
}
