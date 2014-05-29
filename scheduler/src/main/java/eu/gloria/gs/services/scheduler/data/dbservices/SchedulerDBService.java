package eu.gloria.gs.services.scheduler.data.dbservices;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public interface SchedulerDBService {

	public void createSchedule();

	public ScheduleEntry get(@Param(value = "id_") int id);

	public void save(ScheduleEntry entry);

	public boolean contains(@Param(value = "id_") int id);

	public boolean containsRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "uuid_") String uuid);

	public void remove(@Param(value = "id_") int id);

	public List<ScheduleEntry> getByUser(@Param(value = "user_") String user);

	public List<ScheduleEntry> getByRT(@Param(value = "rt_") String rt);

	public ScheduleEntry getByRTLocalId(@Param(value = "uuid_") String rt,
			@Param(value = "uuid_") String lid);

	public int getUserActiveSchedulesCount(@Param(value = "user_") String user);

	public List<ScheduleEntry> getUserActiveSchedules(
			@Param(value = "user_") String user);
	
	public List<ScheduleEntry> getUserInactiveSchedules(
			@Param(value = "user_") String user);

	public List<ScheduleEntry> getAllActiveSchedules();
	
	public List<ScheduleEntry> getActiveByRT(@Param(value = "rt_") String rt);

	public int getLastUserScheduleId(@Param(value = "user_") String user);

	public void setTelescope(@Param(value = "id_") int id,
			@Param(value = "rt_") String rt);

	public void setLastDate(@Param(value = "id_") int id,
			@Param(value = "ld_") Date ld);

	public void setStatus(@Param(value = "id_") int id,
			@Param(value = "status_") String status);

	public void setUuid(@Param(value = "id_") int id,
			@Param(value = "uuid_") String uuid);

	public void setPlan(@Param(value = "id_") int id,
			@Param(value = "plan_") String plan);

	public void setCandidates(@Param(value = "id_") int id,
			@Param(value = "cand_") String candidates);

	public void setResults(@Param(value = "id_") int id,
			@Param(value = "res_") String results);

}
