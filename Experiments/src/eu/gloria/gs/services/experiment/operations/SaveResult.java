/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.io.IOException;
import java.util.Date;

import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.base.results.ExperimentResult;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SaveResult extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		String parameterName = (String) this.getArguments()[0];

		try {
			Object value = this.getContext().getExperimentContext()
					.getParameterValue(parameterName);
			try {

				int rid = this.getContext().getExperimentContext()
						.getReservation();
				String user = this.getAdapter().getReservationInformation(rid)
						.getUser();

				ExperimentResult result = new ExperimentResult();
				result.setAdapter(this.getAdapter());
				result.setDate(new Date());
				result.setUser(user);
				result.setContext(this.getContext().getExperimentContext()
						.getReservation());
				result.setTag(parameterName);
				result.setValue(value);

				result.save();

			} catch (NoSuchReservationException | ExperimentDatabaseException
					| IOException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e1) {
			throw new ExperimentOperationException(e1.getMessage());
		}
	}

}
