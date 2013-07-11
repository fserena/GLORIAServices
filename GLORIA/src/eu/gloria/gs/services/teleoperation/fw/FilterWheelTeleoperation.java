package eu.gloria.gs.services.teleoperation.fw;

import java.util.ArrayList;
import java.util.List;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.fw.operations.GetFiltersOperation;
import eu.gloria.gs.services.teleoperation.fw.operations.SelectFilterOperation;

public class FilterWheelTeleoperation extends AbstractTeleoperation implements
		FilterWheelTeleoperationInterface {

	@Override
	public List<String> getFilters(String rt, String filterWheel)
			throws DeviceOperationFailedException,
			FilterWheelTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(filterWheel);

		GetFiltersOperation operation = null;

		try {
			operation = new GetFiltersOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getFilters/Bad args", rt);

			throw new FilterWheelTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			// TODO: improve this...
			List<String> filters= (List<String>) returns.getReturns().get(0);

			this.processSuccess(rt, filterWheel, "getFilters", null, filters);

			return filters;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new FilterWheelTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void selectFilter(String rt, String filterWheel, String filter)
			throws DeviceOperationFailedException,
			FilterWheelTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(filterWheel);
		args.getArguments().add(filter);

		SelectFilterOperation operation = null;

		try {
			operation = new SelectFilterOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/selectFilter/Bad args", rt);

			throw new FilterWheelTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, filterWheel, "selectFilter",
					new Object[] { filter }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new FilterWheelTeleoperationException(e.getMessage());
		}
	}
}
