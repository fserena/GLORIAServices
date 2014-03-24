package eu.gloria.gs.services.utils;

import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class LogLevelFilter extends LevelFilter {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		FilterReply reply = super.decide(event);
		if (event.getLoggerName().contains(".")) {
			return FilterReply.DENY;
		} else {
			return reply;
		}
	}
}