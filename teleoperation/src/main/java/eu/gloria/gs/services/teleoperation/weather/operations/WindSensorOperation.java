package eu.gloria.gs.services.teleoperation.weather.operations;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.WindSensor;

public abstract class WindSensorOperation extends DeviceOperation {

	private String windSensor;

	public WindSensorOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.windSensor = (String) args.getArguments().get(1);
	}

	public String getWindSensorName() {
		return this.windSensor;
	}

	protected abstract void operateWindSensor(WindSensor barometer, OperationReturn returns)
			throws TeleoperationException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws TeleoperationException {

		ServerKeyData keyData = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createWindSensor(keyData, this.getWindSensorName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {
		this.operateWindSensor((WindSensor) handler, returns);

	}

}
