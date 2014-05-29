package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.WindSensor;

public class GetWindSpeedOperation extends WindSensorOperation {

	public GetWindSpeedOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateWindSensor(WindSensor windSensor, OperationReturn returns)
			throws TeleoperationException {
		double speed = windSensor.getWindSpeed();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(speed);
	}
}
