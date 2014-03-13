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
import eu.gloria.gs.services.teleoperation.weather.operations.IsPressureOnAlarmOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsRHOnAlarmOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsTemperatureOnAlarmOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsWindOnAlarmOperation;

public class WeatherTeleoperation extends AbstractTeleoperation implements
		WeatherTeleoperationInterface {

	public WeatherTeleoperation() {
		this.createLogger(WeatherTeleoperation.class);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface
	 * #getPressure(java.lang.String, java.lang.String)
	 */
	@Override
	public double getPressure(String rt, String barometer)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get pressure";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(barometer);

		GetPressureOperation operation = null;

		try {
			operation = new GetPressureOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, barometer, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double pressure = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, barometer, operationName,
					args.getArguments(), pressure);

			return pressure;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, barometer, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, barometer, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isPressureOnAlarm(String rt, String barometer)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get pressure alarm";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(barometer);

		IsPressureOnAlarmOperation operation = null;

		try {
			operation = new IsPressureOnAlarmOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, barometer, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			boolean alarm = (Boolean) returns.getReturns().get(0);

			this.processSuccess(rt, barometer, operationName,
					args.getArguments(), alarm);

			return alarm;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, barometer, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, barometer, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getTemperature(String rt, String tempSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		String operationName = "get temperature";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(tempSensor);

		GetTemperatureOperation operation = null;

		try {
			operation = new GetTemperatureOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, tempSensor, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double temperature = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, tempSensor, operationName,
					args.getArguments(), temperature);

			return temperature;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, tempSensor, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, tempSensor, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isTemperatureOnAlarm(String rt, String tempSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get temperature alarm";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(tempSensor);

		IsTemperatureOnAlarmOperation operation = null;

		try {
			operation = new IsTemperatureOnAlarmOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, tempSensor, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			boolean alarm = (Boolean) returns.getReturns().get(0);

			this.processSuccess(rt, tempSensor, operationName,
					args.getArguments(), alarm);

			return alarm;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, tempSensor, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, tempSensor, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface
	 * #getWindSpeed(java.lang.String, java.lang.String)
	 */
	@Override
	public double getWindSpeed(String rt, String windSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get wind speed";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(windSensor);

		GetWindSpeedOperation operation = null;

		try {
			operation = new GetWindSpeedOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, windSensor, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double windSpeed = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, windSensor, operationName,
					args.getArguments(), windSpeed);

			return windSpeed;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, windSensor, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, windSensor, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isWindOnAlarm(String rt, String windSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get wind alarm";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(windSensor);

		IsWindOnAlarmOperation operation = null;

		try {
			operation = new IsWindOnAlarmOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, windSensor, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			boolean alarm = (Boolean) returns.getReturns().get(0);

			this.processSuccess(rt, windSensor, operationName,
					args.getArguments(), alarm);

			return alarm;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, windSensor, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, windSensor, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface
	 * #getRelativeHumidity(java.lang.String, java.lang.String)
	 */
	@Override
	public double getRelativeHumidity(String rt, String rhSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get relative humidity";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(rhSensor);

		GetRelativeHumidityOperation operation = null;

		try {
			operation = new GetRelativeHumidityOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, rhSensor, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double humidity = (Double) returns.getReturns().get(0);

			this.processSuccess(rt, rhSensor, operationName,
					args.getArguments(), humidity);

			return humidity;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, rhSensor, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, rhSensor, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isRHOnAlarm(String rt, String rhSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {

		String operationName = "get rh alarm";

		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(rhSensor);

		IsRHOnAlarmOperation operation = null;

		try {
			operation = new IsRHOnAlarmOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, rhSensor, operationName,
					args.getArguments());

			throw new WeatherTeleoperationException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			boolean alarm = (Boolean) returns.getReturns().get(0);

			this.processSuccess(rt, rhSensor, operationName,
					args.getArguments(), alarm);

			return alarm;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, rhSensor, operationName,
					args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, rhSensor, operationName,
					args.getArguments());
			throw new WeatherTeleoperationException(e.getAction());
		}
	}
}
