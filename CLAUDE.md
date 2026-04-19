# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A personal Spring Boot monolith template (练手项目模版). Stack: Java 21, Spring Boot 4.0.5, MyBatis, MySQL, Redis, RabbitMQ.

## Commands

```bash
./mvnw clean install          # Build
./mvnw spring-boot:run        # Run (requires env vars below)
./mvnw test                   # All tests
./mvnw test -Dtest=ClassName  # Single test class
```

### Required environment variables to run

`application.yaml` uses placeholders — these must be provided:
- `serverPort` — HTTP port
- `serverName` — Spring application name
- `dbName` — MySQL database name

Example: `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DserverPort=8080 -DserverName=mdframework -DdbName=example_db"`

## Architecture

### Package structure (`com.nedk.example`)

| Package | Purpose |
|---|---|
| `controller` | REST controllers |
| `service` | Business logic |
| `mapper` | MyBatis data access |
| `model/entity` | DB-mapped POJOs |
| `model/dto` | Request objects (validated with `@Valid`) |
| `model/vo` | Response objects |
| `common/result` | Unified HTTP response wrapper |
| `common/exception` | Global exception handling |
| `common/config` | CORS, Swagger config |
| `common/utils` | `RedisUtil` and other utilities |
| `common/constant` | `SystemConstants` (pagination defaults, token header, soft-delete flags) |

### Unified response pattern

All controllers must return `CommonResult<T>`. Never return raw objects.

```java
CommonResult.success(data)          // 200 with data
CommonResult.success()              // 200 no data (e.g. delete)
CommonResult.success("登录成功！", data) // 200 with custom message
CommonResult.error(ResultCode.XXX)  // error from enum
CommonResult.error("custom msg")    // error with custom message
```

### Error codes (`ResultCode` enum)

- `200` SUCCESS
- `4000` PARAM_ERROR (validation failure)
- `4001` UNAUTHORIZED
- `4003` FORBIDDEN
- `4004` NOT_FOUND
- `5000` SYSTEM_ERROR
- `5001` OPERATE_FAILED

### Exception handling

`GlobalExceptionHandler` (`@ControllerAdvice`) handles three tiers:
1. `BusinessException` → `warn` log, returns its embedded code+message
2. `MethodArgumentNotValidException` / `BindException` → `warn` log, returns first validation error with code `4000`
3. `Exception` (catch-all) → `error` log with stack trace, returns `5000` (never expose internals)

Throw business errors as: `throw new BusinessException(ResultCode.NOT_FOUND)` or `throw new BusinessException(ResultCode.NOT_FOUND, "自定义消息")`.

### Soft delete

Use `SystemConstants.DELETED = 1` / `NOT_DELETED = 0` for logical delete fields. Do not hard-delete records.

### API docs

Swagger UI available at `/swagger-ui.html` when running (SpringDoc OpenAPI 2.3.0).
