package eu.gloria.gs.services.experiment.online;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebParam;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.online.OnlineExperimentException;
import eu.gloria.gs.services.experiment.online.OnlineExperimentInterface;
import eu.gloria.gs.services.experiment.online.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.online.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.online.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.online.data.FeatureCompliantInformation;
import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.online.data.OperationInformation;
import eu.gloria.gs.services.experiment.online.data.ParameterInformation;
import eu.gloria.gs.services.experiment.online.data.ReservationInformation;
import eu.gloria.gs.services.experiment.online.data.FeatureInformation;
import eu.gloria.gs.services.experiment.online.data.TimeSlot;
import eu.gloria.gs.services.experiment.online.models.CustomExperimentException;
import eu.gloria.gs.services.experiment.online.models.CustomExperimentModel;
import eu.gloria.gs.services.experiment.online.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.online.models.ExperimentContext;
import eu.gloria.gs.services.experiment.online.models.ExperimentContextManager;
import eu.gloria.gs.services.experiment.online.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.online.models.ExperimentModelManager;
import eu.gloria.gs.services.experiment.online.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.online.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.online.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.online.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.online.parameters.ParameterTypeNotAvailableException;
import eu.gloria.gs.services.experiment.online.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.online.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.online.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentBooker;
import eu.gloria.gs.services.log.action.ActionLogException;

