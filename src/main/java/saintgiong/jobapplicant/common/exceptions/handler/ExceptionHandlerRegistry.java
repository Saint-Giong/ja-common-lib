package saintgiong.jobapplicant.common.exceptions.handler;

import org.springframework.stereotype.Component;
import saintgiong.jobapplicant.common.models.response.ExceptionResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class ExceptionHandlerRegistry {

    private final Map<Class<? extends Throwable>, Function<Throwable, ExceptionResponse>> handlers = new ConcurrentHashMap<>();

    public <T extends Throwable> void registerHandler(Class<T> exceptionClass, Function<T, ExceptionResponse> handler) {
        handlers.put(exceptionClass, (Function<Throwable, ExceptionResponse>) handler);
    }

    public Function<Throwable, ExceptionResponse> getHandler(Class<? extends Throwable> exceptionClass) {
        return handlers.get(exceptionClass);
    }

    public boolean hasHandler(Class<? extends Throwable> exceptionClass) {
        return handlers.containsKey(exceptionClass);
    }
}
