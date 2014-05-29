package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RTDBService {

	public void create();

	public RTEntry get(@Param(value = "name_") String name);

	public void save(RTEntry entry);

	public boolean contains(@Param(value = "name_") String name);

	public String getOwner(@Param(value = "name_") String name);

	public void setDescription(@Param(value = "name_") String name,
			@Param(value = "description_") String description);

	public String getDescription(@Param(value = "name_") String name);

	public void setImage(@Param(value = "name_") String name,
			@Param(value = "image_") String image);

	public String getImage(@Param(value = "name_") String name);

	public Date getDate(@Param(value = "name_") String name);

	public void setPublicKey(@Param(value = "name_") String name,
			@Param(value = "pk_") String pk);

	public String getPublicKey(@Param(value = "name_") String name);

	public void setCoordinates(@Param(value = "name_") String name,
			@Param(value = "lat_") double latitude,
			@Param(value = "long_") double longitude);

	public double getLongitude(@Param(value = "name_") String name);

	public double getLatitude(@Param(value = "name_") String name);

	public RTAvailabilityEntry getAvailability(
			@Param(value = "name_") String name);

	public void setStartingAvailability(@Param(value = "name_") String name,
			@Param(value = "starting_") Date starting);

	public void setEndingAvailability(@Param(value = "name_") String name,
			@Param(value = "ending_") Date ending);

	public void setObservatory(@Param(value = "name_") String name,
			@Param(value = "oid_") int oid);

	public int getObservatory(@Param(value = "name_") String name);

	public List<String> getByObservatoryId(@Param(value = "oid_") int oid);

	public List<String> getAllInteractive();

	public List<String> getAllBatch();

	public void remove(@Param(value = "name_") String name);

}
