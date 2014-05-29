package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.ExperimentInterface;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentType;
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
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;
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
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.Param;
import eu.gloria.gs.services.log.action.ServiceOperation;
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
		super(Experiment.class.getSimpleName());
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

	private boolean isAdminMode(Action action) {
		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserCredentials(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e) {
			this.logWarning(action);
		}

		return adminMode;
	}

	@Override
	@ServiceOperation(name = "create offline")
	public void createOfflineExperiment(
			@Param(name = "experiment") String experiment)
			throws DuplicateExperimentException, ExperimentException {

		Action action = new Action(Experiment.class, "createOfflineExperiment",
				experiment);

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"OFFLINE");
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get experiment info")
	public ExperimentInformation getExperimentInformation(
			@Param(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException {

		Action action = new Action(Experiment.class,
				"getExperimentInformation", experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter.getExperimentInformation(experiment);
			return expInfo;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get parameter info")
	public ParameterInformation getParameterInformation(
			@Param(name = "experiment") String experiment,
			@Param(name = "parameter") String parameter)
			throws ExperimentException, NoSuchExperimentException {

		Action action = new Action(Experiment.class, "getParameterInformation",
				experiment, parameter);

		ParameterInformation paramInfo;
		try {
			paramInfo = experimentAdapter.getExperimentInformation(experiment)
					.getParameter(parameter);
			return paramInfo;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "set experiment description")
	public void setExperimentDescription(
			@Param(name = "experiment") String experiment, String description)
			throws ExperimentException, NoSuchExperimentException {

		Action action = new Action(Experiment.class,
				"setExperimentDescription", experiment);
		try {
			experimentAdapter.setExperimentDescription(experiment, description);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

	}

	@Override
	@ServiceOperation(name = "get all experiments")
	public List<String> getAllExperiments(
			@Param(name = "type") ExperimentType type)
			throws ExperimentException {

		Action action = new Action(Experiment.class, "getAllExperiments", type);

		List<String> experiments = null;
		try {
			experiments = experimentAdapter.getAllExperiments(type);

			if (experiments == null || experiments.size() == 0) {
				this.logWarning(action);
				throw new ExperimentException("no experiments");
			}

			return experiments;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

	}

	@Override
	@ServiceOperation(name = "contains")
	public boolean containsExperiment(
			@Param(name = "experiment") String experiment)
			throws ExperimentException {
		Action action = new Action(Experiment.class, "containsExperiment",
				experiment);
		try {
			return experimentAdapter.containsExperiment(experiment);

		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get my pending")
	public List<ReservationInformation> getMyPendingReservations(
			@Param(name = "type") ExperimentType type)
			throws ExperimentException, NoReservationsAvailableException {
		Action action = new Action(Experiment.class, "getMyPendingReservations");

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			return this.getPendingReservations(type, adminMode);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	private List<ReservationInformation> getPendingReservations(
			ExperimentType type, boolean adminMode) throws ExperimentException,
			NoReservationsAvailableException {

		List<ReservationInformation> resInfo = null;
		if (adminMode) {
			resInfo = experimentAdapter.getAllPendingReservations(type);
		} else {
			resInfo = experimentAdapter.getUserPendingReservations(type,
					this.getClientUsername());
		}

		return resInfo;
	}

	private boolean anyReservationActiveNow(ExperimentType type,
			boolean adminMode) throws ExperimentException {
		boolean anyActiveNow;
		if (adminMode) {
			anyActiveNow = experimentAdapter.anyReservationActiveNow(type);
		} else {
			anyActiveNow = experimentAdapter.anyUserReservationActiveNow(type,
					this.getClientUsername());
		}

		return anyActiveNow;
	}

	@Override
	@ServiceOperation(name = "any reservation active")
	public boolean anyReservationActiveNow(
			@Param(name = "type") ExperimentType type)
			throws ExperimentException {

		Action action = new Action(Experiment.class, "anyReservationActiveNow",
				type);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			return this.anyReservationActiveNow(type, adminMode);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get current reservations")
	public List<ReservationInformation> getMyCurrentReservations(
			@Param(name = "type") ExperimentType type)
			throws ExperimentException, NoReservationsAvailableException {

		Action action = new Action(Experiment.class,
				"getMyCurrentReservations", type);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			return this.getCurrentReservations(type, adminMode);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

	}

	private List<ReservationInformation> getCurrentReservations(
			ExperimentType type, boolean adminMode) throws ExperimentException,
			NoReservationsAvailableException {
		List<ReservationInformation> reservations;

		if (adminMode) {
			reservations = experimentAdapter.getAllReservationsActiveNow(type);
		} else {
			reservations = experimentAdapter.getUserReservationsActiveNow(type,
					this.getClientUsername());
		}

		return reservations;
	}

	@Override
	@ServiceOperation(name = "apply for")
	public void applyForExperiment(@Param(name = "experiment") String experiment)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException {

		Action action = new Action(Experiment.class, "applyForExperiment",
				experiment);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.applyFor(experiment, this.getClientUsername());
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		} finally {
			GSClientProvider.clearCredentials();
		}
	}

	@Override
	@ServiceOperation(name = "reserve")
	public void reserveExperiment(
			@Param(name = "experiment") String experiment,
			@Param(name = "telescopes") List<String> telescopes,
			@Param(name = "slot") TimeSlot timeSlot)
			throws ExperimentException, NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException {

		Action action = new Action(Experiment.class, "reserveExperiment",
				experiment, telescopes, timeSlot);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			experimentBooker.reserve(experiment, this.getClientUsername(),
					telescopes, timeSlot, adminMode);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		} finally {
			GSClientProvider.clearCredentials();
		}
	}

	@Override
	@ServiceOperation(name = "get available")
	public List<TimeSlot> getAvailableReservations(String experiment,
			List<String> telescopes) throws ExperimentException,
			ExperimentReservationArgumentException {

		Action action = new Action(Experiment.class,
				"getAvailableReservations", experiment);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			List<TimeSlot> timeSlots = experimentBooker.getAvailableTimeSlots(
					experiment, telescopes, adminMode);

			if (timeSlots == null | timeSlots.size() == 0) {
				ExperimentException e = new ExperimentException();
				e.getAction().put("slots", 0);
				throw e;
			}
			
			action.put("slots", timeSlots.size());
			this.logInfo(action);

			return timeSlots;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		} finally {
			GSClientProvider.clearCredentials();
		}

	}

	@Override
	@ServiceOperation(name = "cancel")
	public void cancelExperimentReservation(
			@Param(name = "rid") int reservationId) throws ExperimentException,
			NoSuchReservationException, InvalidUserContextException {

		Action action = new Action(Experiment.class,
				"cancelExperimentReservation", reservationId);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.cancelReservation(this.getClientUsername(),
					reservationId);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		} finally {
			GSClientProvider.clearCredentials();
		}
	}

	@Override
	@ServiceOperation(name = "reset")
	public void resetExperimentContext(@Param(name = "rid") int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException, ExperimentNotInstantiatedException {

		Action action = new Action(Experiment.class, "resetExperimentContext",
				reservationId);

		try {
			if (experimentAdapter.isReservationContextReady(reservationId)) {
				experimentAdapter.deleteExperimentContext(reservationId);
				this.logInfo(action);
			} else if (experimentAdapter.reservationIsActiveNowForUser(
					reservationId, getClientUsername())) {
				throw new ExperimentNotInstantiatedException(reservationId);
			} else if (experimentAdapter.isReservationActiveNow(reservationId)) {
				throw new InvalidUserContextException(getClientUsername(),
						reservationId);
			}
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "execute")
	public void executeExperimentOperation(
			@Param(name = "rid") int reservationId,
			@Param(name = "name") String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, ExperimentException {

		Action action = new Action(Experiment.class,
				"executeExperimentOperation", reservationId, operation);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);
		
		ExperimentContext context;
		try {
			context = contextManager.getContext(this.getClientUsername(),
					reservationId);

			ExperimentType type = experimentAdapter.getExperimentType(context
					.getExperimentName());
			action.put("type", type);

			context.executeOperation(operation);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get runtime")
	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			@Param(name = "rid") int reservationId) throws ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException {

		Action action = new Action(Experiment.class,
				"getExperimentRuntimeInformation", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			boolean grantAccess;
			if (adminMode) {
				grantAccess = experimentAdapter.anyReservationActiveNow(null);
			} else {
				grantAccess = experimentAdapter.anyUserReservationActiveNow(
						null, this.getClientUsername());
			}

			if (!grantAccess) {
				NoSuchReservationException e = new NoSuchReservationException(
						reservationId);
				e.getAction().put("message", "0 active");
				throw e;
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				ExperimentNotInstantiatedException e = new ExperimentNotInstantiatedException(
						reservationId);
				e.getAction().put("message", "context not ready");
				throw e;
			}

			ExperimentRuntimeInformation context = experimentAdapter
					.getExperimentRuntimeContext(reservationId);

			return context;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "add operation")
	public void addExperimentOperation(
			@Param(name = "experiment") String experiment,
			OperationInformation operation) throws ExperimentException,
			NoSuchExperimentException, NoSuchExperimentException {

		Action action = new Action(Experiment.class, "addExperimentOperation",
				experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				e.getAction().put("message", "invalid owner");
				throw e;
			}
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildOperation(operation);
			contextManager.deleteExperimentContexts(experiment);
		} catch (NoSuchOperationException | NoSuchParameterException
				| ExperimentOperationException
				| OperationTypeNotAvailableException e) {
			this.logException(action, e);
			throw new ExperimentException(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

		this.logInfo(action);
	}

	@Override
	@ServiceOperation(name = "add parameter")
	public void addExperimentParameter(
			@Param(name = "experiment") String experiment,
			ParameterInformation parameter) throws ExperimentException,
			NoSuchExperimentException, NoSuchParameterException {

		Action action = new Action(Experiment.class, "addExperimentParameter",
				experiment);

		try {
			ExperimentInformation expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				e.getAction().put("message", "invalid owner");
				throw e;
			}
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
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
				NoSuchParameterException ex = new NoSuchParameterException(
						parameter.getName());
				ex.getAction().put("message", e.getMessage());
				throw ex;
			}
			model.buildParameter(parameter);
		} catch (ExperimentParameterException
				| ParameterTypeNotAvailableException e) {
			this.logException(action, e);
			throw new ExperimentException(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

		this.logInfo(action);
	}

	@Override
	@ServiceOperation(name = "set parameter value")
	public void setExperimentParameterValue(
			@Param(name = "rid") int reservationId,
			@Param(name = "parameter") String parameter, ObjectResponse input)
			throws ExperimentParameterException, ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException {

		Action action = new Action(Experiment.class,
				"setExperimentParameterValue", reservationId, parameter);

		Object objValue = null;
		try {

			objValue = JSONConverter.fromJSON((String) input.content,
					Object.class, null);

		} catch (IOException e) {
			this.logException(action, e);
		}

		try {
			if (!experimentAdapter.reservationIsActiveNowForUser(reservationId,
					this.getClientUsername())) {

				boolean active = experimentAdapter
						.isReservationActiveNow(reservationId);

				if (active) {
					throw new InvalidUserContextException(getClientUsername(),
							reservationId);
				}

				throw new NoSuchReservationException("context not active");
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				throw new ExperimentNotInstantiatedException(
						"context not ready");
			}

			ExperimentContext context = contextManager.getContext(
					this.getClientUsername(), reservationId);

			context.setParameterValue(parameter, objValue);

			this.logInfo(reservationId, getClientUsername(), action);

		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get context")
	public ObjectResponse getExperimentContext(
			@Param(name = "rid") int reservationId) throws ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException {

		Action action = new Action(Experiment.class, "getExperimentContext",
				reservationId);

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
					throw new InvalidUserContextException(getClientPassword(),
							reservationId);
				}
				throw new NoSuchReservationException("context not active");
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				throw new ExperimentNotInstantiatedException(
						"context not ready");
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
				throw new ExperimentException(e.getMessage());
			}

			return response;

		} catch (ExperimentParameterException e) {
			this.logException(action, e);
			throw new ExperimentException(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "is ready")
	public boolean isExperimentContextReady(
			@Param(name = "rid") int reservationId) throws ExperimentException,
			NoSuchReservationException {

		Action action = new Action(Experiment.class,
				"isExperimentContextReady", reservationId);

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
				action.put("message", "no context");
				return false;
			}

			return experimentAdapter.isReservationContextReady(reservationId);

		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get parameter value")
	public ObjectResponse getExperimentParameterValue(
			@Param(name = "rid") int reservationId,
			@Param(name = "parameter") String parameter)
			throws ExperimentParameterException, ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException {

		Action action = new Action(Experiment.class,
				"getExperimentParameterValue", reservationId, parameter);

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
				throw new InvalidUserContextException(getClientUsername(),
						reservationId);
			}

			if (!experimentAdapter.isReservationContextReady(reservationId)) {
				ExperimentNotInstantiatedException e = new ExperimentNotInstantiatedException(
						reservationId);
				e.getAction().put("message", "context not ready");
				throw e;
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
				throw new ExperimentParameterException(parameter,
						e.getMessage());
			}

			return response;

		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "delete")
	public void deleteExperiment(@Param(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException {

		Action action = new Action(Experiment.class, "deleteExperiment",
				experiment);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				e.getAction().put("message", "invalid owner");
				throw e;
			}

			modelManager.deleteModel(experiment);
			contextManager.deleteExperimentContexts(experiment);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get reservation info")
	public ReservationInformation getReservationInformation(
			@Param(name = "rid") int reservationId)
			throws NoSuchReservationException, InvalidUserContextException,
			ExperimentException {

		Action action = new Action(Experiment.class,
				"getReservationInformation", reservationId);

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		try {
			ReservationInformation reservationInfo = experimentAdapter
					.getReservationInformation(reservationId);

			if (!adminMode
					&& !reservationInfo.getUser().equals(
							this.getClientUsername())) {
				throw new InvalidUserContextException(getClientUsername(),
						reservationId);
			}

			return reservationInfo;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

	}

	@Override
	@ServiceOperation(name = "get all parameters")
	public Set<String> getAllExperimentParameters() throws ExperimentException {
		Map<String, ExperimentParameter> parameters = modelManager
				.getAllExperimentParameters();

		return parameters.keySet();
	}

	@Override
	@ServiceOperation(name = "get all operations")
	public Set<String> getAllExperimentOperations() throws ExperimentException {

		return modelManager.getAllExperimentOperations().keySet();
	}

	@Override
	@ServiceOperation(name = "get parameter")
	public ExperimentParameter getExperimentParameter(
			@Param(name = "parameterName") String name)
			throws ExperimentException, NoSuchParameterException {

		return modelManager.getExperimentParameter(name);
	}

	@Override
	@ServiceOperation(name = "get operation")
	public ExperimentOperation getExperimentOperation(
			@Param(name = "operationName") String name)
			throws ExperimentException, NoSuchOperationException {
		return modelManager.getExperimentOperation(name);
	}

	@Override
	@ServiceOperation(name = "create online")
	public void createOnlineExperiment(
			@Param(name = "experiment") String experiment)
			throws ExperimentException, DuplicateExperimentException {

		Action action = new Action(Experiment.class, "createOnlineExperiment",
				experiment);

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"ONLINE");
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get context results")
	public List<ResultInformation> getContextResults(
			@Param(name = "rid") int reservationId) throws ExperimentException,
			NoSuchReservationException {

		Action action = new Action(Experiment.class, "getContextResults",
				reservationId);

		try {
			List<ResultInformation> results = experimentAdapter
					.getContextResults(reservationId);

			return results;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get experiment results")
	public List<ResultInformation> getExperimentResults(
			@Param(name = "experiment") String experiment)
			throws ExperimentException {
		Action action = new Action(Experiment.class, "getExperimentResults",
				experiment);

		try {
			List<ResultInformation> results = experimentAdapter
					.getExperimentResults(experiment);

			return results;
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "delete parameter")
	public void deleteExperimentParameter(
			@Param(name = "experiment") String experiment,
			@Param(name = "parameter") String parameter)
			throws ExperimentException, NoSuchExperimentException {
		Action action = new Action(Experiment.class,
				"deleteExperimentParameter", experiment, parameter);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				e.getAction().put("message", "invalid owner");
				throw e;
			}

			modelManager.deleteParameter(experiment, parameter);
			contextManager.deleteExperimentContexts(experiment);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "delete operation")
	public void deleteExperimentOperation(
			@Param(name = "experiment") String experiment,
			@Param(name = "operation") String operation)
			throws ExperimentException, NoSuchExperimentException {

		Action action = new Action(Experiment.class,
				"deleteExperimentOperation", experiment, operation);

		ExperimentInformation expInfo;
		try {
			expInfo = experimentAdapter
					.getBasicExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				e.getAction().put("message", "invalid owner");
				throw e;
			}

			modelManager.deleteOperation(experiment, operation);
			contextManager.deleteExperimentContexts(experiment);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "empty")
	public void emptyExperiment(@Param(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException {
		Action action = new Action(Experiment.class, "emptyExperiment",
				experiment);

		try {
			experimentAdapter.emptyExperiment(experiment);
			contextManager.deleteExperimentContexts(experiment);

			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

	}

	@Override
	@ServiceOperation(name = "register script")
	public int registerRTScript(@Param(name = "telescope") String telescope,
			@Param(name = "slot") ScriptSlot scriptSlot,
			@Param(name = "operation") String operation, String init,
			String result, boolean notify) throws NoSuchExperimentException,
			ExperimentException, OverlapRTScriptException, InvalidUserException {
		Action action = new Action(Experiment.class, "registerRTScript",
				telescope, scriptSlot, operation);

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		boolean adminMode = this.isAdminMode(action);
		action.put("admin mode", adminMode);

		if (!adminMode) {
			InvalidUserException e = new InvalidUserException(
					getClientUsername());
			throw e;
		}

		String experiment = telescope + "Script";

		try {
			if (!experimentAdapter.containsExperiment(experiment)) {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				e.getAction().put("message", "no script experiment");
				throw e;
			}

			if (!rtRepository.containsRT(telescope)) {
				throw new ExperimentException("wrong telescope");
			}

			int sid = scriptAdapter.addRTScript(this.getClientUsername(),
					experiment, telescope, operation, scriptSlot, init, result,
					notify);

			this.logInfo(action);

			return sid;
		} catch (RTRepositoryException e) {
			this.logException(action, e);
			throw new ExperimentException(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		} finally {
			GSClientProvider.clearCredentials();
		}
	}

	@Override
	@ServiceOperation(name = "get script info")
	public RTScriptInformation getRTScriptInformation(
			@Param(name = "sid") int sid) throws NoSuchScriptException,
			ExperimentException {
		Action action = new Action(Experiment.class, "getRTScriptInformation",
				sid);

		try {
			return scriptAdapter.getRTScriptInformation(sid);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "get all scripts")
	public List<Integer> getAllRTScripts(@Param(name = "rt") String rt)
			throws ExperimentException {
		Action action = new Action(Experiment.class, "getAllRTScripts", rt);

		try {
			return scriptAdapter.getAllRTScripts(rt);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}

	@Override
	@ServiceOperation(name = "remove script")
	public void removeRTScript(@Param(name = "sid") int sid)
			throws NoSuchScriptException, InvalidUserException,
			ExperimentException {
		Action action = new Action(Experiment.class, "removeRTScript", sid);

		try {
			RTScriptInformation scriptInfo = scriptAdapter
					.getRTScriptInformation(sid);
			if (scriptInfo == null) {
				throw new NoSuchScriptException(sid);
			}

			if (!scriptInfo.getUsername().equals(this.getClientUsername())) {
				InvalidUserException e = new InvalidUserException(
						getClientUsername());
				throw e;
			}

			scriptAdapter.removeRTScript(sid);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}

	}

	@Override
	@ServiceOperation(name = "get script operations")
	public List<String> getRTScriptAvailableOperations(
			@Param(name = "rt") String telescope) throws ExperimentException,
			NoSuchScriptException {

		Action action = new Action(Experiment.class,
				"getRTScriptAvailableOperations", telescope);

		try {
			ExperimentInformation expInfo = experimentAdapter
					.getExperimentInformation(telescope + "Script");

			List<OperationInformation> operations = expInfo.getOperations();
			List<String> names = new ArrayList<String>();

			for (OperationInformation op : operations) {
				names.add(op.getName());
			}

			return names;

		} catch (NoSuchExperimentException e) {
			this.logException(action, e);
			throw new ExperimentException(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw e;
		}
	}
}
