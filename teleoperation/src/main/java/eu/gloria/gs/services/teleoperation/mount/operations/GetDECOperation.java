package eu.gloria.gs.services.teleoperation.mount.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Mount;

public class GetDECOperation extends MountOperation {

	public GetDECOperation(OperationArgs args) throws Exception {
		super(args);

	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws TeleoperationException {
		double dec = mount.getDEC();
		
		returns.setReturns(new ArrayList<>());
		returns.getReturns().add(dec);
		
	}
}
