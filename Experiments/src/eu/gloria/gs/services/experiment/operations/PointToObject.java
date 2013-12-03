/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class PointToObject extends ServiceOperation {

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
			String objectParameter = (String) this.getArguments()[1];

			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);
			String object = (String) this.getContext().getExperimentContext()
					.getParameterValue(objectParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> mounts;
			List<String> domes;

			try {
				mounts = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.MOUNT);
				domes = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.DOME);
			} catch (RTRepositoryException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			if (domes != null && domes.size() > 0) {
				String domeName = domes.get(0);
				try {
					DomeOpeningState domeState = this.getDomeTeleoperation()
							.getState(rtName, domeName);

					System.out.println(domeState.name());

					if (domeState.equals(DomeOpeningState.UNDEFINED)
							|| domeState.equals(DomeOpeningState.CLOSED)) {
						this.getDomeTeleoperation().open(rtName, domeName);
					}
				} catch (DomeTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
			}

			if (mounts != null && mounts.size() > 0) {

				String mountName = mounts.get(0);

				try {

					/*
					 * this.getMountTeleoperation().setTrackingRate(rtName,
					 * mountName, TrackingRate.DRIVE_SOLAR);
					 * 
					 * this.getMountTeleoperation().setTracking(rtName,
					 * mountName, true);
					 */

					/*
					 * this.getMountTeleoperation().setSlewRate(rtName,
					 * mountName, "CENTER");
					 */

					this.getMountTeleoperation().slewToObject(rtName,
							mountName, object);

				} catch (MountTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
			}

			else {
				throw new ExperimentOperationException(
						"No mount available on the '" + rtName + "' RT");
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
