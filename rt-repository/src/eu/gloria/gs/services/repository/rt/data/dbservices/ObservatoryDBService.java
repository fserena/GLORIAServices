package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ObservatoryDBService {

	public void create();

	public ObservatoryEntry getByName(@Param(value = "name_") String name);

	public ObservatoryEntry getById(@Param(value = "oid_") int oid);

	public void save(ObservatoryEntry entry);

	public void remove(@Param(value = "name_") String name);

	public boolean contains(@Param(value = "name_") String name);

	public String getCity(@Param(value = "name_") String name);

	public void setCity(@Param(value = "name_") String name,
			@Param(value = "city_") String city);

	public String getCountry(@Param(value = "name_") String name);

	public void setCountry(@Param(value = "name_") String name,
			@Param(value = "country_") String country);
	
	public double getVisibilityRatio(@Param(value = "name_") String name);

	public void setVisibilityRatio(@Param(value = "name_") String name,
			@Param(value = "ratio_") double ratio);

	public double getLightPollution(@Param(value = "name_") String name);

	public void setLightPollution(@Param(value = "name_") String name,
			@Param(value = "light_") double light);
	
	public List<String> getAllNames();
}
