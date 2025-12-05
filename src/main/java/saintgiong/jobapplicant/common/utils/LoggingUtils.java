package saintgiong.jobapplicant.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingUtils {

    private LoggingUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void logInfo(Class<?> clazz, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public static void logError(Class<?> clazz, String message, Throwable t) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }

    public static void logDebug(Class<?> clazz, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public static void logWarn(Class<?> clazz, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }
}
