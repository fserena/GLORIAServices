package eu.gloria.gs.services.scheduler.op;

public class OpMetadata {
	
	private String uuid;
	private String user;
	private String priority;
	private String description;
	
	public OpMetadata(){
//		ResourceBundle rb = ResourceBundle.getBundle("config");
//		
//		priority = rb.getString("metadata.priority.default");
//		this.user = "Anonimous";
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
