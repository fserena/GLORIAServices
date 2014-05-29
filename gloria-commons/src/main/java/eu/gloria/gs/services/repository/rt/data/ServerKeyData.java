/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.repository.rt.data;

import eu.gloria.gs.services.repository.rt.data.RTCredentials;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ServerKeyData {

	private String url;
	private String port;
	private RTCredentials credentials;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RTCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(RTCredentials credentials) {
		this.credentials = credentials;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
