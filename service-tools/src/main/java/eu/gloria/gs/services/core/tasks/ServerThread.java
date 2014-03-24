package eu.gloria.gs.services.core.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServerThread extends Thread {

	private boolean finish = false;
	protected Logger log;

	public ServerThread(String name) {
		super();
		log = LoggerFactory.getLogger(name);
	}

	protected abstract void doWork();

	@Override
	public void run() {
		while (!finish) {
			try {
				this.doWork();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

	}

	public void end() {
		this.finish = true;
		log.info("Trying to end " + log.getName() + "...");
	}

}
