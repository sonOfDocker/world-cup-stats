# Data Ingestion Architecture

## Overview

The data ingestion layer is responsible for transforming raw external datasets into a clean canonical domain model used by the World Cup Stats platform.

The system initially ingests historical FIFA World Cup data from CSV files and converts these records into structured domain entities used by the backend API.

This architecture ensures that raw datasets remain separate from the application's core business model while enabling future extensibility for additional data sources.

---

# Data Source

The initial dataset used by the platform is the **FIFA World Cup historical dataset from Kaggle**.

The dataset includes structured CSV files containing information about:

- tournaments
- matches
- teams
- goals
- match statistics

Example dataset structure:
    
    WorldCupMatches.csv
    WorldCupPlayers.csv
    WorldCups.csv


These files represent the raw input layer of the platform.

Raw data is treated as **immutable input** and is not modified directly by the application.

---

# Ingestion Pipeline

The ingestion pipeline converts raw CSV records into structured domain entities.

Initial pipeline flow:

    CSV Files
        ↓
    CSV Parser
        ↓
    Record Mapping
        ↓
    Canonical Domain Model
        ↓
    API Access


Future pipeline flow:

    CSV Files
        ↓
    CSV Parser
        ↓
    Data Validation
        ↓
    Canonical Domain Model
        ↓
    Relational Database
        ↓
    Spring Boot API


---

# Pipeline Stages

## Stage 1 — Data Extraction

The extraction stage reads CSV files from the dataset.

Responsibilities include:

- locating dataset files
- reading file contents
- converting rows into structured records

Example pseudo-flow:

    CSV File
        ↓
    CSV Reader
        ↓
    Row Objects

This stage is responsible only for reading data and does not perform domain logic.

---

## Stage 2 — Data Parsing

The parsing stage converts raw CSV rows into structured intermediate representations.

Responsibilities include:

- mapping column values
- converting data types
- handling missing values

Example transformation:

    CSV Row
    HomeTeam = "Brazil"
    AwayTeam = "Italy"
    HomeGoals = "3"
    AwayGoals = "2"

        ↓

    Parsed Record
    homeTeam: "Brazil"
    awayTeam: "Italy"
    homeGoals: 3
    awayGoals: 2


Parsing logic should remain isolated from the core domain model.

---

## Stage 3 — Domain Mapping

Parsed records are mapped into the platform's canonical domain entities.

Example mapping:

    Parsed Match Record
        ↓
    Match Domain Entity


Example domain entity:

    Match

    - id
    - tournament
    - homeTeam
    - awayTeam
    - homeGoals
    - awayGoals
    - matchDate
    - stadium


This stage ensures that the internal domain model remains stable even if raw datasets change structure.

---

# Canonical Data Model

The platform uses a canonical domain model representing the core concepts of international football tournaments.

Key entities include:
- Team
- Match
- Tournament
- Player
- MatchStatistics

Relationships between entities:
    
    Tournament
    └── Matches

    Match
    ├── Home Team
    ├── Away Team
    └── Match Statistics


The canonical model acts as the **single source of truth** for the application.

This abstraction ensures that the system remains resilient to changes in raw data formats.

---

# Data Validation

Future iterations of the ingestion pipeline will introduce validation checks to ensure data quality.

Examples include:

- missing team names
- invalid score values
- malformed dates
- duplicate match records

Validation may occur during parsing or domain mapping.

---

# Persistence Strategy (Future Phase)

The initial implementation of the system may operate directly on parsed data in memory.

Later phases will introduce a relational database to persist canonical entities.

Future architecture:

    CSV Dataset
        ↓
    Ingestion Pipeline
        ↓
    Canonical Domain Model
        ↓
    Relational Database (PostgreSQL)
        ↓
    Spring Boot API


This approach allows the API layer to query structured data efficiently while keeping ingestion logic separate.

---

# Incremental Dataset Loading

Future enhancements may include support for:

- incremental dataset updates
- scheduled ingestion jobs
- data versioning

This will enable the platform to ingest new datasets or match data without rebuilding the entire database.

---

# Error Handling Strategy

The ingestion system should gracefully handle malformed or incomplete records.

Examples:

- skip invalid rows
- log ingestion errors
- continue processing remaining data

This ensures that ingestion pipelines remain resilient even when datasets contain inconsistencies.

---

# Data Storage Layers

The platform separates data into logical layers:

## Raw Data Layer

Stores unmodified source datasets.

Example location:

    data/raw/

---

## Processed Data Layer

Stores parsed or transformed data ready for ingestion.

Example location:

    data/processed/

---

## Application Data Layer

Stores canonical domain entities used by the API.

Future implementation:
- PostgreSQL database



---

# Technology Choices

The ingestion pipeline prioritizes simplicity and transparency.

Initial technologies include:

- Java CSV parsing libraries
- Spring Boot service components

Future improvements may introduce:

- Python data processing scripts
- batch ingestion pipelines
- containerized ingestion services

---

# Design Principles

The ingestion architecture follows several key design principles.

### Separation of Concerns

Raw data ingestion should remain separate from the API layer and domain logic.

---

### Canonical Domain Model

All external datasets must be mapped into a stable internal representation.

---

### Extensibility

The ingestion pipeline should support additional data sources without major architectural changes.

---

### Data Transparency

Raw datasets should remain accessible and traceable for debugging and verification.

---

# Summary

The data ingestion layer converts raw FIFA World Cup datasets into a structured domain model used by the World Cup Stats platform.

The architecture ensures that:

- raw datasets remain immutable
- domain entities remain stable
- ingestion logic is isolated from API concerns
- future data sources can be integrated easily

This design enables the platform to evolve from a simple CSV ingestion system into a full analytics platform supporting persistent storage and advanced data processing.
