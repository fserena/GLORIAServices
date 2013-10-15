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
import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class LoadImageUrls extends ServiceOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.operations.ServiceOperation#execute()
	 */
	@Override
	public void execute() throws ExperimentOperationException {

		Integer imageId = -1;
		String jpgParameter = null;
		String fitsParameter = null;

		try {
			String imageIdParameter = (String) this.getArguments()[0];
			imageId = (Integer) this.getContext().getExperimentContext()
					.getParameterValue(imageIdParameter);

			jpgParameter = (String) this.getArguments()[1];
			fitsParameter = (String) this.getArguments()[2];
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		String jpg = null;
		String fits = null;
		ImageInformation imageInfo = null;
		boolean imageFound = true;

		try {
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			imageInfo = this.getImageRepository().getImageInformation(imageId);
		} catch (ImageRepositoryException e) {
			imageFound = false;
		}

		try {
			if (imageFound) {
				imageInfo = this.getImageRepository().getImageInformation(
						imageId);

				jpg = imageInfo.getJpg();
				fits = imageInfo.getFits();
			}

			this.getContext().getExperimentContext()
					.setParameterValue(jpgParameter, jpg);

			this.getContext().getExperimentContext()
					.setParameterValue(fitsParameter, fits);
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException
				| ImageRepositoryException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

}
