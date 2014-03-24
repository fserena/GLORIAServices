package eu.gloria.gs.services.teleoperation.weather.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Barometer;

public class GetPressureOperation extends BarometerOperation {

	public GetPressureOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateBarometer(Barometer barometer, OperationReturn returns)
			throws TeleoperationException {
		double pressure = barometer.getPressure();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(pressure);
	}
}
