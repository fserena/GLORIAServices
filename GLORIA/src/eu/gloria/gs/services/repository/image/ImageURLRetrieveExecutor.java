package eu.gloria.gs.services.repository.image;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.core.ErrorLogEntry;
import eu.gloria.gs.services.core.InfoLogEntry;
import eu.gloria.gs.services.core.LogEntry;
import eu.gloria.gs.services.core.LogStore;
import eu.gloria.gs.services.core.WarningLogEntry;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.image.data.ImageDatabaseException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageRepositoryAdapter;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;

public class ImageURLRetrieveExecutor extends ServerThread {

	private ImageRepositoryAdapter adapter;
	private LogStore logStore;
	private CCDTeleoperationInterface ccd;
	private String username;
	private String password;
	private boolean thereArePending;
	private Map<Integer, Integer> recoverRetries = null;

	public void setAdapter(ImageRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	public void setLogStore(LogStore logStore) {
		this.logStore = logStore;
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

		if (recoverRetries == null) {
			recoverRetries = new HashMap<Integer, Integer>();
		}

		try {
			if (thereArePending) {
				Thread.sleep(100);
				// System.out.println("Image daemon alive...images pending!");
			} else {
				Thread.sleep(1000);
				// System.out.println("Image daemon alive...no images pending!");
			}
		} catch (InterruptedException e) {
		}

		GSClientProvider.setCredentials(this.username, this.password);

		List<ImageInformation> notUrlCompleted = null;

		LogAction preAction = new LogAction();
		preAction.put("sender", "image daemon");

		try {
			notUrlCompleted = this.adapter.getAllWithoutUrl(100);

			if (notUrlCompleted.size() > 0) {

				preAction.put("pending", notUrlCompleted.size());
			}

		} catch (ImageDatabaseException e) {
			preAction.put("cause", "internal error");
			this.logError(preAction);
			preAction.remove("cause");
		}

		thereArePending = false;

		for (ImageInformation imageInfo : notUrlCompleted) {

			LogAction action = new LogAction();
			action.put("sender", "image daemon");
			
			String url = null;

			action.put("id", imageInfo.getId());

			try {
				url = ccd.getImageURL(imageInfo.getRt(), imageInfo.getCcd(),
						imageInfo.getLocalid(), ImageExtensionFormat.JPG);

				adapter.setJpgByRT(imageInfo.getRt(), imageInfo.getLocalid(),
						url);

				url = ccd.getImageURL(imageInfo.getRt(), imageInfo.getCcd(),
						imageInfo.getLocalid(), ImageExtensionFormat.FITS);

				adapter.setFitsByRT(imageInfo.getRt(), imageInfo.getLocalid(),
						url);

				this.logInfo(action);

			} catch (ImageNotAvailableException e) {
				// Ignore the image this time, it will be treated by the
				// next
				// iteration of the executor

				int gid = imageInfo.getId();

				if (recoverRetries.containsKey(gid)) {
					recoverRetries.put(gid, recoverRetries.get(gid) + 1);

					action.put("retries", recoverRetries.get(gid));

					if (recoverRetries.get(gid) == 50) {
						try {
							action.put("operation", "remove");
							adapter.removeImage(imageInfo.getId());

							this.logWarning(action);

						} catch (ImageDatabaseException e1) {
							action.put("cause", "service error");
							this.logError(action);
						}

						recoverRetries.remove(gid);
					}
				} else {
					recoverRetries.put(gid, 0);
					thereArePending = true;
				}
			} catch (CCDTeleoperationException e) {

				if (url == null) {
					url = "";
				}

				action.put("ccd", "fail");
				action.put("operation", "remove");

				try {
					adapter.removeImage(imageInfo.getId());

					this.logError(action);

				} catch (ImageDatabaseException e1) {
					action.put("cause", "internal error");
					this.logError(action);
				}

			} catch (Exception e) {
				action.put("cause", "internal error");
				action.put("operation", "remove");

				try {
					adapter.removeImage(imageInfo.getId());

					this.logError(action);
				} catch (ImageDatabaseException e1) {
					action.put("cause", "internal error");
					this.logError(action);
				}
			}
		}
	}

	private void processLogEntry(LogEntry entry, LogAction action) {
		entry.setUsername(this.username);
		entry.setDate(new Date());

		entry.setAction(action);
		this.logStore.addEntry(entry);
	}

	private void logError(LogAction action) {

		LogEntry entry = new ErrorLogEntry();
		this.processLogEntry(entry, action);
	}

	private void logInfo(LogAction action) {

		LogEntry entry = new InfoLogEntry();
		this.processLogEntry(entry, action);
	}

	private void logWarning(LogAction action) {

		LogEntry entry = new WarningLogEntry();
		this.processLogEntry(entry, action);
	}
}
