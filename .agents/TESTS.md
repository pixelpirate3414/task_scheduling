# Testing Guidelines

- This document defines the project-wide guidelines for writing and running tests. These guidelines apply to all services. Service-specific `TESTS.md` files may extend these guidelines with additional requirements.

## General Rules

- Do not run tests unless explicitly requested by the user.
- Every new business feature must have automated tests.
- Every bug fix must include a regression test.
- Prefer unit tests for isolated business logic.
- Use integration tests with Testcontainers for infrastructure-dependent behavior.
- Tests must be deterministic and independent of execution order.
- Do not introduce unnecessary mocks.
- Do not modify production code solely to make tests pass unless required.
- Do not weaken or remove existing assertions to fix failing tests.
- Tests must not depend on external services or internet access.

## Test location

- Unit tests — inside `unit/` directory
- Integration tests — inside `integration/` directory

## Testing Stack

- JUnit 5 — test framework.
- Mockito — mocking dependencies in unit tests.
- AssertJ — assertions.
- Spring Boot Test — Spring context integration tests.
- Testcontainers — infrastructure integration tests.
- MockMvc — HTTP layer testing (if applicable).
- Awaitility — asynchronous operations (if applicable).

Use existing dependencies and conventions.
Do not introduce new testing libraries without a strong reason.

## Unit Tests

### Scope

Unit tests should cover:
- Business logic.
- Domain services.
- Validators.
- Mappers.
- Calculations and transformations.
- Edge cases and error handling.

### Rules

- Do not start Spring ApplicationContext.
- Do not use @SpringBootTest.
- Instantiate classes directly whenever possible.
- Mock external dependencies using Mockito.
- Use @ExtendWith(MockitoExtension.class) when Mockito is needed.
- Prefer constructor injection.
- Avoid mocking simple DTOs, entities and value objects.
- Do not mock the class under test.

### Assertions

Verify:
- Expected return values.
- State changes.
- Exceptions.
- Important interactions with dependencies.
- Boundary conditions.

## Integration Tests

### General Rules

- Use Testcontainers for integration tests involving infrastructure.
- Never use an embedded database as a replacement for the production database
  when testing database-specific behavior.
- Prefer testing against the same database engine as production.
- Do not mock repositories in tests that verify persistence behavior.
- Do not mock external infrastructure when the purpose of the test
  is to verify integration with it.

### Testcontainers

Use Testcontainers for:
- PostgreSQL / MySQL.
- Kafka.

### Spring Boot

- Use @SpringBootTest only when full application context is required.
- Prefer @DataJpaTest for JPA repository tests.
- Use @WebMvcTest for MVC controller slice tests.
- Reuse containers where appropriate.
- Use dynamic properties or Spring Boot service connections
  to configure container connections.
- Never hardcode local infrastructure credentials.

## Test Selection

Choose the smallest test scope that verifies the behavior.

| Scenario | Test Type |
|----------|-----------|
| Business logic | Unit test |
| Validation | Unit test |
| Mapper | Unit test |
| Repository queries | Integration test + Testcontainers |
| REST controller mapping | @WebMvcTest |
| Kafka integration | Integration test + Kafka Testcontainer |

## Test Quality

- Test behavior, not implementation details.
- Use descriptive test names.
- Follow Arrange / Act / Assert.
- Each test should verify one logical behavior.
- Avoid assertions that are unrelated to the tested behavior.
- Avoid Thread.sleep().
- Use Awaitility for asynchronous conditions.
- Avoid random test data unless randomness is deterministic.
- Use fixed Clock or injectable time providers for time-dependent logic.
- Do not use production data or credentials.
- Clean up test data when necessary.

## Test Coverage

Prioritize coverage of:
- Business-critical logic.
- Error handling.
- Boundary conditions.
- Security-sensitive logic.
- Transactional operations.
- Integration points.

Coverage percentage alone is not a quality metric.

Do not add meaningless tests just to increase coverage.

## Running Tests

Use Gradle Wrapper provided by the project.

For Gradle:

Run all tests:
./gradlew test

Run a specific test:
./gradlew test --tests OrderServiceTest

Integration tests:
./gradlew integrationTest

Always inspect the project build configuration and use
the actual configured test tasks.

## Definition of Done

Before considering a task complete:

1. New business logic has appropriate unit tests.
2. New infrastructure interactions have integration tests where appropriate.
3. Bug fixes include regression tests.
4. Existing tests have not been weakened or removed.
5. All relevant tests pass.
6. No tests depend on execution order.
7. Testcontainers-based tests run without manually started infrastructure.
8. New tests follow existing project conventions.

If tests cannot be executed:
- Explain why.
- Report which tests were not executed.
- Never claim tests passed without actually running them.

## Additional rules

- Do not create abstractions, base test classes or test frameworks
  unless they provide clear value.
- Prefer simple, readable tests.
- Reuse existing test utilities and fixtures.
- Do not create generic test builders for one-time use.
- Do not modify production code just to simplify test setup.
- Do not introduce public methods solely for testing.
- Prefer testing through public APIs.
- If production code is difficult to test, first identify
  whether there is an architectural issue.
- Never use @Disabled to bypass failing tests without justification.
- Never catch and ignore exceptions in tests.
- Never remove assertions to make tests pass.
- Never skip Testcontainers tests because Docker is unavailable
  without explicitly reporting the limitation.