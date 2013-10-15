package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.Arrays;
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
import eu.gloria.gs.services.experiment.base.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ObjectResponse;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.base.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.reservation.ExperimentBooker;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.repository.user.UserRepositoryException;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRole;

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

		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"OFFLINE");
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/offline/new?name=" + experiment
								+ "->ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			throw new ExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/offline/new?name=" + experiment);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ExperimentInformation getExperimentInformation(String experiment)
			throws ExperimentException, NoSuchExperimentException {

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getExperimentInformation(experiment);
			return expInfo;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		} catch (NoSuchExperimentException e) {
			throw e;
		}
	}

	@Override
	public ParameterInformation getParameterInformation(String experiment,
			String parameter) throws ExperimentException,
			NoSuchExperimentException {

		ParameterInformation paramInfo;
		try {
			paramInfo = adapter.getExperimentInformation(experiment)
					.getParameter(parameter);
			return paramInfo;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		} catch (NoSuchExperimentException e) {
			throw e;
		}
	}

	@Override
	public void setExperimentDescription(String experiment, String description)
			throws ExperimentException, NoSuchExperimentException {

		try {
			adapter.setExperimentDescription(experiment, description);
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
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
	public List<String> getAllOnlineExperiments() throws ExperimentException {

		List<String> experiments = null;
		try {
			experiments = adapter.getAllExperiments("ONLINE");

			if (experiments == null || experiments.size() == 0) {

				try {
					this.logAction(this.getClientUsername(),
							"experiments/online/list->ERROR");
				} catch (ActionLogException e) {
					e.printStackTrace();
				}

				throw new ExperimentException(
						"There is no online experiments registered");
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	@Override
	public List<String> getAllOfflineExperiments() throws ExperimentException {

		List<String> experiments = null;
		try {
			experiments = adapter.getAllExperiments("OFFLINE");

			if (experiments == null || experiments.size() == 0) {

				try {
					this.logAction(this.getClientUsername(),
							"experiments/online/list->ERROR");
				} catch (ActionLogException e) {
					e.printStackTrace();
				}

				throw new ExperimentException(
						"There is no online experiments registered");
			}

			return experiments;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	@Override
	public boolean containsExperiment(String experiment)
			throws ExperimentException {

		try {

			boolean contains;
			contains = adapter.containsExperiment(experiment);

			return contains;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	@Override
	public List<ReservationInformation> getMyPendingReservations()
			throws ExperimentException, NoReservationsAvailableException {

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

			throw new ExperimentException(e.getMessage());
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
	public boolean anyReservationActiveNow() throws ExperimentException {

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

			throw new ExperimentException(e.getMessage());
		}
	}

	@Override
	public List<ReservationInformation> getMyCurrentReservations()
			throws ExperimentException, NoReservationsAvailableException {

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
			throw new ExperimentException(e.getMessage());
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
	public void applyForExperiment(String experiment)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException {

		GSClientProvider.setCredentials(this.getUsername(), this.getPassword());

		/*
		 * boolean adminMode = false;
		 * 
		 * UserInformation userInfo = null; try { userInfo =
		 * this.userRepository.getUserInformation(this .getClientUsername()); if
		 * (userInfo.getRoles()[0].equals(UserRole.ADMIN)) { adminMode = true; }
		 * } catch (UserRepositoryException e1) { }
		 */

		try {
			experimentBooker.applyFor(experiment, this.getClientUsername());
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/applications/make?" + experiment
								+ "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new ExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/applications/make?" + experiment);
		} catch (ActionLogException el) {
			el.printStackTrace();
		}
	}

	@Override
	public void reserveExperiment(String experiment, List<String> telescopes,
			TimeSlot timeSlot) throws ExperimentException,
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
			throw new ExperimentException(e.getMessage());
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
			List<String> telescopes) throws ExperimentException,
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
				throw new ExperimentException(
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
			throw new ExperimentException(e.getMessage());
		}

	}

	@Override
	public void cancelExperimentReservation(int reservationId)
			throws ExperimentException, NoSuchReservationException {

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

			throw new ExperimentException(e.getMessage());
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
			ExperimentException, NoSuchReservationException,
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
			throw new ExperimentException(e.getMessage());
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
			int reservationId) throws ExperimentException,
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
			throw new ExperimentException(e.getMessage());
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
			throw new ExperimentException(e.getMessage());
		}

		ExperimentRuntimeInformation context;
		try {
			context = adapter.getExperimentRuntimeContext(reservationId);

			return context;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	@Override
	public void addExperimentOperation(String experiment,
			OperationInformation operation) throws ExperimentException,
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
									+ operation.getName() + "->OWN_ERROR");
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
								+ operation.getName() + "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new ExperimentException(e.getMessage());
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildOperation(operation);
		} catch (InvalidExperimentModelException
				| OperationTypeNotAvailableException
				| CustomExperimentException | ExperimentOperationException
				| ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/" + experiment + "/model/operations/add?"
							+ operation.getName());
		} catch (ActionLogException el) {
			el.printStackTrace();
		}
	}

	@Override
	public void addExperimentParameter(String experiment,
			ParameterInformation parameter) throws ExperimentException,
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
									+ parameter.getName() + "->OWN_ERROR");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new ExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/" + experiment + "/model/parameters/add?"
								+ parameter.getName() + "->DB_ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new ExperimentException(e.getMessage());
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);

			Object[] argsArray = (Object[]) JSONConverter.fromJSON(
					Arrays.toString(parameter.getArguments()), Object[].class,
					null);
			parameter.setArguments(argsArray);
			model.buildParameter(parameter);
		} catch (CustomExperimentException | ParameterTypeNotAvailableException
				| InvalidExperimentModelException | ExperimentDatabaseException
				| ExperimentParameterException | IOException e) {
			throw new ExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/" + experiment + "/model/parameters/add?"
							+ parameter.getName());
		} catch (ActionLogException el) {
			el.printStackTrace();
		}
	}

	@Override
	public void setExperimentParameterValue(int reservationId,
			String parameter, ObjectResponse input) throws ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException {

		String contentStr = String.valueOf(input.content);

		try {
			if (!adapter.anyUserReservationActiveNow(this.getClientUsername())) {
				try {
					this.logAction(
							this.getClientUsername(),
							"experiments/contexts/"
									+ reservationId
									+ "/parameters/set?"
									+ parameter
									+ "&"
									+ contentStr.substring(0,
											Math.min(contentStr.length(), 15))
									+ "->INACTIVE");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new NoSuchReservationException(
						"You have no reservations at this moment");
			}

			if (!adapter.isReservationContextInstantiated(reservationId)) {
				try {
					this.logAction(
							this.getClientUsername(),
							"experiments/contexts/"
									+ reservationId
									+ "/parameters/set?"
									+ parameter
									+ "&"
									+ contentStr.substring(0,
											Math.min(contentStr.length(), 15))
									+ "->NOT_INSTANTIATED");
				} catch (ActionLogException el) {
					el.printStackTrace();
				}
				throw new ExperimentNotInstantiatedException(
						"The experiment is not instantiated");
			}

			ExperimentContext context = contextManager.getContext(
					this.getClientUsername(), reservationId);

			context.setParameterValue(parameter, JSONConverter.fromJSON(
					(String) input.content, Object.class, null));

			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/contexts/"
								+ reservationId
								+ "/parameters/set?"
								+ parameter
								+ "&"
								+ contentStr.substring(0,
										Math.min(contentStr.length(), 15)));
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException | ExperimentParameterException
				| UndefinedExperimentParameterException
				| NoSuchExperimentException | IOException e) {
			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/contexts/"
								+ reservationId
								+ "/parameters/set?"
								+ parameter
								+ "&"
								+ contentStr.substring(0,
										Math.min(contentStr.length(), 15))
								+ "->" + e.getClass().getSimpleName());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new ExperimentException(e.getMessage());
		}
	}

	@Override
	public ObjectResponse getExperimentParameterValue(int reservationId,
			String parameter) throws ExperimentException,
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
			String valueStr = String.valueOf(value);

			try {
				this.logAction(
						this.getClientUsername(),
						"experiments/contexts/"
								+ reservationId
								+ "/parameters/get?"
								+ parameter
								+ "->"
								+ valueStr.substring(0,
										Math.min(valueStr.length(), 15)));
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			return new ObjectResponse(JSONConverter.toJSON(value));

		} catch (ExperimentDatabaseException | InvalidExperimentModelException
				| InvalidUserContextException | ExperimentParameterException
				| NoSuchExperimentException | IOException e) {
			try {
				this.logAction(this.getClientUsername(),
						"experiments/contexts/" + reservationId
								+ "/parameters/get?" + parameter + "->"
								+ e.getClass().getSimpleName());
			} catch (ActionLogException el) {
				el.printStackTrace();
			}
			throw new ExperimentException(e.getMessage());
		}
	}

	@Override
	public void removeExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException {
		try {
			modelManager.removeModel(experiment);
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	@Override
	public ReservationInformation getReservationInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException {

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
			throw new ExperimentException(e.getMessage());
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
			throws ExperimentException, NoSuchExperimentException {

		ExperimentInformation expInfo;
		try {
			expInfo = adapter.getExperimentInformation(experiment);

			if (!expInfo.getAuthor().equals(this.getClientUsername())) {
				throw new NoSuchExperimentException(
						"You cannot edit an experiment that is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}

		CustomExperimentModel model;
		try {
			model = modelManager.getModel(experiment);
			model.buildFeature(feature);
		} catch (InvalidExperimentModelException | CustomExperimentException
				| ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
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
		} catch (InvalidExperimentModelException | CustomExperimentException
				| ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
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
		} catch (InvalidExperimentModelException | CustomExperimentException
				| ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
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
		try {
			modelManager.createModel(experiment, this.getClientUsername(),
					"ONLINE");
		} catch (ExperimentDatabaseException e) {

			try {
				this.logAction(this.getClientUsername(),
						"experiments/online/new?name=" + experiment + "->ERROR");
			} catch (ActionLogException el) {
				el.printStackTrace();
			}

			throw new ExperimentException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(),
					"experiments/online/new?name=" + experiment);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
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

		try {
			List<ResultInformation> results = adapter
					.getContextResults(reservationId);

			return results;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
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
		try {
			List<ResultInformation> results = adapter
					.getExperimentResults(experiment);

			return results;
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentException(e.getMessage());
		}
	}
}
