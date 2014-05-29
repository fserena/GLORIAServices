package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.RHSensor;

public class IsRHOnAlarmOperation extends RHSensorOperation {

	public IsRHOnAlarmOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateRHSensor(RHSensor rhSensor, OperationReturn returns)
			throws TeleoperationException {
		boolean alarm = rhSensor.isOnAlarm();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(alarm);
	}
}
