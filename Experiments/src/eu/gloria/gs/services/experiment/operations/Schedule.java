/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.Date;
import java.util.Timer;

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
public class Schedule extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String delayParameter = (String) this.getArguments()[0];
			String operationName = (String) this.getArguments()[1];

			double delay = 0;

			try {
				delay = (Double) this.getContext().getExperimentContext()
						.getParameterValue(delayParameter);
			} catch (NoSuchParameterException e) {
				throw new ExperimentOperationException(e.getAction());
			}

			OperationContext subContext = null;
			try {
				subContext = this.getContext().getExperimentContext()
						.getOperation(operationName);
			} catch (NoSuchOperationException e) {
				throw new ExperimentOperationException(e.getAction());
			}

			Timer timer = new Timer();
			timer.schedule(new ScheduleTask(subContext),
					Math.max(0, (int) (delay * 1000)));

		} catch (ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
