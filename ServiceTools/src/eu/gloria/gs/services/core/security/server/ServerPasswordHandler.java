package eu.gloria.gs.services.core.security.server;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class ServerPasswordHandler implements CallbackHandler {

	@Override
	public void handle(Callback[] arg0) throws IOException,
			UnsupportedCallbackException {

		WSPasswordCallback pc = (WSPasswordCallback) arg0[0];
	
		if (pc.getIdentifier().equals("user")) {
			pc.setPassword("changeit");
		}
	}

}
