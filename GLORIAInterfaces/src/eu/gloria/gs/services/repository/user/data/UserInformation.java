package eu.gloria.gs.services.repository.user.data;


public class UserInformation {

	private String name;
	private String password;
	private UserRole[] roles;
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserRole[] getRoles() {
		return this.roles;
	}

	public void setRoles(UserRole[] roles) {
		this.roles = roles;
	}
}
