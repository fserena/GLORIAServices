/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class PointToObject extends TeleOperation {

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
			String trackingParameter = (String) this.getArguments()[2];

			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);
			String object = (String) this.getContext().getExperimentContext()
					.getParameterValue(objectParameter);
			String tracking = (String) this.getContext().getExperimentContext()
					.getParameterValue(trackingParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> mounts;
			// List<String> domes;

			try {
				mounts = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.MOUNT);
				// domes = this.getRTRepository().getRTDeviceNames(rtName,
				// DeviceType.DOME);
			} catch (RTRepositoryException e) {
				throw new ExperimentOperationException(e.getAction());
			}

			/*
			 * if (domes != null && domes.size() > 0) { String domeName =
			 * domes.get(0); try { DomeOpeningState domeState =
			 * this.getDomeTeleoperation() .getState(rtName, domeName);
			 * 
			 * System.out.println(domeState.name());
			 * 
			 * if (domeState.equals(DomeOpeningState.CLOSED)) {
			 * this.getDomeTeleoperation().open(rtName, domeName); } } catch
			 * (DomeTeleoperationException e) { throw new
			 * ExperimentOperationException(e.getAction()); } catch
			 * (DeviceOperationFailedException e) { } }
			 */

			if (mounts != null && mounts.size() > 0) {

				String mountName = mounts.get(0);
				try {
					this.getMountTeleoperation().setTracking(rtName, mountName,
							false);
				} catch (DeviceOperationFailedException
						| MountTeleoperationException e) {
				}

				try {
					this.getMountTeleoperation().setTrackingRate(rtName,
							mountName, TrackingRate.valueOf(tracking));

					this.getMountTeleoperation().setTracking(rtName, mountName,
							true);

				} catch (MountTeleoperationException
						| DeviceOperationFailedException e) {
				}

				try {

					this.getMountTeleoperation().slewToObject(rtName,
							mountName, object);

				} catch (MountTeleoperationException
						| DeviceOperationFailedException e) {
					throw new ExperimentOperationException(e.getAction());
				}
			}

			else {
				ExperimentOperationException ex = new ExperimentOperationException(
						this.getContext().getName(), "no mount available");
				ex.getAction().put("rt", rtName);
				throw ex;
			}

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
