package eu.gloria.gs.services.experiment.online.operations;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.contexts.ContextNotReadyException;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextService;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.base.results.ExperimentResult;
import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationException;
import eu.gloria.gs.services.teleoperation.focuser.FocuserTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationException;

public class OperationContextService extends ExperimentContextService {

	public void treatOperation(OperationContext operationContext,
			String operation, Object... operationArguments)
			throws ExperimentOperationException {

		if (operation.equals("pointToObject")) {
			this.pointToObject(operationContext, operationArguments);
		} else if (operation.equals("park")) {
			this.park(operationContext, operationArguments);
		} else if (operation.equals("getStream")) {
			this.getStream(operationContext, operationArguments);
		} else if (operation.equals("takeImage")) {
			this.takeImage(operationContext, operationArguments);
		} else if (operation.equals("constrainedMountMove")) {
			this.constrainedMountMove(operationContext, operationArguments);
		} else if (operation.equals("domeMove")) {
			this.domeMove(operationContext, operationArguments);
		} else if (operation.equals("getContinuousImage")) {
			this.getContinuousImage(operationContext, operationArguments);
		} else if (operation.equals("loadCCDAttributes")) {
			this.loadCCDAttributes(operationContext, operationArguments);
		} else if (operation.equals("saveCCDAttributes")) {
			this.saveCCDAttributes(operationContext, operationArguments);
		} else if (operation.equals("setParameter")) {
			this.setParameter(operationContext, operationArguments);
		} else if (operation.equals("executeSequence")) {
			this.executeSequence(operationContext, operationArguments);
		} else if (operation.equals("changeFocusRelative")) {
			this.changeFocusRelative(operationContext, operationArguments);
		} else if (operation.equals("stopContinuousImage")) {
			this.stopContinuousImage(operationContext, operationArguments);
		} else if (operation.equals("loadImage")) {
			this.loadImageFromRepository(operationContext, operationArguments);
		} else if (operation.equals("saveResult")) {
			this.saveResult(operationContext, operationArguments);
		} else if (operation.equals("getRADEC")) {
			this.getRADEC(operationContext, operationArguments);
		} else if (operation.equals("loadRTName")) {
			this.loadRTName(operationContext, operationArguments);
		} else if (operation.equals("loadRTNames")) {
			this.loadAllRTNames(operationContext, operationArguments);
		} else if (operation.equals("loadDeviceName")) {
			this.loadDeviceName(operationContext, operationArguments);
		}
	}

