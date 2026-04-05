# System Overview

## Project: World Cup Stats Platform

The World Cup Stats Platform is a portfolio architecture project demonstrating how to design and implement a production-style sports analytics system using historical FIFA World Cup data.

The system ingests raw match data, transforms it into a clean canonical domain model, persists the data, and exposes it through a Spring Boot API. The platform will later power an interactive analytics dashboard.

The primary goal of the project is to showcase strong engineering practices including:

* Clean architecture
* Test-driven development (TDD)
* Incremental feature delivery
* Data ingestion pipelines
* API design
* analytics-ready domain modeling

---

# Architectural Status

## Current State

* Monorepo structure with a primary **Spring Boot backend (`api`)**
* Ingestion logic lives **inside the `api` module** as an internal subsystem
* CSV parsing and source-to-canonical mapping are implemented
* Canonical domain model is defined
* PostgreSQL + Flyway integration exists
* Work is in progress to make ingestion **idempotent and persistence-safe**

## Near-Term Focus (Epic #21)

* Harden persistence layer
* Ensure idempotent ingestion into database
* Establish reliable entity relationships
* Enable read APIs backed by persisted data
* Introduce initial read model patterns

## Future Evolution

* Analytics layer (aggregations, metrics)
* React-based visualization dashboard
* Potential extraction of ingestion into a separate service (if justified)

---

# High-Level System Architecture

The system follows a layered architecture with clear separation between ingestion, domain modeling, persistence, and API access.

```
Kaggle Dataset (CSV)
        ↓
Ingestion Layer (inside API)
        ↓
Canonical Domain Model
        ↓
PostgreSQL Database
        ↓
Spring Boot API
        ↓
Future Analytics / React UI
```

---

# System Components

## 1. Data Source Layer

The system consumes historical data from the FIFA World Cup dataset (Kaggle).

This dataset includes:

* tournaments
* matches
* teams
* match metadata

---

## 2. Data Ingestion Layer (Internal to API)

The ingestion layer is implemented **inside the Spring Boot `api` module**.

Responsibilities:

* read CSV datasets
* parse structured records
* validate input data
* map source rows to canonical entities

Pipeline:

CSV → Parser → Source Row → Canonical Domain

Notes:

* batch-style processing
* deterministic behavior
* designed for idempotency

---

## 3. Canonical Domain Model

Core entities (current scope):

* Team
* Match
* Tournament
* Venue
* Stage

Characteristics:

* independent of CSV structure
* independent of API representation
* single source of truth for business logic

---

## 4. Persistence Layer

Canonical entities are stored in PostgreSQL.

Responsibilities:

* persist canonical entities
* enforce relationships
* support idempotent ingestion
* enable efficient reads

Goals:

* no duplicate records
* consistent relationships
* deterministic results

---

## 5. Backend API Layer

Implemented using Spring Boot.

Example endpoints:

GET /teams
GET /matches
GET /tournaments

Structure:

Controller → Service → Repository

Responsibilities:

* serve persisted data
* apply domain logic
* transform domain → DTO
* maintain stable API contracts

---

## 6. Read Model (Emerging)

As persistence becomes the source of truth, the system introduces read models.

Read models:

* optimized for query use
* may differ from canonical models
* served via API

Initial versions will closely mirror domain entities.

---

# Future System Components

## Analytics Layer

* team performance metrics
* match statistics
* tournament comparisons

## Visualization Layer

React dashboard for:

* charts
* insights
* exploration

---

# Engineering Principles

## Test-Driven Development

Red → Green → Refactor

## Clean Architecture

Separation between:

* ingestion
* domain
* persistence
* API

## Incremental Development

Work progresses in small, well-defined stories and epics.

---

# Technology Stack

Backend:

* Java 21
* Spring Boot
* Gradle

Data:

* CSV ingestion
* PostgreSQL
* Flyway

Testing:

* JUnit
* Spring Boot Test

Frontend (future):

* React
