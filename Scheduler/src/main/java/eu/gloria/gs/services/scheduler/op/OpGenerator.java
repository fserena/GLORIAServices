package eu.gloria.gs.services.scheduler.op;


public class OpGenerator {

	public String toXml(OpForm form) {
		StringBuilder sb = new StringBuilder();

		sb.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
				.append("\n");
		sb.append("<plan xmlns=\"http://gloria.eu/rt/entity/scheduler/plan\">")
				.append("\n");

		generateMetadata(form, sb);
		generateConstraints(form, sb);
		generateInstructions(form, sb);

		sb.append("</plan>").append("\n");

		return sb.toString();
	}

	private void generateMetadata(OpForm form, StringBuilder sb) {

		sb.append("<metadata>").append("\n");
		sb.append("	<uuid>").append(form.getMetadata().getUuid())
				.append("</uuid>").append("\n");
		sb.append("	<user>").append(form.getMetadata().getUser())
				.append("</user>").append("\n");
		sb.append("	<priority>").append(form.getMetadata().getPriority())
				.append("</priority>").append("\n");
		sb.append("	<description>").append(form.getMetadata().getDescription())
				.append("</description>").append("\n");
		sb.append("</metadata>").append("\n");
	}

	private void generateConstraints(OpForm form, StringBuilder sb) {

		sb.append("<constraints>").append("\n");
		sb.append("	<targets>").append("\n");
		sb.append("		<target>").append("\n");
		if (form.getTarget().getTargetType().equals("NAME")) {
			sb.append("		<objName>").append(form.getTarget().getObjName())
					.append("</objName>").append("\n");
		} else {
			sb.append("		<coordinates>").append("\n");
			sb.append("			<J2000>");
			sb.append("				<RA>").append(form.getTarget().getRa())
					.append("</RA>").append("\n");
			sb.append("				<DEC>").append(form.getTarget().getDec())
					.append("</DEC>").append("\n");
			sb.append("			</J2000>").append("\n");
			sb.append("		</coordinates>").append("\n");
		}
		sb.append("		</target>").append("\n");
		sb.append("	</targets>").append("\n");

		sb.append("	<mode>batch</mode>").append("\n");
		sb.append("	<filters>").append("\n");
		for (OpExposeData expose : form.getExposures()) {
			sb.append("		<filter>").append(expose.getSelectedFilter())
					.append("</filter>").append("\n");
		}
		sb.append("	</filters>").append("\n");
		if (!ValidatorTools.isEmpty(form.getMoonDistance()))
			sb.append("	<moonDistance>").append(form.getMoonDistance())
					.append("</moonDistance>").append("\n");
		if (!ValidatorTools.isEmpty(form.getMoonAltitude()))
			sb.append("	<moonAltitude>").append(form.getMoonAltitude())
					.append("</moonAltitude>").append("\n");
		if (!ValidatorTools.isEmpty(form.getMoonAltitude()))
			sb.append("	<targetAltitude>").append(form.getTargetAltitude())
					.append("</targetAltitude>").append("\n");

		sb.append("</constraints>").append("\n");
	}

	private void generateInstructions(OpForm form, StringBuilder sb) {

		sb.append("<instructions>").append("\n");

		// Target
		sb.append("	<target>").append("\n");
		if (form.getTarget().getTargetType().equals("NAME")) {
			sb.append("		<objName>").append(form.getTarget().getObjName())
					.append("</objName>").append("\n");
		} else {
			sb.append("		<coordinates>").append("\n");
			sb.append("			<J2000>").append("\n");
			sb.append("				<RA>").append(form.getTarget().getRa())
					.append("</RA>").append("\n");
			sb.append("				<DEC>").append(form.getTarget().getDec())
					.append("</DEC>").append("\n");
			sb.append("			</J2000>").append("\n");
			sb.append("		</coordinates>").append("\n");
		}
		sb.append("	</target>").append("\n");

		// Camera settings
		if (!form.getCamera().getSelectedBinning().equals("NONE")) {
			sb.append(" <cameraSettings>").append("\n");
			sb.append(" 	<binning>").append("\n");
			String[] tokens = form.getCamera().getSelectedBinning().split("x");
			sb.append("			<binX>").append(tokens[0]).append("</binX>")
					.append("\n");
			sb.append("			<binY>").append(tokens[1]).append("</binY>")
					.append("\n");
			sb.append(" 	</binning>").append("\n");
			sb.append(" </cameraSettings>").append("\n");
		}

		// Exposes
		for (OpExposeData expose : form.getExposures()) {
			sb.append(" <expose>").append("\n");
			sb.append("		<expositionTime>").append(expose.getTime())
					.append("</expositionTime>").append("\n");
			if (expose.isRepetitionCount()) {
				sb.append("		<repeatCount>").append(expose.getRepeat())
						.append("</repeatCount>").append("\n");
			} else {
				sb.append("		<repeatDuration>").append(expose.getRepeat())
						.append("</repeatDuration>").append("\n");
			}
			sb.append("		<filter>").append(expose.getSelectedFilter())
					.append("</filter>").append("\n");
			sb.append(" </expose>").append("\n");
		}

		sb.append("</instructions>").append("\n");
	}

}
