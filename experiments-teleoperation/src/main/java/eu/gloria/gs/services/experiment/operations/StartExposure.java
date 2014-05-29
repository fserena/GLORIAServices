/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import java.util.LinkedHashMap;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class StartExposure extends TeleOperation {

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

			String targetParameter = (String) this.getArguments()[2];
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> target = (LinkedHashMap<String, Object>) this
					.getContext().getExperimentContext()
					.getParameterValue(targetParameter);

			ImageTargetData targetData = new ImageTargetData();
			targetData.setObject((String) target.get("object"));

			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> coordinates = (LinkedHashMap<String, Object>) target
					.get("coordinates");

			Object paramValue = coordinates.get("ra");
			double ra;

			if (paramValue instanceof Integer) {
				ra = (int) (Integer) paramValue;
			} else {
				ra = (Double) paramValue;
			}

			paramValue = coordinates.get("dec");
			double dec;

			if (paramValue instanceof Integer) {
				dec = (int) (Integer) paramValue;
			} else {
				dec = (Double) paramValue;
			}

			targetData.setRa(ra);
			targetData.setDec(dec);

			String imageIdParameter = (String) this.getArguments()[3];
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String localId = null;
			int imageId = -1;

			try {
				localId = this.getCCDTeleoperation().startExposure(rtName,
						camName);

				ImageInformation imageInfo = (ImageInformation) this
						.getImageRepository().getImageInformationByRTLocaId(
								rtName, localId);
				this.getImageRepository().setTargetByRTLocalId(rtName, localId,
						targetData);

				imageId = imageInfo.getId();

				int rid = this.getContext().getExperimentContext()
						.getReservation();

				this.getImageRepository()
						.setExperimentReservation(imageId, rid);

				this.getImageRepository().setUser(
						imageId,
						this.getAdapter().getReservationInformation(rid)
								.getUser());

			} catch (DeviceOperationFailedException e) {
			} catch (ActionException e) {
				throw new ExperimentOperationException(e.getAction());
			}

			this.getContext().getExperimentContext()
					.setParameterValue(imageIdParameter, imageId);

		} catch (ExperimentParameterException | NoSuchParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getAction());
		}
	}

}
