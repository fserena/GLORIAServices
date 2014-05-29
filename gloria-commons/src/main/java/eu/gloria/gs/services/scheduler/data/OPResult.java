/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.data;

import java.util.Date;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public abstract class OPResult {

	private Date date;
	private String uuid;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
