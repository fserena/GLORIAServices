package eu.gloria.gs.services.experiment;

import java.util.List;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.FeatureCompliantInformation;
import eu.gloria.gs.services.experiment.base.data.FeatureInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ObjectResponse;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.base.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;

@WebService(name = "ExperimentInterface", targetNamespace = "http://experiment.services.gs.gloria.eu/")
public interface ExperimentInterface {

	public void createOnlineExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, DuplicateExperimentException;

	public void createOfflineExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, DuplicateExperimentException;

	public void removeExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException;

	public void addExperimentOperation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") OperationInformation operation)
			throws ExperimentException, NoSuchExperimentException;

	public void addExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws ExperimentException, NoSuchExperimentException;

	public boolean testExperimentFeature(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws ExperimentException, NoSuchExperimentException;

	public FeatureCompliantInformation getFeatureCompliantInformation(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "feature") FeatureInformation feature)
			throws ExperimentException, NoSuchExperimentException;

	public void addExperimentParameter(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "operation") ParameterInformation parameter)
			throws ExperimentException, NoSuchExperimentException;

	public ExperimentInformation getExperimentInformation(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException, NoSuchExperimentException;

	public void setExperimentDescription(
			@WebParam(name = "experiment") String experiment,
			@WebParam(name = "description") String description)
			throws ExperimentException, NoSuchExperimentException;

	public List<String> getAllOnlineExperiments() throws ExperimentException;

	public List<String> getAllOfflineExperiments() throws ExperimentException;

	public boolean containsExperiment(
			@WebParam(name = "experiment") String experiment)
			throws ExperimentException;

	public List<ReservationInformation> getMyPendingReservations()
			throws ExperimentException, NoReservationsAvailableException;

	public boolean anyReservationActiveNow() throws ExperimentException;

	public List<ReservationInformation> getMyCurrentReservations()
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
			throws ExperimentException, NoSuchReservationException;

	public ReservationInformation getReservationInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException;

	public void executeExperimentOperation(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "operation") String operation)
			throws ExperimentOperationException, NoSuchOperationException,
			ExperimentParameterException, ExperimentNotInstantiatedException,
			ExperimentException, NoSuchReservationException,
			NoSuchExperimentException;

	public void setExperimentParameterValue(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "parameter") String parameter,
			@WebParam(name = "value") ObjectResponse value)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException;

	public ObjectResponse getExperimentParameterValue(
			@WebParam(name = "reservationId") int reservationId,
			@WebParam(name = "parameter") String parameter)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException;

	public ExperimentRuntimeInformation getExperimentRuntimeInformation(
			@WebParam(name = "reservationId") int reservationId)
			throws ExperimentException, ExperimentNotInstantiatedException,
			NoSuchReservationException;

	public Set<String> getAllExperimentParameters() throws ExperimentException;

	public Set<String> getAllExperimentOperations() throws ExperimentException;

	public Set<String> getAllExperimentFeatures() throws ExperimentException;

	public ExperimentParameter getExperimentParameter(
			@WebParam(name = "parameterName") String name)
			throws ExperimentException;

	public ExperimentOperation getExperimentOperation(
			@WebParam(name = "operationName") String name)
			throws ExperimentException;

	public ExperimentFeature getExperimentFeature(
			@WebParam(name = "featureName") String name)
			throws ExperimentException;

}
