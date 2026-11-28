# Summarization Project Context

## Microservice Purpose

`summarization` is the task progress summarization service. It consumes task-related messages through Kafka, prepares a summary of a user's progress with an external LLM API, and communicates the resulting summary through the established service integration contracts.

## Technology Stack

- Java 25 (Gradle toolchain), Gradle Kotlin DSL.
- Spring Boot 4.2.0.
- Spring Kafka for Kafka messaging.
- Spring AI Google GenAI integration for LLM access.
- Spring Data JPA and PostgreSQL for service-owned persistence.

## Responsibilities

The service is responsible for:

- consuming task progress data from Kafka according to the established message contract;
- validating and preparing task data for summarization;
- calling the configured LLM API and validating its response;
- persisting summarization-related data in its own isolated database;
- publishing or otherwise exposing summary results through established integration contracts;
- handling Kafka and LLM failures without losing consistency or exposing sensitive user data.

This service owns its persistence and must not access the API service's database directly. It must not depend on another service's internal implementation. Use Kafka or an explicitly established API for inter-service communication.

## Architecture

The application is organized under the `com.taskscheduler.summarization` package. Keep message handling, summarization use cases, LLM integration, persistence, and configuration in focused components with clear boundaries. Follow existing project patterns as they emerge; do not invent topic names, message formats, retry policies, or cross-service dependencies when they are not defined by the existing contracts.

Expected processing flow: consume a Kafka message, validate and map the task progress data, invoke the LLM integration, validate the response, then persist and deliver the result using the established contract. Handle duplicate or redelivered messages according to the project's idempotency strategy.

## LLM Integration

- Treat LLM responses as untrusted external data and validate them before persistence or publication.
- Configure explicit timeouts and handle provider errors, rate limits, malformed responses, and temporary failures.
- Do not hardcode credentials; use environment-backed configuration or the project's established secret management.
- Avoid logging complete prompts, responses, or sensitive user and task information.
- Do not assume responses are deterministic.

## Kafka and Data Ownership

- Follow existing Kafka message contracts and delivery, retry, and dead-letter policies.
- Consider duplicate delivery and partial failure between message processing, database writes, and result publication.
- Keep schema changes in this service's database within its existing migration mechanism; do not modify existing migrations unless explicitly required.
- Never read or write another service's database.

## Configuration and Entry Points

- Main configuration: `src/main/resources/application.yaml`.
- Application entry point: `src/main/java/com/taskscheduler/summarization/SummarizationApplication.java`.
- Gradle dependencies define Spring Kafka, Spring AI Google GenAI, Spring Data JPA, and PostgreSQL support.

The current application configuration only sets the service name. Inspect and extend the actual configuration and integration contracts before implementing Kafka, LLM, or database behavior; do not assume those integrations are already configured.
