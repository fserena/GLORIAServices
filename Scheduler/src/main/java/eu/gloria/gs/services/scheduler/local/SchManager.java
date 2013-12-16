package eu.gloria.gs.services.scheduler.local;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import eu.gloria.rti_db.GloriaRtiDb;
import eu.gloria.rti_scheduler.GloriaRtiScheduler;

public class SchManager {
	
//	public GloriaRti getRTIProxy() throws Exception{
//		
//		ProxyFactory proxyFactory = new ProxyFactory();
//		
//		URL urlWsdl = new URL("http://localhost:8080/RTI/gloria_rti.wsdl"); //may be file url--> file:\\c:\\tmp\\gloria_rti.wsdl
//		URL urlWs = new URL("http://localhost:8080/RTI/services/gloria_rtiSOAP?wsdl");
//		
//		boolean sessionMantenance = true;
//		String httpHeaderUser = "gloria_user"; 
//		String httpHeaderPw = "12345";
//		
//		proxyFactory.setSSLCacertFile("c:\\repositorio\\workspace\\resources\\certificates\\dev\\cacerts_gloria_ca_dev");
//		proxyFactory.setSSLCacertFilePw("changeit");
//		
//		GloriaRti proxy = proxyFactory.getProxy(urlWsdl, urlWs, sessionMantenance, httpHeaderUser, httpHeaderPw);
//		
//		return proxy;
//	}
	
	//RTISchProxyConnection rts = new RTISchProxyConnection("139.229.12.76", "8080", "RTISch", "gloria_user", "12345", false, "c:\\repositorio\\workspace\\resources\\certificates\\dev\\cacerts_gloria_ca_dev");
	
	public GloriaRtiScheduler getRTISchProxy() throws Exception{
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		
		eu.gloria.rti_scheduler.factory.ProxyFactory proxyFactory = new eu.gloria.rti_scheduler.factory.ProxyFactory();
		
		URL urlWsdl = new URL(rb.getString("rtisch_url_wsdl")); //may be file url--> file:\\c:\\tmp\\gloria_rti.wsdl
		URL urlWs = new URL(rb.getString("rtisch_url_webservice"));
		
		boolean sessionMantenance = true;
		String httpHeaderUser = rb.getString("rtisch_user"); 
		String httpHeaderPw = rb.getString("rtisch_pw");
		
		proxyFactory.setSSLCacertFile(rb.getString("rtisch_https_cacert"));
		proxyFactory.setSSLCacertFilePw(rb.getString("rtisch_https_cacert_pw"));
		
		GloriaRtiScheduler proxy = proxyFactory.getProxy(urlWsdl, urlWs, sessionMantenance, httpHeaderUser, httpHeaderPw);
		
		return proxy;
	}
	
	public GloriaRtiDb getRTIDbProxy() throws Exception{
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		
		eu.gloria.rti_db.factory.ProxyFactory proxyFactory = new eu.gloria.rti_db.factory.ProxyFactory();
		
		URL urlWsdl = new URL(rb.getString("rtidb_url_wsdl")); //may be file url--> file:\\c:\\tmp\\gloria_rti.wsdl
		URL urlWs = new URL(rb.getString("rtidb_url_webservice"));
		
		boolean sessionMantenance = true;
		String httpHeaderUser = rb.getString("rtidb_user"); 
		String httpHeaderPw = rb.getString("rtidb_pw");
		
		proxyFactory.setSSLCacertFile(rb.getString("rtidb_https_cacert"));
		proxyFactory.setSSLCacertFilePw(rb.getString("rtidb_https_cacert_pw"));
		
		GloriaRtiDb proxy = proxyFactory.getProxy(urlWsdl, urlWs, sessionMantenance, httpHeaderUser, httpHeaderPw);
		
		return proxy;
	}
	
	public List<String> getFilterList(){
		
		List<String> result = new ArrayList<String>();
		
		try{
			//ResourceBundle rb = ResourceBundle.getBundle("config");
			//String filters = rb.getString("device.fw.filter_list");
			 String filters ="OPEN;BESSEL_V;BESSEL_B;SLOAN_U;SLOAN_G;SLOAN_R;SLOAN_I;SLOAN_Z";
			String[] tokens = filters.split(";");
			if (tokens != null){
				for (String string : tokens) {
					result.add(string);
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}
	
	public List<String> getBinningList(){
		
		List<String> result = new ArrayList<String>();
		
		try{
			//ResourceBundle rb = ResourceBundle.getBundle("config");
			//String filters = rb.getString("device.camera.binning.values");
			String filters = "1x1";
			String[] tokens = filters.split(";");
			if (tokens != null){
				for (String string : tokens) {
					result.add(string);
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}

}
