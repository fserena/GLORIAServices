package eu.gloria.gs.services.repository.image;

import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageRepositoryAdapter;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageRepositoryAdapterException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;

public class ImageURLRetrieveExecutor extends ServerThread {

	private ImageRepositoryAdapter adapter;
	private ActionLogInterface alog;
	private CCDTeleoperationInterface ccd;
	private String username;
	private String password;
	private boolean thereArePending;

	public void setAdapter(ImageRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	public void setActionLog(ActionLogInterface alog) {
		this.alog = alog;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCCDTeleoperation(CCDTeleoperationInterface ccd) {
		this.ccd = ccd;
	}

	@Override
	protected void doWork() {

		try {
			if (thereArePending) {
				Thread.sleep(100);
				System.out.println("Image daemon alive...images pending!");
			} else {
				Thread.sleep(1000);
				System.out.println("Image daemon alive...no images pending!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GSClientProvider.setCredentials(this.username, this.password);

		List<ImageInformation> notUrlCompleted = null;

		try {
			notUrlCompleted = this.adapter.getAllWithoutUrl();

			if (notUrlCompleted.size() > 0) {
				try {
					alog.registerAction(username, new Date(), "There are "
							+ notUrlCompleted.size() + " image entries to fill");
				} catch (ActionLogException e) {
					System.out.println(e.getMessage());
				}
			}

		} catch (ImageRepositoryAdapterException e) {
			try {
				alog.registerAction(username, new Date(), e.getMessage());
			} catch (ActionLogException ei) {
				System.out.println(ei.getMessage());
			}
		}

		thereArePending = false;

		for (ImageInformation imageInfo : notUrlCompleted) {

			String url = null;

			try {
				url = ccd.getImageURL(imageInfo.getRt(), imageInfo.getCcd(),
						imageInfo.getLocalid(), ImageExtensionFormat.JPG);

				adapter.setUrlByRT(imageInfo.getRt(), imageInfo.getLocalid(),
						url);
			} catch (ImageNotAvailableException e) {
				// Ignore the image this time, it will be treated by the
				// next
				// iteration of the executor
				thereArePending = true;
			} catch (CCDTeleoperationException e) {

				try {
					alog.registerAction(username, new Date(), e.getMessage());
				} catch (ActionLogException ei) {
					System.out.println(ei.getMessage());
				}

				try {
					adapter.removeImage(imageInfo.getId());
				} catch (ImageRepositoryAdapterException e1) {
					e1.printStackTrace();
				}

				try {
					alog.registerAction(username, new Date(), "Image entry "
							+ imageInfo.getId() + " removed");
				} catch (ActionLogException ea) {
					System.out.println(ea.getMessage());
				}
			} catch (Exception e) {
				try {
					alog.registerAction(username, new Date(), e.getMessage());
				} catch (ActionLogException ei) {
					System.out.println(ei.getMessage());
				}
			}
		}
	}
}
