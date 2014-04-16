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
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadContinuousImage extends TeleOperation {

	@Override
	public void execute() throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) this.getArguments()[0];
			String rtName = (String) this.getContext().getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) this.getArguments()[1];
			String camName = (String) this.getContext().getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = null;
			String imageId = null;

			try {
				imageId = this.getCCDTeleoperation().startContinueMode(rtName,
						camName);

			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getAction());
			} catch (DeviceOperationFailedException e) {
			}

			int retries = 0;

			while (retries < 50 && url == null) {

				try {
					url = this.getCCDTeleoperation().getImageURL(rtName,
							camName, imageId, ImageExtensionFormat.JPG);

				} catch (ImageNotAvailableException e) {
					try {
						Thread.sleep((int) (100));
					} catch (InterruptedException s) {
					}
				} catch (CCDTeleoperationException e) {
					ExperimentOperationException ex = new ExperimentOperationException(
							e.getAction());
					ex.getAction().put("retries", retries);
					ex.getAction().child("exception", e.getAction());
					throw ex;
				}

				retries++;
			}

			if (url == null) {
				ExperimentOperationException ex = new ExperimentOperationException(
						this.getContext().getName(), "url not available");
				ex.getAction().put("retries", retries);
				throw ex;
			}

			this.getContext().getExperimentContext()
					.setParameterValue(urlParameter, url);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
