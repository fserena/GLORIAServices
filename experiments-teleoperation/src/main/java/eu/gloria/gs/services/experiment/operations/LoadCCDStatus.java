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
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadCCDStatus extends TeleOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		String rtName = null;
		String ccdName = null;

		try {
			String rtNameParameter = (String) this.getArguments()[0];
			rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String ccdNameParameter = (String) this.getArguments()[1];
			ccdName = (String) this.getContext().getExperimentContext()
					.getParameterValue(ccdNameParameter);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}

		String ccdStatusParameter = (String) this.getArguments()[2];

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String ccdStatus = this.getCCDTeleoperation()
					.getState(rtName, ccdName).name();

			this.getContext().getExperimentContext()
					.setParameterValue(ccdStatusParameter, ccdStatus);
		} catch (CCDTeleoperationException | DeviceOperationFailedException
				| NoSuchParameterException | ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
