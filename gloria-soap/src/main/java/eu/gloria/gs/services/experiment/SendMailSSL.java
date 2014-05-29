package eu.gloria.gs.services.experiment;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.experiment.base.results.ExperimentResult;
import eu.gloria.gs.services.experiment.script.data.RTScriptInformation;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;

public class SendMailSSL {

	private static Properties hostMailProps;
	private String hostAddress;
	private String hostPassword;
	private String apiAddress;

	static {
		hostMailProps = new Properties();
		hostMailProps.put("mail.smtp.host", "smtp.gmail.com");
		hostMailProps.put("mail.smtp.socketFactory.port", "465");
		hostMailProps.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		hostMailProps.put("mail.smtp.auth", "true");
		hostMailProps.put("mail.smtp.port", "465");
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public String getHostPassword() {
		return hostPassword;
	}

	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}

	private void sendMail(String userEmail, String subject, String content) {
		Session session = Session.getDefaultInstance(hostMailProps,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(hostAddress,
								hostPassword);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(hostAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(userEmail));

			message.setContent(content, "text/html; charset=utf-8");
			message.setSubject(subject);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendNotification(RTScriptInformation script,
			List<ResultInformation> results, List<ImageInformation> images)
			throws Exception {

		String content = "<img src=http://gloria-project.eu/wp-content/uploads/2012/10/banner-transpa-notext-250.png>";

		content += "<h2>Script report</h2>";
		content += "<p>You have received this email because your script for executing the operation '"
				+ script.getOperation()
				+ "' on "
				+ script.getRt()
				+ " has been completed.</p>";
		content += "<div style=\"overflow:hidden\"><h3>Images taken</h3>";
		if (images.size() > 0) {
			int order = 0;
			for (ImageInformation image : images) {
				content += "<h4><strong>Image " + order + "</strong></h4>";
				content += "<div><p>Taken with " + image.getCcd()
						+ " exposing " + image.getExposure()
						+ " seconds and pointing at ";

				ImageTargetData target = image.getTarget();
				if (target.getObject() != null) {
					content += "'" + target.getObject() + "'.";
				} else if (target.getRa() != null) {
					content += "RA: " + target.getRa() + ", ";
					content += "DEC: " + target.getDec() + ".";
				} else {
					content += " nothing.";
				}

				if (image.getFits() != null) {
					content += " <a href=" + image.getFits() + ">FITS</a>";
				}
				content += "</p>";
				content += "<a href=" + image.getJpg()
						+ "><img width=\"300\" align=\"left\" src="
						+ image.getJpg() + "></a></div>";
				order++;
			}
		} else {
			content += "<p>No images taken.</p>";
		}
		content += "</div>";
		content += "<div style=\"overflow:hidden\"><h3>Results</h3>";
		if (results.size() > 0) {
			int order = 0;
			for (ResultInformation result : results) {
				content += "<h4><strong>Result " + order + "</strong></h4>";
				content += "<p>Stored on " + result.getDate() + ".</p>";

				content += "<p>" + result.getValue() + "</p>";
			}
		} else {
			content += "<p>No results.</p>";
		}
		content += "</div><br>";
		content += "<p>Sincerely,<br>GLORIA Team</p>";
		content += "<p><i>Follow us in <a href=https://www.facebook.com/GLORIAProject?fref=ts>Facebook</a><i></p>";

		this.sendMail(script.getUsername(), script.getRt() + " script for '"
				+ script.getOperation() + "' completed", content);
	}
}