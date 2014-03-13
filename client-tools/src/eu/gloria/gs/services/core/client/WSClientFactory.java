package eu.gloria.gs.services.core.client;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

import eu.gloria.gs.services.core.client.ClientFactory;
import eu.gloria.gs.services.core.client.ClientProxyFactory;
import eu.gloria.gs.services.core.security.client.WSClientInterceptor;

public class WSClientFactory extends ClientFactory {

	private String host;
	private String port;

	public Object create() {

		ClientProxyFactory factory = new ClientProxyFactory();
		factory.setAddress("https://" + this.host + ":" + this.port
				+ "/GLORIA/services/" + portName);
		factory.setServiceClass(serviceClass);

		Object service = factory.create();

		Client proxy = ClientProxy.getClient(service);

		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();

		TLSClientParameters tls = conduit.getTlsClientParameters();
		if (tls == null)
			tls = new TLSClientParameters();
		tls.setDisableCNCheck(true);
		tls.setUseHttpsURLConnectionDefaultHostnameVerifier(false);
		conduit.setTlsClientParameters(tls);

		Endpoint cxfEndpoint = proxy.getEndpoint();
		WSClientInterceptor interceptor = new WSClientInterceptor();
		cxfEndpoint.getOutInterceptors().add(interceptor);

		return service;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
