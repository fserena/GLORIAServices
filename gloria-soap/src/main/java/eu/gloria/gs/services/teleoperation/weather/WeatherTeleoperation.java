package eu.gloria.gs.services.teleoperation.weather;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.generic.GenericTeleoperationException;
import eu.gloria.gs.services.teleoperation.weather.operations.GetPressureOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.GetRelativeHumidityOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.GetTemperatureOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.GetWindSpeedOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsPressureOnAlarmOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsRHOnAlarmOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsTemperatureOnAlarmOperation;
import eu.gloria.gs.services.teleoperation.weather.operations.IsWindOnAlarmOperation;
import eu.gloria.rti.client.RTSHandler;

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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
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
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new WeatherTeleoperationException(e.getAction());
		}
	}
	
	@Override
	public boolean isOnWeatherAlarm(String rt)
			throws WeatherTeleoperationException {

		String operationName = "is on weather alarm";

		Action action = new Action();
		action.put("name", operationName);
		action.put("rt", rt);

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			boolean alarm = rtHandler.isOnWeatherAlarm();
			action.put("alarm", alarm);
			
			this.logInfo(rt, this.getClientUsername(), action);
			
			return alarm;
		} catch (TeleoperationException e) {
			this.logException(action, e);
			throw new WeatherTeleoperationException(action);
		}
	}
}
