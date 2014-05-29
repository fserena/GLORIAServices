package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.RHSensor;

public class GetRelativeHumidityOperation extends RHSensorOperation {

	public GetRelativeHumidityOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateRHSensor(RHSensor rhSensor, OperationReturn returns)
			throws TeleoperationException {
		double humidity = rhSensor.getRelativeHumidity();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(humidity);
	}
}
