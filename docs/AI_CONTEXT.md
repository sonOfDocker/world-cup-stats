# AI Context: World Cup Stats Project

## Project Purpose

This repository is a portfolio architecture project demonstrating how to build a production-style sports analytics platform.

The project uses historical FIFA World Cup data to build:

- a clean canonical domain model
- a Spring Boot API
- data ingestion pipelines
- analytics endpoints
- eventually a React dashboard

The project emphasizes **clean architecture, TDD, and incremental feature development**.

---

# Current Development Phase

Phase 1 — Core API foundation

Focus:

- domain models
- ingestion pipeline
- API endpoints
- testing patterns

Later phases will introduce:

- analytics queries
- ML features
- visualization layer

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
- CSV ingestion (initial)
- relational database (later phase)

---

# Project Architecture

The architecture follows a standard layered backend structure:

Controller Layer
Handles REST endpoints.

Service Layer
Contains business logic.

Repository Layer
Handles persistence.

DTO Layer
Separates API responses from domain entities.

---

# Domain Model

Core entities include:

Team
Match
Tournament
Player
Statistics

Relationships:

Tournament
-> Matches
Match
-> Teams
Match
-> Statistics

---

# Data Ingestion

Data originates from:

Kaggle FIFA World Cup dataset.

Pipeline:

CSV -> Parser -> Canonical Domain Model -> API

Later:

CSV -> Canonical Model -> Database -> API

---

# Engineering Principles

The repository prioritizes:

TDD development
Red -> Green -> Refactor commits

Clean architecture
Clear separation between controller, service, repository

Readable commits
Each commit represents a small logical step.

---

# Coding Conventions

Controllers should remain thin.

Business logic belongs in services.

Repositories should only handle persistence.

DTOs should be used for API responses.

Avoid leaking domain entities to API layer.

---

# How Stories Should Be Implemented

For each GitHub story:

1. Read the issue and acceptance criteria
2. Identify relevant modules
3. Write a failing test
4. Implement minimal code to pass
5. Refactor
6. Update documentation if needed

---

# What Agents Should Avoid

Do not:

Rewrite unrelated modules.

Introduce unnecessary abstractions.

Change architecture without explicit instruction.

Modify unrelated packages.

---

# Key Modules

Teams API
Match API
Tournament API
CSV ingestion

Agents should locate these modules before implementing features.