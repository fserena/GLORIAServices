package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;
import eu.gloria.rti.client.devices.Mount;

public class SetTrackingRateOperation extends MountOperation {

	private TrackingRate rate;

	public SetTrackingRateOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.rate = (TrackingRate) args.getArguments().get(2);
	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws TeleoperationException {
		mount.setTrackingRate(this.rate);

		returns.setMessage("Set tracking rate operation executed: "
				+ this.getServer() + "," + this.getMountName() + ", " + rate);
	}
}
