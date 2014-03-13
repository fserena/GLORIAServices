/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.data;


/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ObservingPlanInformation {

	private String user;
	private String uuid;
	private String description;
	private Double moonAltitude;
	private Double moonDistance;
	private Double targetAltitude;
	private Integer priority;
	private String object;
	private Double ra;
	private Double dec;
	private String filter;
	private Double exposure;
	
	public ObservingPlanInformation() {

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMoonAltitude() {
		return moonAltitude;
	}

	public void setMoonAltitude(Double moonAltitude) {
		this.moonAltitude = moonAltitude;
	}

	public Double getMoonDistance() {
		return moonDistance;
	}

	public void setMoonDistance(Double moonDistance) {
		this.moonDistance = moonDistance;
	}

	public Double getTargetAltitude() {
		return targetAltitude;
	}

	public void setTargetAltitude(Double targetAltitude) {
		this.targetAltitude = targetAltitude;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Double getRa() {
		return ra;
	}

	public void setRa(Double ra) {
		this.ra = ra;
	}

	public Double getDec() {
		return dec;
	}

	public void setDec(Double dec) {
		this.dec = dec;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Double getExposure() {
		return exposure;
	}

	public void setExposure(Double exposure) {
		this.exposure = exposure;
	}	
}
