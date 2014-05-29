package eu.gloria.gs.services.teleoperation.fw.operations;

import java.util.ArrayList;
import java.util.List;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.FilterWheel;

public class GetFiltersOperation extends FilterWheelOperation {

	public GetFiltersOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateFilterWheel(FilterWheel filterWheel, OperationReturn returns)
			throws TeleoperationException {

		List<String> filters = filterWheel.getFilters();
		
		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(filters);
	}
}
