package eu.gloria.gs.services.log.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import eu.gloria.gs.services.utils.JSONConverter;

@XmlRootElement
@XmlSeeAlso({ Object[].class, LinkedHashMap.class, Action.class, Object.class,
		HashMap.class })
public class ActionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113204093482010938L;
	@XmlElement
	private Action action;
	@XmlElement
	private String json;

	private void putExceptionName() {
		this.action.put("name",
				this.getClass().getSimpleName().replace("Exception", ""));
	}
	
	public ActionException() {
		this.action = new Action();
		this.putExceptionName();
	}

	public ActionException(String message) {
		this.action = new Action();
		this.action.put("message", message);
		this.putExceptionName();
	}

	public ActionException(Action action) {
		this.action = new Action();
		this.action.putAll(action);
		if (action.containsKey("exception")) {
			this.action.put("inner", action.get("exception"));
			this.action.remove("exception");
		}
		
		this.putExceptionName();
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String getMessage() {
		try {
			if (this.action == null || this.action.size() == 0) {
				return super.getMessage();
			} else {
				this.setJSON();
				return this.json;
			}
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
