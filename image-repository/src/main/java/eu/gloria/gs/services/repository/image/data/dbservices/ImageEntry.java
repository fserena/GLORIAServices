package eu.gloria.gs.services.repository.image.data.dbservices;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ImageEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int idimage;
	private String user;
	private Date date;
	private Integer jpg_gen;
	private Integer fits_gen;
	private String host;
	private int rid;
	private String local_id;
	private String rt;
	private String ccd;
	private String target;
	private double exposure;
	
	public Integer getJpg_gen() {
		return jpg_gen;
	}

	public void setJpg_gen(Integer jpg_gen) {
		this.jpg_gen = jpg_gen;
	}

	public Integer getFits_gen() {
		return fits_gen;
	}

	public void setFits_gen(Integer fits_gen) {
		this.fits_gen = fits_gen;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getExposure() {
		return exposure;
	}

	public void setExposure(double exposure) {
		this.exposure = exposure;
	}

	public int getIdimage() {
		return idimage;
	}

	public void setIdimage(int idimage) {
		this.idimage = idimage;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getLocal_id() {
		return local_id;
	}

	public void setLocal_id(String local_id) {
		this.local_id = local_id;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getCcd() {
		return ccd;
	}

	public void setCcd(String ccd) {
		this.ccd = ccd;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
