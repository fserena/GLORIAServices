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

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class MoveNorth extends TeleOperation {

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

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			if (currentMoves < maxMoves) {
				this.getMountTeleoperation().moveNorth(rtName, mountName);

				this.getContext()
						.getExperimentContext()
						.setParameterValue(currentMovesParameter,
								currentMoves + 1);
			} else {
				ExperimentOperationException ex = new ExperimentOperationException(this.getContext().getName(), "on north limit");
				ex.getAction().put("max", maxMoves);
				ex.getAction().put("current", currentMoves);
				
				throw ex;
			}
		} catch (MountTeleoperationException | DeviceOperationFailedException
				| NoSuchParameterException | ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
