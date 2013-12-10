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
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SetTrackingRate extends ServiceOperation {

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
				throw new ExperimentOperationException("Tracking rate cannot be null");
			}

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			this.getMountTeleoperation().setTrackingRate(rtName, mountName,
					TrackingRate.valueOf(rate));
			this.getMountTeleoperation().setTracking(rtName, mountName, true);

		} catch (DeviceOperationFailedException | MountTeleoperationException
				| ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
