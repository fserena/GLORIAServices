package eu.gloria.gs.services.teleoperation.weather.operations;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.Barometer;

public abstract class BarometerOperation extends DeviceOperation {

	private String barometer;

	public BarometerOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.barometer = (String) args.getArguments().get(1);
	}

	public String getBarometerName() {
		return this.barometer;
	}

	protected abstract void operateBarometer(Barometer barometer, OperationReturn returns)
			throws TeleoperationException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws TeleoperationException {

		ServerKeyData keyData = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createBarometer(keyData, this.getBarometerName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {
		this.operateBarometer((Barometer) handler, returns);

	}

}
