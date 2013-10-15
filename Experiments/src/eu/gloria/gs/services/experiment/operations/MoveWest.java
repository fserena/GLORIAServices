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
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class MoveWest extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		String rtName = null;
		String mountName = null;
		int maxMoves = 0;
		int currentMoves = -1;
		String currentMovesParameter = null;

		try {
			String rtNameParameter = (String) this.getArguments()[0];
			rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String domeNameParameter = (String) this.getArguments()[1];
			mountName = (String) this.getContext().getExperimentContext()
					.getParameterValue(domeNameParameter);

			String maxMovesParameter = (String) this.getArguments()[2];
			maxMoves = (Integer) this.getContext().getExperimentContext()
					.getParameterValue(maxMovesParameter);

			currentMovesParameter = (String) this.getArguments()[3];
			currentMoves = (Integer) this.getContext().getExperimentContext()
					.getParameterValue(currentMovesParameter);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			if (currentMoves > -maxMoves) {
				this.getMountTeleoperation().moveWest(rtName, mountName);

				this.getContext()
						.getExperimentContext()
						.setParameterValue(currentMovesParameter,
								currentMoves - 1);
			} else {
				throw new ExperimentOperationException(
						"Cannot move left because it is on the limit");
			}			
		} catch (MountTeleoperationException | DeviceOperationFailedException
				| UndefinedExperimentParameterException
				| NoSuchExperimentException | ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
