package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RTDeviceDBService {

	public void create();
	
	public List<RTDevEntry> getByRT(@Param(value="rt_")String rt);
	public List<RTDevEntry> getByDevice(@Param(value="did_")int did);
	public RTDevEntry get(@Param(value="rt_")String rt, @Param(value="name_")String name);
	public void save(RTDevEntry entry);
	public void remove(@Param(value="rt_")String rt, @Param(value="did_")int did, @Param(value="name_")String name);		
}
