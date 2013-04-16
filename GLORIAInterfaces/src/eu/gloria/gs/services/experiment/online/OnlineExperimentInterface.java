package eu.gloria.gs.services.experiment.online;

import java.util.List;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.experiment.online.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.online.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.online.data.FeatureCompliantInformation;
import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.data.OperationInformation;
import eu.gloria.gs.services.experiment.online.data.ParameterInformation;
import eu.gloria.gs.services.experiment.online.data.ReservationInformation;
import eu.gloria.gs.services.experiment.online.data.FeatureInformation;
import eu.gloria.gs.services.experiment.online.data.TimeSlot;
import eu.gloria.gs.services.experiment.online.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.online.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.online.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.online.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.online.reservation.NoReservationsAvailableException;

@WebService(name = "OnlineExperimentInterface", targetNamespace = "http://online.experiment.services.gs.gloria.eu/")
public interface OnlineExperimentInterface {

	public void createExperiment(
			@WebParam(name = "experiment") String experiment)
			throws OnlineExperimentException, DuplicateExperimentException;

	public void removeExperiment(
			@WebParam(name = "experiment") String experiment)
			throws OnlineExperimentException, NoSuchExperimentException;

	public void addExperimentOperation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") OperationInformation operation)
			throws OnlineExperimentException, NoSuchExperimentException;

	public void addExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws OnlineExperimentException, NoSuchExperimentException;

	public boolean testExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws OnlineExperimentException, NoSuchExperimentException;

	public FeatureCompliantInformation getFeatureCompliantInformation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws OnlineExperimentException, NoSuchExperimentException;

	public void addExperimentParameter(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") ParameterInformation parameter)
			throws OnlineExperimentException, NoSuchExperimentException;

	public ExperimentInformation getExperimentInformation(
			@WebParam(name = "experiment") String experiment)
			throws OnlineExperimentException, NoSuchExperimentException;

	public void setExperimentDescription(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "description") String description)
			throws OnlineExperimentException, NoSuchExperimentException;

	public List<String> getAllOnlineExperiments()
			throws OnlineExperimentException;

	public boolean containsExperiment(
			@WebParam(name = "experiment") String experiment)
			throws OnlineExperimentException;

	public List<ReservationInformation> getMyPendingReservations()
			throws OnlineExperimentException, NoReservationsAvailableException;

	public boolean anyReservationActiveNow() throws OnlineExperimentException;

	public List<ReservationInformation> getMyCurrentReservations()
			throws OnlineExperimentException, NoReservationsAvailableException;

	public void reserveExperiment(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "telescopes") List<String> telescopes,
			@WebParam(name = "timeslot") TimeSlot timeSlot)
			throws OnlineExperimentException, NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException;

	public List<TimeSlot> getAvailableReservations(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "telescopes") List<String> telescopes)
			throws OnlineExperimentException,
			ExperimentReservationArgumentException;

	public void cancelExperimentReservation(
			@WebParam(name = "reservationId") int reservationId)
			throws OnlineExperimentException, NoSuchReservationException;

	public ReservationInformation getReservationInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws OnlineExperimentException;

	public void executeExperimentOperation(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "operation") String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentParameterException, ExperimentNotInstantiatedException,
			OnlineExperimentException, NoSuchReservationException,
			NoSuchExperimentException;

	public void setExperimentParameterValue(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "parameter") String parameter,
			@WebParam(name = "value") Object value)
			throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException;

	public Object getExperimentParameterValue(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "parameter") String parameter)
			throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException;

	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws OnlineExperimentException,
			ExperimentNotInstantiatedException, NoSuchReservationException;

	public Set<String> getAllExperimentParameters()
			throws OnlineExperimentException;

	public Set<String> getAllExperimentOperations()
			throws OnlineExperimentException;

	public Set<String> getAllExperimentFeatures()
			throws OnlineExperimentException;

	public ExperimentParameter getExperimentParameter(
			@WebParam(name = "parameterName") String name)
			throws OnlineExperimentException;

	public ExperimentOperation getExperimentOperation(
			@WebParam(name = "operationName") String name)
			throws OnlineExperimentException;

	public ExperimentFeature getExperimentFeature(
			@WebParam(name = "featureName") String name)
			throws OnlineExperimentException;

}
