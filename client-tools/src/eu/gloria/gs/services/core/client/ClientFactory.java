package eu.gloria.gs.services.core.client;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import eu.gloria.gs.services.core.security.client.WSClientInterceptor;

public class ClientFactory {

	protected String portName;
	protected Class<?> serviceClass;

	public ClientFactory() {
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void setServiceClass(Class<?> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public Object create() {

		ClientProxyFactory factory = new ClientProxyFactory();
		factory.setAddress("https://" + GSClientProvider.getHost() + ":"
				+ GSClientProvider.getPort() + "/GLORIA/services/" + portName);
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

}
