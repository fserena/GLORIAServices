/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class Sequence extends ServiceOperation {

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		for (Object argument : this.getArguments()) {
			String operationName = (String) argument;
			
			OperationContext subContext = null;
			try {
				subContext = this.getContext().getExperimentContext()
						.getOperation(operationName);
			} catch (NoSuchOperationException e) {
				throw new ExperimentOperationException(e.getAction());
			}
			subContext.execute();
		}
	}

}
