# ADR-003: Static Analysis Strategy for Local and CI Workflows

## Status

Accepted

## Context

During Epic 0 (project foundation), we identified a gap in how static analysis findings are surfaced and consumed:

* SonarLint (IntelliJ plugin) provides local feedback but is **not reproducible outside the IDE**
* AI agents (e.g., Junie) and CI workflows rely on **command-line and repository-defined tooling**
* There was no standardized way to:

    * run static analysis from Gradle
    * view findings outside IntelliJ
    * ensure consistent results across environments

This created inconsistency between:

* developer-local feedback (IDE)
* automated workflows (agents, CI)
* repository-defined quality standards

## Decision

We will establish a **lightweight, reproducible static analysis workflow** using Gradle as the single entry point.

Specifically:

* Use `./gradlew check` as the canonical command for:

    * running unit tests
    * executing static analysis
* Integrate **one lightweight static analysis tool** (e.g., Checkstyle) as the initial solution
* Ensure results are:

    * visible in console output and/or generated reports
    * executable without requiring an IDE
* Add a minimal CI step (e.g., GitHub Actions) that runs the same command
* Document the workflow in the repository (README or docs/)

## Rationale

This approach was chosen to prioritize:

* **Reproducibility**: Works the same for developers, agents, and CI
* **Simplicity**: Avoids introducing unnecessary tools or infrastructure early
* **Incremental adoption**: Establishes a foundation that can be extended later
* **Agent compatibility**: Enables AI agents to execute and act on quality checks

Although SonarLint is already in use locally, it is:

* IDE-dependent
* not directly executable via Gradle
* not visible to CI or agents

Therefore, it is not sufficient as the primary project-level solution.

## Alternatives Considered

### 1. SonarQube (self-hosted)

* Rejected due to:

    * infrastructure overhead
    * setup complexity for early-stage project
    * not aligned with Epic 0 goals

### 2. SonarCloud integration (immediate)

* Deferred because:

    * requires external service setup and credentials
    * adds complexity before baseline workflow is established
* May be introduced in a future ADR

### 3. Multiple static analysis tools (Checkstyle + PMD + SpotBugs)

* Rejected for now due to:

    * increased configuration complexity
    * slower feedback loop
    * unnecessary overlap at early stage

### 4. Rely solely on IntelliJ SonarLint plugin

* Rejected because:

    * not reproducible outside IDE
    * not accessible to CI or agents
    * creates inconsistency across environments

## Consequences

### Positive

* Establishes a **single, consistent quality entry point** (`./gradlew check`)
* Enables **CI and agent-driven workflows**
* Improves **transparency of code quality issues**
* Keeps setup lightweight and maintainable

### Negative / Tradeoffs

* Initial analysis coverage is limited (single tool)
* Does not yet provide:

    * centralized dashboards
    * historical tracking
    * advanced code smell detection from Sonar

## Future Considerations

This decision is intentionally incremental. Future enhancements may include:

* Integration with **SonarCloud** for centralized reporting
* Addition of:

    * SpotBugs (bug detection)
    * JaCoCo (test coverage)
* Enforcing quality gates (e.g., failing builds on violations)

These changes will be evaluated as separate ADRs.

## References

* Gradle `check` lifecycle: https://docs.gradle.org/current/userguide/java_plugin.html
* Checkstyle: https://checkstyle.org/
* SonarLint (IDE plugin): https://www.sonarsource.com/products/sonarlint/

---
