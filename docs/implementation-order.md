# Implementation Order

This document defines the recommended implementation order for the World Cup Stats project.

The goal is to build the system from the dataset upward in a way that reflects a real-world data platform.

---

# Phase 0 — Foundation

Completed or mostly completed:

- repository setup
- project structure
- architecture documentation
- initial project board / epics
- AI context documentation

---

# Phase 1 — CSV Ingestion Foundation

Goal:
Read the historical dataset reliably and map it into a source row model.

Recommended stories:

1. Add CSV ingestion package structure
2. Implement `WorldCupMatchCsvRow`
3. Implement CSV parser
4. Add parser tests
5. Add validation for required raw fields
6. Handle encoding / special score formatting safely

Deliverable:
CSV rows can be read into a stable source model.

---

# Phase 2 — Canonical Domain Mapping

Goal:
Transform raw source rows into clean canonical entities.

Recommended stories:

1. Define `Tournament` canonical model
2. Define `Team` canonical model
3. Define `Stadium` canonical model
4. Define `Match` canonical model
5. Define stage/group handling strategy
6. Implement source-to-canonical mapper
7. Add mapper tests

Deliverable:
Source row data can be converted into a clean domain model.

---

# Phase 3 — Persistence Layer

Goal:
Persist canonical entities into a relational database.

Recommended stories:

1. Design initial OLTP schema
2. Add database migrations
3. Create persistence entities if needed
4. Implement repositories
5. Persist tournaments
6. Persist teams
7. Persist stadiums
8. Persist matches
9. Add idempotent ingestion behavior
10. Add persistence tests

Deliverable:
Canonical match data is stored in the database.

---

# Phase 4 — Read APIs

Goal:
Expose read-only APIs backed by persisted data.

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
Support aggregate and trend-oriented insights.

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

# Phase 6 — Observability and Performance

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

# Phase 7 — Deployment and Infrastructure

Goal:
Make the system runnable and deployable in a production-style way.

Recommended stories:

- Docker image
- docker-compose for app + database
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