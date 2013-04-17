package eu.gloria.gs.services.experiment.online.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import eu.gloria.gs.services.experiment.online.data.dbservices.ArgumentEntry;
import eu.gloria.gs.services.experiment.online.data.dbservices.ContextEntry;
import eu.gloria.gs.services.experiment.online.data.dbservices.ExperimentEntry;
import eu.gloria.gs.services.experiment.online.data.dbservices.OnlineExperimentDBService;
import eu.gloria.gs.services.experiment.online.data.dbservices.OperationEntry;
import eu.gloria.gs.services.experiment.online.data.dbservices.ParameterEntry;
import eu.gloria.gs.services.experiment.online.data.dbservices.ReservationEntry;
import eu.gloria.gs.services.experiment.online.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.online.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.online.parameters.ParameterTypeNotAvailableException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.online.reservation.NoReservationsAvailableException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ExperimentDBAdapter {

	private OnlineExperimentDBService service;
	private ExperimentParameterFactory parameterFactory;
	private ExperimentOperationFactory operationFactory;

	/**
	 * 
	 */
	public ExperimentDBAdapter() {

	}

	/**
	 * @param experiment
	 * @param operationInfo
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	public void addExperimentOperation(String experiment,
			OperationInformation operationInfo)
			throws ExperimentDatabaseException, NoSuchExperimentException {

		try {

			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);

				OperationEntry entry = new OperationEntry();
				entry.setExperiment(experimentId);
				entry.setType(operationInfo.getOperationName());
				entry.setOperation(operationInfo.getModelName());

				service.saveExperimentOperation(entry);

			} else
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @param parameterInfo
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	public void addExperimentParameter(String experiment,
			ParameterInformation parameterInfo)
			throws ExperimentDatabaseException, NoSuchExperimentException {

		try {

			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);

				ParameterEntry entry = new ParameterEntry();
				entry.setExperiment(experimentId);
				entry.setParameter(parameterInfo.getModelName());

				String type = parameterInfo.getParameterName();
				type += "[";
				int i = 0;
				for (String argument : parameterInfo.getArguments()) {
					type += argument;
					if (i < parameterInfo.getArguments().length - 1) {
						type += ",";
					}
					i++;
				}

				type += "]";
				entry.setType(type);
				service.saveExperimentParameter(entry);

			} else
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @param parameter
	 * @param rid
	 * @throws NoSuchExperimentException
	 * @throws ExperimentDatabaseException
	 */
	public void addParameterContext(String experiment, String parameter, int rid)
			throws NoSuchExperimentException, ExperimentDatabaseException {

		try {

			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);

				ParameterEntry parameterEntry = service.getExperimentParameter(
						experimentId, parameter);

				if (parameterEntry != null) {
					ContextEntry entry = new ContextEntry();
					entry.setPid(parameterEntry.getIdparameter());
					entry.setRid(rid);
					entry.setValue(null);

					service.saveParameterContext(entry);

				} else
					throw new ExperimentDatabaseException("The experiment '"
							+ experiment
							+ "' does not contain a parameter named '"
							+ parameter + "'");
			} else
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");

		} catch (PersistenceException e) {
			throw new NoSuchExperimentException(e.getMessage());
		}
	}

	/**
	 * @param telescopes
	 * @param timeSlot
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean anyRTReservationBetween(List<String> telescopes,
			TimeSlot timeSlot) throws ExperimentDatabaseException {

		boolean available = true;

		try {
			for (String rt : telescopes) {

				if (service.anyRTReservationBetween(rt, timeSlot.getBegin(),
						timeSlot.getEnd())) {

					available = false;
					break;
				}
			}
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		return !available;
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean anyUserReservationActiveNow(String username)
			throws ExperimentDatabaseException {

		boolean anyActive = false;
		try {
			anyActive = service.anyUserReservationAt(username, new Date());

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		return anyActive;
	}

	/**
	 * @throws ExperimentDatabaseException
	 */
	public void clearAllObsoleteContexts() throws ExperimentDatabaseException {

		try {

			List<ReservationEntry> reservationEntries = service
					.getAllReservationsBefore(new Date());

			for (ReservationEntry entry : reservationEntries) {
				service.removeReservationContext(entry.getIdreservation());
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

	}

	/**
	 * @throws ExperimentDatabaseException
	 */
	public void clearAllObsoleteReservations()
			throws ExperimentDatabaseException {

		try {

			List<ReservationEntry> reservationEntries = service
					.getAllReservationsBefore(new Date());

			for (ReservationEntry entry : reservationEntries) {
				service.removeReservation(entry.getIdreservation());
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean containsExperiment(String experiment)
			throws ExperimentDatabaseException {

		boolean alreadyContained = false;

		try {
			alreadyContained = service.containsExperiment(experiment);
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		return alreadyContained;
	}

	/**
	 * @param experiment
	 * @param author
	 * @throws ExperimentDatabaseException
	 * @throws DuplicateExperimentException
	 */
	public void createExperiment(String experiment, String author)
			throws ExperimentDatabaseException, DuplicateExperimentException {

		boolean alreadyContained = true;

		try {

			if (!service.containsExperiment(experiment)) {
				alreadyContained = false;

				ExperimentEntry entry = new ExperimentEntry();
				entry.setAuthor(author);
				entry.setName(experiment);
				entry.setType("ONLINE");
				service.saveExperiment(entry);
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		if (alreadyContained) {
			throw new DuplicateExperimentException("The experiment '"
					+ experiment + "' already exists");
		}
	}

	/**
	 * @param experiment
	 * @throws NoSuchExperimentException
	 * @throws ExperimentDatabaseException
	 */
	public void deleteExperiment(String experiment)
			throws NoSuchExperimentException, ExperimentDatabaseException {

		try {
			if (service.containsExperiment(experiment)) {
				service.removeExperiment(experiment);
			} else {
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");
			}
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

	}

	/**
	 * @param rid
	 * @throws ExperimentDatabaseException
	 */
	public void deleteExperimentContext(int rid)
			throws ExperimentDatabaseException {

		try {

			service.removeReservationContext(rid);
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

	}

	/**
	 * @param rid
	 * @throws ExperimentDatabaseException
	 */
	public void deleteReservation(int rid) throws ExperimentDatabaseException {

		try {

			service.removeReservation(rid);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public List<String> getAllOnlineExperiments()
			throws ExperimentDatabaseException {

		List<String> experiments = null;

		try {
			experiments = service.getAllTypeExperiments("ONLINE");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		return experiments;
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getAllReservationsActiveNow()
			throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		List<ReservationInformation> reservations = null;

		try {
			List<ReservationEntry> reservationEntries = service
					.getAllReservationsAt(new Date());

			reservations = new ArrayList<ReservationInformation>();

			for (ReservationEntry entry : reservationEntries) {
				ReservationInformation reservation = new ReservationInformation();

				String experimentName = (service.getExperimentById(entry
						.getExperiment())).getName();
				reservation.setExperiment(experimentName);

				List<String> telescopes = service.getAllRTOfReservation(entry
						.getIdreservation());
				reservation.setTelescopes(telescopes);
				reservation.setUser(entry.getUser());

				TimeSlot timeSlot = new TimeSlot();
				timeSlot.setBegin(entry.getBegin());
				timeSlot.setEnd(entry.getEnd());

				reservation.setTimeSlot(timeSlot);
				reservation.setReservationId(entry.getIdreservation());

				reservations.add(reservation);
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		if (reservations == null || reservations.size() == 0) {
			throw new NoReservationsAvailableException(
					"No active reservations now");
		}
		return reservations;
	}

	/**
	 * @param experiment
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	public ExperimentInformation getExperimentInformation(String experiment)
			throws ExperimentDatabaseException, NoSuchExperimentException {

		try {

			ExperimentEntry entry = service.getExperiment(experiment);

			if (entry != null) {
				ExperimentInformation expInfo = new ExperimentInformation();
				expInfo.setName(entry.getName());
				expInfo.setAuthor(entry.getAuthor());
				expInfo.setDescription(entry.getDescription());
				expInfo.setType(ExperimentType.ONLINE);

				int experimentId = entry.getIdexperiment();

				List<OperationEntry> opEntries = service
						.getAllExperimentOperations(experimentId);

				List<OperationInformation> operations = new ArrayList<OperationInformation>();
				expInfo.setOperations(operations);

				for (OperationEntry operationEntry : opEntries) {

					OperationInformation opInfo = new OperationInformation();
					opInfo.setModelName(operationEntry.getOperation());
					opInfo.setOperation(operationFactory
							.createOperation(operationEntry.getType()));
					opInfo.setOperationName(operationEntry.getType());

					List<ArgumentEntry> argumentEntries = service
							.getOperationArguments(operationEntry
									.getIdoperation());
					List<String> arguments = new ArrayList<String>();

					if (argumentEntries != null && argumentEntries.size() > 0) {

						// Pre-reserve space for proper ordering of the
						// arguments
						for (int i = 0; i < argumentEntries.size(); i++) {
							arguments.add(null);
						}

						for (ArgumentEntry argumentEntry : argumentEntries) {

							ParameterEntry paramEntry = service
									.getParameterById(argumentEntry
											.getParameter());

							arguments.set(argumentEntry.getNumber(),
									paramEntry.getParameter());
						}
					}

					opInfo.setArguments(arguments.toArray(new String[0]));
					operations.add(opInfo);
				}

				List<ParameterEntry> paramEntries = service
						.getAllExperimentParameters(experimentId);

				List<ParameterInformation> parameters = new ArrayList<ParameterInformation>();
				expInfo.setParameters(parameters);

				for (ParameterEntry parameterEntry : paramEntries) {

					ParameterInformation paramInfo = new ParameterInformation();
					paramInfo.setModelName(parameterEntry.getParameter());

					String typePattern = parameterEntry.getType();

					String[] parts = typePattern.split("\\[");
					String parameterType = null;
					String[] arguments = null;
					if (parts.length == 2) {
						parameterType = parts[0];
						arguments = parts[1].replace("]", "").split(",");

						if (arguments.length == 1 && arguments[0].equals(""))
							arguments = null;

					} else {
						throw new ParameterTypeNotAvailableException(
								"The type pattern is incorrect: " + typePattern);
					}

					paramInfo.setParameterName(parameterType);
					paramInfo.setParameter(parameterFactory
							.createParameter(parameterType));

					paramInfo.setArguments(arguments);
					parameters.add(paramInfo);
				}

				return expInfo;
			}
		} catch (PersistenceException | ParameterTypeNotAvailableException
				| ExperimentParameterException
				| OperationTypeNotAvailableException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		throw new NoSuchExperimentException("The experiment '" + experiment
				+ "' does not exist.");
	}

	/**
	 * @param rid
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws ExperimentNotInstantiatedException
	 */
	public ExperimentRuntimeInformation getExperimentRuntimeContext(int rid)
			throws ExperimentDatabaseException,
			ExperimentNotInstantiatedException {

		try {

			if (!service.isReservationContextInstantiated(rid)) {
				throw new ExperimentNotInstantiatedException(
						"The reservation '" + rid
								+ "' is not yet instantiated or is invalid");
			}

			List<ContextEntry> context = service.getReservationContext(rid);

			ExperimentRuntimeInformation runtimeContext = new ExperimentRuntimeInformation();

			HashMap<String, Object> parameterValues = new HashMap<String, Object>();
			runtimeContext.setParameterValues(parameterValues);

			for (ContextEntry entry : context) {

				try {
					ParameterEntry parameterEntry = service
							.getParameterById(entry.getPid());

					parameterValues.put(parameterEntry.getParameter(),
							entry.getValue());

				} catch (NullPointerException e) {
					throw new ExperimentDatabaseException(
							"The parameter '"
									+ entry.getPid()
									+ "' referenced by experiment context does not exist");
				}
			}

			ReservationEntry reservationEntry = service.getReservationById(rid);
			Date beginDate = reservationEntry.getBegin();
			Date endDate = reservationEntry.getEnd();
			Date now = new Date();

			runtimeContext
					.setElapsedTime((now.getTime() - beginDate.getTime()) / 1000);
			runtimeContext
					.setRemainingTime((endDate.getTime() - now.getTime()) / 1000);

			return runtimeContext;

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @param parameter
	 * @param rid
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 * @throws ExperimentNotInstantiatedException
	 */
	public Object getParameterContextValue(String experiment, String parameter,
			int rid) throws ExperimentDatabaseException,
			NoSuchExperimentException, ExperimentNotInstantiatedException {

		try {

			if (!service.containsExperiment(experiment)) {
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");
			}

			if (!service.isReservationContextInstantiated(rid)) {
				throw new ExperimentNotInstantiatedException(
						"The reservation '" + rid + "' is not yet instantiated");
			}

			int experimentId = service.getExperimentId(experiment);

			ParameterEntry parameterEntry = service.getExperimentParameter(
					experimentId, parameter);

			if (parameterEntry != null) {

				return service.getParameterContextValue(
						parameterEntry.getIdparameter(), rid);
			} else {
				throw new ExperimentDatabaseException("The experiment '"
						+ experiment + "' does not contain a parameter named '"
						+ parameter + "'");
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param rid
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchReservationException
	 */
	public ReservationInformation getReservationInformation(int rid)
			throws ExperimentDatabaseException, NoSuchReservationException {

		ReservationInformation reservation = null;

		try {
			ReservationEntry reservationEntry = service.getReservationById(rid);

			if (reservationEntry != null) {

				reservation = new ReservationInformation();

				String experimentName = (service
						.getExperimentById(reservationEntry.getExperiment()))
						.getName();
				reservation.setExperiment(experimentName);

				List<String> telescopes = service
						.getAllRTOfReservation(reservationEntry
								.getIdreservation());
				reservation.setTelescopes(telescopes);
				reservation.setUser(reservationEntry.getUser());

				TimeSlot timeSlot = new TimeSlot();
				timeSlot.setBegin(reservationEntry.getBegin());
				timeSlot.setEnd(reservationEntry.getEnd());

				reservation.setTimeSlot(timeSlot);
				reservation.setReservationId(reservationEntry
						.getIdreservation());
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		if (reservation == null)
			throw new NoSuchReservationException(
					"No reservation registered for id '" + rid + "'");

		return reservation;
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getUserPendingReservations(
			String username) throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		List<ReservationInformation> reservationInfos = null;
		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsFrom(username, new Date());

			if (reservationEntries != null) {
				reservationInfos = new ArrayList<ReservationInformation>();

				for (ReservationEntry reservationEntry : reservationEntries) {
					ReservationInformation resInfo = new ReservationInformation();

					ExperimentEntry experimentEntry = service
							.getExperimentById(reservationEntry.getExperiment());

					resInfo.setExperiment(experimentEntry.getName());
					TimeSlot timeSlot = new TimeSlot();
					timeSlot.setBegin(reservationEntry.getBegin());
					timeSlot.setEnd(reservationEntry.getEnd());
					resInfo.setTimeSlot(timeSlot);
					resInfo.setUser(reservationEntry.getUser());

					List<String> telescopes = service
							.getAllRTOfReservation(reservationEntry
									.getIdreservation());
					resInfo.setTelescopes(telescopes);
					resInfo.setReservationId(reservationEntry
							.getIdreservation());

					reservationInfos.add(resInfo);
				}
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		if (reservationInfos == null || reservationInfos.size() == 0) {

			throw new NoReservationsAvailableException("The user '" + username
					+ "' has no pending reservations");
		}

		return reservationInfos;
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getUserReservationActiveNow(
			String username) throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		List<ReservationInformation> reservations = null;

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationAt(username, new Date());

			reservations = new ArrayList<ReservationInformation>();

			for (ReservationEntry entry : reservationEntries) {
				ReservationInformation reservation = new ReservationInformation();

				String experimentName = (service.getExperimentById(entry
						.getExperiment())).getName();
				reservation.setExperiment(experimentName);

				List<String> telescopes = service.getAllRTOfReservation(entry
						.getIdreservation());
				reservation.setTelescopes(telescopes);
				reservation.setUser(entry.getUser());

				TimeSlot timeSlot = new TimeSlot();
				timeSlot.setBegin(entry.getBegin());
				timeSlot.setEnd(entry.getEnd());

				reservation.setTimeSlot(timeSlot);
				reservation.setReservationId(entry.getIdreservation());

				reservations.add(reservation);
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		if (reservations == null || reservations.size() == 0) {
			throw new NoReservationsAvailableException(
					"No active reservations now for the " + "user '" + username
							+ "'");
		}
		return reservations;
	}

	/**
	 * @param rid
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	/**
	 * @param rid
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean isReservationContextInstantiated(int rid)
			throws ExperimentDatabaseException {

		try {

			ReservationEntry reservationEntry = service.getReservationById(rid);

			if (reservationEntry == null) {
				throw new ExperimentDatabaseException("The reservation '" + rid
						+ "' does not exists");
			}

			List<ParameterEntry> parameters = service
					.getAllExperimentParameters(reservationEntry
							.getExperiment());

			List<ContextEntry> context = service.getReservationContext(rid);

			return context != null && parameters != null
					&& context.size() == parameters.size();

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @param telescopes
	 * @param username
	 * @param timeSlot
	 * @throws ExperimentDatabaseException
	 */
	/**
	 * @param experiment
	 * @param telescopes
	 * @param username
	 * @param timeSlot
	 * @throws ExperimentDatabaseException
	 */
	public void makeReservation(String experiment, List<String> telescopes,
			String username, TimeSlot timeSlot)
			throws ExperimentDatabaseException {

		try {
			int experimentId = service.getExperimentId(experiment);

			ReservationEntry reservationEntry = new ReservationEntry();

			reservationEntry.setExperiment(experimentId);
			reservationEntry.setUser(username);
			reservationEntry.setBegin(timeSlot.getBegin());
			reservationEntry.setEnd(timeSlot.getEnd());

			service.saveReservation(reservationEntry);

			reservationEntry = service.getReservation(username, experimentId,
					timeSlot.getBegin(), timeSlot.getEnd());

			for (String rt : telescopes) {

				service.saveRTReservation(reservationEntry.getIdreservation(),
						rt);
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @param description
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	/**
	 * @param experiment
	 * @param description
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	public void setExperimentDescription(String experiment, String description)
			throws ExperimentDatabaseException, NoSuchExperimentException {

		boolean alreadyContained = false;

		try {
			if (service.containsExperiment(experiment)) {

				alreadyContained = true;
				service.setExperimentDescription(experiment, description);

			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		if (!alreadyContained) {
			throw new NoSuchExperimentException("The experiment '" + experiment
					+ "' does not exist.");
		}
	}

	/**
	 * @param service
	 * @throws ExperimentDatabaseException
	 */
	/**
	 * @param service
	 * @throws ExperimentDatabaseException
	 */
	public void setOnlineExperimentDBService(OnlineExperimentDBService service)
			throws ExperimentDatabaseException {
		this.service = service;

		try {
			service.createExperimentTable();
			service.createReservationTable();
			service.createRTReservationTable();
			service.createExperimentOperationTable();
			service.createExperimentParameterTable();
			service.createExperimentArgumentTable();
			service.createExperimentContextTable();
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param experiment
	 * @param operation
	 * @param arguments
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	/**
	 * @param experiment
	 * @param operation
	 * @param arguments
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	public void setOperationArguments(String experiment, String operation,
			String[] arguments) throws ExperimentDatabaseException,
			NoSuchExperimentException {

		try {

			if (service.containsExperiment(experiment)) {
				int experimentId = service.getExperimentId(experiment);

				int order = 0;

				OperationEntry operationEntry = service.getExperimentOperation(
						experimentId, operation);

				if (operationEntry != null) {

					for (String argument : arguments) {

						ParameterEntry parameterEntry = service
								.getExperimentParameter(experimentId, argument);

						if (parameterEntry == null) {
							throw new ExperimentDatabaseException(
									"The experiment '"
											+ experiment
											+ "' does not contain a parameter named '"
											+ argument + "'");
						}

						ArgumentEntry entry = new ArgumentEntry();
						entry.setOperation(operationEntry.getIdoperation());
						entry.setParameter(parameterEntry.getIdparameter());
						entry.setNumber(order);

						service.saveOperationArgument(entry);

						order++;
					}

				} else {
					throw new ExperimentDatabaseException("The experiment '"
							+ experiment
							+ "' does not contain an operation named '"
							+ operation + "'");
				}
			} else {
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");
			}
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

	}

	/**
	 * @param factory
	 */
	public void setOperationFactory(ExperimentOperationFactory factory) {
		this.operationFactory = factory;
	}

	/**
	 * @param experiment
	 * @param parameter
	 * @param rid
	 * @param value
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 * @throws ExperimentNotInstantiatedException
	 */
	/**
	 * @param experiment
	 * @param parameter
	 * @param rid
	 * @param value
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 * @throws ExperimentNotInstantiatedException
	 */
	public void setParameterContextValue(String experiment, String parameter,
			int rid, Object value) throws ExperimentDatabaseException,
			NoSuchExperimentException, ExperimentNotInstantiatedException {

		try {

			if (!service.containsExperiment(experiment)) {
				throw new NoSuchExperimentException("The experiment '"
						+ experiment + "' does not exist");
			}

			if (!service.isReservationContextInstantiated(rid)) {
				throw new ExperimentNotInstantiatedException(
						"The reservation '" + rid + "' is not yet instantiated");
			}

			int experimentId = service.getExperimentId(experiment);

			ParameterEntry parameterEntry = service.getExperimentParameter(
					experimentId, parameter);

			if (parameterEntry != null) {

				service.setParameterContextValue(
						parameterEntry.getIdparameter(), rid, value);

			} else {
				throw new ExperimentDatabaseException("The experiment '"
						+ experiment + "' does not contain a parameter named '"
						+ parameter + "'");
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}
	}

	/**
	 * @param factory
	 */
	public void setParameterFactory(ExperimentParameterFactory factory) {
		this.parameterFactory = factory;
	}
}
