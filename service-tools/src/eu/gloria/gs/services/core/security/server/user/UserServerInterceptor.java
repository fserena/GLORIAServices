package eu.gloria.gs.services.core.security.server.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.phase.Phase;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

import eu.gloria.gs.services.core.security.server.WSServerInterceptor;

public class UserServerInterceptor extends WSServerInterceptor {

	public void setPasswordHandler(UserServerPasswordHandler handler) {

		Map<String, Object> inProps = new HashMap<String, Object>();

		inProps.put(WSHandlerConstants.ACTION,
				WSHandlerConstants.USERNAME_TOKEN);
		inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);

		inProps.put(WSHandlerConstants.PW_CALLBACK_REF, handler);

		this.setProperties(inProps);
	}

	public UserServerInterceptor() {
		this.setPhase(Phase.PRE_INVOKE);
	}
}
