package eu.gloria.gs.services.experiment;

import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentType;
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

	public void createOnlineExperiment(String experiment)
			throws ExperimentException, DuplicateExperimentException;

	public void createOfflineExperiment(String experiment)
			throws ExperimentException, DuplicateExperimentException;

	public void deleteExperiment(String experiment) throws ExperimentException,
			NoSuchExperimentException;

	public void emptyExperiment(String experiment) throws ExperimentException,
			NoSuchExperimentException;

	public void deleteExperimentParameter(String experiment, String parameter)
			throws ExperimentException, NoSuchExperimentException;

	public void deleteExperimentOperation(String experiment, String operation)
			throws ExperimentException, NoSuchExperimentException;

	public void addExperimentOperation(String experiment,
			OperationInformation operation) throws ExperimentException,
			NoSuchExperimentException;

	public void addExperimentParameter(String experiment,
			ParameterInformation parameter) throws ExperimentException,
			NoSuchExperimentException, NoSuchParameterException;

	public ExperimentInformation getExperimentInformation(String experiment)
			throws ExperimentException, NoSuchExperimentException;

	public ParameterInformation getParameterInformation(String experiment,
			String parameter) throws ExperimentException,
			NoSuchExperimentException;

	public void setExperimentDescription(String experiment, String description)
			throws ExperimentException, NoSuchExperimentException;

	public List<String> getAllExperiments(ExperimentType type)
			throws ExperimentException;

	public boolean containsExperiment(String experiment)
			throws ExperimentException;

	public List<ReservationInformation> getMyPendingReservations(
			ExperimentType type) throws ExperimentException,
			NoReservationsAvailableException;

	public boolean anyReservationActiveNow(ExperimentType type)
			throws ExperimentException;

	public List<ReservationInformation> getMyCurrentReservations(
			ExperimentType type) throws ExperimentException,
			NoReservationsAvailableException;

	public void reserveExperiment(String experiment, List<String> telescopes,
			TimeSlot timeSlot) throws ExperimentException,
			NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException;

	public void applyForExperiment(String experiment)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException;

	public List<TimeSlot> getAvailableReservations(String experiment,
			List<String> telescopes) throws ExperimentException,
			ExperimentReservationArgumentException;

	public void cancelExperimentReservation(int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException;

	public ReservationInformation getReservationInformation(int reservationId)
			throws InvalidUserContextException, NoSuchReservationException,
			ExperimentException;

	public void executeExperimentOperation(int reservationId, String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, ExperimentException;

	public void setExperimentParameterValue(int reservationId,
			String parameter, ObjectResponse value)
			throws ExperimentParameterException, ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException, NoSuchParameterException;

	public ObjectResponse getExperimentParameterValue(int reservationId,
			String parameter) throws ExperimentParameterException,
			ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException, InvalidUserContextException,
			NoSuchParameterException;

	public List<ResultInformation> getContextResults(int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException;

	public List<ResultInformation> getExperimentResults(String experiment)
			throws ExperimentException;

	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			int reservationId) throws ExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException,
			InvalidUserContextException;

	public Set<String> getAllExperimentParameters() throws ExperimentException;

	public Set<String> getAllExperimentOperations() throws ExperimentException;

	public ExperimentParameter getExperimentParameter(String name)
			throws ExperimentException, NoSuchParameterException;

	public ExperimentOperation getExperimentOperation(String name)
			throws ExperimentException, NoSuchOperationException;

	public ObjectResponse getExperimentContext(int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException, InvalidUserContextException,
			NoSuchParameterException;

	public boolean isExperimentContextReady(int reservationId)
			throws ExperimentException, NoSuchReservationException;

	public void resetExperimentContext(int reservationId)
			throws ExperimentException, NoSuchReservationException,
			InvalidUserContextException, ExperimentNotInstantiatedException;

	public int registerRTScript(String telescope, ScriptSlot slot,
			String operation, String init, String result, boolean notify)
			throws NoSuchExperimentException, ExperimentException,
			OverlapRTScriptException, InvalidUserException;

	public List<String> getRTScriptAvailableOperations(String telescope)
			throws ExperimentException, NoSuchScriptException;

	public RTScriptInformation getRTScriptInformation(int sid)
			throws NoSuchScriptException, ExperimentException;

	public List<Integer> getAllRTScripts(String rt) throws ExperimentException;

	public void removeRTScript(int sid) throws NoSuchScriptException,
			InvalidUserException, ExperimentException;
}
