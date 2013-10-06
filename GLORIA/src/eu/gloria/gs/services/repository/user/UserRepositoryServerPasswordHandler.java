package eu.gloria.gs.services.repository.user;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

import eu.gloria.gs.services.core.security.server.ServerPasswordHandler;
import eu.gloria.gs.services.repository.user.data.UserRepositoryAdapter;
import eu.gloria.gs.services.repository.user.data.dbservices.UserRepositoryAdapterException;

public class UserRepositoryServerPasswordHandler extends ServerPasswordHandler {
	private UserRepositoryAdapter adapter;

	@Override
	public void handle(Callback[] arg0) throws IOException,
			UnsupportedCallbackException {

		WSPasswordCallback pc = (WSPasswordCallback) arg0[0];
		String user = pc.getIdentifier();

		try {

			if (!adapter.contains(user)) {
				System.out.println("User " + pc.getIdentifier()
						+ " does not exist");
			} else {
				if (adapter.isAdministrator(user)) {
					pc.setPassword(adapter.getPassword(user));
				} else {
					System.out.println("User role not allowed for " + user);
				}
			}
		} catch (UserRepositoryAdapterException e) {
			e.printStackTrace();
		}
	}

	public void setAdapter(UserRepositoryAdapter adapter) {
		this.adapter = adapter;
	}
}
