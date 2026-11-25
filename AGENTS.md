# AGENTS.md

# Project Overview

This project is a multi-user task scheduling platform.

It allows users to:
- Create and manage tasks in TODO format.
- Summarize task-related information using an LLM API.
- Receive email notifications through an email delivery API.

The system is organized as a set of independent services maintained in a monorepository.

# Service Map

| Service | Responsibility | Technology |
|---|---|---|
| `api/` | Application API, user-facing operations, task management and scheduler data | Java / Spring Boot |
| `summarization/` | Summarization of information using an external LLM API | See service implementation |
| `scheduler/` | Scheduling and triggering user notifications | See service implementation |
| `email/` | Email notification delivery API | See service implementation |

Each service owns its implementation details and should remain isolated from the internal implementation of other services.

# Architecture Principles

## Service Boundaries

- Respect service ownership and responsibility boundaries.
- Do not introduce direct dependencies on another service's internal implementation.
- Use established APIs or messaging mechanisms for inter-service communication.
- Do not access another service's database directly.
- Avoid duplicating business logic across services.
- Keep service-specific configuration and infrastructure isolated.

## Data Ownership

- Each service is responsible for its own data and persistence logic.
- Database schema changes must be implemented through the existing migration mechanism.
- Do not modify existing migrations unless the project explicitly requires it.
- Consider data consistency and failure handling when implementing cross-service operations.

## External Integrations

- External API calls must have explicit timeout and error-handling strategies.
- Do not hardcode API credentials, tokens or other secrets.
- Follow existing retry and rate-limiting policies.
- Handle external service failures without compromising application consistency.

## LLM Integration

- Treat LLM responses as untrusted external data.
- Validate and handle LLM responses before using them in application workflows.
- Handle API timeouts, rate limits, malformed responses and provider errors.
- Do not assume that LLM output is deterministic.
- Avoid logging sensitive user information or complete prompts unless explicitly required and safe.

## Email Integration

- Do not assume that a successful API request guarantees email delivery.
- Handle provider errors and temporary failures.
- Follow existing idempotency and retry mechanisms.
- Avoid exposing email credentials or sensitive notification content in logs.

# Agent Instructions

## 1. Instruction Discovery

Before making changes:

1. Check for additional instructions in the repository-root `.agents/` directory.
2. Check for additional instructions in the relevant service directory, if they presented.
3. Read all applicable instructions before modifying code.
4. Apply global instructions together with service-specific instructions.
5. Follow more specific instructions for the relevant component unless they conflict with mandatory repository-wide constraints.

Do not assume that files inside `.agents/` are automatically loaded by Codex.

Read relevant instruction files explicitly.

## 2. Task Analysis

Before implementing a change:

1. Understand the requested behavior and acceptance criteria.
2. Identify the affected services and shared components.
3. Inspect the existing implementation and established patterns.
4. Identify relevant APIs, events and service dependencies.
5. Check whether database schemas or migrations are affected.
6. Identify potential backward-compatibility and regression risks.

For changes affecting multiple services, inspect all relevant service instructions and integration contracts before implementation.

## 3. Implementation

- Prefer small, focused and maintainable changes.
- Follow the existing architecture and coding conventions.
- Avoid unnecessary abstractions and dependencies.
- Do not modify unrelated components.
- Preserve existing public API behavior unless a breaking change is explicitly requested.
- Add or update tests for changed behavior.
- Update documentation when externally visible behavior or architectural decisions change.

Do not introduce a new architectural pattern when an established project pattern already solves the problem.

## 4. Testing and Validation

Follow the testing instructions defined in `TESTS.md`.

Do not run tests unless explicitly requested by the user.

After implementation, review the changes and report which tests were not executed.

## 5. Cross-Service Changes

When a task affects multiple services:

- Identify all affected services before implementation.
- Respect each service's ownership boundaries.
- Inspect existing integration contracts.
- Consider failure scenarios and partial execution.
- Preserve compatibility with existing consumers.
- Consider deployment ordering when interfaces or data formats change.
- Add or update integration and contract tests where appropriate.

Avoid making assumptions about inter-service communication mechanisms. Inspect the existing implementation first.

## 6. Security and Reliability

- Never commit secrets or credentials.
- Do not weaken authentication or authorization without explicit requirements.
- Validate user-controlled input.
- Apply appropriate error handling to external integrations.
- Avoid exposing personal or sensitive information in logs.
- Follow existing security and observability conventions.

## 7. Completion Criteria

A task is complete when:

- The requested behavior is implemented.
- Applicable instructions have been followed.
- Relevant tests have been added or updated.
- Required validation has been executed.
- API and integration compatibility has been considered.
- Documentation has been updated where necessary.
- Known limitations and unresolved risks are reported.

## 8. Final Report

At the end of the task, provide a concise report containing:

- Summary of changes.
- Modified services and components.
- Tests and validation performed.
- API, database or integration changes.
- Known limitations or remaining risks.

# General Guidance

When project-specific information is missing or ambiguous:

- Inspect the existing code and configuration first.
- Prefer established project conventions over assumptions.
- Do not invent service dependencies or architecture decisions.
- Ask for clarification when a missing requirement materially affects implementation or correctness.