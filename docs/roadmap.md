# Project Roadmap

## Overview

The World Cup Stats Platform is an incremental data platform project that evolves from raw dataset ingestion into a full analytics and visualization system.

The system is intentionally built as a deterministic pipeline:

CSV → Source Row → Canonical Domain → Database → API → Analytics → UI

The goal is to demonstrate production-style engineering practices across data ingestion, domain modeling, persistence, API design, and frontend development.

---

# Phase 0 — Foundation

## Goal

Establish the architectural and development foundations of the project.

## Completed Work

* repository initialization
* architecture documentation
* ADRs (architecture decision records)
* development workflow definition
* project structure setup

## Outcome

A well-structured and well-documented project ready for incremental development.

---

# Phase 1 — Deterministic Ingestion Layer

## Goal

Build a reliable ingestion layer that reads raw datasets into structured source models.

## Focus Areas

* CSV parsing
* encoding handling
* raw field validation
* deterministic parsing behavior
* source traceability

## Outcome

The system can reliably read and represent raw dataset rows.

---

# Phase 2 — Canonical Domain Modeling

## Goal

Transform raw dataset rows into a clean, stable canonical domain model.

## Focus Areas

* defining core entities (Match, Team, Tournament, Venue, Stage)
* deterministic mapping logic
* normalization of domain concepts
* separation from raw CSV structure

## Outcome

A consistent internal representation of World Cup data independent of raw dataset format.

---

# Phase 3 — Persistence Hardening & Read Model Enablement

## Goal

Establish the database as the system’s source of truth and enable reliable read capabilities.

## Context

* PostgreSQL and Flyway are already integrated
* Ingestion pipeline exists but must be hardened
* Read APIs currently rely (fully or partially) on non-persistent sources

## Focus Areas

* schema refinement and stability
* Flyway migration correctness
* idempotent ingestion (no duplicate records)
* entity deduplication and identity strategy
* relationship integrity (foreign keys and associations)
* deterministic persistence outcomes
* transition from in-memory/CSV-backed reads → database-backed reads

## Outcome

* canonical data is reliably persisted
* ingestion can be safely re-run without duplication
* database becomes the primary source of truth
* system is ready to support consistent read APIs

---

# Phase 4 — Read API Expansion & Query Capabilities

## Goal

Expose persisted data through stable, flexible, and well-structured APIs.

## Focus Areas

* controller → service → repository layering
* DTO design and API contract stability
* query capabilities (filtering, sorting, basic search)
* pagination and response shaping
* alignment between read models and API responses

## Example Endpoints

GET /teams
GET /matches
GET /tournaments

## Outcome

* API is fully backed by persisted data
* consumers can reliably query historical World Cup data
* system demonstrates production-style API design

---

# Phase 5 — Analytics Layer

## Goal

Provide higher-level insights derived from match data.

## Focus Areas

* aggregation queries
* team performance metrics
* tournament statistics
* trend analysis

## Outcome

The system demonstrates analytical capabilities beyond simple data retrieval.

---

# Phase 6 — Frontend (Mobile-First React)

## Goal

Build a frontend that consumes the API and visualizes data.

## Focus Areas

* responsive UI design
* API integration
* list and detail views
* basic data visualization

## Outcome

A user-facing application that makes the data accessible and interactive.

---

# Phase 7 — Observability and Performance

## Goal

Introduce production-quality system characteristics.

## Focus Areas

* structured logging
* metrics and monitoring
* performance tuning
* improved error handling

## Outcome

Improved system reliability and visibility.

---

# Phase 8 — Deployment and Infrastructure

## Goal

Enable reproducible environments and deployment workflows.

## Focus Areas

* Dockerization
* environment configuration
* CI pipelines
* deployment documentation

## Outcome

The system can be run and deployed consistently across environments.

---

# Future Extensions

Potential future directions include:

* player-level datasets
* real-time data ingestion
* predictive analytics
* machine learning features

These build on the ingestion and canonical model foundation established in earlier phases.

---

# Key Principle

This project is intentionally built from:

data → domain → persistence → API → analytics → UI

This mirrors how real data platforms evolve and demonstrates strong system design thinking.
