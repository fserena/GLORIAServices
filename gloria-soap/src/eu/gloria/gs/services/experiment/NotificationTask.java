package eu.gloria.gs.services.experiment;

import java.util.ArrayList;
import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.utils.JSONConverter;

public class NotificationTask extends ScriptTask {

	private SendMailSSL sender;
	private List<ResultInformation> results;
	private ImageRepositoryInterface imageRepository;
	private String username;
	private String password;

	public void setMailSender(SendMailSSL sender) {
		this.sender = sender;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void run() {

		GSClientProvider.setCredentials(this.username, this.password);

		List<ImageInformation> imageInfos = null;

		try {
			imageInfos = imageRepository.getAllReservationImages(script
					.getRid());

		} catch (ImageRepositoryException e) {
			log.error(e.getMessage());
		}

		if (imageInfos == null) {
			imageInfos = new ArrayList<ImageInformation>();
		}

		try {
			sender.sendNotification(script, results, imageInfos);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setResults(List<ResultInformation> results) {
		this.results = results;
	}

	public void setImageRepository(ImageRepositoryInterface image) {
		this.imageRepository = image;
	}

}
