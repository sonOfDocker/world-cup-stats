# Domain Model

## Overview

The World Cup Stats platform is built around a canonical domain model that represents the core entities involved in FIFA World Cup tournaments.

The purpose of the domain model is to provide a stable, structured representation of tournament data that remains independent from the raw CSV dataset and from API response formats.

The domain model acts as the **single source of truth** for the platform.

All ingestion pipelines, persistence logic, API responses, and analytics computations are built on top of these core entities.

---

# Core Entities

The system models the primary concepts represented in the historical FIFA World Cup match dataset.

```text
Tournament
Team
Match
Stadium
Stage
Player (future)
MatchStatistics (future)
```

These entities are derived from the structure of the dataset and represent the main real-world concepts needed by the platform.

---

# Dataset-Driven Modeling

The historical FIFA World Cup dataset includes columns describing:

## Tournament Context

```text
Tournament Id
Tournament Name
Stage Name
Group Name
Group Stage
Knockout Stage
```

## Match Metadata

```text
Match Id
Match Date
Match Time
Stadium Id
Stadium Name
City Name
Country Name
```

## Team Information

```text
Home Team Id
Home Team Name
Home Team Code
Away Team Id
Away Team Name
Away Team Code
```

## Match Results

```text
Home Team Score
Away Team Score
Score
Result
Draw
```

## Special Match Conditions

```text
Extra Time
Penalty Shootout
Home Team Score Penalties
Away Team Score Penalties
```

The canonical domain model is designed to organize this raw dataset into clear domain concepts that can be reused throughout the application.

---

# Entity Relationships

The relationships between entities form the backbone of the platform.

```text
Tournament
   └── Matches

Match
   ├── Home Team
   ├── Away Team
   ├── Stadium
   ├── Stage
   └── Result Data

Team
   └── Participates in Matches

Stadium
   └── Hosts Matches

Stage
   └── Classifies Match Progression Within a Tournament
```

High-level entity relationship diagram:

```text
Tournament
    │
    │ 1..*
    ▼
Match
 ├── HomeTeam
 ├── AwayTeam
 ├── Stadium
 ├── Stage
 └── Result

Stadium
   └── City / Country
```

This model reflects the structure of the dataset while remaining independent from the raw CSV format.

---

# Entity Definitions

## Tournament

Represents a single FIFA World Cup competition.

Example attributes:

```text
Tournament
- id
- name
- year
```

Examples:

```text
1930 FIFA World Cup
2022 FIFA World Cup
```

A tournament contains many matches and defines the overall competition context.

---

## Team

Represents a national team participating in World Cup tournaments.

Example attributes:

```text
Team
- id
- name
- code
```

Examples:

```text
Brazil
Germany
Argentina
France
```

A team can participate in many matches across one or more tournaments.

---

## Match

Represents a single football match between two teams within a tournament.

Example attributes:

```text
Match
- id
- tournament
- stage
- stadium
- matchDate
- matchTime
- homeTeam
- awayTeam
- homeScore
- awayScore
- score
- result
- draw
- extraTime
- penaltyShootout
- homePenaltyScore
- awayPenaltyScore
```

Matches are the **central entity** of the domain model.

Most analytics and API use cases will be derived from match-level data.

---

## Stadium

Represents the physical venue where a match was played.

Example attributes:

```text
Stadium
- id
- name
- city
- country
```

Examples:

```text
Estádio Mineirão
Lusail Stadium
Maracanã
```

A stadium can host many matches across one or more tournaments.

This entity is modeled separately because the dataset includes distinct stadium and location fields.

---

## Stage

Represents the competitive stage of a match within a tournament.

Example attributes:

```text
Stage
- name
- groupName
- groupStage
- knockoutStage
```

Examples:

```text
Group Stage
Round of 16
Quarter-finals
Semi-finals
Final
```

The stage entity allows the platform to distinguish between:

- early group matches
- knockout round matches
- final-stage tournament matches

This is important for analytics such as:

- knockout performance
- stage-based comparisons
- tournament progression metrics

---

## Player (Future Phase)

