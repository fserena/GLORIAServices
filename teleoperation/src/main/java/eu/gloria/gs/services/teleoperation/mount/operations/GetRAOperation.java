package eu.gloria.gs.services.teleoperation.mount.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Mount;

public class GetRAOperation extends MountOperation {

	public GetRAOperation(OperationArgs args) throws Exception {
		super(args);

	}

	@Override
	protected void operateMount(Mount mount, OperationReturn returns)
			throws TeleoperationException {
		double ra = mount.getRA();
		
		returns.setReturns(new ArrayList<>());
		returns.getReturns().add(ra);
		
	}
}
