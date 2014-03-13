package eu.gloria.gs.services.core.security.server.user;

import java.io.IOException;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.security.server.ServerPasswordHandler;
import eu.gloria.gs.services.repository.user.UserRepositoryException;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRole;

public class UserServerPasswordHandler extends ServerPasswordHandler {

	private UserRepositoryInterface repository;
	private String username;
	private String password;
	private List<UserRole> grantedRoles;

	public void setGrantedRoles(List<UserRole> roles) {
		this.grantedRoles = roles;
	}

	public void setUserRepository(UserRepositoryInterface repository) {
		this.repository = repository;
	}

	@Override
	public void handle(Callback[] arg0) throws IOException,
			UnsupportedCallbackException {

		WSPasswordCallback pc = (WSPasswordCallback) arg0[0];

		GSClientProvider.setCredentials(this.username, this.password);
		try {

			UserInformation userInfo = null;

			userInfo = repository.getUserCredentials(pc.getIdentifier());

			if (userInfo != null) {

				if (this.grantUser(userInfo)) {
					String password = userInfo.getPassword();
					pc.setPassword(password);
				}

			}
		} catch (UserRepositoryException e) {
		}
	}

	public boolean grantUser(UserInformation info) {

		if (grantedRoles == null || grantedRoles.size() == 0)
			return true;

		UserRole[] userRoles = info.getRoles();

		for (UserRole role : userRoles) {
			return grantedRoles.contains(role);
		}

		return false;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
