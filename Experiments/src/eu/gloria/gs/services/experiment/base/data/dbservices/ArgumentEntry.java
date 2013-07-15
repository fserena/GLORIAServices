package eu.gloria.gs.services.experiment.base.data.dbservices;

import java.io.Serializable;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ArgumentEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int operation;
	private int parameter;
	private int number;

	/**
	 * @return
	 */
	public int getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 */
	public void setOperation(int operation) {
		this.operation = operation;
	}

	/**
	 * @return
	 */
	public int getParameter() {
		return parameter;
	}

	/**
	 * @param parameter
	 */
	public void setParameter(int parameter) {
		this.parameter = parameter;
	}

	/**
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}


}
