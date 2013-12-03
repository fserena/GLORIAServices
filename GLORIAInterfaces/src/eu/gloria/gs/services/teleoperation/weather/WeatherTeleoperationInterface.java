package eu.gloria.gs.services.teleoperation.weather;

import javax.jws.WebService;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;

@WebService(name = "WeatherTeleoperationInterface", targetNamespace = "http://weather.teleoperation.services.gs.gloria.eu/")
public interface WeatherTeleoperationInterface {

	public double getPressure(String rt, String barometer)
			throws DeviceOperationFailedException, WeatherTeleoperationException;

	public double getWindSpeed(String rt, String windSensor)
			throws DeviceOperationFailedException, WeatherTeleoperationException;

	public double getRelativeHumidity(String rt, String rhSensor)
			throws DeviceOperationFailedException, WeatherTeleoperationException;
	
	public double getTemperature(String rt, String tempSensor)
			throws DeviceOperationFailedException, WeatherTeleoperationException;
}
