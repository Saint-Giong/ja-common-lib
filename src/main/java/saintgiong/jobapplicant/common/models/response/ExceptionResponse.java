package saintgiong.jobapplicant.common.models.response;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private int code;
    private String message;
    private LocalDateTime timestamp;

    public ExceptionResponse() {
    }

    public ExceptionResponse(int code, String message, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int code;
        private String message;
        private LocalDateTime timestamp;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ExceptionResponse build() {
            return new ExceptionResponse(code, message, timestamp);
        }
    }
}
