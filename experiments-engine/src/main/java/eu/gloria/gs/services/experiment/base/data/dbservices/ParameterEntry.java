package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.io.Serializable;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ParameterEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int idparameter;
	private int experiment;
	private String parameter;
	private String type;

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
	public String getParameter() {
		return parameter;
	}

	/**
	 * @param parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
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
	public int getIdparameter() {
		return idparameter;
	}

	/**
	 * @param idparameter
	 */
	public void setIdparameter(int idparameter) {
		this.idparameter = idparameter;
	}



}
