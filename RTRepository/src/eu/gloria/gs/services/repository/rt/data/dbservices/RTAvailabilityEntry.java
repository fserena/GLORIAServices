package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.io.Serializable;
import java.util.Date;

public class RTAvailabilityEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date starting_avl;
	private Date ending_avl;

	public Date getStarting_avl() {
		return this.starting_avl;
	}

	public void setStarting_avl(Date starting) {
		this.starting_avl = starting;
	}

	public Date getEnding_avl() {
		return this.ending_avl;
	}

	public void setEnding_avl(Date ending) {
		this.ending_avl = ending;
	}

}
