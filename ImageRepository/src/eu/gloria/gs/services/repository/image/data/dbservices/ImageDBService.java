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

	public ImageEntry getByUrl(@Param(value = "url_") String url);

	public void save(ImageEntry entry);

	public boolean contains(@Param(value = "id_") int id);

	public boolean containsUrl(@Param(value = "url_") String url);

	public boolean containsRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid);

	public void remove(@Param(value = "id_") int id);

	public void setReservation(@Param(value = "id_") int id,
			@Param(value = "rid_") int rid);

	public void setUser(@Param(value = "id_") int id,
			@Param(value = "user_") String user);

	public void setUrl(@Param(value = "id_") int id,
			@Param(value = "url_") String url);

	public void setUrlByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid, @Param(value = "url_") String url);

	public List<ImageEntry> getByUser(@Param(value = "user_") String user);

	public List<ImageEntry> getByReservation(@Param(value = "rid_") int rid);

	public List<ImageEntry> getAllBetweenDates(
			@Param(value = "from_") Date from, @Param(value = "to_") Date to);

	public List<ImageEntry> getByRT(@Param(value = "rt_") String rt);

	public ImageEntry getByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid);

	public List<ImageEntry> getAllWithoutUrl();
}
