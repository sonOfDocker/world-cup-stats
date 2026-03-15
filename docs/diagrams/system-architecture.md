# System Architecture Diagram

## Overview

This document provides a visual overview of the architecture for the **World Cup Stats Platform**.

The system is designed as a layered analytics platform that ingests historical FIFA World Cup data, converts it into a canonical domain model, exposes the data through a REST API, and eventually powers an analytics dashboard.

The architecture intentionally mirrors patterns used in real production systems.

---

# High-Level Architecture

```
           +-------------------------------+
           |  FIFA World Cup Dataset      |
           |  (Kaggle CSV Files)          |
           +---------------+--------------+
                           |
                           v
                +-------------------------+
                | Data Ingestion Pipeline |
                | CSV Parsing + Mapping   |
                +-----------+-------------+
                            |
                            v
                +-------------------------+
                | Canonical Domain Model  |
                | Team, Match, Tournament |
                +-----------+-------------+
                            |
                            v
                +-------------------------+
                | Relational Database     |
                | (PostgreSQL - future)   |
                +-----------+-------------+
                            |
                            v
                +-------------------------+
                | Spring Boot API         |
                | REST Endpoints          |
                +-----------+-------------+
                            |
                            v
                +-------------------------+
                | Analytics Layer         |
                | Aggregations & Metrics  |
                +-----------+-------------+
                            |
                            v
                +-------------------------+
                | React Dashboard         |
                | Charts & Visualizations |
                +-------------------------+
```

---

# Data Flow

The system processes data through several stages.

```
Raw Dataset
    ↓
Ingestion Pipeline
    ↓
Canonical Domain Model
    ↓
API Layer
    ↓
Analytics Layer
    ↓
Visualization Layer
```

Each stage has a well-defined responsibility and remains isolated from the others.

---

# Component Breakdown

## Data Source Layer

The system initially consumes historical data from the **FIFA World Cup Kaggle dataset**.

Example files:

```
WorldCupMatches.csv
WorldCupPlayers.csv
WorldCups.csv
```

These files represent the raw data input layer.

---

## Data Ingestion Layer

The ingestion pipeline transforms raw CSV records into domain entities.

Responsibilities include:

- reading CSV files
- parsing rows
- validating fields
- mapping records into domain objects

Example pipeline:

```
CSV Files
   ↓
CSV Reader
   ↓
Parsed Records
   ↓
Domain Entities
```

The ingestion layer isolates raw datasets from the rest of the application.

---

## Canonical Domain Model

The domain model represents the core football concepts used throughout the system.

Primary entities:

```
Team
Match
Tournament
Player
MatchStatistics
```

Example relationship:

```
Tournament
   └── Matches

Match
   ├── Home Team
   ├── Away Team
   └── Match Statistics
```

This canonical model ensures that the rest of the application does not depend directly on the raw dataset format.

---

## Persistence Layer (Future)

Future iterations of the platform will persist canonical entities in a relational database.

Proposed technology:

```
PostgreSQL
```

Updated flow:

```
CSV Dataset
   ↓
Ingestion Pipeline
   ↓
Domain Model
   ↓
PostgreSQL Database
```

This allows the API to efficiently query historical data.

---

## API Layer

The backend API exposes the platform’s data through REST endpoints.

Example endpoints:

```
GET /teams
GET /matches
GET /tournaments
GET /analytics/top-teams
```

The API follows a layered structure:

```
Controller
   ↓
Service
   ↓
Repository
```

Responsibilities include:

- serving data to external clients
- applying business logic
- formatting API responses

---

## Analytics Layer

The analytics layer computes higher-level insights from match data.

Example analytics include:

- team performance metrics
- goal statistics
- tournament trends
- historical comparisons

Example analytics endpoints:

```
GET /analytics/team-performance
GET /analytics/goals-per-tournament
GET /analytics/top-teams
```

This layer enables the platform to move beyond simple data retrieval and support sports analytics use cases.

---

## Visualization Layer (Future)

The final phase of the project introduces a web-based analytics dashboard.

Planned technology:

```
React
```

Possible features include:

- team performance charts
- tournament comparisons
- historical match statistics
- goal distribution graphs

Example architecture:

```
Spring Boot API
      ↓
React Dashboard
      ↓
Interactive Charts
```

Visualization libraries may include:

- Recharts
- Chart.js
- D3.js

---

# Architecture Goals

The platform architecture was designed with several goals in mind.

## Separation of Concerns

Each layer has a clearly defined responsibility.

```
Ingestion → Domain → API → Analytics → Visualization
```

---

## Extensibility

New datasets, analytics features, and APIs can be added without major architectural changes.

---

## Maintainability

The layered architecture ensures that components remain loosely coupled and easier to evolve over time.

---

## Real-World Engineering Practices

The project mirrors patterns commonly used in production systems including:

- layered backend architecture
- canonical domain modeling
- data ingestion pipelines
- analytics aggregation layers

---

# Summary

The World Cup Stats Platform is structured as a modular analytics system that ingests historical football data and exposes it through a scalable API and analytics layer.

The architecture supports:

- structured data ingestion
- domain-driven modeling
- REST API access
- analytics computation
- future visualization dashboards

This design allows the project to evolve from a simple dataset API into a full sports analytics platform.