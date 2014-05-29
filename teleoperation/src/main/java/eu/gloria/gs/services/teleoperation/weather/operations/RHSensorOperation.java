package eu.gloria.gs.services.teleoperation.weather.operations;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.RHSensor;

public abstract class RHSensorOperation extends DeviceOperation {

	private String rhSensor;

	public RHSensorOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.rhSensor = (String) args.getArguments().get(1);
	}

	public String getRHSensorName() {
		return this.rhSensor;
	}

	protected abstract void operateRHSensor(RHSensor rhSensor, OperationReturn returns)
			throws TeleoperationException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws TeleoperationException {

		ServerKeyData keyData = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createRHSensor(keyData, this.getRHSensorName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {
		this.operateRHSensor((RHSensor) handler, returns);

	}

}
