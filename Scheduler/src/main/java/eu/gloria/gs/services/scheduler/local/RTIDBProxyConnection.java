package eu.gloria.gs.services.scheduler.local;

import java.net.MalformedURLException;
import java.net.URL;

import eu.gloria.rti_db.GloriaRtiDb;
import eu.gloria.rti_db.factory.ProxyFactory;

public class RTIDBProxyConnection {

	private String rtsName = null;

	private URL urlWsdl;
	private URL urlWs;

	private boolean sessionMantenance;
	private String httpHeaderUser;

	private String httpHeaderPw;

	private GloriaRtiDb proxy;

	public RTIDBProxyConnection(String ip, String port, String wsName,
			String user, String pass, String cacertFile) {

		ProxyFactory proxyFactory = new ProxyFactory();

		try {
			// urlWsdl = new URL("https://" + ip+":"+port
			// +"/RTI/gloria_rti.wsdl"); //may be file url-->
			// file:\\c:\\tmp\\gloria_rti.wsdl
			// urlWs = new URL("https://" + ip+":"+port +
			// "/RTI/services/gloria_rtiSOAP?wsdl");

			urlWsdl = new URL("https://" + ip + ":" + port + "/" + wsName
					+ "/gloria_rti.wsdl"); // may be file url-->
											// file:\\c:\\tmp\\gloria_rti.wsdl
			urlWs = new URL("https://" + ip + ":" + port + "/" + wsName
					+ "/services/gloria_rtiSOAP?wsdl");

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sessionMantenance = true;

		httpHeaderUser = user;
		httpHeaderPw = pass;

		// proxyFactory.setSSLCacertFile("c:\\repositorio\\workspace\\resources\\certificates\\dev\\cacerts_gloria_ca_dev");
		// proxyFactory.setSSLCacertFile(cacertFile);
		// proxyFactory.setSSLCacertFilePw("changeit");

		try {
			proxy = proxyFactory.getProxy(urlWsdl, urlWs, sessionMantenance,
					httpHeaderUser, httpHeaderPw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RTIDBProxyConnection(String ip, String port, String wsName,
			String user, String pass, boolean secureProtocol, String cacertFile) {

		ProxyFactory proxyFactory = new ProxyFactory();

		try {
			// urlWsdl = new URL("https://" + ip+":"+port
			// +"/RTI/gloria_rti.wsdl"); //may be file url-->
			// file:\\c:\\tmp\\gloria_rti.wsdl
			// urlWs = new URL("https://" + ip+":"+port +
			// "/RTI/services/gloria_rtiSOAP?wsdl");

			String protocol = "http";
			if (secureProtocol) {
				protocol = "https";
			}

			urlWsdl = new URL(protocol + "://" + ip + ":" + port + "/" + wsName
					+ "/gloria_rti_db.wsdl"); // may be file url-->
												// file:\\c:\\tmp\\gloria_rti.wsdl
			urlWs = new URL(protocol + "://" + ip + ":" + port + "/" + wsName
					+ "/services/gloria_rti_dbSOAP?wsdl");

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sessionMantenance = true;

		httpHeaderUser = user;
		httpHeaderPw = pass;

		// proxyFactory.setSSLCacertFile("c:\\repositorio\\workspace\\resources\\certificates\\dev\\cacerts_gloria_ca_dev");
		// proxyFactory.setSSLCacertFile(cacertFile);
		// proxyFactory.setSSLCacertFilePw("changeit");

		try {
			proxy = proxyFactory.getProxy(urlWsdl, urlWs, sessionMantenance,
					httpHeaderUser, httpHeaderPw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GloriaRtiDb getProxy() {
		return proxy;
	}

	public String getRtsName() {
		return rtsName;
	}

	public void setRtsName(String rtsName) {
		this.rtsName = rtsName;
	}

	public String toString() {

		return getRtsName();
	}

}
