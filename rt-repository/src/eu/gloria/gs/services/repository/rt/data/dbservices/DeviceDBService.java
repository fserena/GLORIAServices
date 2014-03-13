package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DeviceDBService {

	public void create();

	public void save(DeviceEntry entry);

	public void remove(@Param(value = "type_") String type,
			@Param(value = "model_") String model);

	public DeviceEntry get(@Param(value = "type_") String type,
			@Param(value = "model_") String model);
	
	public DeviceEntry getById(@Param(value = "did_") int did);

	public boolean contains(@Param(value = "type_") String type,
			@Param(value = "model_") String model);
	
	public List<String> getModels(@Param(value = "type_") String type);

	public List<String> getTypes();

	public String getDescription(@Param(value = "type_") String type,
			@Param(value = "model_") String model);

	public void setDescription(@Param(value = "type_") String type,
			@Param(value = "model_") String model,
			@Param(value = "desc_") String description);
}
