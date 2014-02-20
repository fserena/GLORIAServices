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
	
	public FilterWheelTeleoperation() {
		this.createLogger(FilterWheelTeleoperation.class);
	}

	@Override
	public List<String> getFilters(String rt, String filterWheel)
			throws DeviceOperationFailedException,
			FilterWheelTeleoperationException {

		String operationName = "get filters";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(filterWheel);

		GetFiltersOperation operation = null;

		try {
			operation = new GetFiltersOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, filterWheel, operationName,
					args.getArguments());

			throw new FilterWheelTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			// TODO: improve this...
			List<String> filters = (List<String>) returns.getReturns().get(0);

			this.processSuccess(rt, filterWheel, operationName, args.getArguments(),
					filters);

			return filters;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, filterWheel, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, filterWheel, operationName,
					args.getArguments());
			throw new FilterWheelTeleoperationException(e.getAction());
		}
	}

	@Override
	public void selectFilter(String rt, String filterWheel, String filter)
			throws DeviceOperationFailedException,
			FilterWheelTeleoperationException {
		
		String operationName = "select filter";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(filterWheel);
		args.getArguments().add(filter);

		SelectFilterOperation operation = null;

		try {
			operation = new SelectFilterOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, filterWheel, operationName,
					args.getArguments());

			throw new FilterWheelTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, filterWheel, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, filterWheel, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, filterWheel, operationName,
					args.getArguments());
			throw new FilterWheelTeleoperationException(e.getAction());
		}
	}
}
