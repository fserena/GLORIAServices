/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.fw.FilterWheelTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadFilters extends TeleOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) this.getArguments()[0];
			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String fwNameParameter = (String) this.getArguments()[1];
			String fwName = (String) this.getContext().getExperimentContext()
					.getParameterValue(fwNameParameter);

			String listParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> filters = this.getFilterWheelTeleoperation()
					.getFilters(rtName, fwName);

			this.getContext().getExperimentContext()
					.setParameterValue(listParameter, filters);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException
				| FilterWheelTeleoperationException
				| DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}
}
