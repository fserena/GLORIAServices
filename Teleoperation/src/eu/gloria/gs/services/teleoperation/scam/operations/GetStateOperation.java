package eu.gloria.gs.services.teleoperation.scam.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.scam.SCamState;
import eu.gloria.rti.client.devices.Scam;

public class GetStateOperation extends SurveillanceCameraOperation {

	public GetStateOperation(OperationArgs args) throws Exception {
		super(args);

	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws RTSException {
		SCamState state = scam.getState();
		
		returns.setReturns(new ArrayList<>());
		returns.getReturns().add(state);
	}
}
