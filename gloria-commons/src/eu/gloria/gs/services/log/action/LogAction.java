package eu.gloria.gs.services.log.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({ Object[].class, LinkedHashMap.class, HashMap.class,
		LinkedHashMap.class })
public class LogAction extends LinkedHashMap<String, Object> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113204093482010938L;

}
