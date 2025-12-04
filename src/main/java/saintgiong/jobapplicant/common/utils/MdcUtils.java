package saintgiong.jobapplicant.common.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class MdcUtils {

    public static final String CORRELATION_ID = "correlationId";
    public static final String USER_ID = "userId";

    public void put(String key, String value) {
        if (key != null && value != null) {
            MDC.put(key, value);
        }
    }

    public String get(String key) {
        return MDC.get(key);
    }

    public void remove(String key) {
        MDC.remove(key);
    }

    public void clear() {
        MDC.clear();
    }

    public void setCorrelationId(String correlationId) {
        put(CORRELATION_ID, correlationId);
    }

    public String getCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }

    public void setContextMap(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

    public Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }
}
