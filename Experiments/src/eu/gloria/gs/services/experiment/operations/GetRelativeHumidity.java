/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class GetRelativeHumidity extends ServiceOperation {

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
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			this.getContext().getExperimentContext()
					.setParameterValue(humidityParameter, humidity);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}
}
