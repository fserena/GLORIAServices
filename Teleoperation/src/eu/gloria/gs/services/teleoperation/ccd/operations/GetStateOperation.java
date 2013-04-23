package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.ccd.CCDState;
import eu.gloria.rti.client.devices.CCD;

public class GetStateOperation extends CCDOperation {

	public GetStateOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {

		CCDState state = ccd.getState();

		returns.setReturns(new ArrayList<>());

		returns.getReturns().add(state);
	}
}
