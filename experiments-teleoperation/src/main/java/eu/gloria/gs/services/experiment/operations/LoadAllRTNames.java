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
public class LoadAllRTNames extends TeleOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		int rid = this.getContext().getExperimentContext().getReservation();

		ReservationInformation resInfo = null;

		String rtNameListParameter = (String) this.getArguments()[0];
		List<String> telescopes = null;

		try {
			resInfo = this.getAdapter().getReservationInformation(rid);

			telescopes = resInfo.getTelescopes();
		} catch (ActionException e) {
			throw new ExperimentOperationException(e.getAction());
		}

		try {
			this.getContext().getExperimentContext()
					.setParameterValue(rtNameListParameter, telescopes);
		} catch (NoSuchParameterException | ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
