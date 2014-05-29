package eu.gloria.gs.services.teleoperation.dome.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Dome;

public class SetTrackingOperation extends DomeOperation {

	private boolean tracking;

	public SetTrackingOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.tracking = (Boolean) args.getArguments().get(2);
	}

	@Override
	protected void operateDome(Dome dome, OperationReturn returns)
			throws TeleoperationException {
		dome.setTracking(this.tracking);
	}
}
