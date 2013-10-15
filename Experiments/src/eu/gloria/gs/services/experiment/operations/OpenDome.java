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
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class OpenDome extends ServiceOperation {

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

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			this.getDomeTeleoperation().open(rtName, domeName);
		} catch (DomeTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
