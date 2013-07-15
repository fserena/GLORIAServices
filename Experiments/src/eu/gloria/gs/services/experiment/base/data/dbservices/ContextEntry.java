package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.io.Serializable;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ContextEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int pid;
	private int rid;
	private Object value;

	/**
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * @param pid
	 */
	public void setPid(int pid) {
		this.pid = pid;
	}

	/**
	 * @return
	 */
	public int getRid() {
		return rid;
	}

	/**
	 * @param rid
	 */
	public void setRid(int rid) {
		this.rid = rid;
	}

	
}
