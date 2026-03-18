# Implementation Order

This document defines the recommended implementation order for the World Cup Stats project.

The goal is to build the system from the dataset upward in a way that reflects a real-world data platform.

The system is intentionally built as a deterministic data pipeline:

CSV → Source Row → Canonical Domain → Database → API → Analytics → UI

---

# Phase 0 — Foundation

Completed or mostly completed:

- repository setup
- project structure
- architecture documentation
- initial project board / epics
- AI context documentation

---

# Phase 1 — Deterministic CSV Ingestion Foundation

Goal:
Build a reliable and deterministic ingestion pipeline that reads raw datasets into structured source models.

Key principles:

- deterministic parsing (same input → same output)
- explicit handling of encoding and malformed data
- preservation of raw source values for traceability
- no hidden transformations

Recommended stories:

1. Add CSV ingestion package structure
2. Implement `WorldCupMatchCsvRow` (source row model)
3. Implement CSV parser
4. Add parser tests (including edge cases)
5. Add validation for required raw fields
6. Handle encoding / special score formatting safely
7. Preserve raw identifiers (row id, match id, tournament id)

Deliverable:

CSV rows can be read into a stable, traceable source model.

---

# Phase 2 — Canonical Domain Mapping

Goal:
Transform raw source rows into clean, stable canonical entities.

Key principles:

- deterministic mapping (same source → same canonical output)
- separation of source model and domain model
- normalization of domain concepts (teams, matches, tournaments)
- no leakage of raw CSV structure into domain

Recommended stories:

1. Define `Tournament` canonical model
2. Define `Team` canonical model
3. Define `Stadium` canonical model
4. Define `Match` canonical model
5. Define stage/group handling strategy
6. Implement source-to-canonical mapper
7. Add mapper tests
8. Introduce canonical identifiers (stable, deterministic or persisted)

Deliverable:

Source row data can be converted into a clean, stable domain model.

---

# Phase 3 — Persistence Layer (Idempotent Ingestion)

Goal:
Persist canonical entities into a relational database with idempotent ingestion behavior.

Key principles:

- idempotent ingestion (re-running ingestion produces the same result)
- no duplicate entities
- consistent schema evolution via migrations
- traceability from canonical entities back to source data

Recommended stories:

1. Design initial OLTP schema
2. Add database migrations (Flyway)
3. Create persistence entities if needed
4. Implement repositories
5. Persist tournaments
6. Persist teams
7. Persist stadiums
8. Persist matches
9. Implement idempotent ingestion strategy (upsert or deduplication)
10. Add persistence tests

Deliverable:

Canonical match data is stored in the database reliably and repeatably.

---

# Phase 4 — Read APIs

Goal:
Expose read-only APIs backed by persisted data.

Key principles:

- clear separation of controller, service, repository layers
- DTO-based responses (no domain leakage)
- predictable query behavior

Recommended stories:

1. GET `/teams`
2. GET `/teams/{id}`
3. GET `/tournaments`
4. GET `/tournaments/{id}`
5. GET `/matches`
6. GET `/matches/{id}`
7. Filter matches by tournament
8. Filter matches by team
9. Filter matches by stage

Deliverable:

Users can query core historical data through REST endpoints.

---

# Phase 5 — Analytical Queries

Goal:
Support aggregate and trend-oriented insights derived from canonical data.

Key principles:

- analytics built on canonical model (not raw data)
- reusable aggregation logic
- clear separation from ingestion logic

Recommended stories:

1. wins by team
2. goals by tournament
3. goals by team
4. matches by stage
5. penalty shootout summaries
6. historical performance trends

Deliverable:

The system demonstrates analytical value beyond basic CRUD.

---

# Phase 6 — Frontend (Mobile-First React)

Goal:
Build a mobile-first frontend that consumes the API and visualizes data.

Key principles:

- API-driven UI (no direct data access)
- mobile-first responsive design
- incremental feature delivery aligned with APIs

Recommended stories:

1. Initialize React application
2. Establish API client layer
3. Build mobile-first layout (navigation, routing)
4. Display teams list view
5. Display tournaments list view
6. Display matches list view
7. Match detail screen
8. Add filtering UI (team, tournament, stage)
9. Basic analytics visualizations (charts)

Deliverable:

A working frontend consuming the API with mobile-first UX.

---

# Phase 7 — Observability and Performance

Goal:
Improve production-style qualities of the system.

Recommended stories:

- structured logging
- ingestion metrics
- API metrics
- query timing
- performance tuning
- error visibility

---

# Phase 8 — Deployment and Infrastructure

Goal:
Make the system runnable and deployable in a production-style way.

Recommended stories:

- Docker image
- docker-compose for app + database + web
- environment config
- CI pipeline
- deployment documentation

---

# Deferred Phase — Player Data Enrichment

This phase should not begin until a supporting dataset is chosen.

Potential future additions:

- player canonical model
- squad ingestion
- player APIs
- player analytics

Current dataset does not support this phase.