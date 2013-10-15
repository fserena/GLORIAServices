package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public interface ExperimentDBService {

	/**
	 * 
	 */
	public void createExperimentTable();

	/**
	 * 
	 */
	public void createReservationTable();

	/**
	 * 
	 */
	public void createRTReservationTable();

	/**
	 * 
	 */
	public void createExperimentParameterTable();

	/**
	 * 
	 */
	public void createExperimentOperationTable();

	/**
	 * 
	 */
	public void createExperimentArgumentTable();

	/**
	 * 
	 */
	public void createExperimentContextTable();

	/**
	 * 
	 */
	public void createExperimentResultsTable();

	/**
	 * @param name
	 * @return
	 */
	public ExperimentEntry getExperiment(@Param(value = "name_") String name);

	/**
	 * @param experiment
	 * @param parameter
	 * @return
	 */
	public ParameterEntry getExperimentParameter(
			@Param(value = "experiment_") int experiment,
			@Param(value = "parameter_") String parameter);

	/**
	 * @param pid
	 * @return
	 */
	public ParameterEntry getParameterById(@Param(value = "pid_") int pid);
	
	/**
	 * @param oid
	 * @return
	 */
	public OperationEntry getOperationById(@Param(value = "oid_") int oid);

	/**
	 * @param pid
	 * @param rid
	 * @param value
	 */
	public void setParameterContextValue(@Param(value = "pid_") int pid,
			@Param(value = "rid_") int rid,
			@Param(value = "value_") Object value);

	/**
	 * @param pid
	 * @param rid
	 * @return
	 */
	public Object getParameterContextValue(@Param(value = "pid_") int pid,
			@Param(value = "rid_") int rid);

	/**
	 * @param experiment
	 * @param operation
	 * @return
	 */
	public OperationEntry getExperimentOperation(
			@Param(value = "experiment_") int experiment,
			@Param(value = "operation_") String operation);

	/**
	 * @param pid
	 * @param rid
	 * @return
	 */
	public ContextEntry getParametertContext(@Param(value = "pid_") int pid,
			@Param(value = "rid_") String rid);

	/**
	 * @param experiment
	 * @return
	 */
	public List<ParameterEntry> getAllExperimentParameters(
			@Param(value = "experiment_") int experiment);

	/**
	 * @param experiment
	 * @return
	 */
	public List<OperationEntry> getAllExperimentOperations(
			@Param(value = "experiment_") int experiment);

	/**
	 * @param rid
	 * @return
	 */
	public List<ContextEntry> getReservationContext(
			@Param(value = "rid_") int rid);

	/**
	 * @param rid
	 * @return
	 */
	public boolean isReservationContextInstantiated(
			@Param(value = "rid_") int rid);

	/**
	 * @param opid
	 * @return
	 */
	public List<ArgumentEntry> getOperationArguments(
			@Param(value = "opid_") int opid);

	/**
	 * @param entry
	 */
	public void saveOperationArgument(ArgumentEntry entry);

	/**
	 * @param name
	 * @return
	 */
	public int getExperimentId(@Param(value = "name_") String name);

	/**
	 * @param id
	 * @return
	 */
	public ExperimentEntry getExperimentById(
			@Param(value = "idexperiment_") int id);

	/**
	 * @param entry
	 */
	public void saveExperiment(ExperimentEntry entry);

	/**
	 * @param entry
	 */
	public void saveParameterContext(ContextEntry entry);

	/**
	 * @param entry
	 */
	public void saveExperimentOperation(OperationEntry entry);

	/**
	 * @param entry
	 */
	public void saveExperimentParameter(ParameterEntry entry);

	/**
	 * @param name
	 * @return
	 */
	public boolean containsExperiment(@Param(value = "name_") String name);

	/**
	 * @param name
	 * @param description
	 */
	public void setExperimentDescription(@Param(value = "name_") String name,
			@Param(value = "description_") String description);

	/**
	 * @param name
	 */
	public void removeExperiment(@Param(value = "name_") String name);

	/**
	 * @param type
	 * @return
	 */
	public List<String> getAllTypeExperiments(
			@Param(value = "type_") String type);

	/**
	 * @param user
	 * @return
	 */
	public List<ReservationEntry> getUserReservations(
			@Param(value = "user_") String user);

	/**
	 * @param user
	 * @param rid
	 * @return
	 */
	public ReservationEntry getUserReservationById(
			@Param(value = "user_") String user, @Param(value = "rid_") int rid);

	/**
	 * @param rid
	 * @return
	 */
	public ReservationEntry getReservationById(@Param(value = "rid_") int rid);

	/**
	 * @param user
	 * @param from
	 * @return
	 */
	public List<ReservationEntry> getUserReservationsFrom(
			@Param(value = "user_") String user,
			@Param(value = "from_") Date from);

	/**
	 * @param user
	 * @param from
	 * @return
	 */
	public List<ReservationEntry> getAllReservationsFrom(
			@Param(value = "from_") Date from);

	/**
	 * @param user
	 * @param experiment
	 * @param begin
	 * @param end
	 * @return
	 */
	public ReservationEntry getReservation(@Param(value = "user_") String user,
			@Param(value = "experiment_") int experiment,
			@Param(value = "begin_") Date begin, @Param(value = "end_") Date end);

	/**
	 * @param user
	 * @param experiment
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean containsReservation(@Param(value = "user_") String user,
			@Param(value = "experiment_") int experiment,
			@Param(value = "begin_") Date begin, @Param(value = "end_") Date end);

	/**
	 * @param user
	 * @param when
	 * @return
	 */
	public boolean anyUserReservationAt(@Param(value = "user_") String user,
			@Param(value = "when_") Date when);

	/**
	 * @param when
	 * @return
	 */
	public boolean anyReservationAt(@Param(value = "when_") Date when);

	/**
	 * @param user
	 * @param when
	 * @return
	 */
	public List<ReservationEntry> getUserReservationAt(
			@Param(value = "user_") String user,
			@Param(value = "when_") Date when);

	/**
	 * @param when
	 * @return
	 */
	public List<ReservationEntry> getAllReservationsAt(
			@Param(value = "when_") Date when);

	/**
	 * @param when
	 * @return
	 */
	public List<ReservationEntry> getAllReservationsBefore(
			@Param(value = "when_") Date when);

	/**
	 * @param rt
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean anyRTReservationBetween(@Param(value = "rt_") String rt,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	/**
	 * @param rt
	 * @param until
	 * @return
	 */
	public List<ReservationEntry> getAllReservationsUntil(
			@Param(value = "rt_") String rt, @Param(value = "until_") Date until);

	/**
	 * @param rid
	 * @return
	 */
	public List<String> getAllRTOfReservation(@Param(value = "rid_") int rid);

	/**
	 * @param experiment
	 * @return
	 */
	public List<ReservationEntry> getExperimentReservations(
			@Param(value = "experiment_") String experiment);

	/**
	 * @param entry
	 */
	public void saveReservation(ReservationEntry entry);

	/**
	 * @param reservation_id
	 * @param rt_name
	 */
	public void saveRTReservation(
			@Param(value = "reservation_id_") int reservation_id,
			@Param(value = "rt_name_") String rt_name);

	/**
	 * @param rid
	 */
	public void removeReservation(@Param(value = "rid_") int rid);

	/**
	 * @param idreservation
	 */
	public void removeReservationContext(
			@Param(value = "rid_") int idreservation);
	
	
	public void saveResult(ResultEntry entry);
	
	public List<ResultEntry> getContextResults(@Param(value = "rid_") int rid);
	
	public List<Integer> getAllExperimentContexts(@Param(value = "name_") String name);
	
}
