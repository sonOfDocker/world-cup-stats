# ADR-004: Ingestion Implemented Within Java API (Deferred Orchestration Layer)

## Status
Accepted

## Context

The system requires a data ingestion pipeline to process historical FIFA World Cup data from CSV datasets into the application’s canonical domain model.

At this stage, ingestion consists of:
- Parsing raw CSV files into structured source row models
- (Future) Mapping into canonical domain models
- (Future) Persisting into a relational database

A key architectural decision is whether ingestion should:
1. Be implemented within the existing Java/Spring Boot `api` project, or
2. Be handled by a separate ingestion/orchestration system (e.g., Python service, Airflow, Prefect, or scheduled batch jobs)

The project is currently in an early phase, focused on:
- Demonstrating strong system design fundamentals
- Building a deterministic, testable ingestion pipeline
- Maintaining a simple and cohesive development workflow

---

## Decision

Ingestion logic will be implemented **within the existing Java API project** as part of an internal ingestion layer.

We explicitly **defer introducing a separate orchestration or scheduling system** (e.g., Airflow, Prefect, cron-based workers, or external services) until later phases.

---

## Rationale

### 1. Architectural Simplicity (YAGNI)

Introducing a separate ingestion service or orchestration layer at this stage would:
- Add operational complexity (deployment, configuration, monitoring)
- Require cross-service contracts prematurely
- Slow down iteration and learning

The current scope does not yet justify distributed ingestion infrastructure.

---

### 2. Deterministic, Testable Pipeline First

The immediate goal is to build a pipeline that is:
- Deterministic (same input → same output)
- Idempotent (safe to re-run)
- Fully testable with unit and integration tests

Embedding ingestion within the Java codebase allows:
- Tight control over execution
- Easier TDD workflows
- Strong type safety across parsing and mapping layers

---

### 3. Cohesion with Domain Model

The ingestion pipeline feeds directly into canonical domain models that are also defined in the same codebase.

Keeping ingestion co-located:
- Reduces impedance mismatch between layers
- Avoids premature serialization/deserialization boundaries
- Enables faster iteration on schema and mapping logic

---

### 4. Portfolio and Demonstration Value

This project is intended to showcase:
- End-to-end system design
- Data modeling and ingestion pipelines
- Backend engineering fundamentals

Implementing ingestion in Java:
- Demonstrates ownership across the full pipeline
- Highlights backend engineering depth beyond REST APIs
- Avoids hiding complexity in external tools

---

### 5. Deferred Complexity (Intentional Evolution)

This decision is **not permanent**.

Future phases may introduce:
- Scheduled ingestion jobs (e.g., cron, Spring scheduling)
- External orchestration (e.g., Airflow, Prefect)
- Streaming or near-real-time ingestion
- Separate ingestion services or pipelines

By deferring this complexity, we:
- Ensure current abstractions are stable first
- Avoid premature architectural constraints
- Allow evolution based on real system needs

---

## Consequences

### Positive
- Faster development and iteration
- Simpler deployment model (single service)
- Strong testability and determinism
- Clear separation of ingestion → canonical → persistence within one codebase

### Negative
- No built-in scheduling or orchestration yet
- Limited scalability for large or distributed ingestion workloads
- Tight coupling between ingestion and application runtime

---

## Alternatives Considered

### 1. Separate Python Ingestion Service
- Pros:
    - Rich ecosystem for data processing
    - Flexible scripting and transformation
- Cons:
    - Introduces multi-language complexity
    - Requires inter-service communication
    - Overkill for current dataset size and scope

### 2. Workflow Orchestration Tools (Airflow, Prefect)
- Pros:
    - Strong scheduling and observability
    - Industry-standard for batch pipelines
- Cons:
    - High setup and operational overhead
    - Not necessary for a single dataset ingestion use case
    - Premature for current project phase

### 3. External Batch Job / Worker Service
- Pros:
    - Separation of concerns
    - Independent scaling
- Cons:
    - Additional deployment unit
    - Requires queueing or coordination mechanisms
    - Increases system complexity early

---

## Future Considerations

This decision should be revisited when:
- Multiple datasets or ingestion sources are introduced
- Scheduling or recurring ingestion becomes necessary
- Data volume significantly increases
- Observability and pipeline monitoring requirements grow
- Real-time or streaming ingestion is introduced

At that point, introducing:
- A dedicated ingestion service
- Orchestration tools
- Or event-driven pipelines

may be justified.

---

## Related Decisions

- ADR-001: Static Analysis Workflow
- ADR-002: Flyway Schema Migration Strategy
- (Future) ADR: Idempotent Ingestion Pipeline Design