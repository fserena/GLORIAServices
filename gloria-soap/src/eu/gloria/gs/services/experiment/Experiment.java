package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebParam;

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
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.models.CustomExperimentModel;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
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
import eu.gloria.gs.services.experiment.script.NoSuchScriptException;
import eu.gloria.gs.services.experiment.script.OverlapRTScriptException;
import eu.gloria.gs.services.experiment.script.data.RTScriptDBAdapter;
import eu.gloria.gs.services.experiment.script.data.RTScriptInformation;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.repository.user.InvalidUserException;
import eu.gloria.gs.services.repository.user.UserRepositoryException;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRole;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.gs.services.utils.ObjectResponse;

public class Experiment extends GSLogProducerService implements
		ExperimentInterface {

	private ExperimentDBAdapter experimentAdapter;
	private RTScriptDBAdapter scriptAdapter;
	private ExperimentBooker experimentBooker;
	private ExperimentModelManager modelManager;
	private ExperimentContextManager contextManager;
	private UserRepositoryInterface userRepository;
	private RTRepositoryInterface rtRepository;

	public Experiment() {
		this.createLogger(Experiment.class);
	}

	public void setExperimentAdapter(ExperimentDBAdapter adapter) {
		this.experimentAdapter = adapter;
	}

	public void setRTScriptAdapter(RTScriptDBAdapter adapter) {
		this.scriptAdapter = adapter;
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

	public void setRTRepository(RTRepositoryInterface rtRepository) {
		this.rtRepository = rtRepository;
	}

	private void buildErrorLog(LogAction action, String origin,
			String... reasons) {
		LogAction error = new LogAction();
		action.put("error", error);
		error.put("origin", origin);
		error.put("reasons", reasons);
	}

	private void buildErrorLogFromException(LogAction action,
			ActionException e, String origin, String... reasons) {
		LogAction error = new LogAction();
		action.put("error", error);
		if (e != null) {
			error.put("details", e.getAction());
		}
		error.put("origin", origin);
		error.put("reasons", reasons);
	}

	private void buildWarningLog(LogAction action, ActionException e,
			String origin, String... reasons) {
		LogAction warning = new LogAction();
		action.put("warning", warning);
		if (e != null) {
			warning.put("details", e.getAction());
		}
		warning.put("origin", origin);
		warning.put("reasons", reasons);
	}

	private void buildOperationLog(LogAction action, String operation,
			Object... args) {
		LogAction op = new LogAction();
		action.put("operation", op);
		op.put("name", operation);
		op.put("args", args);
	}

	private void treatUserRepositoryException(LogAction action,
			ActionException e) {
		buildWarningLog(action, e, "user repository");
		this.logWarning(getClientUsername(), action);
		action.remove("warning");
	}

	private boolean isAdminMode(LogAction action) {
		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserCredentials(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e) {
			treatUserRepositoryException(action, e);
		}

		return adminMode;
	}

	@Override
	public void createOfflineExperiment(String experiment)
			throws DuplicateExperimentException, ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "new offline", experiment);

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"OFFLINE");
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(this.getClientUsername(), action);
			throw new ExperimentException(action);
		}

		this.logInfo(this.getClientUsername(), action);
	}

	@Override
	public ExperimentInformation getExperimentInformation(String experiment)
			throws ExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get experiment info", experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter.getExperimentInformation(experiment);
			return expInfo;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public ParameterInformation getParameterInformation(String experiment,
			String parameter) throws ExperimentException,
			NoSuchExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get param info", experiment);

		ParameterInformation paramInfo;
		try {
			paramInfo = experimentAdapter.getExperimentInformation(experiment)
					.getParameter(parameter);
			return paramInfo;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public void setExperimentDescription(String experiment, String description)
			throws ExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "set experiment description", experiment);

		try {
			experimentAdapter.setExperimentDescription(experiment, description);
			this.logInfo(this.getClientUsername(), action);

		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			throw new ExperimentException(action);
		} catch (NoSuchExperimentException e) {
			buildErrorLogFromException(action, e, "database",
					"experiment does not exist");
			this.logError(this.getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<String> getAllOnlineExperiments() throws ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get all online");

		List<String> experiments = null;
		try {
			experiments = experimentAdapter.getAllExperiments("ONLINE");

			if (experiments == null || experiments.size() == 0) {

				buildWarningLog(action, null, "logic", "no experiments");
				this.logWarning(getClientUsername(), action);
				throw new ExperimentException(action);
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<String> getAllOfflineExperiments() throws ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get all offline");

		List<String> experiments = null;
		try {
			experiments = experimentAdapter.getAllExperiments("OFFLINE");

			if (experiments == null || experiments.size() == 0) {
				buildWarningLog(action, null, "logic", "no experiments");
				this.logWarning(getClientUsername(), action);
				throw new ExperimentException(action);
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public boolean containsExperiment(String experiment)
			throws ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "contains experiment");

		try {

			return experimentAdapter.containsExperiment(experiment);

		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyPendingReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get all pending");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations(null, adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<ReservationInformation> getMyPendingOfflineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get pending offline");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations("OFFLINE", adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyPendingOnlineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get pending online");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations("ONLINE", adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
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
					resInfo = experimentAdapter.getAllPendingReservations();
				} else {
					resInfo = experimentAdapter.getAllPendingReservations(type);
				}
			} else {
				if (type == null) {
					resInfo = experimentAdapter.getUserPendingReservations(this
							.getClientUsername());

				} else {
					resInfo = experimentAdapter.getUserPendingReservations(
							type, this.getClientUsername());
				}
			}

			return resInfo;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getAction());
		}
	}

	private boolean anyReservationActiveNow(String type, boolean adminMode)
			throws ExperimentException {
		boolean anyActiveNow;
		try {

			if (adminMode) {
				if (type == null) {
					anyActiveNow = experimentAdapter.anyReservationActiveNow();
				} else {
					anyActiveNow = experimentAdapter
							.anyReservationActiveNow(type);
				}
			} else {
				if (type == null) {
					anyActiveNow = experimentAdapter
							.anyUserReservationActiveNow(this
									.getClientUsername());
				} else {
					anyActiveNow = experimentAdapter
							.anyUserReservationActiveNow(type,
									this.getClientUsername());
				}
			}

			return anyActiveNow;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getAction());
		}
	}

	@Override
	public boolean anyReservationActiveNow() throws ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "any active");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow(null, adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public boolean anyOnlineReservationActiveNow() throws ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "any online active");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow("ONLINE", adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public boolean anyOfflineReservationActiveNow() throws ExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "any online active");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow("OFFLINE", adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentOnlineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get active online");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations("ONLINE", adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<ReservationInformation> getMyCurrentOfflineReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get active offline");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations("OFFLINE", adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentReservations()
			throws ExperimentException, NoReservationsAvailableException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get all active");

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations(null, adminMode);
		} catch (ExperimentException e) {
			buildErrorLogFromException(action, e, "logic", "internal problem");
			this.logError(getClientUsername(), action);
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
					reservations = experimentAdapter
							.getAllReservationsActiveNow();
				} else {
					reservations = experimentAdapter
							.getAllReservationsActiveNow(type);
				}
			} else {
				if (type == null) {
					reservations = experimentAdapter
							.getUserReservationsActiveNow(this
									.getClientUsername());
				} else {
					reservations = experimentAdapter
							.getUserReservationsActiveNow(type,
									this.getClientUsername());
				}
			}

			return reservations;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getAction());
		}
	}

	@Override
	public void applyForExperiment(String experiment)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "apply for", experiment);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.applyFor(experiment, this.getClientUsername());
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
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
		buildOperationLog(action, "reserve", experiment, telescopes, timeSlot);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		try {
			experimentBooker.reserve(experiment, this.getClientUsername(),
					telescopes, timeSlot, adminMode);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public List<TimeSlot> getAvailableReservations(String experiment,
			List<String> telescopes) throws ExperimentException,
			ExperimentReservationArgumentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get available", experiment, telescopes);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			List<TimeSlot> timeSlots = experimentBooker.getAvailableTimeSlots(
					experiment, telescopes, adminMode);

			if (timeSlots == null | timeSlots.size() == 0) {
				buildErrorLogFromException(action, null, "logic",
						"not available");
				throw new ExperimentException(action);
			}

			return timeSlots;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public void cancelExperimentReservation(int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException {

		LogAction action = new LogAction();
		buildOperationLog(action, "cancel", reservationId);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.cancelReservation(this.getClientUsername(),
					reservationId);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		} catch (InvalidUserContextException e) {
			buildErrorLogFromException(action, e, "logic", "invalid user");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new InvalidUserContextException(action);
		} catch (NoSuchReservationException e) {
			buildErrorLogFromException(action, e, "logic", "invalid rid");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new NoSuchReservationException(action);
		}

		this.logContextInfo(getClientUsername(), reservationId, action);
	}

	@Override
	public void resetExperimentContext(int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException, ExperimentNotInstantiatedException {

		LogAction action = new LogAction();
		buildOperationLog(action, "reset context", reservationId);

		try {
			if (experimentAdapter.isReservationContextReady(reservationId)) {
				experimentAdapter.deleteExperimentContext(reservationId);
			} else if (experimentAdapter.reservationIsActiveNowForUser(
					reservationId, getClientUsername())) {
				buildErrorLog(action, "logic", "invalid user");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentNotInstantiatedException(action);
			} else if (experimentAdapter.isReservationActiveNow(reservationId)) {
				buildErrorLog(action, "logic", "invalid user");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new InvalidUserContextException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database", "query problem",
					"invalid reservation?");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		} catch (NoSuchReservationException e) {
			buildErrorLogFromException(action, e, "logic", "invalid rid");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new NoSuchReservationException(action);
		}

		this.logContextInfo(getClientUsername(), reservationId, action);
	}

	@Override
	public void executeExperimentOperation(int reservationId, String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException {

		LogAction action = new LogAction();
		buildOperationLog(action, "execute", reservationId, operation);

		ExperimentContext context;
		try {
			context = contextManager.getContext(this.getClientUsername(),
					reservationId);

			context.executeOperation(operation);

		} catch (ExperimentDatabaseException
				| UndefinedExperimentParameterException e) {
			buildErrorLogFromException(action, e, "database", "query problem",
					"invalid parameter?");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentOperationException(action);
		} catch (ExperimentParameterException e) {
			buildErrorLogFromException(action, e, "logic", "parameter problem");
			this.logContextError(getClientUsername(), reservationId, action);
			action.put("details", e.getAction());
			throw new ExperimentOperationException(action);
		} catch (InvalidUserContextException e) {
			buildErrorLogFromException(action, e, "logic", "invalid user");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new InvalidUserContextException(action);
		} catch (ExperimentOperationException e) {
			buildErrorLogFromException(action, e, "logic", "operation error");
			this.logContextError(getClientUsername(), reservationId, action);
			throw e;
		} catch (NoSuchOperationException e) {
			buildErrorLogFromException(action, e, "logic", "unknown operation");
			this.logContextError(getClientUsername(), reservationId, action);
			throw e;
		}

		this.logContextInfo(getClientUsername(), reservationId, action);
	}

	@Override
	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			int reservationId) throws ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get runtime", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			boolean grantAccess;
			if (adminMode) {
				grantAccess = experimentAdapter.anyReservationActiveNow();
			} else {
				grantAccess = experimentAdapter
						.anyUserReservationActiveNow(this.getClientUsername());
			}

			if (!grantAccess) {
				buildErrorLogFromException(action, null, "logic", "0 active");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new NoSuchReservationException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		}

		try {
			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				buildErrorLogFromException(action, null, "logic",
						"context not ready");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentNotInstantiatedException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		}

		ExperimentRuntimeInformation context;
		try {
			context = experimentAdapter
					.getExperimentRuntimeContext(reservationId);

			return context;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public void addExperimentOperation(String experiment,
			OperationInformation operation) throws ExperimentException,
			NoSuchExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "add operation", experiment,
				operation.getName(), operation.getType(),
				operation.getArguments());

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				buildErrorLogFromException(action, null, "logic",
						"invalid owner");
				this.logError(getClientUsername(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildOperation(operation);
			contextManager.deleteExperimentContexts(experiment);
		} catch (OperationTypeNotAvailableException
				| ExperimentOperationException | ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);
	}

	@Override
	public void addExperimentParameter(String experiment,
			ParameterInformation parameter) throws ExperimentException,
			NoSuchExperimentException, NoSuchParameterException {

		LogAction action = new LogAction();
		buildOperationLog(action, "add parameter", experiment,
				parameter.getName(), parameter.getType(),
				parameter.getArguments());

		try {
			ExperimentInformation expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				buildErrorLogFromException(action, null, "logic",
						"invalid owner");
				this.logError(getClientUsername(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
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
				buildErrorLogFromException(action, null, "json",
						"arguments conversion");
				this.logError(getClientUsername(), action);
				throw new NoSuchParameterException(action);
			}
			model.buildParameter(parameter);
		} catch (ParameterTypeNotAvailableException
				| ExperimentDatabaseException | ExperimentParameterException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
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
		Object objValue = null;
		try {

			objValue = JSONConverter.fromJSON((String) input.content,
					Object.class, null);

		} catch (IOException e) {
			buildErrorLogFromException(action, null, "json", "value conversion");
			this.logContextError(getClientUsername(), reservationId, action);
		}

		buildOperationLog(action, "set value", reservationId, parameter,
				objValue);

		try {
			if (!experimentAdapter.reservationIsActiveNowForUser(reservationId,
					this.getClientUsername())) {

				boolean active = experimentAdapter
						.isReservationActiveNow(reservationId);

				if (active) {
					buildErrorLogFromException(action, null, "logic",
							"invalid user");
					this.logContextError(getClientUsername(), reservationId,
							action);
					throw new InvalidUserContextException(action);
				}

				buildErrorLogFromException(action, null, "logic", "no context");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new NoSuchReservationException(action);
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				buildErrorLogFromException(action, null, "logic",
						"context not ready");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentNotInstantiatedException(action);
			}

			ExperimentContext context = contextManager.getContext(
					this.getClientUsername(), reservationId);

			context.setParameterValue(parameter, objValue);

			this.logContextInfo(getClientUsername(), reservationId, action);

		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentParameterException(action);
		} catch (InvalidUserContextException e) {
			buildErrorLogFromException(action, e, "logic", "invalid user");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new InvalidUserContextException(action);
		} catch (NoSuchParameterException e) {
			buildErrorLogFromException(action, e, "logic",
					"parameter not valid");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new NoSuchParameterException(action);
		}
	}

	@Override
	public ObjectResponse getExperimentContext(int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException, InvalidUserContextException,
			NoSuchParameterException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get context", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {

			boolean active = experimentAdapter
					.isReservationActiveNow(reservationId);
			boolean grantAccess;

			if (adminMode) {
				grantAccess = active;
			} else {
				grantAccess = experimentAdapter.reservationIsActiveNowForUser(
						reservationId, this.getClientUsername());
			}

			if (!grantAccess) {
				if (active) {
					buildErrorLogFromException(action, null, "logic",
							"invalid user");
					this.logContextError(getClientUsername(), reservationId,
							action);
					throw new InvalidUserContextException(action);
				}
				buildErrorLogFromException(action, null, "logic", "no context");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new NoSuchReservationException(action);
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				buildErrorLogFromException(action, null, "logic",
						"context not ready");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentNotInstantiatedException(action);
			}

			ExperimentContext context;

			ReservationInformation resInfo = this.experimentAdapter
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
				buildErrorLogFromException(action, null, "json",
						"value conversion");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentException(action);
			}

			return response;

		} catch (ExperimentDatabaseException | ExperimentParameterException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		} catch (InvalidUserContextException e) {
			buildErrorLogFromException(action, e, "logic", "invalid user");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new InvalidUserContextException(action);
		} catch (NoSuchParameterException e) {
			buildErrorLogFromException(action, e, "logic",
					"parameter not valid");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new NoSuchParameterException(action);
		}
	}

	@Override
	public boolean isExperimentContextReady(int reservationId)
			throws ExperimentException, NoSuchReservationException {

		LogAction action = new LogAction();
		buildOperationLog(action, "is context ready", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {

			boolean grantAccess;
			if (adminMode) {
				grantAccess = experimentAdapter
						.isReservationActiveNow(reservationId);
			} else {
				grantAccess = experimentAdapter.reservationIsActiveNowForUser(
						reservationId, this.getClientUsername());
			}

			if (!grantAccess) {
				buildErrorLogFromException(action, null, "logic", "no context");
				this.logContextError(getClientUsername(), reservationId, action);
				return false;
			}

			return experimentAdapter.isReservationContextReady(reservationId);

		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public ObjectResponse getExperimentParameterValue(int reservationId,
			String parameter) throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException {

		LogAction action = new LogAction();
		buildOperationLog(action, "get value", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {

			boolean grantAccess;
			if (adminMode) {
				grantAccess = experimentAdapter
						.isReservationActiveNow(reservationId);
			} else {
				grantAccess = experimentAdapter.reservationIsActiveNowForUser(
						reservationId, this.getClientUsername());
			}

			if (!grantAccess) {
				buildErrorLogFromException(action, null, "logic", "no context");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new NoSuchReservationException(action);
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				buildErrorLogFromException(action, null, "logic",
						"context not ready");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentNotInstantiatedException(action);
			}

			ExperimentContext context;

			ReservationInformation resInfo = this.experimentAdapter
					.getReservationInformation(reservationId);

			context = contextManager.getContext(resInfo.getUser(),
					reservationId);

			Object value = context.getParameterValue(parameter);

			ObjectResponse response = null;
			try {
				response = new ObjectResponse(JSONConverter.toJSON(value));
			} catch (IOException e) {
				buildErrorLogFromException(action, null, "json",
						"value conversion");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new ExperimentParameterException(action);
			}

			return response;

		} catch (ExperimentDatabaseException | ExperimentParameterException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new ExperimentParameterException(action);
		} catch (InvalidUserContextException e) {
			buildErrorLogFromException(action, e, "logic", "invalid user");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new InvalidUserContextException(action);
		} catch (NoSuchParameterException e) {
			buildErrorLogFromException(action, e, "logic",
					"parameter not valid");
			this.logContextError(getClientUsername(), reservationId, action);
			throw new NoSuchParameterException(action);
		}
	}

	@Override
	public void deleteExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException {

		LogAction action = new LogAction();
		buildOperationLog(action, "remove experiment", experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				buildErrorLogFromException(action, null, "logic",
						"invalid owner");
				this.logError(getClientUsername(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		try {
			modelManager.deleteModel(experiment);
			contextManager.deleteExperimentContexts(experiment);
			this.logInfo(getClientUsername(), action);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
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
		buildOperationLog(action, "get reservation info", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			ReservationInformation reservationInfo = experimentAdapter
					.getReservationInformation(reservationId);

			if (!adminMode
					&& !reservationInfo.getUser().equals(
							this.getClientUsername())) {
				buildErrorLogFromException(action, null, "logic",
						"invalid user");
				this.logContextError(getClientUsername(), reservationId, action);
				throw new InvalidUserContextException(action);
			}

			return reservationInfo;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logContextError(getClientUsername(), reservationId, action);
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
			throws ExperimentException, NoSuchParameterException {

		return modelManager.getExperimentParameter(name);
	}

	@Override
	public ExperimentOperation getExperimentOperation(
			@WebParam(name = "operationName") String name)
			throws ExperimentException, NoSuchOperationException {
		return modelManager.getExperimentOperation(name);
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
		buildOperationLog(action, "new online", experiment);

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"ONLINE");
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, null, "database");
			this.logError(getClientUsername(), action);
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
		buildOperationLog(action, "get context results", reservationId);

		try {
			List<ResultInformation> results = experimentAdapter
					.getContextResults(reservationId);

			return results;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, null, "database");
			this.logContextError(getClientUsername(), reservationId, action);
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
		buildOperationLog(action, "get experiment results", experiment);

		try {
			List<ResultInformation> results = experimentAdapter
					.getExperimentResults(experiment);

			return results;
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, null, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public void deleteExperimentParameter(String experiment, String parameter)
			throws ExperimentException, NoSuchExperimentException {
		LogAction action = new LogAction();
		buildOperationLog(action, "remove experiment parameter", experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				buildErrorLogFromException(action, null, "logic",
						"invalid owner");
				this.logError(getClientUsername(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		try {
			modelManager.deleteParameter(experiment, parameter);
			contextManager.deleteExperimentContexts(experiment);
			this.logInfo(getClientUsername(), action);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);

	}

	@Override
	public void deleteExperimentOperation(String experiment, String operation)
			throws ExperimentException, NoSuchExperimentException {
		LogAction action = new LogAction();
		buildOperationLog(action, "remove experiment operation", experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				buildErrorLogFromException(action, null, "logic",
						"invalid owner");
				this.logError(getClientUsername(), action);
				throw new NoSuchExperimentException(action);
			}
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		try {
			modelManager.deleteOperation(experiment, operation);
			contextManager.deleteExperimentContexts(experiment);
			this.logInfo(getClientUsername(), action);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

		this.logInfo(getClientUsername(), action);

	}

	@Override
	public void emptyExperiment(String experiment) throws ExperimentException,
			NoSuchExperimentException {
		LogAction action = new LogAction();
		buildOperationLog(action, "empty experiment", experiment);

		try {
			experimentAdapter.emptyExperiment(experiment);
			contextManager.deleteExperimentContexts(experiment);

			this.logInfo(getClientUsername(), action);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public int registerRTScript(String telescope, ScriptSlot scriptSlot,
			String operation, String init, String result, boolean notify)
			throws NoSuchExperimentException, ExperimentException,
			OverlapRTScriptException, InvalidUserException {
		LogAction action = new LogAction();
		buildOperationLog(action, "register script", telescope, scriptSlot,
				operation);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = this.isAdminMode(action);

		action.put("admin mode", adminMode);

		if (!adminMode) {
			throw new InvalidUserException(action);
		}

		String experiment = telescope + "Script";

		try {
			if (!experimentAdapter.containsExperiment(experiment)) {
				throw new NoSuchExperimentException(action);
			}

			if (!rtRepository.containsRT(telescope)) {
				action.put("wrong telescope", telescope);
				throw new ExperimentException(action);
			}

			int sid = scriptAdapter.addRTScript(this.getClientUsername(),
					experiment, telescope, operation, scriptSlot, init, result,
					notify);

			this.logInfo(getClientUsername(), action);

			return sid;
		} catch (ExperimentDatabaseException | RTRepositoryException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public RTScriptInformation getRTScriptInformation(int sid)
			throws NoSuchScriptException, ExperimentException {
		LogAction action = new LogAction();

		try {
			return scriptAdapter.getRTScriptInformation(sid);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public List<Integer> getAllRTScripts(String rt) throws ExperimentException {
		LogAction action = new LogAction();

		try {
			return scriptAdapter.getAllRTScripts(rt);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}
	}

	@Override
	public void removeRTScript(int sid) throws NoSuchScriptException,
			InvalidUserException, ExperimentException {
		LogAction action = new LogAction();

		try {
			RTScriptInformation scriptInfo = scriptAdapter
					.getRTScriptInformation(sid);
			if (scriptInfo == null) {
				throw new NoSuchScriptException(sid);
			}

			if (!scriptInfo.getUsername().equals(this.getClientUsername())) {
				throw new InvalidUserException("wrong user");
			}

			scriptAdapter.removeRTScript(sid);
		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		}

	}

	@Override
	public List<String> getRTScriptAvailableOperations(String telescope)
			throws ExperimentException, NoSuchScriptException {

		LogAction action = new LogAction();

		try {
			ExperimentInformation expInfo = experimentAdapter
					.getExperimentInformation(telescope + "Script");

			List<OperationInformation> operations = expInfo.getOperations();
			List<String> names = new ArrayList<String>();

			for (OperationInformation op : operations) {
				names.add(op.getName());
			}

			return names;

		} catch (ExperimentDatabaseException e) {
			buildErrorLogFromException(action, e, "database");
			this.logError(getClientUsername(), action);
			throw new ExperimentException(action);
		} catch (NoSuchExperimentException e) {
			action.put("rt", telescope);
			action.put("cause", "no experiment");
			throw new NoSuchScriptException(action);
		}
	}
}
