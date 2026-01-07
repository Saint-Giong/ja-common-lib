package saintgiong.jobapplicant.common.exceptions;

import saintgiong.jobapplicant.common.utils.LoggingUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import saintgiong.jobapplicant.common.models.response.ExceptionResponse;
import java.time.LocalDateTime;
import java.util.function.Function;
import saintgiong.jobapplicant.common.exceptions.handler.ExceptionHandlerRegistry;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionHandlerRegistry registry;

    public GlobalExceptionHandler(ExceptionHandlerRegistry registry) {
        this.registry = registry;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException ex) {
        LoggingUtils.logError(GlobalExceptionHandler.class, "BaseException occurred: " + ex.getMessage(), ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        HttpStatus httpStatus = HttpStatus.resolve(ex.getCode());
        if (httpStatus == null) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        LoggingUtils.logError(GlobalExceptionHandler.class, "No resource found: " + ex.getMessage(), ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        LoggingUtils.logError(GlobalExceptionHandler.class, "Exception occurred: " + ex.getMessage(), ex);

        if (registry.hasHandler(ex.getClass())) {
            Function<Throwable, ExceptionResponse> handler = registry.getHandler(ex.getClass());
            ExceptionResponse response = handler.apply(ex);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        ExceptionResponse response = ExceptionResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
