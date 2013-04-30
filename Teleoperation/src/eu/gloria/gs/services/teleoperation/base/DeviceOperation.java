package eu.gloria.gs.services.teleoperation.base;

import java.util.ArrayList;

public abstract class DeviceOperation extends Operation {

	private String device;

	public DeviceOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.device = (String) args.getArguments().get(1);
	}

	public String getDevice() {
		return this.device;
	}

	protected abstract DeviceHandler getDeviceHandler(ServerResolver resolver) throws TeleoperationException;

	protected abstract void operateHandler(DeviceHandler handler,
			OperationReturn returns) throws TeleoperationException;

	@Override
	public OperationReturn execute(ServerResolver resolver) throws Exception {

		DeviceHandler handler = null;

		handler = this.getDeviceHandler(resolver);

		OperationReturn returns = new OperationReturn();
		returns.setReturns(new ArrayList<Object>());

		this.operateHandler(handler, returns);

		return returns;
	}

}
