package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ReservationEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int idreservation;
	private String user;
	private int experiment;
	private Date begin;
	private Date end;
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getIdreservation() {
		return idreservation;
	}

	public void setIdreservation(int id) {
		this.idreservation = id;
	}
	
	public int getExperiment() {
		return experiment;
	}

	public void setExperiment(int experiment) {
		this.experiment = experiment;
	}
	
	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
