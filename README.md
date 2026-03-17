# World Cup Stats

This project combines two passions: football (soccer) and designing data
systems end to end — from raw data ingestion to analysis and visualization.

The initial focus is on FIFA World Cup data, used as a concrete dataset for
exploring data modeling, service design, and analytics workflows.

## Project Structure

This repository is organized as a simple monorepo:

- `api/` — Java-based HTTP API
    - Reads semi-trusted raw datasets
    - Exposes read-only endpoints for match and tournament data

- `web/` — Frontend application (React)
    - Consumes the API only
    - No direct access to raw data files

- `data/` — Data assets
    - `raw/` contains unmodified datasets (e.g., Kaggle CSVs)
    - Treated as semi-trusted sources

- `docs/` — Design notes, dataset documentation, and architectural decisions

## Current Status

- Canonical ingestion pipeline: **parked**
- API reads directly from raw CSV data
- Focus is on building a thin, end-to-end vertical slice (API → UI)

This structure is expected to evolve as ingestion, normalization, and analytics
layers are introduced.

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
    The API will be available at `http://localhost:8080`. By default, it uses the `local` profile, which connects to the PostgreSQL database.
4.  **Verify Ingestion & Migrations:**
    Check the database connection and schema migrations by running integration tests:
    ```bash
    ./gradlew integrationTest
    ```

5.  **Quality Checks:**
    Run unit tests and static analysis:
    ```bash
    ./gradlew check
    ```
    *Note: Unit tests use an in-memory H2 database, also initialized by Flyway to match the production schema.*

For detailed instructions on the backend, see [api/README.md](api/README.md).
