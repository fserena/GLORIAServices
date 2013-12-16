package eu.gloria.gs.services.log.action.data.dbservices;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public interface ActionLogDBService {

	public void create();

	public List<ActionLogEntry> getAllByUser(@Param(value = "user_") String user);

	public List<ActionLogEntry> getAllByUserAndDate(
			@Param(value = "user_") String user,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public List<ActionLogEntry> getAllByRid(@Param(value = "rid_") int rid);
	
	public List<ActionLogEntry> getAllByRidAndDate(
			@Param(value = "int_") int rid,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public List<ActionLogEntry> getAllByDate(@Param(value = "from_") Date from,
			@Param(value = "to_") Date to);

	public List<ActionLogEntry> getAllByRt(@Param(value = "rt_") String rt);

	public List<ActionLogEntry> getAllByRtAndDate(
			@Param(value = "rt_") String rt,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);
	
	public List<ActionLogEntry> getByUserAndType(
			@Param(value = "user_") String user,
			@Param(value = "type_") String type);

	public List<ActionLogEntry> getByUserTypeAndDate(
			@Param(value = "user_") String user,
			@Param(value = "type_") String type,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public List<ActionLogEntry> getByRidAndType(@Param(value = "rid_") int rid,
			@Param(value = "type_") String type);

	public List<ActionLogEntry> getByRidTypeAndDate(
			@Param(value = "rid_") int rid,
			@Param(value = "type_") String type,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public List<ActionLogEntry> getByDateAndType(
			@Param(value = "from_") Date from, @Param(value = "to_") Date to,
			@Param(value = "type_") String type);

	public List<ActionLogEntry> getByRtAndType(@Param(value = "rt_") String rt,
			@Param(value = "type_") String type);

	public List<ActionLogEntry> getByRtTypeAndDate(
			@Param(value = "rt_") String rt,
			@Param(value = "type_") String type,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public void save(ActionLogEntry entry);

	public boolean containsUser(@Param(value = "user_") String user);

	public boolean containsUserByDate(@Param(value = "user_") String user,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public boolean containsRid(@Param(value = "rid_") int rid);

	public boolean containsRidByDate(@Param(value = "rid_") int rid,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public boolean containsRt(@Param(value = "rt_") String rt);

	public boolean containsRtByDate(@Param(value = "rt_") String rt,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public boolean containsDate(@Param(value = "from_") Date from,
			@Param(value = "to_") Date to);

	public void removeByUser(@Param(value = "user_") String user);

	public void removeByRid(@Param(value = "rid_") int rid);

	public void removeByRt(@Param(value = "rt_") String rt);

	public void removeByDate(@Param(value = "from_") Date from,
			@Param(value = "to_") Date to);
}
