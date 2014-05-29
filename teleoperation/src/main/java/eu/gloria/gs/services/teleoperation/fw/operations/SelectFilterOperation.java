package eu.gloria.gs.services.teleoperation.fw.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.FilterWheel;

public class SelectFilterOperation extends FilterWheelOperation {

	private String filter;

	public SelectFilterOperation(OperationArgs args) throws Exception {
		super(args);

		this.filter = (String) args.getArguments().get(2);
	}

	@Override
	protected void operateFilterWheel(FilterWheel filterWheel, OperationReturn returns)
			throws TeleoperationException {

		filterWheel.selectFilter(filter);
	}
}
