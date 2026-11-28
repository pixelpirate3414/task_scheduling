# Java and Spring Coding Standards

## 1. General Principles

This document defines project-wide coding standards for all Java and Spring Boot services.

All new code and modifications to existing code must follow these guidelines.

Rules are classified as:

- **MUST** — mandatory requirement.
- **SHOULD** — preferred approach. Deviations should have a reasonable justification.
- **MAY** — optional approach, depending on the situation.

General principles:

- Prefer simple, readable, and maintainable code.
- Follow existing project conventions unless they conflict with these standards.
- Avoid unnecessary abstractions and premature generalization.
- Do not introduce additional dependencies without a clear reason.
- Keep classes focused on a single responsibility.

## 2. Dependency Injection

- **MUST:** Use constructor injection for Spring dependencies.
- **SHOULD:** Use Lombok `@RequiredArgsConstructor` to generate constructors for required dependencies.
- **MUST NOT:** Use field injection with `@Autowired`.
- **MUST NOT:** Use setter injection for mandatory dependencies.
- **SHOULD:** Declare injected dependencies as `private final`.

Example:

```java
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
}
```

Avoid:

```java
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
}
```

## 3. Service Layer

- **SHOULD:** Define a service interface and a separate implementation class.
- **MUST:** Keep business logic in the service layer rather than controllers.
- **MUST:** Keep services focused on a specific business responsibility.
- **SHOULD:** Use descriptive names for service interfaces and implementations.
- **MUST NOT:** Put persistence or HTTP-specific logic into services unless explicitly required by the responsibility.

Naming convention:

- Interface: `TaskService`
- Implementation: `TaskServiceImpl`

Example:

```java
public interface TaskService {

    TaskResponse getTask(Long id);

    TaskResponse createTask(CreateTaskRequest request);
}
```

```java
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponse getTask(Long id) {
        // Implementation
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        // Implementation
    }
}
```

## 4. Transaction Management

- **MUST:** Define transaction boundaries explicitly for operations that require transactional consistency.
- **SHOULD:** Prefer method-level `@Transactional` annotations over class-level annotations.
- **SHOULD:** Use `@Transactional(readOnly = true)` for read-only operations where appropriate.
- **MUST NOT:** Add `@Transactional` to every method without considering whether a transaction is necessary.
- **MUST:** Use `@Transactional` on public service methods that represent transactional business operations.
- **SHOULD:** Keep transactions as short as reasonably possible.
- **MUST NOT:** Perform slow external API calls inside database transactions unless there is a specific reason.

Example:

```java
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public TaskResponse getTask(Long id) {
        // Read operation
    }

    @Transactional
    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        // Write operation
    }
}
```

## 5. Spring Components

Use Spring annotations according to component responsibility:

- `@RestController` — HTTP controllers.
- `@Service` — business logic.
- `@Repository` — persistence components when an explicit repository implementation is needed.
- `@Component` — general-purpose Spring-managed components.
- `@Configuration` — configuration classes.
- `@ConfigurationProperties` — type-safe application configuration.

Avoid using `@Component` when a more specific stereotype is appropriate.

## 6. DTOs and Entities

- **MUST:** Separate API DTOs from persistence entities.
- **MUST NOT:** Expose JPA entities directly from REST controllers.
- **SHOULD:** Use Java records for immutable DTOs when appropriate.
- **MUST:** Validate incoming request data using Jakarta Bean Validation where applicable.
- **SHOULD:** Keep mapping logic explicit and easy to understand.

Example:

```java
public record CreateTaskRequest(
    @NotBlank String title,
    @NotNull LocalDateTime dueDate
) {}
```

## 7. Exception Handling

- **SHOULD:** Use meaningful domain-specific exceptions when they improve clarity.
- **MUST:** Handle API errors consistently.
- **SHOULD:** Use centralized exception handling with `@RestControllerAdvice`.
- **MUST NOT:** Swallow exceptions silently.
- **MUST NOT:** Use generic exceptions when a more specific exception is appropriate.
- **MUST NOT:** Expose internal implementation details or sensitive information in API error responses.

## 8. Validation

- **MUST:** Validate external input at application boundaries.
- **SHOULD:** Use Jakarta Bean Validation annotations such as `@NotNull`, `@NotBlank`, `@Size`, and `@Valid`.
- **MUST:** Keep business invariants in the appropriate business layer rather than relying exclusively on request validation.

## 9. Logging

- **SHOULD:** Use SLF4J for application logging.
- **MUST NOT:** Use `System.out.println()` or `System.err.println()` for application logging.
- **MUST NOT:** Log passwords, access tokens, secrets, or sensitive personal data.
- **SHOULD:** Use appropriate log levels:
    - `ERROR` — unexpected errors requiring attention.
    - `WARN` — potentially problematic situations.
    - `INFO` — important application events.
    - `DEBUG` — diagnostic information.

## 10. Configuration

- **SHOULD:** Use `@ConfigurationProperties` for groups of related configuration properties.
- **MUST NOT:** Hardcode environment-specific URLs, credentials, or secrets.
- **MUST:** Keep secrets outside source control.
- **SHOULD:** Use constructor-based configuration binding where appropriate.

## 11. Java Code Conventions

- **MUST:** Follow standard Java naming conventions.
- Classes and interfaces: `PascalCase`.
- Methods and variables: `camelCase`.
- Constants: `UPPER_SNAKE_CASE`.
- **SHOULD:** Use `final` for fields that are not reassigned.
- **SHOULD:** Prefer immutable objects where practical.
- **SHOULD:** Use `Optional` primarily as a return type when absence is a meaningful result.
- **MUST NOT:** Use `Optional` for entity fields, DTO fields, or method parameters without a specific reason.
- **SHOULD:** Prefer meaningful names over abbreviations.
- **MUST NOT:** Introduce unnecessary utility classes or abstractions.

## 12. Code Quality

- **MUST:** Remove unused imports, variables, and methods.
- **MUST:** Avoid duplicated business logic.
- **SHOULD:** Keep methods small and focused.
- **SHOULD:** Prefer early returns over deeply nested conditional structures.
- **MUST NOT:** Leave temporary debugging code in committed changes.
- **SHOULD:** Use existing project libraries and patterns instead of introducing alternative solutions for the same purpose.

## 13. Applying These Standards

These standards apply to all Java and Spring Boot services in the project.

Service-specific `CODING.md` files may extend these standards with additional requirements.

When existing code differs from these standards:

- Follow these standards when implementing new code.
- Avoid unrelated refactoring solely to make existing code compliant.
- Preserve established patterns when changing legacy code unless they conflict with a mandatory requirement.
- If a requirement cannot reasonably be followed, explain the reason.