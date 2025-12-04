# Common Library

This library provides shared utilities, models, and exception handling for the Job Applicant system.

## Features

- **Models**: Shared DTOs and domain models.
- **Utils**:
    - `MdcUtils`: Helpers for SLF4J MDC.
    - `LoggingUtils`: Standardized logging.
- **Exceptions**:
    - `GlobalExceptionHandler`: A generic `@ControllerAdvice` handler.
    - `ExceptionHandlerRegistry`: Dynamic registration of exception handlers.
    - `BaseException`: Base class for custom application exceptions.
- **Auto-Configuration**: Automatically configures components when imported into a Spring Boot application.

## Setup

Simply add the dependency to your project. The library uses Spring Boot Auto-Configuration, so no manual `@ComponentScan` is required.

```xml
<dependency>
    <groupId>saintgiong.jobapplicant</groupId>
    <artifactId>common</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Usage

### MDC Utils

Use `MdcUtils` to manage ThreadLocal context, such as correlation IDs.

```java
import saintgiong.jobapplicant.common.utils.MdcUtils;

public void processTransaction() {
    MdcUtils.setCorrelationId(MdcUtils.generateCorrelationId());
    try {
        // Your business logic here
        // Logs within this block will have the correlationId
    } finally {
        MdcUtils.remove(MdcUtils.CORRELATION_ID);
    }
}
```

**Example Output (Log):**
```text
2023-10-27 10:00:00.000 INFO [service-name,550e8400-e29b-41d4-a716-446655440000] ...
```
*(Note: The actual output format depends on your `logback.xml` configuration, but the correlation ID will be available in the MDC)*

### LoggingUtils

Use `LoggingUtils` for consistent logging.

```java
import saintgiong.jobapplicant.common.utils.LoggingUtils;

public class MyService {
    public void doSomething() {
        // Standard logging
        LoggingUtils.logInfo(MyService.class, "Operation started");
    }
}
```

**Example Output:**
```text
2023-10-27 10:00:00.000 INFO  [MyService] Operation started
```

### Exception Handling

#### 1. Custom Exceptions
Extend `BaseException` to create custom exceptions with error codes.

```java
import saintgiong.jobapplicant.common.exceptions.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String userId) {
        super("USER_NOT_FOUND", "User with ID " + userId + " not found");
    }
}
```

#### 2. Global Exception Handler
The `GlobalExceptionHandler` automatically handles `BaseException` and returns a standardized `ExceptionResponse`.

**Response Format:**
```json
{
  "code": "USER_NOT_FOUND",
  "message": "User with ID 123 not found",
  "timestamp": "2023-10-27T10:00:00"
}
```

#### 3. Registering Custom Handlers
You can register handlers for specific exceptions (including third-party exceptions) dynamically or via a configuration bean.

```java
import org.springframework.context.annotation.Configuration;
import saintgiong.jobapplicant.common.exceptions.handler.ExceptionHandlerRegistry;
import saintgiong.jobapplicant.common.models.response.ExceptionResponse;
import java.time.LocalDateTime;

@Configuration
public class ExceptionConfig {

    public ExceptionConfig(ExceptionHandlerRegistry registry) {
        registry.registerHandler(IllegalArgumentException.class, ex ->
            ExceptionResponse.builder()
                .code("INVALID_ARGUMENT")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
}
```
