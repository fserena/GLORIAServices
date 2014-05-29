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
	private Integer parameter;
	private Integer pointer;
	private int number;
	private String subarg;

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
	public Integer getParameter() {
		return parameter;
	}

	/**
	 * @param parameter
	 */
	public void setParameter(Integer parameter) {
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

	public String getSubarg() {
		return subarg;
	}

	public void setSubarg(String subarg) {
		this.subarg = subarg;
	}

	public Integer getPointer() {
		return pointer;
	}

	public void setPointer(Integer pointer) {
		this.pointer = pointer;
	}

}
