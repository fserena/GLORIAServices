/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class GetCCDCapabilities extends TeleOperation {

	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) this.getArguments()[0];
			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) this.getArguments()[1];
			String camName = (String) this.getContext().getExperimentContext()
					.getParameterValue(camNameParameter);

			String featuresParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			Map<String, Object> capabilities = new LinkedHashMap<String, Object>();
			boolean modifiable = false;

			try {
				modifiable = this.getCCDTeleoperation().gainIsModifiable(
						rtName, camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getAction());
			} catch (DeviceOperationFailedException e) {
			}

			capabilities.put("set-gain", modifiable);

			try {
				modifiable = this.getCCDTeleoperation().gammaIsModifiable(
						rtName, camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getAction());
			} catch (DeviceOperationFailedException e) {
			}

			capabilities.put("set-gamma", modifiable);

			this.getContext().getExperimentContext()
					.setParameterValue(featuresParameter, capabilities);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}
}
