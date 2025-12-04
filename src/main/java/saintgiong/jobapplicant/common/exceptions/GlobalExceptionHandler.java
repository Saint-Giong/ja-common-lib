package saintgiong.jobapplicant.common.exceptions;

import lombok.RequiredArgsConstructor;
import saintgiong.jobapplicant.common.utils.LoggingUtils;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionHandlerRegistry registry;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException ex) {
        LoggingUtils.logError(GlobalExceptionHandler.class, "BaseException occurred: " + ex.getMessage(), ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
