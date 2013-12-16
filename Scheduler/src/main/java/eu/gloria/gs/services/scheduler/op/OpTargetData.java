package eu.gloria.gs.services.scheduler.op;

public class OpTargetData {
	
	private String objName;
	private String ra;
	private String dec;
	private String targetType;
	
	public OpTargetData(){
		targetType = "NAME";
	}
	
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public String getRa() {
		return ra;
	}
	public void setRa(String ra) {
		this.ra = ra;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

}
