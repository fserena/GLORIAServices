package eu.gloria.gs.services.experiment.script.data.dbservices;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public interface RTScriptDBService {

	/**
	 * 
	 */
	public void createRTScriptTable();

	public void createRTScriptRidTable();

	/**
	 * @param name
	 * @return
	 */
	public RTScriptEntry getRTScript(@Param(value = "id_") int id);

	public void saveRTScript(RTScriptEntry entry);

	public void saveRTScriptRid(@Param(value = "sid_") int sid,
			@Param(value = "rid_") int rid);

	public List<RTScriptEntry> getAllTimelyScriptsAt(
			@Param(value = "when_") Date when);

	public List<RTScriptEntry> getAllDailyScriptsAt(
			@Param(value = "when_") Date when);

	public void prepareAllDailyNotActive();

	public List<Integer> getAllRTScripts(@Param(value = "rt_") String rt);

	public void setScriptStatus(@Param(value = "sid_") int id,
			@Param(value = "status_") String status);

	public String getScriptStatus(@Param(value = "sid_") int id);

	public void setReservation(@Param(value = "sid_") int id,
			@Param(value = "rid_") Integer rid);

	public boolean anyRTScriptBetween(@Param(value = "rt_") String rt,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public Integer getScriptId(@Param(value = "rt_") String rt,
			@Param(value = "begin_") Date begin);

	public void removeScript(@Param(value = "sid_") int sid);

	public List<Integer> getAllScriptRids(@Param(value = "sid_") int sid);
}
