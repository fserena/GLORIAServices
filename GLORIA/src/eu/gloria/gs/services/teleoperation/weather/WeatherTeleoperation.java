package eu.gloria.gs.services.teleoperation.weather;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.weather.operations.GetPressureOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.GetRelativeHumidityOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.GetTemperatureOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.GetWindSpeedOperation;

public class WeatherTeleoperation extends AbstractTeleoperation implements
		WeatherTeleoperationInterface {	

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface#getPressure(java.lang.String, java.lang.String)
	 */
	@Override
	public double getPressure(String rt, String barometer)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(barometer);

		GetPressureOperation operation = null;

		try {
			operation = new GetPressureOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getPressure/Bad args", rt);

			throw new WeatherTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double pressure = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, barometer, "getPressure", null, pressure);

			return pressure;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new WeatherTeleoperationException(e.getMessage());
		}
	}
	
	@Override
	public double getTemperature(String rt, String tempSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(tempSensor);

		GetTemperatureOperation operation = null;

		try {
			operation = new GetTemperatureOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getTemperature/Bad args", rt);

			throw new WeatherTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double temperature = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, tempSensor, "getTemperature", null, temperature);

			return temperature;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new WeatherTeleoperationException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface#getWindSpeed(java.lang.String, java.lang.String)
	 */
	@Override
	public double getWindSpeed(String rt, String windSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(windSensor);

		GetWindSpeedOperation operation = null;

		try {
			operation = new GetWindSpeedOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getWindSpeed/Bad args", rt);

			throw new WeatherTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double windSpeed = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, windSensor, "getWindSpeed", null, windSpeed);

			return windSpeed;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new WeatherTeleoperationException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface#getRelativeHumidity(java.lang.String, java.lang.String)
	 */
	@Override
	public double getRelativeHumidity(String rt, String rhSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(rhSensor);

		GetRelativeHumidityOperation operation = null;

		try {
			operation = new GetRelativeHumidityOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getRelativeHumidity/Bad args", rt);

			throw new WeatherTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double humidity = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, rhSensor, "getRelativeHumidity", null, humidity);

			return humidity;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new WeatherTeleoperationException(e.getMessage());
		}
	}
}
