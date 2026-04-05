# System Architecture Diagram

## Overview

This document provides a visual overview of the architecture for the World Cup Stats Platform.

The system is currently implemented as a single backend service with an internal ingestion pipeline and database-backed read APIs.

---

# Current Architecture

```
Kaggle Dataset (CSV)
        ↓
Ingestion Pipeline (inside API)
        ↓
Canonical Domain Model
        ↓
PostgreSQL Database
        ↓
Spring Boot API
```

---

# Data Flow (Current)

```
CSV Dataset
    ↓
CSV Parser
    ↓
Source Row Model
    ↓
Canonical Domain Model
    ↓
Database Persistence
    ↓
API Endpoints
```

---

# Near-Term Evolution (Epic #21)

```
CSV Dataset
    ↓
Idempotent Ingestion Pipeline
    ↓
Canonical Domain Model
    ↓
Relational Database (source of truth)
    ↓
Read Models
    ↓
API Endpoints
```

Focus:

* idempotent ingestion
* no duplicate records
* strong relationships
* DB-backed reads

---

# Future Architecture (Planned)

```
Spring Boot API
        ↓
Analytics Layer
        ↓
React Dashboard
```

---

# Key Principles

## Single Deployable (Current)

* One Spring Boot service
* Ingestion is internal

## Separation of Concerns

Ingestion → Domain → Persistence → API → (Future Analytics)

## Evolvability

* ingestion can be extracted later
* analytics layer can be added without breaking core system
