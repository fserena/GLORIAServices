package eu.gloria.gs.services.scheduler.op;

import java.util.ArrayList;
import java.util.List;

import eu.gloria.rti_scheduler.tools.RTISchProxyConnection;

public class TelescopeInfo {
	
	private String ip = null;
	private String port = null;
	private String webAppname = null;
	private String user = null;
	private String password = null;
	private boolean httpsEnable;
	private String id = null;
	
	private String ipDB = null;
	private String portDB = null;
	private String webAppnameDB = null;
	private String userDB = null;
	private String passwordDB = null;
	private boolean httpsEnableDB;
	
	private ArrayList<String> binning = new ArrayList<String>();
	private ArrayList<String> filters = new ArrayList<String>();
	
	public ArrayList<String> getBinning() {
		return binning;
	}


	public ArrayList<String> getFilters() {
		return filters;
	}

	public void setFilters(ArrayList<String> filters) {
		this.filters = filters;
	}

	public String getIpDB() {
		return ipDB;
	}

	public void setIpDB(String ipDB) {
		this.ipDB = ipDB;
	}

	public String getPortDB() {
		return portDB;
	}

	public void setPortDB(String portDB) {
		this.portDB = portDB;
	}

	public String getWebAppnameDB() {
		return webAppnameDB;
	}

	public void setWebAppnameDB(String webAppnameDB) {
		this.webAppnameDB = webAppnameDB;
	}

	public String getUserDB() {
		return userDB;
	}

	public void setUserDB(String userDB) {
		this.userDB = userDB;
	}

	public String getPasswordDB() {
		return passwordDB;
	}

	public void setPasswordDB(String passwordDB) {
		this.passwordDB = passwordDB;
	}

	public boolean isHttpsEnableDB() {
		return httpsEnableDB;
	}

	public void setHttpsEnableDB(boolean httpsEnableDB) {
		this.httpsEnableDB = httpsEnableDB;
	}
	
	public boolean getHttpsEnable(){
		return httpsEnable;
	}
	
	public boolean getHttpsEnableDB(){
		return httpsEnableDB;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	private final String reposittory = "c:\\repositorio\\workspace\\resources\\certificates\\dev\\cacerts_gloria_ca_dev";
	
	
	public static List<TelescopeInfo> getTelescopeInfo(){
		ArrayList<TelescopeInfo> telescopesInfo = new ArrayList<TelescopeInfo>();
		
		TelescopeInfo cerroTololo = new TelescopeInfo();
		
		cerroTololo.setHttpsEnable(false);
		cerroTololo.setIp("139.229.12.76");
		cerroTololo.setPassword("12345");
		cerroTololo.setUser("gloria_user");
		cerroTololo.setPort("8080");
		cerroTololo.setWebAppname("RTISch");
		cerroTololo.setId("Cerro Tololo");
		
		cerroTololo.setHttpsEnable(false);
		cerroTololo.setIpDB("139.229.12.76");
		cerroTololo.setPasswordDB("12345");
		cerroTololo.setUserDB("gloria_user");
		cerroTololo.setPortDB("8080");
		cerroTololo.setWebAppnameDB("RTIDB");
		
		cerroTololo.filters.add("OPEN");
		cerroTololo.filters.add("BESSEL_V");
		cerroTololo.filters.add("SLOAN_U");
		cerroTololo.filters.add("SLOAN_G");
		cerroTololo.filters.add("SLOAN_I");
		cerroTololo.filters.add("SLOAN_R");
		cerroTololo.filters.add("SLOAN_Z");
		
		cerroTololo.binning.add("1x1");
		
		telescopesInfo.add(cerroTololo);
		
		TelescopeInfo tau = new TelescopeInfo();
		tau.setHttpsEnable(false);
		tau.setIp("88.198.125.131");
		tau.setPassword("1q2w3e4r");
		tau.setUser("gloria_user");
		tau.setPort("8080");
		tau.setWebAppname("RTISch");
		tau.setId("TAU");
		
		tau.filters.add("OPEN");
		tau.filters.add("BESSEL_V");
		tau.filters.add("BESSEL_B");
		tau.filters.add("BESSEL_R");
		
		tau.binning.add("1x1");
		tau.binning.add("2x2");
		tau.binning.add("3x3");
		tau.binning.add("4x4");
		
		tau.setHttpsEnable(false);
		tau.setIpDB("88.198.125.131");
		tau.setPasswordDB("1q2w3e4r");
		tau.setUserDB("gloria_user");
		tau.setPortDB("8080");
		tau.setWebAppnameDB("RTIDB");

		
		
		telescopesInfo.add(tau);
		
		TelescopeInfo bootes2 = new TelescopeInfo();
		
		bootes2.setHttpsEnable(false);
		bootes2.setIp("161.111.233.66");
		bootes2.setPassword("1234");
		bootes2.setUser("gloriauser");
		bootes2.setPort("8080");
		bootes2.setWebAppname("RTISch");
		bootes2.setId("Bootes 2");
		
		bootes2.setHttpsEnable(false);
		bootes2.setIpDB("161.111.233.66");
		bootes2.setPasswordDB("1234");
		bootes2.setUserDB("gloriauser");
		bootes2.setPortDB("8080");
		bootes2.setWebAppnameDB("RTIDB");
		
		bootes2.filters.add("OPEN");
		bootes2.filters.add("BESSEL_V");
		bootes2.filters.add("SLOAN_U");
		bootes2.filters.add("SLOAN_G");
		bootes2.filters.add("SLOAN_I");
		bootes2.filters.add("SLOAN_R");
		bootes2.filters.add("SLOAN_Z");
		
		bootes2.binning.add("1x1");
		
		telescopesInfo.add(bootes2);
		

		
		return telescopesInfo;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getWebAppname() {
		return webAppname;
	}
	public void setWebAppname(String webAppname) {
		this.webAppname = webAppname;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isHttpsEnable() {
		return httpsEnable;
	}
	public void setHttpsEnable(boolean httpsEnable) {
		this.httpsEnable = httpsEnable;
	}
	public String getReposittory() {
		return reposittory;
	} 


}
