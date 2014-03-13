package eu.gloria.gs.services.experiment.parameters;

import eu.gloria.gs.services.experiment.base.contexts.ContextNotReadyException;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextService;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterContext;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;

public class ParameterContextService extends ExperimentContextService {

	public void setValue(ParameterContext parameterContext,
			Object[] operationArguments) throws ExperimentParameterException {

		try {

			parameterContext.setValue(null, operationArguments[0]);
		} catch (NoSuchParameterException | ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getAction());
		}
	}

	public void treatOperation(ParameterContext parameterContext,
			String operation, Object... operationArguments)
			throws ExperimentParameterException, ContextNotReadyException {

		if (operation.equals("setValue")) {
			this.setValue(parameterContext, operationArguments);
		} /*else if (operation.equals("loadRTName")) {
			this.loadRTName(parameterContext, operationArguments);
		} else if (operation.equals("loadDeviceName")) {
			this.loadDeviceName(parameterContext, operationArguments);
		}*/

	}

	/*private void loadDeviceName(ParameterContext parameterContext,
			Object[] operationArguments) throws ExperimentParameterException,
			ContextNotReadyException {

		String rtParameter = (String) operationArguments[0];
		int deviceOrder = (Integer) operationArguments[1];
		DeviceType deviceType = DeviceType
				.valueOf((String) operationArguments[2]);

		String rtName;
		try {
			rtName = (String) parameterContext.getExperimentContext()
					.getParameterValue(rtParameter);

			if (rtName == null) {
				throw new ContextNotReadyException(parameterContext
						.getExperimentContext().getReservation());
			}
		} catch (NoSuchParameterException | ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getAction());
		}

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		List<String> deviceNames = null;

		try {
			deviceNames = this.getRTRepository().getRTDeviceNames(rtName,
					deviceType);
		} catch (RTRepositoryException e) {
			throw new ExperimentParameterException(e.getAction());
		}

		String deviceName = deviceNames.get(deviceOrder);

		try {
			parameterContext.setValue(null, deviceName);
		} catch (NoSuchParameterException | ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getAction());
		}

	}

	private void loadRTName(ParameterContext parameterContext,
			Object[] operationArguments) throws ExperimentParameterException {
		int rid = parameterContext.getExperimentContext().getReservation();

		ReservationInformation resInfo = null;

		try {
			resInfo = this.getAdapter().getReservationInformation(rid);
		} catch (ExperimentDatabaseException | NoSuchReservationException e) {
			throw new ExperimentParameterException(e.getAction());
		}

		List<String> telescopes = resInfo.getTelescopes();
		String telescopeName = telescopes.get((Integer) operationArguments[0]);

		try {
			parameterContext.setValue(null, telescopeName);
		} catch (NoSuchParameterException | ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getAction());
		}

	}*/
}
