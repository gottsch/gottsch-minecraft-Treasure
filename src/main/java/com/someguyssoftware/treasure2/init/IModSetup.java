package com.someguyssoftware.treasure2.init;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;

/**
 * TODO this might need to merge with TreasureSetup if everything is going to be static
 * TODO move to GottschCore
 *TODO pass in the config for logging properties
 * @author Mark Gottschling on Jan 5, 2021
 *
 */
public interface IModSetup {
	/**
	 * TODO need the ILoggerConfig or just IConfig
	 * @param modName
	 * @param object
	 */
	public static void addRollingFileAppender(String modName, Object object) {

		String appenderName = modName + "Appender";
		String loggerFolder = "logs/treasure2/";
		if (!loggerFolder.endsWith("/")) {
			loggerFolder += "/";
		}

		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();

		// create a sized-based trigger policy, using config setting for size.
		SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(TreasureConfig.LOGGING.size.get());
		// create the pattern for log statements
		PatternLayout layout = PatternLayout.newBuilder().withPattern("%d [%t] %p %c | %F:%L | %m%n")
				.withAlwaysWriteExceptions(true).build();

		// create a rolling file appender
		Appender appender = RollingFileAppender.newBuilder()
				.withFileName(loggerFolder + /*modConfig.getLoggerFilename()*/"treasure2" + ".log")
				.withFilePattern(loggerFolder + /*modConfig.getLoggerFilename()*/"treasure2" + "-%d{yyyy-MM-dd-HH_mm_ss}.log")
				.withAppend(true).setName(appenderName).withBufferedIo(true).withImmediateFlush(true)
				.withPolicy(policy)
				.setLayout(layout)
				.setIgnoreExceptions(true).withAdvertise(false).setConfiguration(config).build();

		appender.start();
		config.addAppender(appender);
		
		// create a appender reference
		AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
		AppenderRef[] refs = new AppenderRef[] {ref};
		LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.toLevel(TreasureConfig.LOGGING.level.get(), Level.INFO), modName, "true", refs, null, config, null );
		loggerConfig.addAppender(appender, null, null);
		config.addLogger(modName, loggerConfig);
		
		// update logger with new appenders
		ctx.updateLoggers();
	}

}