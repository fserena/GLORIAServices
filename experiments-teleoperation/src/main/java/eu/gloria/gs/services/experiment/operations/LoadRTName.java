/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.List;

import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.log.action.ActionException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class LoadRTName extends TeleOperation {

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		int rid = this.getContext().getExperimentContext().getReservation();

		ReservationInformation resInfo = null;

		String rtOrderParameter = (String) this.getArguments()[0];
		String rtNameParameter = (String) this.getArguments()[1];
		String telescopeName = null;

		try {
			resInfo = this.getAdapter().getReservationInformation(rid);
			int rtOrder = (Integer) this.getContext().getExperimentContext()
					.getParameterValue(rtOrderParameter);

			List<String> telescopes = resInfo.getTelescopes();
			telescopeName = telescopes.get(rtOrder);
		} catch (ActionException e) {
			throw new ExperimentOperationException(e.getAction());
		}

		try {
			this.getContext().getExperimentContext().setParameterValue(
					rtNameParameter, telescopeName);
		} catch (NoSuchParameterException
				| ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
