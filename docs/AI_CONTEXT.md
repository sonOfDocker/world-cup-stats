# AI Context: World Cup Stats Project

## Project Purpose

This repository is a portfolio architecture project demonstrating how to build a production-style sports analytics platform.

The project uses historical FIFA World Cup match data to build:

- a clean canonical domain model
- a data ingestion pipeline
- a Spring Boot REST API
- analytical query endpoints
- eventually a React visualization layer

The project emphasizes:

- clean architecture
- test-driven development (TDD)
- incremental feature delivery
- strong data modeling practices

The goal is to demonstrate how a real-world data platform evolves from **raw dataset ingestion to analytics APIs**.

---

# Dataset Overview

The project currently uses a historical FIFA World Cup dataset containing **match-level data from 1930–2022**.

Typical fields include:

- tournament name
- match date
- stage
- stadium
- city
- home team
- away team
- home score
- away score
- extra time
- penalties

The dataset **does NOT contain player-level data**.

Because of this, the initial domain model focuses on **tournaments, teams, matches, and venues**.

Player-related entities may be introduced in future phases when additional datasets are added.

---

# Current Development Phase

Current phase focuses on **building the historical match data foundation**.

Primary goals:

- ingest historical World Cup match data from CSV
- define canonical domain models
- persist canonical entities to a relational database
- expose read-only APIs for querying match data

Later phases will introduce:

- analytical queries and aggregations
- performance optimization
- observability
- visualization dashboards
- additional datasets (ex: player-level data)

---

# Core Technologies

Backend

- Java 21
- Spring Boot
- Gradle Kotlin DSL

Testing

- JUnit
- Spring Boot Test

Data

- CSV ingestion (initial data source)
- relational database (persistence layer)

Future additions may include:

- React dashboard
- analytics pipelines
- additional sports datasets

---

# Project Architecture

The architecture follows a layered backend structure.

Controller Layer  
Handles REST endpoints and request validation.

Service Layer  
Contains application logic and orchestration.

Repository Layer  
Handles database persistence and queries.

DTO Layer  
Separates API responses from domain entities.

---

# Data Flow Architecture

The system processes data through the following stages:

Raw Dataset

CSV dataset containing historical World Cup match data.

↓

Source Model

Represents rows from the CSV file.

↓

Canonical Domain Model

Normalized domain entities representing the core concepts of the system.

↓

Persistence Layer

Canonical entities stored in a relational database.

↓

API Layer

REST endpoints expose match, tournament, and team data.

---

# Domain Model

The current dataset supports **match-level historical data**.

Core entities include:

- Tournament
- Match
- Team
- Stadium
- Stage
- Group (if present in dataset)

Relationships:

Tournament  
→ contains many Matches

Match  
→ belongs to one Tournament  
→ has one home Team  
→ has one away Team  
→ occurs at one Stadium  
→ belongs to one Stage

Team  
→ participates in many Matches

Stadium  
→ location where Matches are played

---

# Future Domain Extensions

Some entities are intentionally **not implemented yet** because the current dataset does not contain this information.

Future entities may include:

- Player
- Lineup
- GoalEvent
- CardEvent
- SubstitutionEvent
- Referee
- Manager

These will require additional datasets.

---

# Data Ingestion

Data originates from:

Kaggle FIFA World Cup historical match dataset.

Pipeline:

CSV  
→ CSV Parser  
→ Source Row Model  
→ Canonical Domain Model  
→ Database Persistence  
→ API

---

# Engineering Principles

The repository prioritizes:

Test Driven Development

Red → Green → Refactor commits

Clean Architecture

Clear separation between controller, service, repository, and domain logic.

Incremental delivery

Features are implemented in small, verifiable steps.

Readable commits

Each commit should represent a logical step forward.

---

# Coding Conventions

Controllers should remain thin.

Business logic belongs in services.

Repositories should only handle persistence.

DTOs should be used for API responses.

Domain entities should not be exposed directly in API responses.

---

# What Agents Should Avoid

Agents should not:

- rewrite unrelated modules
- introduce unnecessary abstractions
- change architecture without explicit instruction
- modify unrelated packages

Changes should remain scoped to the story being implemented.

---

# Key Modules

Core modules expected in the repository:

- CSV ingestion
- Tournament domain
- Match domain
- Team domain
- REST API layer

Agents should locate relevant modules before implementing new features.