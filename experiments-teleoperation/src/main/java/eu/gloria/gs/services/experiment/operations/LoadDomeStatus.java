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
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadDomeStatus extends TeleOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		String rtName = null;
		String domeName = null;

		try {
			String rtNameParameter = (String) this.getArguments()[0];
			rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String domeNameParameter = (String) this.getArguments()[1];
			domeName = (String) this.getContext().getExperimentContext()
					.getParameterValue(domeNameParameter);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}

		String domeStatusParameter = (String) this.getArguments()[2];

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String domeStatus = this.getDomeTeleoperation()
					.getState(rtName, domeName).name();

			this.getContext().getExperimentContext()
					.setParameterValue(domeStatusParameter, domeStatus);
		} catch (DomeTeleoperationException | DeviceOperationFailedException
				| NoSuchParameterException | ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
