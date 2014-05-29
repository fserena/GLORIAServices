package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.TempSensor;

public class IsTemperatureOnAlarmOperation extends TempSensorOperation {

	public IsTemperatureOnAlarmOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateTempSensor(TempSensor tempSensor, OperationReturn returns)
			throws TeleoperationException {
		boolean alarm = tempSensor.isOnAlarm();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(alarm);
	}
}
