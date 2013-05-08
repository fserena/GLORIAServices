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
import eu.gloria.gs.services.repository.user.UserRepositoryException;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRole;

public class OnlineExperiment extends GSLogProducerService implements
		OnlineExperimentInterface {

	private ExperimentDBAdapter adapter;
	private ExperimentBooker experimentBooker;
	private ExperimentModelManager modelManager;
	private ExperimentContextManager contextManager;
	private UserRepositoryInterface userRepository;

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

	public void setUserRepository(UserRepositoryInterface userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void createExperiment(String experiment)
			throws DuplicateExperimentException, OnlineExperimentException {

		try {
			modelManager.createModel(experiment, this.getClientUsername());
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/new?name=" + experiment + "->ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "experiments/new?name="
					+ experiment);
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
			this.logAction(this.getClientUsername(), "experiments/"
					+ experiment + "/setDescription");
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

				try {
					this.logAction(this.getClientUsername(),
							"experiments/online/list->ERROR");
				} catch (ActionLogException e) {
					e.printStackTrace();
				}

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

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
		}

		List<ReservationInformation> resInfo = null;
		try {
			if (adminMode) {
				resInfo = adapter.getUserPendingReservations();
			} else {
				resInfo = adapter.getUserPendingReservations(this
						.getClientUsername());
			}

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/pending->" + resInfo.size());
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

			return resInfo;
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/pending->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			throw new OnlineExperimentException(e.getMessage());
		} catch (NoReservationsAvailableException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/pending->[]");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw e;
		}
	}

	@Override
	public boolean anyReservationActiveNow() throws OnlineExperimentException {

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
		}

		boolean anyActiveNow;
		try {

			if (adminMode) {
				anyActiveNow = adapter.anyUserReservationActiveNow();
			} else {
				anyActiveNow = adapter.anyUserReservationActiveNow(this
						.getClientUsername());
			}

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/anyactive->" + anyActiveNow);
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			return anyActiveNow;
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/anyactive->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentReservations()
			throws OnlineExperimentException, NoReservationsAvailableException {

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
		}

		List<ReservationInformation> reservations;
		try {

			if (adminMode) {
				reservations = adapter.getAllReservationsActiveNow();
			} else {
				reservations = adapter.getUserReservationActiveNow(this
						.getClientUsername());
			}

			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/reservations/activenow->"
								+ reservations.size());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			return reservations;
		} catch (ExperimentDatabaseException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/activenow->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new OnlineExperimentException(e.getMessage());
		} catch (NoReservationsAvailableException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/activenow->[]");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw e;
		}
	}

	@Override
	public void reserveExperiment(String experiment, List<String> telescopes,
			TimeSlot timeSlot) throws OnlineExperimentException,
			NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException {

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
		}

		try {
			experimentBooker.reserve(experiment, this.getClientUsername(),
					telescopes, timeSlot, adminMode);
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/reservations/make?" + experiment + "&"
								+ telescopes.toString() + "&{"
								+ timeSlot.getBegin() + timeSlot.getEnd() + "}"
								+ "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(
					this.getClientUsername(),
					"experiments/reservations/make?" + experiment + "&"
							+ telescopes.toString() + "&{"
							+ timeSlot.getBegin() + timeSlot.getEnd() + "}");
		} catch (ActionLogException el) {
			el.printStackTrace();
		}
	}

	@Override
	public List<TimeSlot> getAvailableReservations(String experiment,
			List<String> telescopes) throws OnlineExperimentException,
			ExperimentReservationArgumentException {

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
		}

		try {
			List<TimeSlot> timeSlots = experimentBooker.getAvailableTimeSlots(
					experiment, telescopes, adminMode);

			if (timeSlots == null | timeSlots.size() == 0) {
				try {
					this.logAction(
							this.getClientUsername(),
							"experiments/reservations/available?"
									+ telescopes.toString() + "[]");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new OnlineExperimentException(
						"There are no timeslots available");
			}

			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/reservations/available?"
								+ telescopes.toString() + "->"
								+ timeSlots.size());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			return timeSlots;
		} catch (ExperimentDatabaseException e) {
			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/reservations/available?"
								+ telescopes.toString() + "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new OnlineExperimentException(e.getMessage());
		}

	}

	@Override
	public void cancelExperimentReservation(int reservationId)
			throws OnlineExperimentException, NoSuchReservationException {

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		try {
			experimentBooker.cancelReservation(this.getClientUsername(),
					reservationId);
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservations/cancel?" + reservationId
								+ "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/reservations/cancel?" + reservationId);
		} catch (ActionLogException el) {
			el.printStackTrace();
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

			try {
				this.logAction(this.getClientUsername(),
						"experiments/contexts/execute?" + reservationId + "&"
								+ operation + "->"
								+ e.getClass().getSimpleName());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/contexts/execute?" + reservationId + "&"
							+ operation);
		} catch (ActionLogException el) {
			el.printStackTrace();
		}
	}

	@Override
	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			int reservationId) throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
		}

		try {
			boolean grantAccess;
			if (adminMode) {
				grantAccess = adapter.anyUserReservationActiveNow();
			} else {
				grantAccess = adapter.anyUserReservationActiveNow(this
						.getClientUsername());
			}

			if (!grantAccess) {
				try {
					this.logAction(this.getClientUsername(),
							"experiments/contexts/get?" + reservationId
									+ "->INACTIVE");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}

				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/contexts/get?" + reservationId
								+ "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new OnlineExperimentException(e.getMessage());
		}

		try {
			if (!adapter.isReservationContextInstantiated(reservationId)) {

				try {
					this.logAction(this.getClientUsername(),
							"experiments/contexts/get?" + reservationId
									+ "->NOT_INSTANTIATED");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}

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
				try {
					this.logAction(
							this.getClientUsername(),
							"experiments/" + experiment
									+ "/model/operations/add?"
									+ operation.getModelName() + "->OWN_ERROR");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new NoSuchExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/" + experiment + "/model/operations/add?"
								+ operation.getModelName() + "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
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
			this.logAction(this.getClientUsername(),
					"experiments/" + experiment + "/model/operations/add?"
							+ operation.getModelName());
		} catch (ActionLogException el) {
			el.printStackTrace();
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

				try {
					this.logAction(
							this.getClientUsername(),
							"experiments/" + experiment
									+ "/model/parameters/add?"
									+ parameter.getModelName() + "->OWN_ERROR");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new OnlineExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/" + experiment + "/model/parameters/add?"
								+ parameter.getModelName() + "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
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
			this.logAction(this.getClientUsername(),
					"experiments/" + experiment + "/model/parameters/add?"
							+ parameter.getModelName());
		} catch (ActionLogException el) {
			el.printStackTrace();
		}
	}

	@Override
	public void setExperimentParameterValue(int reservationId,
			String parameter, Object value) throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		try {
			if (!adapter.anyUserReservationActiveNow(this.getClientUsername())) {
				try {
					this.logAction(this.getClientUsername(),
							"experiments/contexts/" + reservationId
									+ "/parameters/set?" + parameter + "&"
									+ String.valueOf(value) + "->INACTIVE");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}

			if (!adapter.isReservationContextInstantiated(reservationId)) {
				try {
					this.logAction(this.getClientUsername(),
							"experiments/contexts/" + reservationId
									+ "/parameters/set?" + parameter + "&"
									+ String.valueOf(value)
									+ "->NOT_INSTANTIATED");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new ExperimentNotInstantiatedException(
						"The experiment is not instantiated");
			}

			ExperimentContext context = contextManager.getContext(
					this.getClientUsername(), reservationId);

			context.setParameterValue(parameter, value);

			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/contexts/" + reservationId
								+ "/parameters/set?" + parameter + "&"
								+ String.valueOf(value));
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException | ExperimentParameterException
				| UndefinedExperimentParameterException
				| NoSuchExperimentException e) {
			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/contexts/" + reservationId
								+ "/parameters/set?" + parameter + "&"
								+ String.valueOf(value) + "->"
								+ e.getClass().getSimpleName());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new OnlineExperimentException(e.getMessage());
		}
	}

	@Override
	public Object getExperimentParameterValue(int reservationId,
			String parameter) throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
		}

		try {

			boolean grantAccess;
			if (adminMode) {
				grantAccess = adapter.anyUserReservationActiveNow();
			} else {
				grantAccess = adapter.anyUserReservationActiveNow(this
						.getClientUsername());
			}

			if (!grantAccess) {
				try {
					this.logAction(this.getClientUsername(),
							"experiments/contexts/" + reservationId
									+ "/parameters/get?" + parameter
									+ "->INACTIVE");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}

			if (!adapter.isReservationContextInstantiated(reservationId)) {
				try {
					this.logAction(this.getClientUsername(),
							"experiments/contexts/" + reservationId
									+ "/parameters/get?" + parameter
									+ "->NOT_INSTANTIATED");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new ExperimentNotInstantiatedException(
						"The experiment is not instantiated");
			}

			ExperimentContext context;

			ReservationInformation resInfo = this.adapter
					.getReservationInformation(reservationId);

			context = contextManager.getContext(resInfo.getUser(),
					reservationId);

			Object value = context.getParameterValue(parameter);

			try {
				this.logAction(this.getClientUsername(),
						"experiments/contexts/" + reservationId
								+ "/parameters/get?" + parameter + "->"
								+ String.valueOf(value));
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			return value;

		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException | ExperimentParameterException
				| NoSuchExperimentException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/contexts/" + reservationId
								+ "/parameters/get?" + parameter + "->"
								+ e.getClass().getSimpleName());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
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

		boolean adminMode = false;

		UserInformation userInfo = null;
		try {
			userInfo = this.userRepository.getUserInformation(this
					.getClientUsername());
			if (userInfo.getRoles()[0].equals(UserRole.ADMIN)) {
				adminMode = true;
			}
		} catch (UserRepositoryException e1) {
		}

		try {
			ReservationInformation reservationInfo = adapter
					.getReservationInformation(reservationId);

			if (!adminMode
					&& !reservationInfo.getUser().equals(
							this.getClientUsername())) {
				try {
					this.logAction(this.getClientUsername(),
							"experiments/reservation/get?" + reservationId
									+ "->OWN_ERROR");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new NoSuchReservationException(
						"You have nothing to do with that reservation :)");
			}

			try {
				this.logAction(this.getClientUsername(),
						"experiments/reservation/get?" + reservationId);
			} catch (ActionLogException el) {
				el.printStackTrace();
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
