package eu.gloria.gs.services.teleoperation.weather.operations;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.TempSensor;

public abstract class TempSensorOperation extends DeviceOperation {

	private String tempSensor;

	public TempSensorOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.tempSensor = (String) args.getArguments().get(1);
	}

	public String getTempSensorName() {
		return this.tempSensor;
	}

	protected abstract void operateTempSensor(TempSensor tempSensor, OperationReturn returns)
			throws TeleoperationException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws TeleoperationException {

		ServerKeyData keyData = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createTempSensor(keyData, this.getTempSensorName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {
		this.operateTempSensor((TempSensor) handler, returns);

	}

}
