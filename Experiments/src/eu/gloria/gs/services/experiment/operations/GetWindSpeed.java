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
public class GetWindSpeed extends ServiceOperation {

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

			String windSensorNameParameter = (String) this.getArguments()[1];
			String windSensorName = (String) this.getContext().getExperimentContext()
					.getParameterValue(windSensorNameParameter);

			String windSpeedParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double windSpeed = -1;

			try {
				windSpeed = this.getWeatherTeleoperation().getWindSpeed(rtName, windSensorName);
			} catch (WeatherTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			this.getContext().getExperimentContext()
					.setParameterValue(windSpeedParameter, windSpeed);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}
}
