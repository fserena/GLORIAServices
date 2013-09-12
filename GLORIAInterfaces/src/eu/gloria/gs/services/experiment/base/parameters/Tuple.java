/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.base.parameters;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
@XmlRootElement
@XmlSeeAlso(Tuple.class)
public class Tuple<X,Y> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 121997144747439260L;
	private X a;
	private Y b;	
	
	public Tuple() {
		
	}

	public Tuple(X a, Y b) {
		this.a = a;
		this.b = b;
	}

	public X getA() {
		return a;
	}

	public void setA(X a) {
		this.a = a;
	}

	public Y getB() {
		return b;
	}

	public void setB(Y b) {
		this.b = b;
	}

}
