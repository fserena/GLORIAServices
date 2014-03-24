/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.Date;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.results.ExperimentResult;
import eu.gloria.gs.services.log.action.ActionException;

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

		} catch (ActionException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
