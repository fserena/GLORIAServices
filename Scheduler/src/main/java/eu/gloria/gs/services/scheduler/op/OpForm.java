package eu.gloria.gs.services.scheduler.op;

import java.util.ArrayList;
import java.util.List;

public class OpForm {
	
	private String moonDistance;
	private String moonAltitude;
	private String targetAltitude;
	
	private String user;
	
	private OpTargetData target;
	private List<OpExposeData> exposures;
	private OpCameraData camera;
	private OpMetadata metadata;
	
	private String cmd;
	private String cmdParams;
	
	public OpForm(){
		
		target = new OpTargetData();
		exposures =  new ArrayList<OpExposeData>();
		//exposures.add(new OpExposeData());
		//exposures.add(new OpExposeData());
		
		camera = new OpCameraData();
		metadata = new OpMetadata();
		
	}
	
	public String toString(){
		String result = "OpForm::[moonDistance=" + moonDistance + ", moonAltitude=" + moonAltitude + ", targetAltitude=" + targetAltitude + ", ";
		for (OpExposeData expose : exposures) {
			result = result + expose.toString();
		}
		result = result + "]";
		return result;
	}
	
	public void addExposure(){
		exposures.add(new OpExposeData());
	}
	
	public void removeSelectedExposures(){
		
		List<OpExposeData> tmp = new ArrayList<OpExposeData>();
		
		for (OpExposeData expose : exposures) {
			if (!expose.isSelected()){
				tmp.add(expose);
			}
		}
		
		this.exposures = tmp;
		
	}
	
	
	
	
	public String getMoonDistance() {
		return moonDistance;
	}
	public void setMoonDistance(String moonDistance) {
		this.moonDistance = moonDistance;
	}
	public String getMoonAltitude() {
		return moonAltitude;
	}
	public void setMoonAltitude(String moonAltitude) {
		this.moonAltitude = moonAltitude;
	}
	public String getTargetAltitude() {
		return targetAltitude;
	}
	public void setTargetAltitude(String targetAltitude) {
		this.targetAltitude = targetAltitude;
	}
	public OpTargetData getTarget() {
		return target;
	}
	public void setTarget(OpTargetData target) {
		this.target = target;
	}
	public List<OpExposeData> getExposures() {
		return exposures;
	}
	public void setExposures(List<OpExposeData> exposures) {
		this.exposures = exposures;
	}


	public String getCmd() {
		return cmd;
	}


	public void setCmd(String cmd) {
		this.cmd = cmd;
	}


	public String getCmdParams() {
		return cmdParams;
	}


	public void setCmd_params(String cmdParams) {
		this.cmdParams = cmdParams;
	}

	public OpCameraData getCamera() {
		return camera;
	}

	public void setCamera(OpCameraData camera) {
		this.camera = camera;
	}

	public void setCmdParams(String cmdParams) {
		this.cmdParams = cmdParams;
	}

	public OpMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(OpMetadata metadata) {
		this.metadata = metadata;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
