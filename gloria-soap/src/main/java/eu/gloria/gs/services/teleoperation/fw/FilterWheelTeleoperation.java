package eu.gloria.gs.services.teleoperation.fw;

import java.util.List;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.fw.operations.GetFiltersOperation;
import eu.gloria.gs.services.teleoperation.fw.operations.SelectFilterOperation;

public class FilterWheelTeleoperation extends AbstractTeleoperation implements
		FilterWheelTeleoperationInterface {
	
	public FilterWheelTeleoperation() {
		super(FilterWheelTeleoperation.class.getSimpleName());
	}

	@Override
	public List<String> getFilters(String rt, String filterWheel)
			throws DeviceOperationFailedException,
			FilterWheelTeleoperationException {
		try {
			@SuppressWarnings("unchecked")
			List<String> filters = (List<String>) this.invokeGetOperation(GetFiltersOperation.class, rt, filterWheel);
			return filters;
		} catch (ActionException e) {
			throw new FilterWheelTeleoperationException(e.getAction());
		}
	}

	@Override
	public void selectFilter(String rt, String filterWheel, String filter)
			throws DeviceOperationFailedException,
			FilterWheelTeleoperationException {
		try {
			this.invokeSetOperation(SelectFilterOperation.class, rt, filterWheel, filter);
		} catch (ActionException e) {
			throw new FilterWheelTeleoperationException(e.getAction());
		}		
	}
}
