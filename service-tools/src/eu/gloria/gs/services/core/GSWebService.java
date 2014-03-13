package eu.gloria.gs.services.core;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GSWebService {

	@Resource
	protected WebServiceContext context;
	private String username;
	private String password;

	protected Logger log = LoggerFactory.getLogger(GSLogProducerService.class
			.getSimpleName());

	public GSWebService() {
	}

	protected void createLogger(Class<?> cl) {
		log = LoggerFactory.getLogger(cl.getSimpleName());
		log.debug("Logger for " + cl.getSimpleName() + " created");
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
