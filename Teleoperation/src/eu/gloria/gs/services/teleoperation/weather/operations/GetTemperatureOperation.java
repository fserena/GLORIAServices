package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.TempSensor;

public class GetTemperatureOperation extends TempSensorOperation {

	public GetTemperatureOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateTempSensor(TempSensor tempSensor, OperationReturn returns)
			throws TeleoperationException {
		double temp = tempSensor.getTemperature();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(temp);
	}
}
