# System Overview

## Project: World Cup Stats Platform

The World Cup Stats Platform is a portfolio architecture project demonstrating how to design and implement a production-style sports analytics system using historical FIFA World Cup data.

The system ingests raw match data, transforms it into a clean canonical domain model, exposes the data through a Spring Boot API, and will eventually power an interactive analytics dashboard.

The primary goal of the project is to showcase strong engineering practices including:

- Clean architecture
- Test-driven development (TDD)
- Incremental feature delivery
- Data ingestion pipelines
- API design
- analytics-ready domain modeling

---

# High-Level System Architecture

The system follows a layered architecture with a clear separation between data ingestion, domain modeling, API access, and future analytics capabilities.

        +-----------------------------+
        | Kaggle World Cup Dataset    |
        | (CSV Files)                 |
        +--------------+--------------+
                       |
                       v
            +-----------------------+
            | Data Ingestion Layer  |
            | CSV Parsing Pipeline  |
            +-----------+-----------+
                        |
                        v
            +-----------------------+
            | Canonical Domain      |
            | Model                 |
            +-----------+-----------+
                        |
                        v
            +-----------------------+
            | Spring Boot API       |
            | REST Endpoints        |
            +-----------+-----------+
                        |
                        v
            +-----------------------+
            | Future Analytics      |
            | & Visualization       |
            | (React Dashboard)     |
            +-----------------------+


---

# System Components

## 1. Data Source Layer

The initial data source for the system is the public **FIFA World Cup dataset available on Kaggle**.

This dataset includes historical records of:

- World Cup tournaments
- matches
- teams
- goals
- statistics

The dataset is provided as CSV files and serves as the raw input to the platform.

Future extensions may introduce:

- real-time match data
- external sports APIs
- additional historical datasets

---

## 2. Data Ingestion Layer

The ingestion layer is responsible for transforming raw data into structured domain objects used by the platform.

Initial implementation:

    CSV Files

        ↓

    CSV Parser

        ↓

    Canonical Domain Model


Responsibilities of the ingestion layer include:

- reading CSV datasets
- parsing structured records
- validating data
- mapping records to domain entities

Future iterations of the ingestion layer will support:

- database persistence
- incremental dataset updates
- data quality validation

---

## 3. Domain Model

The domain model represents the core concepts of international football tournaments and match statistics.

Key entities include:

- **Team**
- **Match**
- **Tournament**
- **Player**
- **MatchStatistics**

These entities form the canonical representation of the dataset used throughout the system.

Example relationships:

    Tournament
    └── Matches

    Match
    ├── Home Team
    ├── Away Team
    └── Match Statistics


The domain model is intentionally designed to remain independent from API and persistence concerns.

---

## 4. Backend API Layer

The backend API is implemented using **Spring Boot** and exposes the platform's data through RESTful endpoints.

Example endpoints:

    GET /teams
    GET /matches
    GET /tournaments
    GET /statistics


The API architecture follows a layered structure:
    
    Controller Layer
        ↓
    Service Layer
        ↓
    Repository Layer


### Controller Layer

Responsible for:

- defining REST endpoints
- handling HTTP requests
- returning API responses

Controllers remain thin and delegate business logic to services.

---

### Service Layer

The service layer contains the core business logic of the system.

Responsibilities include:

- coordinating data access
- performing transformations
- implementing domain logic
- preparing responses for the API layer

---

### Repository Layer

The repository layer handles persistence operations.

Initial implementation may operate on in-memory or parsed datasets.

Future iterations will integrate with a relational database.

---

### DTO Layer

Data Transfer Objects (DTOs) are used to prevent domain entities from being exposed directly through the API.

DTOs provide:

- API response formatting
- decoupling between domain model and API contract
- versioning flexibility

---

# Future System Components

The project roadmap includes several planned extensions.

## Relational Database

Later phases will introduce a relational database to persist canonical data.

Possible technologies include:

- PostgreSQL
- Dockerized database environment

Updated pipeline:

    CSV Dataset
        ↓
    Ingestion Pipeline
        ↓
    Canonical Model
        ↓
    Relational Database
        ↓
    Spring Boot API

---

## Analytics Layer

Future versions of the platform will include advanced analytics features such as:

- team performance metrics
- historical match trends
- tournament comparisons
- statistical summaries

These analytics endpoints will be exposed through the API and consumed by a frontend dashboard.

---

## Visualization Layer

The final phase of the project will introduce a web-based analytics dashboard.

Planned technology:

- React

The dashboard will consume the backend API and present:

- match statistics
- tournament summaries
- team performance data
- interactive charts

---

# Engineering Principles

The project emphasizes strong engineering discipline and production-style development workflows.

Key principles include:

## Test-Driven Development (TDD)

Features are implemented using the TDD cycle:

    Red → Green → Refactor


Each feature begins with a failing test before implementation.

---

## Clean Architecture

The system maintains strict separation between:

- controllers
- services
- repositories
- domain models

This promotes maintainability and scalability.

---

## Incremental Development

The system is developed in small, logical steps using GitHub issues and project boards.

Each commit should represent a meaningful change aligned with a feature or story.

---

# Technology Stack

### Backend

- Java 21
- Spring Boot
- Gradle Kotlin DSL

### Testing

- JUnit
- Spring Boot Test

### Data

- CSV ingestion (initial)
- relational database (future phase)

### Frontend (future)

- React

---

# Repository Structure

The repository is organized to separate documentation, services, data pipelines, and infrastructure components.

Example structure:

    docs/
    └── architecture/
    └── diagrams/

    services/
    api/
    web/

    data/
    └── raw/
    └── processed/

    infrastructure/
    docker/


---

# Project Goals

This repository aims to demonstrate the following engineering capabilities:

- designing a clean domain model
- building maintainable APIs
- implementing data ingestion pipelines
- applying TDD workflows
- structuring production-style repositories
- documenting system architecture clearly

The project is intentionally designed to evolve in phases, mirroring how real engineering systems grow over time.