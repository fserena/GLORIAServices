package eu.gloria.gs.services.core.security.client;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;

public class ClientPasswordHandler implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		Credentials credentials = ThreadCredentialsStore.getCredentials();

		pc.setIdentifier(credentials.getUsername());
		pc.setPassword(credentials.getPassword());

	}

}