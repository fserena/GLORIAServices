package eu.gloria.gs.services.experiment.base.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.experiment.ExperimentException;
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
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.gs.services.utils.LoggerEntity;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ExperimentDBAdapter extends LoggerEntity {

	private ExperimentDBService service;
	private ExperimentParameterFactory parameterFactory;
	private ExperimentOperationFactory operationFactory;

	/**
	 * 
	 */
	public ExperimentDBAdapter() {
		super(ExperimentDBAdapter.class.getSimpleName());
	}

	public void addExperimentOperation(String experiment,
			OperationInformation operationInfo) throws ExperimentException,
			NoSuchExperimentException {

		try {
			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);

				OperationEntry entry = new OperationEntry();
				entry.setExperiment(experimentId);
				entry.setType(operationInfo.getType());
				entry.setOperation(operationInfo.getName());

				service.saveExperimentOperation(entry);
			} else {
				throw new NoSuchExperimentException(experiment);
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public void addExperimentParameter(String experiment,
			ParameterInformation parameterInfo) throws ExperimentException,
			NoSuchExperimentException {

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
			} else {
				throw new NoSuchExperimentException(experiment);
			}

		} catch (PersistenceException | IOException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public void addParameterContext(String experiment, String parameter, int rid)
			throws NoSuchExperimentException, ExperimentException {

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
					ActionException ex = new ActionException(
							"parameter does not exist");
					ex.getAction().put("name", parameter);
					ex.getAction().put("rid", rid);

					log.warn(ex.getAction().toString());
					throw new ExperimentException(ex.getAction());
				}
			} else {
				throw new NoSuchExperimentException(experiment);
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public boolean anyReservationActiveNow(ExperimentType type)
			throws ExperimentException {

		boolean anyActive = false;
		try {
			if (type == null) {
				anyActive = service.anyReservationAt(new Date());
			} else {
				anyActive = service.anyReservationAtByType(type.name(),
						new Date());
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		return anyActive;
	}

	public boolean anyRTReservationBetween(List<String> telescopes,
			TimeSlot timeSlot) throws ExperimentException {

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
			throw new ExperimentException(e.getMessage());
		}

		return !available;
	}

	public boolean anyUserReservationActiveNow(ExperimentType type,
			String username) throws ExperimentException {

		boolean anyActive = false;
		try {
			if (type == null) {
				anyActive = service.anyUserReservationAt(username, new Date());
			} else {
				anyActive = service.anyUserReservationAtByType(type.name(),
						username, new Date());
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		return anyActive;
	}

	public void clearAllObsoleteContexts() throws ExperimentException {

		try {

			List<ReservationEntry> reservationEntries = service
					.getAllReservationsShouldBeObsolete();

			for (ReservationEntry entry : reservationEntries) {
				service.removeReservationContext(entry.getIdreservation());
				service.setReservationStatus(entry.getIdreservation(),
						"OBSOLETE");
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	public void clearAllObsoleteReservations() throws ExperimentException {

		try {

			List<ReservationEntry> reservationEntries = service
					.getAllReservationsBefore(new Date());

			for (ReservationEntry entry : reservationEntries) {
				service.removeReservation(entry.getIdreservation());
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public boolean containsExperiment(String experiment)
			throws ExperimentException {

		boolean alreadyContained = false;

		try {
			alreadyContained = service.containsExperiment(experiment);
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		return alreadyContained;
	}

	public void createExperiment(String experiment, String author, String type)
			throws ExperimentException, DuplicateExperimentException {

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
			throw new ExperimentException(e.getMessage());
		}

		if (alreadyContained) {
			throw new DuplicateExperimentException(experiment);
		}
	}

	public void deleteExperiment(String experiment)
			throws NoSuchExperimentException, ExperimentException {

		try {
			if (service.containsExperiment(experiment)) {
				service.removeExperiment(experiment);
			} else {
				NoSuchExperimentException e = new NoSuchExperimentException(
						experiment);
				log.error(e.getMessage());
				throw e;
			}
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	public void deleteExperimentContext(int rid) throws ExperimentException {

		try {

			service.removeReservationContext(rid);
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	public void deleteReservation(int rid) throws ExperimentException {

		try {

			service.removeReservation(rid);

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public void emptyExperiment(String experiment) throws ExperimentException,
			NoSuchExperimentException {

		try {

			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);

				service.removeAllExperimentOperations(experimentId);
				service.removeAllExperimentParameters(experimentId);
				service.removeExperimentContexts(experimentId);
			} else {
				throw new NoSuchExperimentException(experiment);
			}

		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ExperimentException();
		}
	}

	public List<String> getAllExperiments(ExperimentType type)
			throws ExperimentException {

		List<String> experiments = null;

		try {
			experiments = service.getAllTypeExperiments(type.name());

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		return experiments;
	}

	public List<ReservationInformation> getAllPendingReservations(
			ExperimentType type) throws ExperimentException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries;

			if (type == null) {
				reservationEntries = service.getAllReservationsFrom(new Date());
			} else {
				reservationEntries = service.getAllReservationsFromByType(
						type.name(), new Date());
			}

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public List<ReservationInformation> getAllReservationsActiveNow(
			ExperimentType type) throws ExperimentException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries;
			if (type == null) {
				reservationEntries = service.getAllReservationsAt(new Date());
			} else {
				reservationEntries = service.getAllReservationsAtByType(
						type.name(), new Date());
			}

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("active");
			}

			return this.processReservationEntries(reservationEntries);
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public ExperimentInformation getBasicExperimentInformation(String experiment)
			throws ExperimentException, NoSuchExperimentException {

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
			throw new ExperimentException(e.getMessage());
		}

		throw new NoSuchExperimentException(experiment);
	}

	public List<ResultInformation> getContextResults(int context)
			throws ExperimentException, NoSuchReservationException {

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
			throw new ExperimentException(e.getMessage());
		}
	}

	public ExperimentInformation getExperimentInformation(String experiment)
			throws ExperimentException, NoSuchExperimentException {

		try {

			ExperimentEntry entry = service.getExperiment(experiment);

			if (entry != null) {
				ExperimentInformation expInfo = new ExperimentInformation();
				expInfo.setName(entry.getName());
				expInfo.setAuthor(entry.getAuthor());
				expInfo.setDescription(entry.getDescription());
				expInfo.setType(ExperimentType.valueOf(entry.getType()));

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
						ParameterTypeNotAvailableException e = new ParameterTypeNotAvailableException(
								experiment, parameterEntry.getParameter(),
								typePattern);
						log.error(e.getMessage());
						throw e;
					}

					paramInfo.setType(parameterType);
					paramInfo.setParameter(parameterFactory
							.createParameter(parameterType));

					paramInfo.setArguments(arguments);
					parameters.add(paramInfo);
				}

				return expInfo;
			}
		} catch (PersistenceException | IOException
				| ParameterTypeNotAvailableException
				| ExperimentParameterException
				| OperationTypeNotAvailableException e) {
			throw new ExperimentException(e.getMessage());
		}

		throw new NoSuchExperimentException(experiment);
	}

	public List<ResultInformation> getExperimentResults(String experiment)
			throws ExperimentException {

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
			throw new ExperimentException(e.getMessage());
		}
	}

	public ExperimentRuntimeInformation getExperimentRuntimeContext(int rid)
			throws ExperimentException, ExperimentNotInstantiatedException {

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
			throw new ExperimentException(e.getMessage());
		}
	}

	public ExperimentType getExperimentType(String experiment)
			throws ExperimentException {

		try {
			ExperimentEntry expEntry = service.getExperiment(experiment);
			return ExperimentType.valueOf(expEntry.getType());
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public Object getParameterContextValue(String parameter, int rid)
			throws ExperimentException, NoSuchParameterException,
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
				throw new ExperimentException("parameter problem");
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public ReservationInformation getReservationInformation(int rid)
			throws ExperimentException, NoSuchReservationException {

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
			throw new ExperimentException(e.getMessage());
		}

		if (reservation == null) {
			throw new NoSuchReservationException(rid);
		}

		return reservation;
	}

	public List<ReservationInformation> getUserPendingReservations(
			String username) throws ExperimentException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsFrom(username, new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public List<ReservationInformation> getUserPendingReservations(
			ExperimentType type, String username) throws ExperimentException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries = service
					.getUserReservationsFromByType(type.name(), username,
							new Date());

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("pending");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public List<ReservationInformation> getUserReservationsActiveNow(
			ExperimentType type, String username) throws ExperimentException,
			NoReservationsAvailableException {

		try {
			List<ReservationEntry> reservationEntries;

			if (type == null) {
				reservationEntries = service.getUserReservationsAt(username,
						new Date());
			} else {
				reservationEntries = service.getUserReservationsAtByType(
						type.name(), username, new Date());
			}

			if (reservationEntries == null || reservationEntries.size() == 0) {
				throw new NoReservationsAvailableException("active");
			}

			return this.processReservationEntries(reservationEntries);

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public boolean isReservationActiveNow(int rid) throws ExperimentException {

		boolean anyActive = false;
		try {
			anyActive = service.isReservationActiveNow(rid, new Date());

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		return anyActive;
	}

	public boolean isReservationContextReady(int rid)
			throws ExperimentException, NoSuchReservationException {

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
			throw new ExperimentException(e.getMessage());
		}
	}

	public synchronized int makeReservation(String experiment,
			List<String> telescopes, String username, TimeSlot timeSlot)
			throws ExperimentException {

		try {
			int experimentId = service.getExperimentId(experiment);

			if (this.anyRTReservationBetween(telescopes, timeSlot)) {
				throw new ExperimentException("already reserved");
			}

			ReservationEntry reservationEntry = new ReservationEntry();

			reservationEntry.setExperiment(experimentId);
			reservationEntry.setUser(username);
			reservationEntry.setBegin(timeSlot.getBegin());
			reservationEntry.setEnd(timeSlot.getEnd());
			reservationEntry.setStatus("SCHEDULED");

			service.saveReservation(reservationEntry);

			List<Integer> reservations = service.getReservationIds(username,
					experimentId, timeSlot.getBegin(), timeSlot.getEnd());

			if (telescopes != null) {
				for (Integer id : reservations) {
					List<String> storedRTs = service.getAllRTOfReservation(id);
					if (storedRTs == null || storedRTs.size() == 0) {
						for (String rt : telescopes) {
							service.saveRTReservation(id, rt);
						}
						
						return id;
					}
				}				
			}

			return reservationEntry.getIdreservation();

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
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

	public void removeExperimentOperation(String experiment, String opName)
			throws ExperimentException, NoSuchExperimentException {

		try {

			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);
				service.removeExperimentOperation(experimentId, opName);
				service.removeExperimentContexts(experimentId);
			} else {
				throw new NoSuchExperimentException(experiment);
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public void removeExperimentParameter(String experiment, String paramName)
			throws ExperimentException, NoSuchExperimentException {

		try {

			if (service.containsExperiment(experiment)) {

				int experimentId = service.getExperimentId(experiment);

				service.removeDependantOperations(experimentId, paramName);
				service.removeExperimentParameter(experimentId, paramName);
				service.removeExperimentContexts(experimentId);
			} else {
				throw new NoSuchExperimentException(experiment);
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @return
	 * @throws ActionException
	 */
	public boolean reservationIsActiveNowForUser(int rid, String user)
			throws ExperimentException {

		boolean anyActive = false;
		try {
			anyActive = service.isReservationActiveNowForUser(rid, user,
					new Date());

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		return anyActive;
	}

	public void saveResult(int reservationId, String tag, String user,
			Object value) throws ExperimentException {

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
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @param rid
	 * @throws ActionException
	 */
	public void setContextError(int rid) throws ExperimentException {

		try {

			service.setReservationStatus(rid, "ERROR");

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @param rid
	 * @throws ActionException
	 */
	public void setContextInit(int rid) throws ExperimentException {

		try {

			service.setReservationStatus(rid, "INIT");

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @param rid
	 * @throws ActionException
	 */
	public void setContextObsolete(int rid) throws ExperimentException {

		try {

			service.setReservationStatus(rid, "OBSOLETE");

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @param rid
	 * @throws ActionException
	 */
	public void setContextReady(int rid) throws ExperimentException {
		try {
			service.setReservationStatus(rid, "READY");

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @param service
	 * @throws ActionException
	 */
	/**
	 * @param service
	 * @throws ActionException
	 */
	public void setExperimentDBService(ExperimentDBService service)
			throws ActionException {
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
			log.info("Database ready");
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	public void setExperimentDescription(String experiment, String description)
			throws ExperimentException, NoSuchExperimentException {

		boolean alreadyContained = false;

		try {
			if (service.containsExperiment(experiment)) {

				alreadyContained = true;
				service.setExperimentDescription(experiment, description);

			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

		if (!alreadyContained) {
			throw new NoSuchExperimentException(experiment);
		}
	}

	public void setOperationArguments(String experiment, String operation,
			String[] arguments) throws ExperimentException,
			NoSuchParameterException, NoSuchOperationException,
			NoSuchExperimentException {

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
					throw new NoSuchOperationException(operation);
				}
			} else {
				throw new NoSuchExperimentException(experiment);
			}
		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}

	}

	/**
	 * @param factory
	 */
	public void setOperationFactory(ExperimentOperationFactory factory) {
		this.operationFactory = factory;
		log.info("Operation factory injected");
	}

	public void setParameterContextValue(String parameter, int rid, Object value)
			throws ExperimentException, NoSuchParameterException,
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
				service.setParameterContextValue(
						parameterEntry.getIdparameter(), rid, value);
			} else {
				throw new NoSuchParameterException(parameter);
			}

		} catch (PersistenceException e) {
			throw new ExperimentException(e.getMessage());
		}
	}

	/**
	 * @param factory
	 */
	public void setParameterFactory(ExperimentParameterFactory factory) {
		this.parameterFactory = factory;
		log.info("Parameter factory injected");
	}
}