public class OnlineExperiment extends GSLogProducerService implements
		OnlineExperimentInterface {

	private ExperimentDBAdapter adapter;
	private ExperimentBooker experimentBooker;
	private ExperimentModelManager modelManager;
	private ExperimentContextManager contextManager;

	public OnlineExperiment() {
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setBooker(ExperimentBooker booker) {
		this.experimentBooker = booker;
	}

	public void setModelManager(ExperimentModelManager manager) {
		this.modelManager = manager;
	}

	public void setContextManager(ExperimentContextManager manager) {
		this.contextManager = manager;
	}

	@Override
	public void createExperiment(String experiment)
			throws DuplicateExperimentException, OnlineExperimentException {

		try {
			this.logAction(this.getClientUsername(),
					"Trying to create a new experiment named '" + experiment
							+ "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

		try {
			modelManager.createModel(experiment, this.getClientUsername());
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"Succesful creation of a new experiment named '"
							+ experiment + "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

	}

	@Override
	public ExperimentInformation getExperimentInformation(String experiment)
			throws OnlineExperimentException, NoSuchExperimentException {

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getExperimentInformation(experiment);
			return expInfo;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		} catch (NoSuchExperimentException e) {
			throw e;
		}
	}

	@Override
	public void setExperimentDescription(String experiment, String description)
			throws OnlineExperimentException, NoSuchExperimentException {

		try {
			adapter.setExperimentDescription(experiment, description);
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		} catch (NoSuchExperimentException e) {
			throw e;
		}

		try {
			this.logAction(this.getClientUsername(), "Set the '" + experiment
					+ "' experiment description");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<String> getAllOnlineExperiments()
			throws OnlineExperimentException {

		List<String> experiments = null;
		try {
			experiments = adapter.getAllOnlineExperiments();

			if (experiments == null || experiments.size() == 0) {
				throw new OnlineExperimentException(
						"There is no online experiments registered");
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

	}

	@Override
	public boolean containsExperiment(String experiment)
			throws OnlineExperimentException {

		try {

			boolean contains;
			contains = adapter.containsExperiment(experiment);

			return contains;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public List<ReservationInformation> getMyPendingReservations()
			throws OnlineExperimentException, NoReservationsAvailableException {

		List<ReservationInformation> resInfo = null;
		try {
			resInfo = adapter.getUserPendingReservations(this
					.getClientUsername());

			return resInfo;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		} catch (NoReservationsAvailableException e) {
			throw e;
		}
	}

	@Override
	public boolean anyReservationActiveNow() throws OnlineExperimentException {

		boolean anyActiveNow;
		try {
			anyActiveNow = adapter.anyUserReservationActiveNow(this
					.getClientUsername());

			return anyActiveNow;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentReservations()
			throws OnlineExperimentException, NoReservationsAvailableException {

		List<ReservationInformation> reservations;
		try {
			reservations = adapter.getUserReservationActiveNow(this
					.getClientUsername());

			return reservations;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		} catch (NoReservationsAvailableException e) {
			throw e;
		}
	}

	@Override
	public void reserveExperiment(String experiment, List<String> telescopes,
			TimeSlot timeSlot) throws OnlineExperimentException,
			NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException {

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			this.logAction(this.getClientUsername(),
					"Trying to make a reservation of the '" + experiment
							+ "' experiment on '" + telescopes.toString() + "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

		try {
			experimentBooker.reserve(experiment, this.getClientUsername(),
					telescopes, timeSlot);
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"Successful reservation of the '" + experiment
							+ "' experiment on '" + telescopes.toString() + "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<TimeSlot> getAvailableReservations(String experiment,
			List<String> telescopes) throws OnlineExperimentException,
			ExperimentReservationArgumentException {

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			List<TimeSlot> timeSlots = experimentBooker.getAvailableTimeSlots(
					experiment, telescopes);

			if (timeSlots == null | timeSlots.size() == 0) {
				throw new OnlineExperimentException(
						"There is no timeslots available");
			}

			return timeSlots;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

	}

	@Override
	public void cancelExperimentReservation(int reservationId)
			throws OnlineExperimentException, NoSuchReservationException {

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			this.logAction(this.getClientUsername(),
					"Trying to cancel the reservation '" + reservationId + "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

		try {
			experimentBooker.cancelReservation(this.getClientUsername(),
					reservationId);
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"Successful cancel of the reservation '" + reservationId
							+ "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executeExperimentOperation(int reservationId, String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentParameterException, ExperimentNotInstantiatedException,
			OnlineExperimentException, NoSuchReservationException,
			NoSuchExperimentException {

		ExperimentContext context;
		try {
			context = contextManager.getContext(this.getClientUsername(),
					reservationId);

			context.executeOperation(operation);

		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException
				| UndefinedExperimentParameterException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"'" + context.getExperimentName()
							+ "' experiment operation executed: '" + operation
							+ "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			int reservationId) throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		try {
			if (!adapter.anyUserReservationActiveNow(this.getClientUsername())) {
				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			if (!adapter.isReservationContextInstantiated(reservationId)) {
				throw new ExperimentNotInstantiatedException(
						"The experiment is not instantiated");
			}
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		ExperimentRuntimeInformation context;
		try {
			context = adapter.getExperimentRuntimeContext(reservationId);

			return context;
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

	}

	@Override
	public void addExperimentOperation(String experiment,
			OperationInformation operation) throws OnlineExperimentException,
			NoSuchExperimentException, NoSuchExperimentException {

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				throw new NoSuchExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildOperation(operation);
		} catch (InvalidExperimentModelException
				| OperationTypeNotAvailableException
				| CustomExperimentException | ExperimentOperationException
				| ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "'" + experiment
					+ "' experiment operation added: '" + operation + "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addExperimentParameter(String experiment,
			ParameterInformation parameter) throws OnlineExperimentException,
			NoSuchExperimentException, NoSuchExperimentException {

		try {
			ExperimentInformation expInfo = adapter
					.getExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				throw new OnlineExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {

			throw new OnlineExperimentException(e.getMessage());
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildParameter(parameter);
		} catch (CustomExperimentException | ParameterTypeNotAvailableException
				| InvalidExperimentModelException | ExperimentDatabaseException
				| ExperimentParameterException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "'" + experiment
					+ "' experiment parameter added: '" + parameter + "'");
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setExperimentParameterValue(int reservationId,
			String parameter, Object value) throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		try {
			if (!adapter.anyUserReservationActiveNow(this.getClientUsername())) {
				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}

			if (!adapter.isReservationContextInstantiated(reservationId)) {
				throw new ExperimentNotInstantiatedException(
						"The experiment is not instantiated");
			}

			ExperimentContext context = contextManager.getContext(
					this.getClientUsername(), reservationId);

			context.setParameterValue(parameter, value);

			try {
				this.logAction(this.getClientUsername(),
						"'" + context.getExperimentName()
								+ "' experiment value parameter modified: '"
								+ parameter + "'");
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException | ExperimentParameterException
				| UndefinedExperimentParameterException
				| NoSuchExperimentException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public Object getExperimentParameterValue(int reservationId,
			String parameter) throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		try {
			if (!adapter.anyUserReservationActiveNow(this.getClientUsername())) {
				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}

			if (!adapter.isReservationContextInstantiated(reservationId)) {
				throw new ExperimentNotInstantiatedException(
						"The experiment is not instantiated");
			}

			ExperimentContext context;
			context = contextManager.getContext(this.getClientUsername(),
					reservationId);

			return context.getParameterValue(parameter);
		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException | ExperimentParameterException
				| NoSuchExperimentException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public void removeExperiment(
			@WebParam(name = "experiment") String experiment)
			throws OnlineExperimentException, NoSuchExperimentException {
		try {
			modelManager.removeModel(experiment);
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public ReservationInformation getReservationInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws OnlineExperimentException {

		try {
			ReservationInformation reservationInfo = adapter
					.getReservationInformation(reservationId);

			if (!reservationInfo.getUser().equals(this.getClientUsername())) {
				throw new NoSuchReservationException(
						"You have nothing to do with that reservation :)");
			}

			return reservationInfo;
		} catch (ExperimentDatabaseException | NoSuchReservationException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

	}

	@Override
	public Set<String> getAllExperimentParameters()
			throws OnlineExperimentException {

		Map<String, ExperimentParameter> parameters = modelManager
				.getAllExperimentParameters();

		return parameters.keySet();
	}

	@Override
	public Set<String> getAllExperimentOperations()
			throws OnlineExperimentException {

		return modelManager.getAllExperimentOperations().keySet();
	}

	@Override
	public ExperimentParameter getExperimentParameter(
			@WebParam(name = "parameterName") String name)
			throws OnlineExperimentException {

		return modelManager.getExperimentParameter(name);
	}

	@Override
	public ExperimentOperation getExperimentOperation(
			@WebParam(name = "operationName") String name)
			throws OnlineExperimentException {
		return modelManager.getExperimentOperation(name);
	}

	@Override
	public void addExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "segment") FeatureInformation feature)
			throws OnlineExperimentException, NoSuchExperimentException {

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				throw new NoSuchExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildFeature(feature);
		} catch (InvalidExperimentModelException | CustomExperimentException
				| ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public boolean testExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws OnlineExperimentException, NoSuchExperimentException {

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			return model.testFeature(feature);
		} catch (InvalidExperimentModelException | CustomExperimentException
				| ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public FeatureCompliantInformation getFeatureCompliantInformation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws OnlineExperimentException, NoSuchExperimentException {
		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			return model.getFeatureCompliantInformation(feature);
		} catch (InvalidExperimentModelException | CustomExperimentException
				| ExperimentDatabaseException e) {
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.experiment.online.OnlineExperimentInterface#
	 * getAllExperimentFeatures()
	 */
	@Override
	public Set<String> getAllExperimentFeatures()
			throws OnlineExperimentException {
		return modelManager.getAllExperimentFeatures().keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.experiment.online.OnlineExperimentInterface#
	 * getExperimentFeature(java.lang.String)
	 */
	@Override
	public ExperimentFeature getExperimentFeature(
			@WebParam(name = "featureName") String name)
			throws OnlineExperimentException {
		return modelManager.getExperimentFeature(name);
	}
}
