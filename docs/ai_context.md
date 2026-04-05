# AI Context — World Cup Stats Platform

## Purpose

This document provides architectural context for AI agents contributing to the World Cup Stats Platform.

It defines:

* system structure
* data flow
* domain boundaries
* current implementation state
* rules for implementing new features

Agents must follow this context to ensure consistency, correctness, and alignment with the project roadmap.

---

# System Overview

The system is a data platform that ingests historical World Cup data and exposes it through an API.

The architecture follows a deterministic pipeline:

CSV → Source Model → Canonical Domain → Database → API → (Future Analytics/UI)

---

# Current Implementation State

## Runtime Architecture

* Single Spring Boot application (`api` module)
* Ingestion pipeline is implemented **inside the API module**
* PostgreSQL + Flyway are integrated
* API endpoints exist and are transitioning to database-backed reads

## Current Focus (Epic #21)

* Persistence hardening
* Idempotent ingestion
* Relationship integrity
* Transition to DB-backed read APIs
* Introduction of read models

---

# Data Flow

## Ingestion Pipeline

Kaggle CSV Dataset
→ Kaggle Source Model
→ Canonical Domain Model
→ Database Persistence

## Read Path

Database
→ Repository
→ Service
→ Controller
→ API Response (DTO)

---

# Layer Responsibilities

## 1. Source Layer (Kaggle-Specific)

Represents raw dataset structure.

Examples:

* KaggleMatchCsvRow
* KaggleWorldCupCsvParser

Rules:

* Must mirror CSV structure exactly
* Must NOT contain business logic
* Must NOT be used outside ingestion layer

---

## 2. Canonical Domain Model

Represents the system’s core business entities.

Current entities:

* Tournament
* Match
* Team
* Venue
* Stage

Rules:

* Must be independent of CSV structure
* Must be independent of API contracts
* Serves as the system’s source of truth for business logic

---

## 3. Persistence Layer

Responsible for storing canonical entities in PostgreSQL.

Responsibilities:

* enforce entity relationships
* support idempotent ingestion
* ensure no duplicate records
* provide reliable query access

---

## 4. API Layer

Exposes data through REST endpoints.

Structure:

Controller → Service → Repository

Responsibilities:

* serve data from persistence layer
* apply domain logic
* map domain → DTO

---

## 5. Read Model (Emerging)

Read models are optimized representations for API consumption.

Rules:

* may differ from canonical domain model
* should be designed for query use cases
* initially may mirror domain models closely

---

# Critical Architectural Rules

## 1. Strict Layer Separation

Never bypass layers.

Correct:

Source → Mapper → Domain → Repository → API

Incorrect:

Source → API
Source → Database directly
Domain → CSV

---

## 2. Source vs Domain Separation

Do NOT mix Kaggle models with domain models.

Examples:

* KaggleMatchCsvRow → ONLY used in ingestion
* Match → canonical domain model

---

## 3. Deterministic Behavior

The system must be deterministic.

* Same input → same output
* Re-running ingestion must not create duplicates
* Mapping must not depend on external state

---

## 4. Idempotent Ingestion (Critical for Epic #21)

Ingestion must:

* avoid duplicate records
* correctly match existing entities
* preserve relationships

This is a core requirement for all persistence-related work.

---

## 5. Database as Source of Truth

The database is becoming the system’s primary source of truth.

* API must read from database (not CSV)
* ingestion must fully populate canonical data
* in-memory or CSV-backed reads are transitional only

---

# Naming Conventions

## Source Layer

Use explicit source naming:

* KaggleMatchCsvRow
* KaggleWorldCupCsvParser

## Domain Layer

Use clean business naming:

* Match
* Team
* Tournament
* Venue
* Stage

## Do NOT mix naming across layers

---

# What NOT to Do

* Do NOT introduce new domain concepts without updating canonical docs
* Do NOT expose source models through API
* Do NOT bypass repository layer
* Do NOT introduce premature microservices
* Do NOT generalize ingestion prematurely (keep Kaggle-specific for now)

---

# Future Direction (Awareness Only)

The system is designed to evolve toward:

* multi-source ingestion (other tournaments, leagues)
* analytics layer
* frontend visualization

However:

* do NOT implement future architecture prematurely
* focus only on current story scope

---

# Summary for Agents

When implementing stories:

1. Respect layer boundaries
2. Keep source models separate from domain models
3. Ensure deterministic and idempotent behavior
4. Use database as the source of truth
5. Align all work with the current phase (Persistence Hardening & Read Enablement)

This ensures consistency, scalability, and correctness across the system.
