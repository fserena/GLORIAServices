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
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class GetStream extends ServiceOperation {

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) this.getArguments()[0];
			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) this.getArguments()[1];
			String camName = (String) this.getContext().getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = this.getSCamTeleoperation().getImageURL(rtName,
					camName);

			this.getContext().getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| SCamTeleoperationException | DeviceOperationFailedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
