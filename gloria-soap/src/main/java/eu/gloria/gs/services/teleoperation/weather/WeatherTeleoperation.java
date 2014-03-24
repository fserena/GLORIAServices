package eu.gloria.gs.services.teleoperation.weather;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
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
		super(WeatherTeleoperation.class.getSimpleName());
	}

	@Override
	public double getPressure(String rt, String barometer)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(GetPressureOperation.class,
					rt, barometer);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isPressureOnAlarm(String rt, String barometer)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Boolean) this.invokeGetOperation(
					IsPressureOnAlarmOperation.class, rt, barometer);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getTemperature(String rt, String tempSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(
					GetTemperatureOperation.class, rt, tempSensor);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isTemperatureOnAlarm(String rt, String tempSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Boolean) this.invokeGetOperation(
					IsTemperatureOnAlarmOperation.class, rt, tempSensor);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getWindSpeed(String rt, String windSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(
					GetWindSpeedOperation.class, rt, windSensor);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}		
	}

	@Override
	public boolean isWindOnAlarm(String rt, String windSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Boolean) this.invokeGetOperation(
					IsWindOnAlarmOperation.class, rt, windSensor);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}		
	}

	@Override
	public double getRelativeHumidity(String rt, String rhSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(
					GetRelativeHumidityOperation.class, rt, rhSensor);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}		
	}

	@Override
	public boolean isRHOnAlarm(String rt, String rhSensor)
			throws DeviceOperationFailedException,
			WeatherTeleoperationException {
		try {
			return (Boolean) this.invokeGetOperation(
					IsRHOnAlarmOperation.class, rt, rhSensor);
		} catch (TeleoperationException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}		
	}
}