	private void loadRTName(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		int rid = operationContext.getExperimentContext().getReservation();

		ReservationInformation resInfo = null;

		String rtOrderParameter = (String) operationArguments[0];
		String rtNameParameter = (String) operationArguments[1];
		String telescopeName = null;

		try {
			resInfo = this.getAdapter().getReservationInformation(rid);
			int rtOrder = (Integer) operationContext.getExperimentContext()
					.getParameterValue(rtOrderParameter);

			List<String> telescopes = resInfo.getTelescopes();
			telescopeName = telescopes.get(rtOrder);
		} catch (ExperimentDatabaseException | NoSuchReservationException
				| ExperimentParameterException
				| ExperimentNotInstantiatedException
				| NoSuchExperimentException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {
			operationContext.getExperimentContext().setParameterValue(
					rtNameParameter, telescopeName);
		} catch (UndefinedExperimentParameterException
				| NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void loadDeviceName(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		String rtParameter = (String) operationArguments[0];
		String deviceOrderParameter = (String) operationArguments[1];
		String deviceTypeParameter = (String) operationArguments[2];

		String rtName;
		try {
			rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtParameter);
		} catch (NoSuchExperimentException | ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		int deviceOrder;
		try {
			deviceOrder = (Integer) operationContext.getExperimentContext()
					.getParameterValue(deviceOrderParameter);
		} catch (NoSuchExperimentException | ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		DeviceType deviceType;
		try {
			String deviceTypeStr = (String) operationContext
					.getExperimentContext().getParameterValue(
							deviceTypeParameter);

			deviceType = DeviceType.valueOf(deviceTypeStr);
		} catch (NoSuchExperimentException | ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		List<String> deviceNames = null;

		try {
			deviceNames = this.getRTRepository().getRTDeviceNames(rtName,
					deviceType);
		} catch (RTRepositoryException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		String deviceName = deviceNames.get(deviceOrder);

		try {
			
			LinkedHashMap<String, Object> result = new LinkedHashMap<>();
			result.put("name", deviceName);
			
			operationContext.getExperimentContext().setParameterValue(
					(String) operationArguments[3], result);
		} catch (UndefinedExperimentParameterException
				| NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void loadAllRTNames(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		int rid = operationContext.getExperimentContext().getReservation();

		ReservationInformation resInfo = null;

		String rtNameListParameter = (String) operationArguments[0];
		List<String> telescopes = null;

		try {
			resInfo = this.getAdapter().getReservationInformation(rid);

			telescopes = resInfo.getTelescopes();
		} catch (ExperimentDatabaseException | NoSuchReservationException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {
			operationContext.getExperimentContext().setParameterValue(
					rtNameListParameter, telescopes);
		} catch (UndefinedExperimentParameterException
				| NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| ExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

	}

	private void executeSequence(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		for (Object argument : operationArguments) {
			String operationNameParameter = (String) argument;
			String operationName = null;

			try {
				operationName = (String) operationContext
						.getExperimentContext().getParameterValue(
								operationNameParameter);
			} catch (ExperimentParameterException | NoSuchExperimentException
					| ExperimentNotInstantiatedException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			OperationContext subContext = null;
			try {
				subContext = operationContext.getExperimentContext()
						.getOperation(operationName);
			} catch (NoSuchOperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			}
			subContext.execute();
		}
	}

	private void loadImageFromRepository(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String dateParameter = (String) operationArguments[0];
			String urlParameter = (String) operationArguments[1];

			Date day = null;

			try {
				day = (Date) operationContext.getExperimentContext()
						.getParameterValue(dateParameter);
			} catch (NoSuchExperimentException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			Date from = calendar.getTime();

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date to = calendar.getTime();

			List<Integer> imageIds;
			String url = null;
			try {
				imageIds = this.getImageRepository()
						.getAllImageIdentifiersByDate(from, to);

				if (imageIds != null) {
					int id = imageIds.get((int) (Math.random() * imageIds
							.size()));
					url = this.getImageRepository().getImageInformation(id)
							.getUrl();
				}
			} catch (ImageRepositoryException e1) {

			}

			try {
				operationContext.getExperimentContext().setParameterValue(
						urlParameter, url);
			} catch (NoSuchExperimentException
					| UndefinedExperimentParameterException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

		} catch (ExperimentParameterException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void saveResult(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		String parameterName = (String) operationArguments[0];

		try {
			Object value = operationContext.getExperimentContext()
					.getParameterValue(parameterName);

			try {

				ExperimentResult result = new ExperimentResult();
				result.setAdapter(this.getAdapter());
				result.setDate(new Date());
				result.setUser("gloria-admin");
				result.setContext(operationContext.getExperimentContext()
						.getReservation());
				result.setTag(parameterName);
				result.setValue(value);

				result.save();

			} catch (ExperimentDatabaseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ExperimentParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchExperimentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExperimentNotInstantiatedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void setParameter(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String parameterName = (String) operationArguments[0];

			String valueParameter = (String) operationArguments[1];
			Object value = (Object) operationContext.getExperimentContext()
					.getParameterValue(valueParameter);

			try {
				operationContext.getExperimentContext().setParameterValue(
						parameterName, value);
			} catch (NoSuchExperimentException
					| UndefinedExperimentParameterException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void saveCCDAttributes(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String brightnessParameter = (String) operationArguments[2];
			Integer brightness = (Integer) operationContext
					.getExperimentContext().getParameterValue(
							brightnessParameter);

			String contrastParameter = (String) operationArguments[3];
			Integer contrast = (Integer) operationContext
					.getExperimentContext()
					.getParameterValue(contrastParameter);

			String gainParameter = (String) operationArguments[4];
			Integer gain = (Integer) operationContext.getExperimentContext()
					.getParameterValue(gainParameter);

			String exposureParameter = (String) operationArguments[5];
			Double exposure = (Double) operationContext.getExperimentContext()
					.getParameterValue(exposureParameter);

			String gammaParameter = (String) operationArguments[6];
			Integer gamma = (Integer) operationContext.getExperimentContext()
					.getParameterValue(gammaParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			try {
				this.getCCDTeleoperation().setExposureTime(rtName, camName,
						Math.min(2.0, exposure));
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			try {
				this.getCCDTeleoperation().setBrightness(rtName, camName,
						brightness);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			try {
				this.getCCDTeleoperation().setContrast(rtName, camName,
						contrast);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

			try {
				this.getCCDTeleoperation().setGain(rtName, camName, gain);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

			try {
				this.getCCDTeleoperation().setGamma(rtName, camName, gamma);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void loadCCDAttributes(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String brightnessParameter = (String) operationArguments[2];
			String contrastParameter = (String) operationArguments[3];
			String gainParameter = (String) operationArguments[4];
			String exposureParameter = (String) operationArguments[5];
			String gammaParameter = (String) operationArguments[6];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double exposure = -1;
			int brightness = -1;
			int contrast = -1;
			int gain = -1;
			int gamma = -1;

			try {
				exposure = this.getCCDTeleoperation().getExposureTime(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			try {
				brightness = (int) this.getCCDTeleoperation().getBrightness(
						rtName, camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			try {
				contrast = (int) this.getCCDTeleoperation().getContrast(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			try {
				gain = (int) this.getCCDTeleoperation()
						.getGain(rtName, camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			try {
				gamma = (int) this.getCCDTeleoperation().getGamma(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {

			}

			operationContext.getExperimentContext().setParameterValue(
					brightnessParameter, brightness);
			operationContext.getExperimentContext().setParameterValue(
					contrastParameter, contrast);
			operationContext.getExperimentContext().setParameterValue(
					gainParameter, gain);
			operationContext.getExperimentContext().setParameterValue(
					gammaParameter, gamma);
			operationContext.getExperimentContext().setParameterValue(
					exposureParameter, exposure);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void getContinuousImage(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) operationArguments[2];

			String formatParameter = (String) operationArguments[3];
			String format = (String) operationContext.getExperimentContext()
					.getParameterValue(formatParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = null;
			String imageId = null;
			double exposure = 0.0;

			try {
				this.getCCDTeleoperation().stopContinueMode(rtName, camName);
				exposure = this.getCCDTeleoperation().getExposureTime(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

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
							camName, imageId,
							ImageExtensionFormat.valueOf(format));

				} catch (ImageNotAvailableException e) {
					try {
						Thread.sleep((int) (exposure * 1000 + 100));
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

			operationContext.getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void stopContinuousImage(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			try {
				this.getCCDTeleoperation().stopContinueMode(rtName, camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void domeMove(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		String rtName = null;
		String domeName = null;
		String movement = null;

		try {
			String rtNameParameter = (String) operationArguments[0];
			rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String domeNameParameter = (String) operationArguments[1];
			domeName = (String) operationContext.getExperimentContext()
					.getParameterValue(domeNameParameter);

			String movementParameter = (String) operationArguments[2];
			movement = (String) operationContext.getExperimentContext()
					.getParameterValue(movementParameter);
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			if (movement.toUpperCase().equals("OPEN")) {
				this.getDomeTeleoperation().open(rtName, domeName);
			} else if (movement.toUpperCase().equals("CLOSE")) {
				this.getDomeTeleoperation().close(rtName, domeName);
			} else {
				throw new ExperimentOperationException(
						"The movement direction is incorrect: '" + movement
								+ "'");
			}
		} catch (DomeTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void changeFocusRelative(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		String rtName = null;
		String focusName = null;
		Integer steps = null;

		try {
			String rtNameParameter = (String) operationArguments[0];
			rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String focusNameParameter = (String) operationArguments[1];
			focusName = (String) operationContext.getExperimentContext()
					.getParameterValue(focusNameParameter);

			String stepsParameter = (String) operationArguments[2];
			steps = (Integer) operationContext.getExperimentContext()
					.getParameterValue(stepsParameter);
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

		try {
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			this.getFocuserTeleoperation().moveRelative(rtName, focusName,
					steps);

		} catch (FocuserTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void constrainedMountMove(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String mountNameParameter = (String) operationArguments[1];
			String mountName = (String) operationContext.getExperimentContext()
					.getParameterValue(mountNameParameter);

			String directionParameter = (String) operationArguments[2];
			String direction = (String) operationContext.getExperimentContext()
					.getParameterValue(directionParameter);

			String maxMovesParameter = (String) operationArguments[3];
			int maxMoves = (Integer) operationContext.getExperimentContext()
					.getParameterValue(maxMovesParameter);

			String currentMovesParameter = (String) operationArguments[4];
			int currentMoves = (Integer) operationContext
					.getExperimentContext().getParameterValue(
							currentMovesParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			if (direction.equals("LEFT")) {
				if (currentMoves > -maxMoves) {
					this.getMountTeleoperation().moveWest(rtName, mountName);

					operationContext.getExperimentContext().setParameterValue(
							currentMovesParameter, currentMoves - 1);
				} else {
					throw new ExperimentOperationException(
							"Cannot move left because it is on the limit");
				}
			} else if (direction.equals("RIGHT")) {
				if (currentMoves < maxMoves) {
					this.getMountTeleoperation().moveEast(rtName, mountName);

					operationContext.getExperimentContext().setParameterValue(
							currentMovesParameter, currentMoves + 1);
				} else {
					throw new ExperimentOperationException(
							"Cannot move right because it is on the limit");
				}
			} else if (direction.equals("UP")) {
				if (currentMoves < maxMoves) {
					this.getMountTeleoperation().moveNorth(rtName, mountName);

					operationContext.getExperimentContext().setParameterValue(
							currentMovesParameter, currentMoves + 1);
				} else {
					throw new ExperimentOperationException(
							"Cannot move up because it is on the limit");
				}
			} else if (direction.equals("DOWN")) {
				if (currentMoves > -maxMoves) {
					this.getMountTeleoperation().moveSouth(rtName, mountName);

					operationContext.getExperimentContext().setParameterValue(
							currentMovesParameter, currentMoves - 1);
				} else {
					throw new ExperimentOperationException(
							"Cannot move down because it is on the limit");
				}
			}
		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException
				| MountTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void takeImage(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {
			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) operationArguments[2];
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = null;
			String imageId = null;

			try {
				imageId = this.getCCDTeleoperation().startExposure(rtName,
						camName);
			} catch (CCDTeleoperationException e) {
				throw new ExperimentOperationException(e.getMessage());
			} catch (DeviceOperationFailedException e) {
			}

			int retries = 0;

			while (retries < 10 && url == null) {
				ImageInformation imageInfo = this.getImageRepository()
						.getImageInformationByRTLocaId(rtName, imageId);

				url = imageInfo.getUrl();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				retries++;
			}

			int rid = operationContext.getExperimentContext().getReservation();

			this.getImageRepository().setExperimentReservationByUrl(url, rid);

			ReservationInformation resInfo = this.getAdapter()
					.getReservationInformation(rid);

			this.getImageRepository().setUserByUrl(url, resInfo.getUser());

			if (url == null) {
				throw new ExperimentOperationException(
						"Cannot recover the instantaneous image url from the camera");
			}

			operationContext.getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException
				| ImageRepositoryException | ExperimentDatabaseException
				| NoSuchReservationException e) {
			throw new ExperimentOperationException(e.getMessage());
		}

	}

	private void getStream(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		try {
			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String camNameParameter = (String) operationArguments[1];
			String camName = (String) operationContext.getExperimentContext()
					.getParameterValue(camNameParameter);

			String urlParameter = (String) operationArguments[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			String url = this.getSCamTeleoperation().getImageURL(rtName,
					camName);

			operationContext.getExperimentContext().setParameterValue(
					urlParameter, url);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| SCamTeleoperationException | DeviceOperationFailedException
				| UndefinedExperimentParameterException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void park(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		try {

			String rtNameParameter = (String) operationArguments[0];

			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> mounts;
			List<String> domes;

			try {
				mounts = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.MOUNT);
				domes = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.DOME);
			} catch (RTRepositoryException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			if (domes != null && domes.size() > 0) {
				String domeName = domes.get(0);
				try {
					this.getDomeTeleoperation().close(rtName, domeName);
				} catch (DomeTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
			}

			if (mounts != null && mounts.size() > 0) {

				String mountName = mounts.get(0);
				try {
					this.getMountTeleoperation().setTracking(rtName, mountName,
							false);
				} catch (MountTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
				try {
					this.getMountTeleoperation().park(rtName, mountName);
				} catch (MountTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
			} else {
				throw new ExperimentOperationException(
						"No mount available on the '" + rtName + "' RT");
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void pointToObject(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {

		try {
			String rtNameParameter = (String) operationArguments[0];
			String objectParameter = (String) operationArguments[1];

			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);
			String object = (String) operationContext.getExperimentContext()
					.getParameterValue(objectParameter);

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			List<String> mounts;
			List<String> domes;

			try {
				mounts = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.MOUNT);
				domes = this.getRTRepository().getRTDeviceNames(rtName,
						DeviceType.DOME);
			} catch (RTRepositoryException e) {
				throw new ExperimentOperationException(e.getMessage());
			}

			if (domes != null && domes.size() > 0) {
				String domeName = domes.get(0);
				try {
					DomeOpeningState domeState = this.getDomeTeleoperation()
							.getState(rtName, domeName);

					System.out.println(domeState.name());

					// if (domeState.equals(DomeOpeningState.UNDEFINED)
					// || domeState.equals(DomeOpeningState.CLOSED)) {
					this.getDomeTeleoperation().open(rtName, domeName);
					// }
				} catch (DomeTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
			}

			if (mounts != null && mounts.size() > 0) {

				String mountName = mounts.get(0);

				try {

					this.getMountTeleoperation().setTrackingRate(rtName,
							mountName, TrackingRate.DRIVE_SOLAR);

					this.getMountTeleoperation().setTracking(rtName, mountName,
							true);

					this.getMountTeleoperation().setSlewRate(rtName, mountName,
							"CENTER");

					this.getMountTeleoperation().slewToObject(rtName,
							mountName, object);

				} catch (MountTeleoperationException e) {
					throw new ExperimentOperationException(e.getMessage());
				} catch (DeviceOperationFailedException e) {
				}
			}

			else {
				throw new ExperimentOperationException(
						"No mount available on the '" + rtName + "' RT");
			}

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}

	private void getRADEC(OperationContext operationContext,
			Object[] operationArguments) throws ExperimentOperationException {
		try {

			String rtNameParameter = (String) operationArguments[0];
			String rtName = (String) operationContext.getExperimentContext()
					.getParameterValue(rtNameParameter);

			String mountNameParameter = (String) operationArguments[1];
			String mountName = (String) operationContext.getExperimentContext()
					.getParameterValue(mountNameParameter);

			String resultParameter = (String) operationArguments[2];

			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			double ra = this.getMountTeleoperation().getRA(rtName, mountName);
			double dec = this.getMountTeleoperation().getDEC(rtName, mountName);

			operationContext.getExperimentContext().setParameterValue(
					resultParameter, ra);

		} catch (ExperimentParameterException | NoSuchExperimentException
				| ExperimentNotInstantiatedException
				| UndefinedExperimentParameterException
				| MountTeleoperationException | DeviceOperationFailedException e) {
			throw new ExperimentOperationException(e.getMessage());
		}
	}
}