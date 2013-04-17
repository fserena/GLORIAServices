package eu.gloria.gs.services.experiment.online.data.dbservices;

import java.io.Serializable;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ExperimentEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private String name;
	private int idexperiment;
	private String description;
	private String author;
	private String type;
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * @return
	 */
	public int getIdexperiment() {
		return idexperiment;
	}

	/**
	 * @param id
	 */
	public void setIdexperiment(int id) {
		this.idexperiment = id;
	}
}
