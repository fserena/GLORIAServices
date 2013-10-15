/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.op;

import java.util.ArrayList;
import java.util.Date;

import eu.gloria.gs.services.scheduler.data.ObservingPlanInformation;
import eu.gloria.gs.services.scheduler.local.GenericSchException;
import eu.gloria.gs.services.scheduler.local.SchHandler;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ObservingPlan {

	private ObservingPlanInformation opInfo;
	private SchHandler handler;
	private String XmlOp;

	public ObservingPlan() {

	}

	public ObservingPlanInformation getOpInfo() {
		return opInfo;
	}

	public void setOpInfo(ObservingPlanInformation opInfo) {
		this.opInfo = opInfo;
	}

	public void build() throws GenericSchException {
		OpMetadata metadata = new OpMetadata();

		// if (this.opInfo.getUuid() == null) {
		this.opInfo.setUuid(handler.requestUuid());
		// }

		metadata.setUuid(this.opInfo.getUuid());
		this.opInfo.setUuid(metadata.getUuid());
		metadata.setPriority(String.valueOf(this.opInfo.getPriority()));
		metadata.setDescription(this.opInfo.getDescription());
		metadata.setUser(this.opInfo.getUser());

		OpForm form = new OpForm();

		form.setMetadata(metadata);
		form.setMoonDistance(String.valueOf(this.opInfo.getMoonDistance()));
		form.setMoonAltitude(String.valueOf(this.opInfo.getMoonAltitude()));
		form.setTargetAltitude(String.valueOf(this.opInfo.getTargetAltitude()));

		OpTargetData targetData = new OpTargetData();

		if (this.opInfo.getObject() != null) {
			targetData.setTargetType("NAME");
			targetData.setObjName(this.opInfo.getObject());
		} else {
			targetData.setTargetType("RADEC");
			targetData.setRa(String.valueOf(this.opInfo.getRa()));
			targetData.setDec(String.valueOf(this.opInfo.getDec()));
		}

		form.setTarget(targetData);

		OpCameraData cameraData = new OpCameraData();

		cameraData.setSelectedBinning("1x1");

		form.setCamera(cameraData);

		OpExposeData exposeData = new OpExposeData();
		exposeData.setRepetitionCount(true);
		exposeData.setTime(String.valueOf(this.opInfo.getExposure()));
		exposeData.setRepeat("1");
		exposeData.setSelectedFilter(this.opInfo.getFilter());

		form.getExposures().add(exposeData);

		OpGenerator generator = new OpGenerator();

		this.setXmlOp(generator.toXml(form));
	}

	public String getXmlOp() {
		return XmlOp;
	}

	private void setXmlOp(String xmlOp) {
		XmlOp = xmlOp;
	}

	public SchHandler getHandler() {
		return handler;
	}

	public void setHandler(SchHandler handler) {
		this.handler = handler;
	}

	public void advertise() throws GenericSchException {

		ArrayList<String> input = new ArrayList<String>();
		input.add(this.getXmlOp());

		handler.advertise(new Date(), input);

	}
}
