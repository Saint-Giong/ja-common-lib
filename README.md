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

## 🚀 Quick Setup for Team Members

### Option 1: Automated Setup (Recommended)

Run the setup script to automatically configure Maven:

```bash
cd ja-common-lib
chmod +x setup-maven.sh
./setup-maven.sh
```

The script will:
1. Prompt for your GitHub username
2. Prompt for your GitHub Personal Access Token (PAT)
3. Create/update `~/.m2/settings.xml` with proper authentication
4. Backup existing settings if present

### Option 2: Manual Setup

#### Step 1: Generate GitHub Personal Access Token

1. Go to https://github.com/settings/tokens
2. Click **"Generate new token (classic)"**
3. Give it a name: `Maven Package Access`
4. Select scope: **`read:packages`** (required)
5. Click **"Generate token"** and **copy it immediately**

#### Step 2: Configure Maven Settings

Create or edit `~/.m2/settings.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_PAT_TOKEN</password>
    </server>
  </servers>

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo.maven.apache.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/saint-giong/ja-common-lib</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

</settings>
```

Replace `YOUR_GITHUB_USERNAME` and `YOUR_GITHUB_PAT_TOKEN` with your credentials.

#### Step 3: Verify Setup

```bash
# Clear any cached authentication failures
rm -rf ~/.m2/repository/io/github/saint-giong/common/

# Test by building a microservice
cd ../ja-authentication-service
mvn clean install -U -DskipTests
```

### Add to Microservices

Each microservice's `pom.xml` should include:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.saint-giong</groupId>
        <artifactId>common</artifactId>
        <version>0.0.9</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/saint-giong/ja-common-lib</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

## ⚠️ Troubleshooting

### Error: `401 Unauthorized`

**Solutions**:
1. Check token expiration - generate a new PAT
2. Verify token has `read:packages` scope
3. Clear cached failures: `rm -rf ~/.m2/repository/io/github/saint-giong/common/`
4. Force update: `mvn clean install -U -DskipTests`

### Error: `Non-parseable settings`

**Solutions**:
1. Verify XML syntax: `xmllint --noout ~/.m2/settings.xml`
2. Re-run setup script: `./setup-maven.sh`

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
