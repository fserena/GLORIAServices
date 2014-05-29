package eu.gloria.gs.services.repository.image.data.dbservices;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public interface ImageDBService {

	public void create();

	public ImageEntry get(@Param(value = "id_") int id);

	public List<ImageEntry> getRandom(@Param(value = "count_") int count);

	public List<ImageEntry> getUserRandom(@Param(value = "user_") String user,
			@Param(value = "count_") int count);

	public ImageEntry getByFits(@Param(value = "host_") String host,
			@Param(value = "lid_") String lid);

	public ImageEntry getByJpg(@Param(value = "host_") String host,
			@Param(value = "lid_") String lid);

	public void save(ImageEntry entry);

	public boolean contains(@Param(value = "id_") int id);

	public boolean containsJpg(@Param(value = "host_") String host,
			@Param(value = "lid_") String lid);

	public boolean containsFits(@Param(value = "host_") String host,
			@Param(value = "lid_") String lid);

	public boolean containsRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid);

	public void remove(@Param(value = "id_") int id);

	public void setReservation(@Param(value = "id_") int id,
			@Param(value = "rid_") int rid);

	public void setUser(@Param(value = "id_") int id,
			@Param(value = "user_") String user);

	public void setJpg(@Param(value = "id_") int id,
			@Param(value = "host_") String host);

	public void setFits(@Param(value = "id_") int id,
			@Param(value = "host_") String host);

	public void setJpgByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid,
			@Param(value = "host_") String host);

	public void setFitsByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid,
			@Param(value = "host_") String host);

	public void setTargetByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid,
			@Param(value = "target_") String target);

	public List<ImageEntry> getByUser(@Param(value = "user_") String user);

	public List<ImageEntry> getByReservation(@Param(value = "rid_") int rid);

	public List<Integer> getAllObjectImages(
			@Param(value = "object_") String object);

	public List<Integer> getAllObjectImagesBetweenDates(
			@Param(value = "object_") String object,
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public List<Integer> getAllBetweenDates(@Param(value = "from_") Date from,
			@Param(value = "to_") Date to, @Param(value = "limit_") int limit);

	public List<Integer> getRandomBetweenDates(@Param(value = "from_") Date from,
			@Param(value = "to_") Date to, @Param(value = "limit_") int limit);
	
	public List<ImageEntry> getByRT(@Param(value = "rt_") String rt);

	public ImageEntry getByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid);

	public List<ImageEntry> getAllWithoutUrl();
}
