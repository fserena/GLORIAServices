package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ResultEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int idresult;
	private String value;
	private Date date;

	public int getIdresult() {
		return idresult;
	}

	public void setIdresult(int idresult) {
		this.idresult = idresult;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
