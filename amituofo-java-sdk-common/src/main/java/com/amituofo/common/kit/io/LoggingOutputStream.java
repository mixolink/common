package com.amituofo.common.kit.io;

import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggingOutputStream extends LogOutputStream {

	/**
	 * The logger to write to.
	 */
	private Logger log;

	/**
	 * The log level.
	 */
	private Level level;

	/**
	 * Creates the Logging instance to flush to the given logger.
	 *
	 * @param log   the Logger to write to
	 * @param level the log level
	 * @throws IllegalArgumentException in case if one of arguments is null.
	 */
	public LoggingOutputStream(final Logger log, final Level level) throws IllegalArgumentException {
		if (log == null || level == null) {
			throw new IllegalArgumentException("Logger or log level must be not null");
		}
		this.log = log;
		this.level = level;
	}

	@Override
	protected void processLine(String line, int logLevel) {
		log.log(level, line);
	}
}