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
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SetCCDExposure extends ServiceOperation {

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

			String camNameParameter = (String) this.getArguments()[1];
			String camName = (String) this.getContext().getExperimentContext()
					.getParameterValue(camNameParameter);

			String exposureParameter = (String) this.getArguments()[2];

			Object paramValue = this.getContext().getExperimentContext()
					.getParameterValue(exposureParameter);

			double exposure;

			if (paramValue instanceof Integer) {
				exposure = (int) (Integer) paramValue;
			} else {
				exposure = (Double) paramValue;
			}

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			try {
				this.getCCDTeleoperation().setExposureTime(rtName, camName,
						Math.min(120, exposure));
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getAction());
			} catch (DeviceOperationFailedException e) {

			}

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
