package eu.gloria.gs.services.repository.user.data.dbservices;

import org.apache.ibatis.annotations.Param;

public interface UserDBService {

	public void create();

	public UserEntry get(@Param(value = "name_") String name);

	public void save(UserEntry entry);

	public boolean contains(@Param(value = "name_") String name);

	public String getPassword(@Param(value = "name_") String name);

	public void setPassword(@Param(value = "name_") String name,
			@Param(value = "password_") String password);

	public String getRoles(@Param(value = "name_") String name);

	public void setRoles(@Param(value = "name_") String name,
			@Param(value = "roles_") String roles);

	public void remove(@Param(value = "name_") String name);
}
