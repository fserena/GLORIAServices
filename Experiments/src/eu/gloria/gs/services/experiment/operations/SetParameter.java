/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;


import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class SetParameter extends ServiceOperation {

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String parameterName = (String) this.getArguments()[0];

			String valueParameter = (String) this.getArguments()[1];
			Object value = (Object) this.getContext().getExperimentContext()
					.getParameterValue(valueParameter);

			try {
				this.getContext().getExperimentContext().setParameterValue(
						parameterName, value);
			} catch (NoSuchExperimentException
					| UndefinedExperimentParameterException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
