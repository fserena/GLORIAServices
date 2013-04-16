package eu.gloria.gs.services.log.action.data.dbservices;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public interface ActionLogDBService {

	public void create();

	public ActionLogEntry get(@Param(value = "name_") String name);

	public void save(ActionLogEntry entry);

	public boolean contains(@Param(value = "name_") String name);

	public String getOwner(@Param(value = "name_") String name);

	public void setDescription(@Param(value = "name_") String name,
			@Param(value = "description_") String description);

	public String getDescription(@Param(value = "name_") String name);

	public void setPublicKey(@Param(value = "name_") String name,
			@Param(value = "pk_") String pk);

	public String getPublicKey(@Param(value = "name_") String name);

	public void setCoordinates(@Param(value = "name_") String name,
			@Param(value = "lat_") double latitude,
			@Param(value = "long_") double longitude);

	public double getLongitude(@Param(value = "name_") String name);

	public double getLatitude(@Param(value = "name_") String name);

	public void setObservatory(@Param(value = "name_") String name,
			@Param(value = "oid_") int oid);

	public int getObservatory(@Param(value = "name_") String name);

	public List<String> getByObservatoryId(@Param(value = "oid_") int oid);

	public void remove(@Param(value = "name_") String name);
}
