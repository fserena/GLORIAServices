package eu.gloria.gs.services.log.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import eu.gloria.gs.services.utils.JSONConverter;

@XmlRootElement
@XmlSeeAlso({ Object[].class, LinkedHashMap.class, LogAction.class, Object.class,
		HashMap.class })
public class ActionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113204093482010938L;
	@XmlElement
	private LogAction action;
	private String json;

	public ActionException(LogAction action) {
		this.setAction(action);
	}

	/**
	 * @return the action
	 */
	public LogAction getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(LogAction action) {
		this.action = action;
	}

	@Override
	public String getMessage() {
		try {
			if (this.action.size() == 0) {
				this.json =  super.getMessage();
			}
			this.setJSON();
			return this.json;
		} catch (IOException e) {
			return null;
		}
	}

	public void setJSON() throws IOException {
		this.json = JSONConverter.toJSON(action);
	}
	
	public String getJSON() {
		return this.json;
	}
}
