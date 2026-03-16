# ADR 002: Use Flyway for Database Schema Migrations

## Status

Accepted

## Context

The World Cup Stats project requires a consistent strategy for managing database schema creation and evolution during development.

The project will store canonical domain data derived from historical FIFA World Cup match datasets. As the application evolves, new tables and schema changes will need to be introduced in a controlled and repeatable way.

Without a migration strategy, developers may:

- create schema manually
- run ad-hoc SQL scripts
- end up with inconsistent database states across environments

This becomes especially problematic when:

- multiple developers work on the project
- schema evolves frequently
- CI environments require predictable database initialization

Because the project aims to demonstrate **production-style engineering practices**, schema evolution should be version-controlled and automated.

## Decision

The project will use **Flyway** to manage database schema migrations.

Flyway will:

- run automatically when the Spring Boot application starts
- apply versioned SQL migration files in order
- track applied migrations using a schema history table
- ensure all environments run the same schema structure

Migration files will be stored in:

    src/main/resources/db/migration

Migration files will follow Flyway naming conventions, for example:

    V1__initial_schema.sql
    V2__add_team_table.sql
    V3__add_match_table.sql

## Consequences

### Positive

- Schema evolution becomes version controlled.
- Local development environments remain consistent.
- CI environments can initialize databases automatically.
- The approach reflects common production practices used in Spring Boot services.
- Database changes are documented through migration history.

### Negative

- Developers must create migrations for schema changes rather than modifying the database manually.
- Migration ordering must be managed carefully as the project grows.

These tradeoffs are acceptable and typical for modern backend services.

## Alternatives Considered

### Hibernate Auto-DDL

Spring Boot can automatically generate schema from entity classes.

This approach was rejected because:

- schema changes are not version controlled
- database state becomes harder to reproduce
- schema drift may occur between environments

### Manual SQL Initialization Scripts

Running raw SQL scripts manually was also considered.

This approach was rejected because:

- schema updates become difficult to track
- scripts may be applied inconsistently
- ordering and versioning become error-prone

## Resulting Architecture

Database schema changes will follow this workflow:

    Developer creates migration file
        ↓
    Migration committed to repository
        ↓
    Flyway runs on application startup
        ↓
    Database schema updated automatically
        ↓
    Schema history table records migration

This ensures all environments share the same schema state.

## Related Stories

- Story #40 — Docker Compose Postgres setup
- Story #42 — Local environment configuration
- Story #43 — Verify Spring Boot database connectivity
- Story #44 — Establish database initialization strategy