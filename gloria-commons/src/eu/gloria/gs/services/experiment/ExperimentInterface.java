package eu.gloria.gs.services.experiment;

import java.util.List;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.base.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.script.NoSuchScriptException;
import eu.gloria.gs.services.experiment.script.OverlapRTScriptException;
import eu.gloria.gs.services.experiment.script.data.RTScriptInformation;
import eu.gloria.gs.services.repository.user.InvalidUserException;
import eu.gloria.gs.services.utils.ObjectResponse;

@WebService(name = "ExperimentInterface", targetNamespace = "http://experiment.services.gs.gloria.eu/")
public interface ExperimentInterface {

	public void createOnlineExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, DuplicateExperimentException;

	public void createOfflineExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, DuplicateExperimentException;

	public void deleteExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException;

	public void emptyExperiment(@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException;

	public void deleteExperimentParameter(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "parameter") String parameter)
			throws ExperimentException, NoSuchExperimentException;

	public void deleteExperimentOperation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") String operation)
			throws ExperimentException, NoSuchExperimentException;

	public void addExperimentOperation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") OperationInformation operation)
			throws ExperimentException, NoSuchExperimentException;

	public void addExperimentParameter(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") ParameterInformation parameter)
			throws ExperimentException, NoSuchExperimentException,
			NoSuchParameterException;

	public ExperimentInformation getExperimentInformation(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException;

	public ParameterInformation getParameterInformation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "parameter") String parameter)
			throws ExperimentException, NoSuchExperimentException;

	public void setExperimentDescription(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "description") String description)
			throws ExperimentException, NoSuchExperimentException;

	public List<String> getAllOnlineExperiments() throws ExperimentException;

	// public List<String> getAllExperiments() throws ExperimentException;

	public List<String> getAllOfflineExperiments() throws ExperimentException;

	public boolean containsExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException;

	public List<ReservationInformation> getMyPendingReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public List<ReservationInformation> getMyPendingOnlineReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public List<ReservationInformation> getMyPendingOfflineReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public boolean anyReservationActiveNow() throws ExperimentException;

	public boolean anyOfflineReservationActiveNow() throws ExperimentException;

	public boolean anyOnlineReservationActiveNow() throws ExperimentException;

	public List<ReservationInformation> getMyCurrentReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public List<ReservationInformation> getMyCurrentOfflineReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public List<ReservationInformation> getMyCurrentOnlineReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public void reserveExperiment(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "telescopes") List<String> telescopes,
			@WebParam(name = "timeslot") TimeSlot timeSlot)
			throws ExperimentException, NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException;

	public void applyForExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException;

	public List<TimeSlot> getAvailableReservations(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "telescopes") List<String> telescopes)
			throws ExperimentException, ExperimentReservationArgumentException;

	public void cancelExperimentReservation(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException;

	public ReservationInformation getReservationInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws InvalidUserContextException, NoSuchReservationException,
			ExperimentException;

	public void executeExperimentOperation(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "operation") String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentNotInstantiatedException, ExperimentException,
			NoSuchReservationException, NoSuchExperimentException,
			InvalidUserContextException;

	public void setExperimentParameterValue(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "parameter") String parameter,
			@WebParam(name = "value") ObjectResponse value)
			throws ExperimentNotInstantiatedException,
			NoSuchReservationException, ExperimentParameterException,
			InvalidUserContextException, NoSuchParameterException;

	public ObjectResponse getExperimentParameterValue(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "parameter") String parameter)
			throws ExperimentNotInstantiatedException,
			NoSuchReservationException, ExperimentParameterException,
			InvalidUserContextException, NoSuchParameterException;

	public List<ResultInformation> getContextResults(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException;

	public List<ResultInformation> getExperimentResults(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException;

	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException, InvalidUserContextException;

	public Set<String> getAllExperimentParameters() throws ExperimentException;

	public Set<String> getAllExperimentOperations() throws ExperimentException;

	public ExperimentParameter getExperimentParameter(
			@WebParam(name = "parameterName") String name)
			throws ExperimentException, NoSuchParameterException;

	public ExperimentOperation getExperimentOperation(
			@WebParam(name = "operationName") String name)
			throws ExperimentException, NoSuchOperationException;

	/**
	 * @param reservationId
	 * @return
	 * @throws ExperimentException
	 * @throws ExperimentNotInstantiatedException
	 * @throws NoSuchReservationException
	 * @throws InvalidUserContextException
	 * @throws NoSuchParameterException
	 */
	public ObjectResponse getExperimentContext(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException, InvalidUserContextException,
			NoSuchParameterException;

	/**
	 * @param reservationId
	 * @return
	 * @throws ExperimentException
	 * @throws ExperimentNotInstantiatedException
	 * @throws NoSuchReservationException
	 */
	public boolean isExperimentContextReady(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, NoSuchReservationException;

	public void resetExperimentContext(int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException, ExperimentNotInstantiatedException;

	public int registerRTScript(@WebParam(name = "telescope") String telescope,
			@WebParam(name = "slot") ScriptSlot slot,
			@WebParam(name = "operation") String operation,
			@WebParam(name = "init") String init,
			@WebParam(name = "result") String result,
			@WebParam(name = "notify") boolean notify)
			throws NoSuchExperimentException, ExperimentException,
			OverlapRTScriptException, InvalidUserException;

	public List<String> getRTScriptAvailableOperations(
			@WebParam(name = "telescope") String telescope)
			throws ExperimentException, NoSuchScriptException;

	public RTScriptInformation getRTScriptInformation(int sid)
			throws NoSuchScriptException, ExperimentException;

	public List<Integer> getAllRTScripts(String rt) throws ExperimentException;

	public void removeRTScript(int sid) throws NoSuchScriptException,
			InvalidUserException, ExperimentException;
}
