package eu.gloria.gs.services.core.client;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.gloria.gs.services.core.security.client.WSClientInterceptor;

public class ClientFactory {

	protected String portName;
	protected Class<?> serviceClass;
	private static Logger log = LoggerFactory.getLogger(ClientFactory.class
			.getSimpleName());

	public ClientFactory() {
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void setServiceClass(Class<?> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public Object create() {

		try {
			ClientProxyFactory factory = new ClientProxyFactory();
			factory.setAddress("https://" + GSClientProvider.getHost() + ":"
					+ GSClientProvider.getPort() + "/gloria-soap/services/"
					+ portName);
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

			log.info("Client created: " + factory.getAddress());

			return service;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

}
