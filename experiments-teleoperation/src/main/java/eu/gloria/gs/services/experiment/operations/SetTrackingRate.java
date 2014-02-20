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
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SetTrackingRate extends TeleOperation {

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
			String mountParameter = (String) this.getArguments()[1];
			String rateParameter = (String) this.getArguments()[2];

			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);
			String mountName = (String) this.getContext()
					.getExperimentContext().getParameterValue(mountParameter);
			String rate = (String) this.getContext().getExperimentContext()
					.getParameterValue(rateParameter);
			
			if (rate == null) {
				ExperimentOperationException ex = new ExperimentOperationException(
						this.getContext().getName(), "tracking rate cannot be null");
				throw ex;
			}

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			this.getMountTeleoperation().setTrackingRate(rtName, mountName,
					TrackingRate.valueOf(rate));
			this.getMountTeleoperation().setTracking(rtName, mountName, true);

		} catch (DeviceOperationFailedException | MountTeleoperationException
				| ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
