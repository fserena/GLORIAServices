package eu.gloria.gs.services.teleoperation.fw.operations;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.FilterWheel;

public abstract class FilterWheelOperation extends DeviceOperation {

	private String filterWheel;

	public FilterWheelOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.filterWheel = (String) args.getArguments().get(1);
	}

	public String getFilterWheelName() {
		return this.filterWheel;
	}

	protected abstract void operateFilterWheel(FilterWheel fw, OperationReturn returns)
			throws TeleoperationException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws TeleoperationException {

		ServerKeyData keyData = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createFilterWheel(keyData,
				this.getFilterWheelName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {

		this.operateFilterWheel((FilterWheel) handler, returns);

	}

}
