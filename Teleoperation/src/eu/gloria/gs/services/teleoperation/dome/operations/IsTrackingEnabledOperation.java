package eu.gloria.gs.services.teleoperation.dome.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.Dome;

public class IsTrackingEnabledOperation extends DomeOperation {

	public IsTrackingEnabledOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateDome(Dome dome, OperationReturn returns)
			throws RTSException {
		boolean tracking = dome.isTrackingEnabled();

		returns.setMessage("Get dome tracking mode operation executed: "
				+ tracking + ", " + this.getServer() + "," + this.getDomeName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(tracking);
		
	}
}
