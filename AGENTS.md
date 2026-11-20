# AGENTS.md

## Project overview

This repository contains a Gradle Kotlin DSL Java project under `api/`. The application uses the package `com.taskscheduller.api` (preserve this spelling unless a task explicitly changes it).

## Working conventions

- Keep application code in `api/src/main/java` and tests in `api/src/test/java`.
- Follow the existing Java and Spring Boot conventions in the project; check `api/build.gradle.kts` before adding dependencies or changing build behavior.
- Keep configuration in `api/src/main/resources/application.yaml` unless the existing structure calls for another location.
- Avoid committing generated build output, local IDE state, or secrets.
- Make focused changes and preserve unrelated user changes.

## Build and tests

Do not run builds or tests unless the user explicitly asks you to.

Run Gradle from `api/` using the checked-in wrapper:

```powershell
./gradlew.bat build
./gradlew.bat test
```

On shells that support the Unix wrapper, `./gradlew build` and `./gradlew test` are equivalent.

## Validation

For code changes, run the narrowest relevant tests, then the broader build when practical. Report commands run and any failures or unverified areas.
