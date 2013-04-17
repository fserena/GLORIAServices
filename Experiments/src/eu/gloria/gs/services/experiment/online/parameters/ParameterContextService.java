package eu.gloria.gs.services.experiment.online.parameters;

import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.online.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.data.ReservationInformation;
import eu.gloria.gs.services.experiment.online.models.ContextNotReadyException;
import eu.gloria.gs.services.experiment.online.models.ExperimentContextService;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.reservation.NoSuchReservationException;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.data.DeviceType;

public class ParameterContextService extends ExperimentContextService {

	public void setValue(ParameterContext parameterContext,
			Object[] operationArguments) throws ExperimentParameterException {

		try {

			parameterContext.setValue(operationArguments[0]);
		} catch (UndefinedExperimentParameterException
				| NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getMessage());
		}
	}

	public void treatOperation(ParameterContext parameterContext,
			String operation, Object... operationArguments)
			throws ExperimentParameterException, ContextNotReadyException {

		if (operation.equals("setValue")) {
			this.setValue(parameterContext, operationArguments);
		} else if (operation.equals("loadRTName")) {
			this.loadRTName(parameterContext, operationArguments);
		} else if (operation.equals("loadDeviceName")) {
			this.loadDeviceName(parameterContext, operationArguments);
		}

	}

	private void loadDeviceName(ParameterContext parameterContext,
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
				throw new ContextNotReadyException(rtParameter);
			}
		} catch (NoSuchExperimentException | ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getMessage());
		}

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		List<String> deviceNames = null;

		try {
			deviceNames = this.getRTRepository().getRTDeviceNames(rtName,
					deviceType);
		} catch (RTRepositoryException e) {
			throw new ExperimentParameterException(e.getMessage());
		}

		String deviceName = deviceNames.get(deviceOrder);

		try {
			parameterContext.setValue(deviceName);
		} catch (UndefinedExperimentParameterException
				| NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getMessage());
		}

	}

	private void loadRTName(ParameterContext parameterContext,
			Object[] operationArguments) throws ExperimentParameterException {
		int rid = parameterContext.getExperimentContext().getReservation();

		ReservationInformation resInfo = null;

		try {
			resInfo = this.getAdapter().getReservationInformation(rid);
		} catch (ExperimentDatabaseException | NoSuchReservationException e) {
			throw new ExperimentParameterException(e.getMessage());
		}

		List<String> telescopes = resInfo.getTelescopes();
		String telescopeName = telescopes.get((Integer) operationArguments[0]);

		try {
			parameterContext.setValue(telescopeName);
		} catch (UndefinedExperimentParameterException
				| NoSuchExperimentException
				| ExperimentNotInstantiatedException e) {
			throw new ExperimentParameterException(e.getMessage());
		}

	}
}
