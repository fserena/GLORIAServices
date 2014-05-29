/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.utils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
@XmlRootElement
@XmlSeeAlso({ Object[].class, LinkedHashMap.class })
public class ObjectResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1468526250649255606L;
	@XmlElement
	public Object content;

	public ObjectResponse() {

	}

	public ObjectResponse(Object obj) {
		if (obj instanceof List) {
			this.content = ((List<?>)obj).toArray(new Object[0]);
		} else {
			this.content = obj;
		}
	}

	/*
	 * public Object getContent() { return this.content; }
	 * 
	 * public void setContent(Object obj) { this.content = obj; }
	 */
}
