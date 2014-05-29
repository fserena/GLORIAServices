package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.WindSensor;

public class IsWindOnAlarmOperation extends WindSensorOperation {

	public IsWindOnAlarmOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateWindSensor(WindSensor windSensor,
			OperationReturn returns) throws TeleoperationException {
		boolean alarm = windSensor.isOnAlarm();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(alarm);
	}
}