Represents an individual player participating in a match or tournament.

Example attributes:

```text
Player
- id
- name
- team
- position
```

Player-level data is not the primary focus of the initial dataset implementation but may be introduced in future phases.

---

## MatchStatistics (Future Phase)

Represents statistical information associated with a match beyond final scoreline data.

Example attributes:

```text
MatchStatistics
- matchId
- possession
- shots
- shotsOnTarget
- fouls
- yellowCards
- redCards
```

This entity is reserved for future analytics expansion if richer match statistics are introduced.

---

# Supporting Value Objects

In addition to core entities, the domain may include smaller supporting structures or value objects.

Examples include:

```text
MatchResult
Location
Scoreline
```

## MatchResult

Can be derived from match data such as:

```text
homeWin
awayWin
draw
```

## Location

Can represent:

```text
city
country
```

## Scoreline

Can represent:

```text
homeGoals
awayGoals
homePenaltyGoals
awayPenaltyGoals
```

These may be modeled as embedded value objects rather than full entities, depending on implementation needs.

---

# Canonical Model Strategy

The domain model serves as the **canonical representation** of World Cup data within the system.

This means:

- raw CSV rows are mapped into domain entities
- persistence models are derived from domain entities
- API responses are derived from domain entities
- analytics computations operate on domain entities

Example pipeline:

```text
Raw Dataset
   ↓
CSV Parsing
   ↓
Canonical Domain Model
   ↓
Persistence / API / Analytics
```

This architecture protects the rest of the system from changes in the dataset structure.

---

# Separation from External Representations

The domain model is intentionally separated from:

- raw CSV column structure
- API DTOs
- frontend models
- persistence-specific schemas

Example transformation:

```text
CSV Record
   ↓
Canonical Domain Entity
   ↓
DTO
   ↓
API Response
```

This separation ensures that:

- ingestion logic stays isolated
- API contracts remain stable
- analytics logic is reusable
- persistence changes do not leak into the rest of the application

---

# Example Canonical Match Representation

Example match based on the dataset:

```text
Match
- id: 300186474
- tournament: 2014 FIFA World Cup
- stage: Semi-finals
- stadium: Estádio Mineirão
- city: Belo Horizonte
- country: Brazil
- matchDate: 2014-07-08
- homeTeam: Brazil
- awayTeam: Germany
- homeScore: 1
- awayScore: 7
- draw: false
- extraTime: false
- penaltyShootout: false
```

This canonical match becomes the foundation for:

- match endpoints
- team analytics
- tournament summaries
- stage-based reporting
- stadium-based queries

---

# Domain Model in the Architecture

The domain model sits at the center of the platform architecture.

```text
Dataset
   ↓
Ingestion Pipeline
   ↓
Canonical Domain Model
   ↓
Database (future)
   ↓
API Layer
   ↓
Analytics Layer
```

This ensures that all parts of the system operate on a consistent internal representation.

---

# Design Principles

The domain model follows several key principles.

## Stability

The domain model should remain stable even if the raw dataset evolves.

---

## Expressiveness

Entities should clearly reflect real-world football concepts represented in the data.

---

## Independence

Domain entities should not depend on:

- Spring controllers
- API DTOs
- database tables
- frontend models

---

## Reusability

The same canonical model should support:

- data ingestion
- persistence
- API responses
- analytics queries

---

# Initial Implementation Guidance

For the initial project phases, the most important entities to implement are:

```text
Tournament
Team
Match
Stadium
Stage
```

These entities are sufficient to support:

- CSV ingestion
- basic REST endpoints
- tournament and match queries
- early analytics features

Player and MatchStatistics can be deferred until a later phase.

---

# Summary

The World Cup Stats domain model defines the canonical entities and relationships used throughout the platform.

By modeling the system around the actual dataset, the platform ensures that:

- ingestion pipelines remain flexible
- domain concepts remain clear
- API responses remain stable
- analytics features can be built consistently

The introduction of `Stadium` and `Stage` makes the model more faithful to the real FIFA World Cup dataset and better supports future analytics and reporting use cases.