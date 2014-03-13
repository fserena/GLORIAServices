package eu.gloria.gs.services.repository.user.data;

import java.util.Date;

public class UserInformation {

	private String name;
	private String password;
	private UserRole[] roles;
	private Date creationDate;
	private String language;
	private String ocupation;
	private String alias;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOcupation() {
		return ocupation;
	}

	public void setOcupation(String ocupation) {
		this.ocupation = ocupation;
	}

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
