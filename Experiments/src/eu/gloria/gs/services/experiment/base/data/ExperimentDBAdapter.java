package eu.gloria.gs.services.experiment.base.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentType;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.data.dbservices.ArgumentEntry;
import eu.gloria.gs.services.experiment.base.data.dbservices.ContextEntry;
import eu.gloria.gs.services.experiment.base.data.dbservices.ExperimentDBService;
import eu.gloria.gs.services.experiment.base.data.dbservices.ExperimentEntry;
import eu.gloria.gs.services.experiment.base.data.dbservices.OperationEntry;
import eu.gloria.gs.services.experiment.base.data.dbservices.ParameterEntry;
import eu.gloria.gs.services.experiment.base.data.dbservices.ReservationEntry;
import eu.gloria.gs.services.experiment.base.data.dbservices.ResultEntry;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ExperimentDBAdapter {

	private ExperimentDBService service;
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
				entry.setType(operationInfo.getType());
				entry.setOperation(operationInfo.getName());

				service.saveExperimentOperation(entry);

			} else
				throw new NoSuchExperimentException(experiment);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
				entry.setParameter(parameterInfo.getName());

				String type = parameterInfo.getType();

				type += JSONConverter.toJSON(parameterInfo.getArguments());

				entry.setType(type);
				service.saveExperimentParameter(entry);

			} else
				throw new NoSuchExperimentException(experiment);

		} catch (PersistenceException | IOException e) {
			throw new ExperimentDatabaseException();
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

				} else {
					ExperimentDatabaseException ex = new ExperimentDatabaseException(
							"parameter does not exist");
					ex.getAction().put("name", parameter);
					ex.getAction().put("rid", rid);

					throw ex;
				}
			} else
				throw new NoSuchExperimentException(experiment);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
			throw new ExperimentDatabaseException();
		}

		return !available;
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean isReservationActiveNow(int rid)
			throws ExperimentDatabaseException {

		boolean anyActive = false;
		try {
			anyActive = service.isReservationActiveNow(rid, new Date());

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		return anyActive;
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean isReservationIsActiveNowForUser(int rid, String user)
			throws ExperimentDatabaseException {

		boolean anyActive = false;
		try {
			anyActive = service.isReservationActiveNowForUser(rid, user,
					new Date());

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		return anyActive;
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean anyReservationActiveNow(String type)
			throws ExperimentDatabaseException {

		boolean anyActive = false;
		try {
			anyActive = service.anyReservationAtByType(type, new Date());

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		return anyActive;
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean anyReservationActiveNow() throws ExperimentDatabaseException {

		boolean anyActive = false;
		try {
			anyActive = service.anyReservationAt(new Date());

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		return anyActive;
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public boolean anyUserReservationActiveNow(String type, String username)
			throws ExperimentDatabaseException {

		boolean anyActive = false;
		try {
			anyActive = service.anyUserReservationAtByType(type, username,
					new Date());

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		return anyActive;
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
			throw new ExperimentDatabaseException();
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
				service.setReservationStatus(entry.getIdreservation(),
						"OBSOLETE");
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
			throw new ExperimentDatabaseException();
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
			throw new ExperimentDatabaseException();
		}

		return alreadyContained;
	}

	/**
	 * @param experiment
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public String getExperimentType(String experiment)
			throws ExperimentDatabaseException {

		try {
			ExperimentEntry expEntry = service.getExperiment(experiment);
			return expEntry.getType();
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param experiment
	 * @param author
	 * @throws ExperimentDatabaseException
	 * @throws DuplicateExperimentException
	 */
	public void createExperiment(String experiment, String author, String type)
			throws ExperimentDatabaseException, DuplicateExperimentException {

		boolean alreadyContained = true;

		try {

			if (!service.containsExperiment(experiment)) {
				alreadyContained = false;

				ExperimentEntry entry = new ExperimentEntry();
				entry.setAuthor(author);
				entry.setName(experiment);
				entry.setType(type);
				service.saveExperiment(entry);
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		if (alreadyContained) {
			throw new DuplicateExperimentException(experiment);
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
				throw new NoSuchExperimentException(experiment);
			}
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
			throw new ExperimentDatabaseException();
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
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 */
	public List<String> getAllExperiments(String type)
			throws ExperimentDatabaseException {

		List<String> experiments = null;

		try {
			experiments = service.getAllTypeExperiments(type);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		return experiments;
	}

	/**
	 * @param experiment
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoSuchExperimentException
	 */
	public ExperimentInformation getBasicExperimentInformation(String experiment)
			throws ExperimentDatabaseException, NoSuchExperimentException {

		try {

			ExperimentEntry entry = service.getExperiment(experiment);

			if (entry != null) {
				ExperimentInformation expInfo = new ExperimentInformation();
				expInfo.setName(entry.getName());
				expInfo.setAuthor(entry.getAuthor());
				expInfo.setDescription(entry.getDescription());
				expInfo.setType(ExperimentType.ONLINE);

				return expInfo;
			}
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		throw new NoSuchExperimentException(experiment);
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
					opInfo.setName(operationEntry.getOperation());
					opInfo.setOperation(operationFactory
							.createOperation(operationEntry.getType()));
					opInfo.setType(operationEntry.getType());

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

							ParameterEntry paramEntry = null;

							if (argumentEntry.getParameter() != null) {
								paramEntry = service
										.getParameterById(argumentEntry
												.getParameter());
							}

							OperationEntry opEntry = null;

							if (argumentEntry.getPointer() != null) {
								opEntry = service
										.getOperationById(argumentEntry
												.getPointer());
							}

							String actualArgument = null;

							if (paramEntry != null) {
								actualArgument = paramEntry.getParameter();
							} else if (opEntry != null) {
								actualArgument = opEntry.getOperation();
							}
							String subArg = argumentEntry.getSubarg();
							if (subArg != null && !subArg.equals("")) {
								actualArgument = actualArgument + "." + subArg;
							}

							arguments.set(argumentEntry.getNumber(),
									actualArgument);
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
					paramInfo.setName(parameterEntry.getParameter());

					String typePattern = parameterEntry.getType();
					String parameterType = null;
					String argStr = null;
					Object[] objectArgs = null;
					String[] arguments = null;

					int headerEndIndex = typePattern.indexOf("[");
					if (headerEndIndex >= 0) {
						parameterType = typePattern.substring(0, Math.min(
								headerEndIndex, typePattern.length() - 1));
						argStr = typePattern.substring(headerEndIndex);
						objectArgs = (Object[]) JSONConverter.fromJSON(argStr,
								Object[].class, null);

						arguments = new String[objectArgs.length];
						int i = 0;
						for (Object arg : objectArgs) {
							if (arg instanceof String) {
								arguments[i] = (String) arg;
							} else {
								arguments[i] = JSONConverter.toJSON(arg);
							}
							i++;
						}

					} else {
						throw new ParameterTypeNotAvailableException(
								experiment, parameterEntry.getParameter(),
								typePattern);
					}

					paramInfo.setType(parameterType);
					paramInfo.setParameter(parameterFactory
							.createParameter(parameterType));

					paramInfo.setArguments((String[]) arguments);
					parameters.add(paramInfo);
				}

				return expInfo;
			}
		} catch (PersistenceException | IOException
				| ParameterTypeNotAvailableException
				| ExperimentParameterException
				| OperationTypeNotAvailableException e) {
			throw new ExperimentDatabaseException();
		}

		throw new NoSuchExperimentException(experiment);
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
				throw new ExperimentNotInstantiatedException(rid);
			}

			ExperimentRuntimeInformation runtimeContext = new ExperimentRuntimeInformation();

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
			throw new ExperimentDatabaseException();
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
	public Object getParameterContextValue(String parameter, int rid)
			throws ExperimentDatabaseException, NoSuchParameterException,
			ExperimentNotInstantiatedException {

		try {

			if (!service.isReservationContextInstantiated(rid)) {
				throw new ExperimentNotInstantiatedException(rid);
			}

			ReservationEntry resEntry = service.getReservationById(rid);

			int experimentId = resEntry.getExperiment();

			ParameterEntry parameterEntry = service.getExperimentParameter(
					experimentId, parameter);

			if (parameterEntry != null) {
				return service.getParameterContextValue(
						parameterEntry.getIdparameter(), rid);

			} else {
				throw new ExperimentDatabaseException("parameter problem");
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
				reservation.setStatus(reservationEntry.getStatus());

				TimeSlot timeSlot = new TimeSlot();
				timeSlot.setBegin(reservationEntry.getBegin());
				timeSlot.setEnd(reservationEntry.getEnd());

				reservation.setTimeSlot(timeSlot);
				reservation.setReservationId(reservationEntry
						.getIdreservation());
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}

		if (reservation == null)
			throw new NoSuchReservationException(rid);

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

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsFrom(username, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getUserPendingReservations(String type,
			String username) throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsFromByType(type, username, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getAllPendingReservations(String type)
			throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getAllReservationsFromByType(type, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getAllPendingReservations()
			throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getAllReservationsFrom(new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	private List<ReservationInformation> processReservationEntries(
			List<ReservationEntry> entries) {

		List<ReservationInformation> reservations = null;

		reservations = new ArrayList<ReservationInformation>();

		for (ReservationEntry entry : entries) {
			ReservationInformation reservation = new ReservationInformation();

			String experimentName = (service.getExperimentById(entry
					.getExperiment())).getName();
			reservation.setExperiment(experimentName);

			List<String> telescopes = service.getAllRTOfReservation(entry
					.getIdreservation());
			reservation.setTelescopes(telescopes);
			reservation.setUser(entry.getUser());
			reservation.setStatus(entry.getStatus());

			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setBegin(entry.getBegin());
			timeSlot.setEnd(entry.getEnd());

			reservation.setTimeSlot(timeSlot);
			reservation.setReservationId(entry.getIdreservation());

			reservations.add(reservation);
		}

		return reservations;
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getAllReservationsActiveNow()
			throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getAllReservationsAt(new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("active");
			}

			return this.processReservationEntries(reservationEntries);
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getAllReservationsActiveNow(String type)
			throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getAllReservationsAtByType(type, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("active");
			}

			return this.processReservationEntries(reservationEntries);
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getUserReservationsActiveNow(
			String username) throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsAt(username, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("active");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param username
	 * @return
	 * @throws ExperimentDatabaseException
	 * @throws NoReservationsAvailableException
	 */
	public List<ReservationInformation> getUserReservationsActiveNow(
			String type, String username) throws ExperimentDatabaseException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsAtByType(type, username, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("active");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
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
	 * @throws NoSuchReservationException
	 */
	public boolean isReservationContextReady(int rid)
			throws ExperimentDatabaseException, NoSuchReservationException {

		try {

			ReservationEntry reservationEntry = service.getReservationById(rid);

			if (reservationEntry == null) {
				throw new NoSuchReservationException(rid);
			}
			String status = reservationEntry.getStatus();

			if (status == null || !status.equals("READY")) {
				return false;
			}

			List<ParameterEntry> parameters = service
					.getAllExperimentParameters(reservationEntry
							.getExperiment());

			Integer paramsInContext = service.getParametersNumberInContext(rid);

			return paramsInContext != null && parameters != null
					&& paramsInContext == parameters.size();

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

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
			reservationEntry.setStatus("SCHEDULED");

			service.saveReservation(reservationEntry);

			reservationEntry = service.getReservation(username, experimentId,
					timeSlot.getBegin(), timeSlot.getEnd());

			if (telescopes != null) {

				for (String rt : telescopes) {

					service.saveRTReservation(
							reservationEntry.getIdreservation(), rt);
				}
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param rid
	 * @throws ExperimentDatabaseException
	 */
	public void setContextReady(int rid) throws ExperimentDatabaseException {

		try {

			service.setReservationStatus(rid, "READY");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param rid
	 * @throws ExperimentDatabaseException
	 */
	public void setContextInit(int rid) throws ExperimentDatabaseException {

		try {

			service.setReservationStatus(rid, "INIT");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param rid
	 * @throws ExperimentDatabaseException
	 */
	public void setContextError(int rid) throws ExperimentDatabaseException {

		try {

			service.setReservationStatus(rid, "ERROR");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param rid
	 * @throws ExperimentDatabaseException
	 */
	public void setContextObsolete(int rid) throws ExperimentDatabaseException {

		try {

			service.setReservationStatus(rid, "OBSOLETE");

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

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
			throw new ExperimentDatabaseException();
		}

		if (!alreadyContained) {
			throw new NoSuchExperimentException(experiment);
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
	public void setExperimentDBService(ExperimentDBService service)
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
			service.createExperimentResultsTable();
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
	 * @throws NoSuchParameterException
	 */
	public void setOperationArguments(String experiment, String operation,
			String[] arguments) throws ExperimentDatabaseException,
			NoSuchExperimentException, NoSuchParameterException {

		try {

			if (service.containsExperiment(experiment)) {
				int experimentId = service.getExperimentId(experiment);

				int order = 0;

				OperationEntry operationEntry = service.getExperimentOperation(
						experimentId, operation);

				if (operationEntry != null) {

					for (String argument : arguments) {

						String actualArgument = argument;

						if (argument.contains(".")) {
							actualArgument = argument.substring(0,
									argument.indexOf("."));
						}

						ParameterEntry parameterEntry = service
								.getExperimentParameter(experimentId,
										actualArgument);

						OperationEntry argOpEntry = service
								.getExperimentOperation(experimentId,
										actualArgument);

						if (parameterEntry == null && argOpEntry == null) {

							throw new NoSuchParameterException(actualArgument);
						}

						ArgumentEntry entry = new ArgumentEntry();
						entry.setOperation(operationEntry.getIdoperation());
						if (parameterEntry != null) {
							entry.setParameter(parameterEntry.getIdparameter());
						} else {
							entry.setParameter(null);
						}

						if (argOpEntry != null) {
							entry.setPointer(argOpEntry.getIdoperation());
						} else {
							entry.setPointer(null);
						}

						if (argument.contains(".")) {
							entry.setSubarg(argument.substring(
									argument.indexOf(".") + 1,
									argument.length()));
						}
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
				throw new NoSuchExperimentException(experiment);
			}
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
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
	 * @throws NoSuchParameterException
	 */
	public void setParameterContextValue(String parameter, int rid, Object value)
			throws ExperimentDatabaseException,
			ExperimentNotInstantiatedException, NoSuchParameterException {

		try {

			/*
			 * if (!service.containsExperiment(experiment)) { throw new
			 * NoSuchExperimentException("The experiment '" + experiment +
			 * "' does not exist"); }
			 */

			if (!service.isReservationContextInstantiated(rid)) {
				throw new ExperimentNotInstantiatedException(rid);
			}

			ReservationEntry resEntry = service.getReservationById(rid);

			int experimentId = resEntry.getExperiment();

			ParameterEntry parameterEntry = service.getExperimentParameter(
					experimentId, parameter);

			if (parameterEntry != null) {

				service.setParameterContextValue(
						parameterEntry.getIdparameter(), rid, value);

			} else {
				throw new NoSuchParameterException(parameter);
			}

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	/**
	 * @param factory
	 */
	public void setParameterFactory(ExperimentParameterFactory factory) {
		this.parameterFactory = factory;
	}

	public void saveResult(int reservationId, String tag, String user,
			Object value) throws ExperimentDatabaseException {

		try {

			int experimentId = service.getReservationById(reservationId)
					.getExperiment();
			int tagId = service.getExperimentParameter(experimentId, tag)
					.getIdparameter();

			ResultEntry entry = new ResultEntry();
			entry.setDate(new Date());
			entry.setValue(value);
			entry.setUser(user);
			entry.setTag(tagId);
			entry.setContext(reservationId);
			service.saveResult(entry);

		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	public List<ResultInformation> getExperimentResults(String experiment)
			throws ExperimentDatabaseException {

		try {

			List<ResultInformation> results = new ArrayList<>();

			List<Integer> contexts = service
					.getAllExperimentContexts(experiment);

			for (Integer context : contexts) {
				List<ResultEntry> contextResults = service
						.getContextResults(context);

				for (ResultEntry res : contextResults) {
					ResultInformation resInfo = new ResultInformation();
					resInfo.setDate(res.getDate());
					resInfo.setExperiment(experiment);
					resInfo.setReservationId(context);
					resInfo.setUser(res.getUser());
					resInfo.setValue(res.getValue());

					results.add(resInfo);
				}
			}

			return results;
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}

	public List<ResultInformation> getContextResults(int context)
			throws ExperimentDatabaseException, NoSuchReservationException {

		try {

			List<ResultInformation> results = new ArrayList<>();

			List<ResultEntry> contextResults = service
					.getContextResults(context);

			ReservationEntry resEntry = service.getReservationById(context);

			if (resEntry == null) {
				throw new NoSuchReservationException(context);
			}

			int expId = resEntry.getExperiment();
			String experiment = service.getExperimentById(expId).getName();

			for (ResultEntry res : contextResults) {
				ResultInformation resInfo = new ResultInformation();
				resInfo.setDate(res.getDate());
				resInfo.setExperiment(experiment);
				resInfo.setReservationId(context);
				resInfo.setUser(res.getUser());
				resInfo.setValue(res.getValue());

				results.add(resInfo);
			}

			return results;
		} catch (PersistenceException e) {
			throw new ExperimentDatabaseException();
		}
	}
}
