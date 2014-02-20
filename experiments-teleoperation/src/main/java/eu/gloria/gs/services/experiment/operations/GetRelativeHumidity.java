/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class GetRelativeHumidity extends TeleOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) this.getArguments()[0];
			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String rhSensorNameParameter = (String) this.getArguments()[1];
			String rhSensorName = (String) this.getContext()
					.getExperimentContext()
					.getParameterValue(rhSensorNameParameter);

			String humidityParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double humidity = -1;

			try {
				humidity = this.getWeatherTeleoperation().getRelativeHumidity(
						rtName, rhSensorName);
			} catch (WeatherTeleoperationException e) {
				throw new ExperimentOperationException(e.getAction());
			} catch (DeviceOperationFailedException e) {

			}

			this.getContext().getExperimentContext()
					.setParameterValue(humidityParameter, humidity);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}
}
