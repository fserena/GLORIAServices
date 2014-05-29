package eu.gloria.gs.services.core.security.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

public class WSServerInterceptor extends WSS4JInInterceptor {
	
	public WSServerInterceptor()
	{				
		Map<String, Object> inProps = new HashMap<String, Object>();
		
		inProps.put(WSHandlerConstants.ACTION,
				WSHandlerConstants.USERNAME_TOKEN);
		inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
				ServerPasswordHandler.class.getName());
		
		this.setProperties(inProps);
	}
}
