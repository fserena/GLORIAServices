/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadContinuousImage extends ServiceOperation {

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

			String urlParameter = (String) this.getArguments()[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = null;
			String imageId = null;
			/*double exposure = 0.0;

			try {
				this.getCCDTeleoperation().stopContinueMode(rtName, camName);
				exposure = this.getCCDTeleoperation().getExposureTime(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}*/

			try {
				imageId = this.getCCDTeleoperation().startContinueMode(rtName,
						camName);

			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

			int retries = 0;

			while (retries < 10 && url == null) {

				try {
					url = this.getCCDTeleoperation().getImageURL(rtName,
							camName, imageId, ImageExtensionFormat.JPG);

				} catch (ImageNotAvailableException e) {
					try {
						Thread.sleep((int) (1000 + 100));
					} catch (InterruptedException s) {
					}
				} catch (CCDTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				}

				retries++;
			}

			if (url == null) {
				throw new ExperimentOperationException(
						"Cannot recover the continuous image url from the camera");
			}

			this.getContext().getExperimentContext()
					.setParameterValue(urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
