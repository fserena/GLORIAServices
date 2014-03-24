package eu.gloria.gs.services.core;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import org.apache.ws.security.WSUsernameTokenPrincipal;
import eu.gloria.gs.services.utils.LoggerEntity;

public abstract class GSWebService extends LoggerEntity {

	@Resource
	protected WebServiceContext context;
	private String username;
	private String password;

	protected GSWebService(String name) {
		super(name);
	}

	protected String getClientUsername() {
		WSUsernameTokenPrincipal client = (WSUsernameTokenPrincipal) context
				.getUserPrincipal();

		return client.getName();
	}

	protected String getClientPassword() {
		WSUsernameTokenPrincipal client = (WSUsernameTokenPrincipal) context
				.getUserPrincipal();

		return client.getPassword();
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
