package eu.gloria.gs.services.core;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import org.apache.ws.security.WSUsernameTokenPrincipal;

public abstract class GSWebService {

	@Resource
	protected WebServiceContext context;
	private String username;
	private String password;

	public GSWebService() {
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
