package eu.gloria.gs.services.repository.user;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

import eu.gloria.gs.services.core.security.server.ServerPasswordHandler;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.repository.user.data.UserRepositoryAdapter;

public class UserRepositoryServerPasswordHandler extends ServerPasswordHandler {
	private UserRepositoryAdapter adapter;

	@Override
	public void handle(Callback[] arg0) throws IOException,
			UnsupportedCallbackException {

		WSPasswordCallback pc = (WSPasswordCallback) arg0[0];
		String user = pc.getIdentifier();

		try {

			if (!adapter.containsName(user)) {				
			} else {
				if (adapter.isAdministrator(user)) {
					pc.setPassword(adapter.getPassword(user));
				}
			}
		} catch (ActionException e) {
		}
	}

	public void setAdapter(UserRepositoryAdapter adapter) {
		this.adapter = adapter;
	}
}
