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
