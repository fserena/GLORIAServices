package eu.gloria.gs.services.teleoperation.dome.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.Dome;

public class GetStateOperation extends DomeOperation {

	public GetStateOperation(OperationArgs args) throws Exception {
		super(args);

	}

	@Override
	protected void operateDome(Dome dome, OperationReturn returns)
			throws RTSException {
		DomeOpeningState state = dome.getOpeningStatus();

		returns.setReturns(new ArrayList<>());

		returns.getReturns().add(state);
	}
}
