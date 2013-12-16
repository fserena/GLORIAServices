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
public class GetPressure extends ServiceOperation {

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

			String barometerNameParameter = (String) this.getArguments()[1];
			String barometerName = (String) this.getContext()
					.getExperimentContext()
					.getParameterValue(barometerNameParameter);

			String pressureParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double pressure = -1;

			try {
				pressure = this.getWeatherTeleoperation().getPressure(rtName,
						barometerName);
			} catch (WeatherTeleoperationException e) {
				throw new ExperimentOperationException(e.getAction());
			} catch (DeviceOperationFailedException e) {

			}

			this.getContext().getExperimentContext()
					.setParameterValue(pressureParameter, pressure);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException
			 e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}
}
