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
import eu.gloria.gs.services.teleoperation.focuser.FocuserTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class FocusRelativeMove extends TeleOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		String rtName = null;
		String focusName = null;
		Integer steps = null;

		try {
			String rtNameParameter = (String) this.getArguments()[0];
			rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String focusNameParameter = (String) this.getArguments()[1];
			focusName = (String) this.getContext().getExperimentContext()
					.getParameterValue(focusNameParameter);

			String stepsParameter = (String) this.getArguments()[2];
			steps = (Integer) this.getContext().getExperimentContext()
					.getParameterValue(stepsParameter);
		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}

		try {
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			this.getFocuserTeleoperation().moveRelative(rtName, focusName,
					steps);

		} catch (FocuserTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}
}
