/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class Iteration extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		String operationName = (String) this.getArguments()[0];
		String cursorNameParameter = (String) this.getArguments()[1];
		String maxIterationsNameParameter = (String) this.getArguments()[2];

		try {
			int cursorInit = (Integer) this.getContext().getExperimentContext()
					.getParameterValue(cursorNameParameter);
			int maxIterations = (Integer) this.getContext()
					.getExperimentContext()
					.getParameterValue(maxIterationsNameParameter);

			for (int i = cursorInit; i < maxIterations; i++) {
				OperationContext subContext = null;
				try {
					subContext = this.getContext().getExperimentContext()
							.getOperation(operationName);
				} catch (NoSuchOperationException e) {
					throw new ExperimentOperationException(e.getAction());
				}
				subContext.execute();

				this.getContext().getExperimentContext()
						.setParameterValue(cursorNameParameter, i + 1);
			}

		} catch (NoSuchParameterException | ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
