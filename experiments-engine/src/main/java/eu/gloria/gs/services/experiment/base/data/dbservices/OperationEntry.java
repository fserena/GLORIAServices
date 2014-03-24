package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.io.Serializable;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class OperationEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int experiment;
	private String operation;
	private String type;
	private int idoperation;

	/**
	 * @return
	 */
	public int getExperiment() {
		return experiment;
	}

	/**
	 * @param experiment
	 */
	public void setExperiment(int experiment) {
		this.experiment = experiment;
	}

	/**
	 * @return
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}	

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public int getIdoperation() {
		return idoperation;
	}

	/**
	 * @param idoperation
	 */
	public void setIdoperation(int idoperation) {
		this.idoperation = idoperation;
	}

}
