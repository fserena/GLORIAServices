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

	public ImageEntry getByFits(@Param(value = "fits_") String fits);

	public ImageEntry getByJpg(@Param(value = "jpg_") String jpg);

	public void save(ImageEntry entry);

	public boolean contains(@Param(value = "id_") int id);

	public boolean containsJpg(@Param(value = "jpg_") String jpg);

	public boolean containsFits(@Param(value = "fits_") String fits);

	public boolean containsRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid);

	public void remove(@Param(value = "id_") int id);

	public void setReservation(@Param(value = "id_") int id,
			@Param(value = "rid_") int rid);

	public void setUser(@Param(value = "id_") int id,
			@Param(value = "user_") String user);

	public void setJpg(@Param(value = "id_") int id,
			@Param(value = "jpg_") String jpg);

	public void setFits(@Param(value = "id_") int id,
			@Param(value = "fits_") String fits);

	public void setJpgByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid, @Param(value = "jpg_") String jpg);

	public void setFitsByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid,
			@Param(value = "fits_") String fits);

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
			@Param(value = "to_") Date to);

	public List<ImageEntry> getByRT(@Param(value = "rt_") String rt);

	public ImageEntry getByRTLocalId(@Param(value = "rt_") String rt,
			@Param(value = "lid_") String lid);

	public List<ImageEntry> getAllWithoutUrl();
}
