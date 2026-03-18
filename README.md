# World Cup Stats

This project combines two passions: football (soccer) and designing data
systems end to end — from raw data ingestion to analysis and visualization.

The system is intentionally built as a deterministic data pipeline:

CSV → Source Row → Canonical Domain → Database → API → Analytics → UI

The initial dataset is FIFA World Cup data, used as a concrete foundation for
exploring data modeling, ingestion pipelines, and analytics workflows.

---

## Project Structure

This repository is organized as a simple monorepo:

- `api/` — Java-based HTTP API
    - Exposes read-only endpoints backed by persisted data
    - Follows layered architecture (controller → service → repository)

- `web/` — Frontend application (React)
    - Consumes the API only
    - Mobile-first, API-driven UI

- `data/` — Data assets
    - `raw/` contains unmodified datasets (e.g., Kaggle CSVs)
    - Treated as semi-trusted source systems

- `docs/` — Design notes, dataset documentation, and architectural decisions

---

## Current Status

- Phase 0 (Foundation): **completed**
    - repository setup
    - architecture documentation
    - ADRs and project structure

- System design is aligned around a deterministic ingestion pipeline

- Current focus:
  → building the **idempotent ingestion module**

Planned evolution:

1. Deterministic CSV ingestion (source models)
2. Canonical domain mapping
3. Idempotent persistence layer
4. Read APIs backed by database
5. Analytical queries
6. Mobile-first frontend

Note:

The API currently reads from raw CSV data as a temporary bridge.
This will be replaced by the canonical ingestion pipeline.

---

## Local Development Setup

To get started with local development, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/world-cup-stats.git
    cd world-cup-stats
    ```

2.  **Start the local database:**
    Ensure you have Docker and Docker Compose installed.
    ```bash
    docker compose up -d
    ```

    On startup, the application uses **Flyway** to automatically create the schema and seed initial data in the PostgreSQL database.

3.  **Run the Backend (API):**
    ```bash
    cd api
    ./gradlew bootRun
    ```

    The API will be available at `http://localhost:8080`.

    By default, it uses the `local` profile, which connects to the PostgreSQL database.

4.  **Verify Ingestion & Migrations:**
    ```bash
    ./gradlew integrationTest
    ```

    This validates database connectivity and schema migrations.

5.  **Quality Checks:**
    ```bash
    ./gradlew check
    ```

    *Unit tests use an in-memory H2 database initialized via Flyway to match the production schema.*

---

## Design Principles

- Deterministic data processing (same input → same output)
- Idempotent ingestion (safe to re-run without duplication)
- Clear separation of source, domain, and persistence models
- API-driven architecture (no direct data access from UI)
- Incremental, phase-based system evolution

---

## Why This Project Exists

This project is designed to demonstrate:

- Data pipeline design and ingestion strategies
- Domain modeling and normalization
- API and service-layer architecture
- Analytical query development
- End-to-end system thinking

It reflects how real-world data platforms evolve from raw data to user-facing applications.