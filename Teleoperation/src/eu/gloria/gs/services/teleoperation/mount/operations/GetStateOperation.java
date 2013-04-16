package eu.gloria.gs.services.teleoperation.mount.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.mount.MountState;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.Mount;

public class GetStateOperation extends MountOperation {

	public GetStateOperation(OperationArgs args) throws Exception {
		super(args);

	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws RTSException {
		MountState state = mount.getState();
		
		returns.setReturns(new ArrayList<>());
		returns.getReturns().add(state);
		
	}
}