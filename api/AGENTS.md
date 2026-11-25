# API Project Context

## Microservice Purpose

`api` is the task management service's HTTP API. It provides user registration and login, access to the current user's data, and operations on that user's tasks. Access to tasks is tied to the authenticated owner.

## Technology Stack

- Java 25 (Gradle toolchain), Gradle Kotlin DSL.
- Spring Boot 4.1.2: Spring MVC for the HTTP API, Spring Data JPA for data persistence, and Spring Security with OAuth2 Resource Server for API security.
- JWT for authentication; the configuration secret is supplied through `JWT_SECRET`.
- PostgreSQL as the primary database.
- Liquibase for schema migrations; Hibernate runs with `ddl-auto: validate`.
- springdoc OpenAPI for HTTP API documentation.
- MapStruct and Lombok for DTO mapping and reducing boilerplate.

## Responsibilities

The service is responsible for:

- user registration and login with email and password;
- retrieving information about the current user;
- creating, retrieving, deleting, completing, and reopening tasks;
- checking task permissions by verifying ownership;
- validating input DTOs and converting errors into HTTP responses;
- storing users and tasks and managing the database schema.

The task model contains a title, an optional description, a status, a creation time, and a completion time. The supported statuses are `IN_PROGRESS` and `DONE`. Time-based scheduling, background execution, and notifications are not implemented in the current code.

## Architecture

The application is organized in layers under the `com.taskscheduller.api` package (the spelling `taskscheduller` intentionally matches the existing code):

1. **Controllers** (`controller`) handle HTTP requests under `/api/auth`, `/api/user`, and `/api/tasks`, use DTOs, and call services. Controller documentation interfaces are located in `controller/docs`.
2. **Services** (`service`, `service/impl`) contain the registration, authentication, and task/user management use cases.
3. **Repositories** (`repository`) provide data access through Spring Data JPA.
4. **Entities** (`entity`) represent users and tasks; the relationship between tasks and their owner is many-to-one.
5. **DTOs and mappers** (`dto`, `mapper`) separate HTTP contracts from the JPA model.
6. **Security and configuration** (`security`, `config`) configure JWT, user loading, handling of unauthorized requests, Jackson, and OpenAPI.
7. The Liquibase changelog in `src/main/resources/db/changelog` defines the database schema.

Request flow: HTTP controller → application service → repository → PostgreSQL; the result is mapped to a response DTO. Protected requests are checked by Spring Security, and the owner ID is taken from the authentication context.

## Configuration and Entry Points

- Main configuration: `src/main/resources/application.yaml`.
- PostgreSQL connection settings are provided through the `POSTGRES_URL`, `POSTGRES_USER`, and `POSTGRES_PASSWORD` environment variables; local defaults are available.
- The `.env` file can be loaded locally through an optional config import; secrets should not be included in documentation or committed.
- Liquibase changelog: `src/main/resources/db/changelog/db.changelog-master.yaml`.
- Application entry point: `src/main/java/com/taskscheduller/api/ApiApplication.java`.
