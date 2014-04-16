package eu.gloria.gs.services.core.tasks;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.LogType;
import eu.gloria.gs.services.utils.JSONConverter;

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

	protected void log(LogType type, Action action) {
		try {
			String message = JSONConverter.toJSON(action);
			if (type.equals(LogType.INFO)) {
				log.info(message);
			} else if (type.equals(LogType.ERROR)) {
				log.info(message);
			} else {
				log.warn(message);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	protected void log(LogType type, String message) {
		if (type.equals(LogType.INFO)) {
			log.info(message);
		} else if (type.equals(LogType.ERROR)) {
			log.info(message);
		} else {
			log.warn(message);
		}
	}
}
