package saintgiong.jobapplicant.common.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class LoggingUtils {

    public void logInfo(Class<?> clazz, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public void logError(Class<?> clazz, String message, Throwable t) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }

    public void logDebug(Class<?> clazz, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public void logWarn(Class<?> clazz, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }
}
