package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebParam;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.ExperimentInterface;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.FeatureCompliantInformation;
import eu.gloria.gs.services.experiment.base.data.FeatureInformation;
import eu.gloria.gs.services.experiment.base.data.JSONConverter;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.models.CustomExperimentException;
import eu.gloria.gs.services.experiment.base.models.CustomExperimentModel;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.base.models.ExperimentModelManager;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.base.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.reservation.ExperimentBooker;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.user.UserRepositoryException;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRole;
import eu.gloria.gs.services.utils.ObjectResponse;

public class Experiment extends GSLogProducerService implements
		ExperimentInterface {

	private ExperimentDBAdapter adapter;
	private ExperimentBooker experimentBooker;
	private ExperimentModelManager modelManager;
	private ExperimentContextManager contextManager;
	private UserRepositoryInterface userRepository;

	public Experiment() {
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

	public void setUserRepository(UserRepositoryInterface userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void createOfflineExperiment(String experiment)
			throws DuplicateExperimentException, ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "new offline");
		action.put("name", experiment);

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"OFFLINE");
		} catch (ExperimentDatabaseException e) {

			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(this.getClientUsername(), action);
	}

	@Override
	public ExperimentInformation getExperimentInformation(String experiment)
			throws ExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "get experiment info");
		action.put("name", experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getExperimentInformation(experiment);
			return expInfo;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public ParameterInformation getParameterInformation(String experiment,
			String parameter) throws ExperimentException,
			NoSuchExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "get parameter info");
		action.put("name", experiment);

		ParameterInformation paramInfo;
		try {
			paramInfo = adapter.getExperimentInformation(experiment)
					.getParameter(parameter);
			return paramInfo;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public void setExperimentDescription(String experiment, String description)
			throws ExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "set experiment description");
		action.put("experiment", experiment);

		try {
			adapter.setExperimentDescription(experiment, description);
			this.logInfo(this.getClientUsername(), action);

		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		} catch (NoSuchExperimentException e) {
			action.put("cause", "experiment does not exist");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<String> getAllOnlineExperiments() throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "get all online");

		List<String> experiments = null;
		try {
			experiments = adapter.getAllExperiments("ONLINE");

			if (experiments == null || experiments.size() == 0) {

				action.put("cause", "no experiments");
				this.logWarning(getClientUsername(), action);

				throw new ExperimentException(action);
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<String> getAllOfflineExperiments() throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "get all online");

		List<String> experiments = null;
		try {
			experiments = adapter.getAllExperiments("OFFLINE");

			if (experiments == null || experiments.size() == 0) {
				action.put("cause", "no experiments");
				this.logWarning(getClientUsername(), action);
				throw new ExperimentException(action);
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public boolean containsExperiment(String experiment)
			throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "contains experiment");
		action.put("experiment", experiment);

		try {

			return adapter.containsExperiment(experiment);

		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyPendingReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "get all pending");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations(null, adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<ReservationInformation> getMyPendingOfflineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "get pending offline");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations("OFFLINE", adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyPendingOnlineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "get pending offline");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations("ONLINE", adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	private List<ReservationInformation> getPendingReservations(String type,
			boolean adminMode) throws ExperimentException,
			NoReservationsAvailableException {
		List<ReservationInformation> resInfo = null;
		try {
			if (adminMode) {
				if (type == null) {
					resInfo = adapter.getAllPendingReservations();
				} else {
					resInfo = adapter.getAllPendingReservations(type);
				}
			} else {
				if (type == null) {
					resInfo = adapter.getUserPendingReservations(this
							.getClientUsername());

				} else {
					resInfo = adapter.getUserPendingReservations(type,
							this.getClientUsername());
				}
			}

			return resInfo;
		} catch (ExperimentDatabaseException e) {
			this.logError(getClientUsername(), e.getAction());
			throw new ExperimentException(e.getAction());
		}
	}

	private boolean anyReservationActiveNow(String type, boolean adminMode)
			throws ExperimentException {
		boolean anyActiveNow;
		try {

			if (adminMode) {
				if (type == null) {
					anyActiveNow = adapter.anyReservationActiveNow();
				} else {
					anyActiveNow = adapter.anyReservationActiveNow(type);
				}
			} else {
				if (type == null) {
					anyActiveNow = adapter.anyUserReservationActiveNow(this
							.getClientUsername());
				} else {
					anyActiveNow = adapter.anyUserReservationActiveNow(type,
							this.getClientUsername());
				}
			}

			return anyActiveNow;
		} catch (ExperimentDatabaseException e) {
			this.logError(getClientUsername(), e.getAction());
			throw new ExperimentException(e.getAction());
		}
	}

	@Override
	public boolean anyReservationActiveNow() throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "any active");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow(null, adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public boolean anyOnlineReservationActiveNow() throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "any online active");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow("ONLINE", adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public boolean anyOfflineReservationActiveNow() throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "any offline active");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow("OFFLINE", adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentOnlineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "get active online");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations("ONLINE", adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<ReservationInformation> getMyCurrentOfflineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "get active offline");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations("OFFLINE", adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "get all active");

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations(null, adminMode);
		} catch (ExperimentException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	private List<ReservationInformation> getCurrentReservations(String type,
			boolean adminMode) throws ExperimentException,
			NoReservationsAvailableException {
		List<ReservationInformation> reservations;
		try {

			if (adminMode) {
				if (type == null) {
					reservations = adapter.getAllReservationsActiveNow();
				} else {
					reservations = adapter.getAllReservationsActiveNow(type);
				}
			} else {
				if (type == null) {
					reservations = adapter.getUserReservationsActiveNow(this
							.getClientUsername());
				} else {
					reservations = adapter.getUserReservationsActiveNow(type,
							this.getClientUsername());
				}
			}

			return reservations;
		} catch (ExperimentDatabaseException e) {
			this.logError(getClientUsername(), e.getAction());
			throw new ExperimentException(e.getAction());
		}
	}

	@Override
	public void applyForExperiment(String experiment)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "apply for");
		action.put("experiment", experiment);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.applyFor(experiment, this.getClientUsername());
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public void reserveExperiment(String experiment, List<String> telescopes,
			TimeSlot timeSlot) throws ExperimentException,
			NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException {

		LogAction action = new LogAction();
		action.put("operation", "reserve");
		action.put("experiment", experiment);
		action.put("rts", telescopes);
		action.put("slot", timeSlot);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			experimentBooker.reserve(experiment, this.getClientUsername(),
					telescopes, timeSlot, adminMode);
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public List<TimeSlot> getAvailableReservations(String experiment,
			List<String> telescopes) throws ExperimentException,
			ExperimentReservationArgumentException {

		LogAction action = new LogAction();
		action.put("operation", "get available");
		action.put("experiment", experiment);
		action.put("rts", telescopes);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());
		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);
		try {
			List<TimeSlot> timeSlots = experimentBooker.getAvailableTimeSlots(
					experiment, telescopes, adminMode);

			if (timeSlots == null | timeSlots.size() == 0) {
				action.put("cause", "no timeslots available");
				throw new ExperimentException(action);
			}

			return timeSlots;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public void cancelExperimentReservation(int reservationId)
			throws ExperimentException, NoSuchReservationException {

		LogAction action = new LogAction();
		action.put("operation", "cancel");
		action.put("rid", reservationId);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.cancelReservation(this.getClientUsername(),
					reservationId);
		} catch (ExperimentDatabaseException e) {

			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public void executeExperimentOperation(int reservationId, String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException {

		LogAction action = new LogAction();
		action.put("operation", "execute");
		action.put("rid", reservationId);
		action.put("name", operation);

		ExperimentContext context;
		try {
			context = contextManager.getContext(this.getClientUsername(),
					reservationId);

			context.executeOperation(operation);

		} catch (ExperimentDatabaseException
				| UndefinedExperimentParameterException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentOperationException(action);
		} catch (ExperimentParameterException e) {
			action.put("cause", "parameter problem");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentOperationException(action);
		} catch (InvalidUserContextException e) {
			action.put("cause", "invalid user");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new InvalidUserContextException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			int reservationId) throws ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException {

		LogAction action = new LogAction();
		action.put("operation", "get runtime");
		action.put("rid", reservationId);

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			boolean grantAccess;
			if (adminMode) {
				grantAccess = adapter.anyReservationActiveNow();
			} else {
				grantAccess = adapter.anyUserReservationActiveNow(this
						.getClientUsername());
			}

			if (!grantAccess) {
				action.put("cause", "any active");
				this.logError(getClientPassword(), action);
				throw new NoSuchReservationException(action);
			}
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		try {
			if (!adapter.isReservationContextReady(reservationId)) {
				action.put("ready", false);
				action.put("cause", "context not ready");
				this.logError(getClientPassword(), action);
				throw new ExperimentNotInstantiatedException(action);
			}
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			action.put("while", "asking for context ready");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		action.put("ready", true);

		ExperimentRuntimeInformation context;
		try {
			context = adapter.getExperimentRuntimeContext(reservationId);

			return context;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			action.put("while", "getting runtime");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public void addExperimentOperation(String experiment,
			OperationInformation operation) throws ExperimentException,
			NoSuchExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "add operation");
		action.put("experiment", experiment);
		action.put("name", operation.getName());
		action.put("type", operation.getType());
		action.put("args", operation.getArguments());

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				action.put("cause", "invalid owner");
				this.logError(getClientPassword(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			action.put("while", "getting basic info");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildOperation(operation);
		} catch (OperationTypeNotAvailableException
				| ExperimentOperationException | ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			action.put("while", "adding operation");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public void addExperimentParameter(String experiment,
			ParameterInformation parameter) throws ExperimentException,
			NoSuchExperimentException, NoSuchParameterException {

		LogAction action = new LogAction();
		action.put("operation", "add parameter");
		action.put("experiment", experiment);
		action.put("name", parameter.getName());
		action.put("type", parameter.getType());
		action.put("args", parameter.getArguments());

		try {
			ExperimentInformation expInfo = adapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				action.put("cause", "invalid owner");
				this.logError(getClientPassword(), action);
				throw new ExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			action.put("while", "getting basic info");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);

			Object[] argsArray;
			try {
				argsArray = (Object[]) JSONConverter.fromJSON(
						Arrays.toString(parameter.getArguments()),
						Object[].class, null);

				parameter.setArguments(argsArray);
			} catch (IOException e) {
				action.put("cause", "args json error");
				this.logError(getClientPassword(), action);
				throw new NoSuchParameterException(action);
			}
			model.buildParameter(parameter);
		} catch (ParameterTypeNotAvailableException
				| ExperimentDatabaseException | ExperimentParameterException e) {
			action.put("cause", "internal error");
			action.put("while", "adding parameter");
			this.logError(getClientPassword(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public void setExperimentParameterValue(int reservationId,
			String parameter, ObjectResponse input)
			throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException {

		LogAction action = new LogAction();
		action.put("operation", "set parameter");
		action.put("rid", reservationId);
		action.put("name", parameter);
		action.put("value", input.content);

		try {
			if (!adapter.isReservationIsActiveNowForUser(reservationId,
					this.getClientUsername())) {

				boolean active = adapter.isReservationActiveNow(reservationId);

				if (active) {
					action.put("cause", "invalid user");
					this.logError(getClientUsername(), action);
					throw new InvalidUserContextException(action);
				}

				action.put("cause", "no context");
				this.logError(getClientUsername(), action);
				throw new NoSuchReservationException(action);
			}

			if (!adapter.isReservationContextReady(reservationId)) {
				action.put("ready", false);
				action.put("cause", "context not ready");
				throw new ExperimentNotInstantiatedException(action);
			}

			action.put("ready", true);

			ExperimentContext context = contextManager.getContext(
					this.getClientUsername(), reservationId);

			try {
				context.setParameterValue(parameter, JSONConverter.fromJSON(
						(String) input.content, Object.class, null));
			} catch (IOException e) {
				action.put("cause", "value json error");
				this.logError(getClientPassword(), action);
				throw new NoSuchParameterException(action);
			}

			this.logInfo(getClientUsername(), action);

		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentParameterException(action);
		} catch (InvalidUserContextException e) {
			action.put("cause", "invalid user");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new InvalidUserContextException(action);
		} catch (NoSuchParameterException e) {
			action.put("cause", "parameter not valid");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new NoSuchParameterException(action);
		}
	}

	@Override
	public ObjectResponse getExperimentContext(int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException, InvalidUserContextException,
			NoSuchParameterException {

		LogAction action = new LogAction();
		action.put("operation", "get context");
		action.put("rid", reservationId);

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {

			boolean active = adapter.isReservationActiveNow(reservationId);
			action.put("ready", active);
			boolean grantAccess;

			if (adminMode) {
				grantAccess = active;
			} else {
				grantAccess = adapter.isReservationIsActiveNowForUser(
						reservationId, this.getClientUsername());
			}

			if (!grantAccess) {
				if (active) {
					action.put("cause", "invalid user");
					this.logError(getClientUsername(), action);
					throw new InvalidUserContextException(action);
				}
				action.put("cause", "no context");
				this.logError(getClientUsername(), action);
				throw new NoSuchReservationException(action);
			}

			if (!adapter.isReservationContextReady(reservationId)) {
				action.put("cause", "not ready");
				this.logError(getClientUsername(), action);
				throw new ExperimentNotInstantiatedException(action);
			}

			ExperimentContext context;

			ReservationInformation resInfo = this.adapter
					.getReservationInformation(reservationId);

			context = contextManager.getContext(resInfo.getUser(),
					reservationId);

			LinkedHashMap<String, Object> contextValues = new LinkedHashMap<>();

			for (String parameter : context.getParameterNames()) {
				Object value = context.getParameterValue(parameter);
				contextValues.put(parameter, value);
			}

			ObjectResponse response = null;
			try {
				response = new ObjectResponse(
						JSONConverter.toJSON(contextValues));
			} catch (IOException e) {
				action.put("cause", "value json error");
				this.logError(getClientPassword(), action);
				throw new ExperimentException(action);
			}

			return response;

		} catch (ExperimentDatabaseException | ExperimentParameterException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		} catch (InvalidUserContextException e) {
			action.put("cause", "invalid user");
			action.put("more", e.getAction());
			throw new InvalidUserContextException(action);
		} catch (NoSuchParameterException e) {
			action.put("cause", "parameter not valid");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new NoSuchParameterException(action);
		}
	}

	@Override
	public boolean isExperimentContextReady(int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException {

		LogAction action = new LogAction();
		action.put("operation", "is context ready");
		action.put("rid", reservationId);

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);
		try {

			boolean grantAccess;
			if (adminMode) {
				grantAccess = adapter.isReservationActiveNow(reservationId);
			} else {
				grantAccess = adapter.isReservationIsActiveNowForUser(
						reservationId, this.getClientUsername());
			}

			if (!grantAccess) {
				action.put("cause", "no context");
				this.logError(getClientUsername(), action);
				throw new NoSuchReservationException(action);
			}

			return adapter.isReservationContextReady(reservationId);

		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public ObjectResponse getExperimentParameterValue(int reservationId,
			String parameter) throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException {

		LogAction action = new LogAction();
		action.put("operation", "get parameter");
		action.put("rid", reservationId);

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {

			boolean grantAccess;
			if (adminMode) {
				grantAccess = adapter.isReservationActiveNow(reservationId);
			} else {
				grantAccess = adapter.isReservationIsActiveNowForUser(
						reservationId, this.getClientUsername());
			}

			if (!grantAccess) {
				action.put("cause", "no context");
				this.logError(getClientUsername(), action);
				throw new NoSuchReservationException(action);
			}

			if (!adapter.isReservationContextReady(reservationId)) {
				action.put("ready", false);
				action.put("cause", "context not ready");
				this.logError(getClientUsername(), action);

				throw new ExperimentNotInstantiatedException(action);
			}

			action.put("ready", true);

			ExperimentContext context;

			ReservationInformation resInfo = this.adapter
					.getReservationInformation(reservationId);

			context = contextManager.getContext(resInfo.getUser(),
					reservationId);

			Object value = context.getParameterValue(parameter);

			ObjectResponse response = null;
			try {
				response = new ObjectResponse(JSONConverter.toJSON(value));
			} catch (IOException e) {
				action.put("cause", "value json error");
				this.logError(getClientPassword(), action);
				throw new ExperimentParameterException(action);
			}

			return response;

		} catch (ExperimentDatabaseException | ExperimentParameterException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentParameterException(action);
		} catch (InvalidUserContextException e) {
			action.put("cause", "invalid user");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new InvalidUserContextException(action);
		} catch (NoSuchParameterException e) {
			action.put("cause", "parameter not valid");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new NoSuchParameterException(action);
		}
	}

	@Override
	public void removeExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "remove experiment");
		action.put("experiment", experiment);

		try {
			modelManager.removeModel(experiment);
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public ReservationInformation getReservationInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws NoSuchReservationException, InvalidUserContextException,
			ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "get rid info");
		action.put("rid", reservationId);

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
			action.put("cause", "user rep error");
			this.logWarning(getClientUsername(), action);
			action.remove("cause");
		}

		action.put("admin mode", adminMode);

		try {
			ReservationInformation reservationInfo = adapter
					.getReservationInformation(reservationId);

			if (!adminMode
					&& !reservationInfo.getUser().equals(
							this.getClientUsername())) {
				action.put("cause", "invalid user");
				this.logError(getClientUsername(), action);
				throw new InvalidUserContextException(action);
			}

			return reservationInfo;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

	}

	@Override
	public Set<String> getAllExperimentParameters() throws ExperimentException {

		Map<String, ExperimentParameter> parameters = modelManager
				.getAllExperimentParameters();

		return parameters.keySet();
	}

	@Override
	public Set<String> getAllExperimentOperations() throws ExperimentException {

		return modelManager.getAllExperimentOperations().keySet();
	}

	@Override
	public ExperimentParameter getExperimentParameter(
			@WebParam(name = "parameterName") String name)
			throws ExperimentException {

		return modelManager.getExperimentParameter(name);
	}

	@Override
	public ExperimentOperation getExperimentOperation(
			@WebParam(name = "operationName") String name)
			throws ExperimentException {
		return modelManager.getExperimentOperation(name);
	}

	@Override
	public void addExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "segment") FeatureInformation feature)
			throws ExperimentException, NoSuchExperimentException,
			OperationTypeNotAvailableException,
			ParameterTypeNotAvailableException {

		LogAction action = new LogAction();
		action.put("operation", "add feature");
		action.put("experiment", experiment);
		action.put("name", feature.getName());

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				action.put("cause", "invalid user");
				this.logError(getClientUsername(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			action.put("while", "getting basic info");
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildFeature(feature);

			this.logInfo(getClientUsername(), action);

		} catch (CustomExperimentException | ExperimentDatabaseException e) {
			action.put("while", "adding feature");
			action.put("cause", "internal error");
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	@Override
	public boolean testExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws ExperimentException, NoSuchExperimentException {

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			return model.testFeature(feature);
		} catch (CustomExperimentException | ExperimentDatabaseException e) {
			throw new ExperimentException(e.getAction());
		}
	}

	@Override
	public FeatureCompliantInformation getFeatureCompliantInformation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws ExperimentException, NoSuchExperimentException {
		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			return model.getFeatureCompliantInformation(feature);
		} catch (CustomExperimentException | ExperimentDatabaseException e) {
			throw new ExperimentException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.experiment.online.OnlineExperimentInterface#
	 * getAllExperimentFeatures()
	 */
	@Override
	public Set<String> getAllExperimentFeatures() throws ExperimentException {
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
			throws ExperimentException {
		return modelManager.getExperimentFeature(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.ExperimentInterface#createOnlineExperiment
	 * (java.lang.String)
	 */
	@Override
	public void createOnlineExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, DuplicateExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "new online");
		action.put("experiment", experiment);

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"ONLINE");
		} catch (ExperimentDatabaseException e) {

			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);

			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.ExperimentInterface#getContextResults
	 * (int)
	 */
	@Override
	public List<ResultInformation> getContextResults(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, NoSuchReservationException {

		LogAction action = new LogAction();
		action.put("operation", "get context results");
		action.put("rid", reservationId);

		try {
			List<ResultInformation> results = adapter
					.getContextResults(reservationId);

			return results;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.experiment.ExperimentInterface#getExperimentResults
	 * (java.lang.String)
	 */
	@Override
	public List<ResultInformation> getExperimentResults(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException {

		LogAction action = new LogAction();
		action.put("operation", "get experiment results");
		action.put("experiment", experiment);

		try {
			List<ResultInformation> results = adapter
					.getExperimentResults(experiment);

			return results;
		} catch (ExperimentDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ExperimentException(action);
		}
	}
}
