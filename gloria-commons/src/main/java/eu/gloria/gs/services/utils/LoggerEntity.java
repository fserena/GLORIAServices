package eu.gloria.gs.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggerEntity {

	protected Logger log;

	protected LoggerEntity(String name) {
		log = LoggerFactory.getLogger(name);
		log.info(name + " created");
	}
}
