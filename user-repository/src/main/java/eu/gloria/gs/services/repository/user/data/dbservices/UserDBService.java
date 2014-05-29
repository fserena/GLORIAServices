package eu.gloria.gs.services.repository.user.data.dbservices;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

public interface UserDBService {

	public void create();

	public UserEntry get(@Param(value = "name_") String name);

	public void save(UserEntry entry);

	public boolean containsName(@Param(value = "name_") String name);

	public boolean containsAlias(@Param(value = "alias_") String alias);

	public Date getDate(@Param(value = "name_") String name);

	public String getPassword(@Param(value = "name_") String name);

	public void setPassword(@Param(value = "name_") String name,
			@Param(value = "password_") String password);

	public String getLanguage(@Param(value = "name_") String name);

	public void setLanguage(@Param(value = "name_") String name,
			@Param(value = "language_") String language);

	public String getOcupation(@Param(value = "name_") String name);

	public void setOcupation(@Param(value = "name_") String name,
			@Param(value = "ocupation_") String ocupation);

	public String getRoles(@Param(value = "name_") String name);

	public void setRoles(@Param(value = "name_") String name,
			@Param(value = "roles_") String roles);

	public void remove(@Param(value = "name_") String name);
}
