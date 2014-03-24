package eu.gloria.gs.services.repository.user;

import java.util.Map;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;

public class UserRepositoryServerInterceptor extends WSS4JInInterceptor {
	public UserRepositoryServerInterceptor(Map<String, Object> properties) {
		super(properties);

		/*
		 * Map<String, Object> inProps = new HashMap<String, Object>();
		 * 
		 * inProps.put(WSHandlerConstants.ACTION,
		 * WSHandlerConstants.USERNAME_TOKEN);
		 * inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		 * inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
		 * UserRepositoryServerPasswordHandler.class.getName());
		 * this.setProperties(inProps);
		 */
	}
}
