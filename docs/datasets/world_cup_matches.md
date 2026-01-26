# World Cup Matches Dataset

## Dataset Overview

- **Name:** FIFA World Cup Matches (1930–2022)
- **Competition:** Men’s FIFA World Cup (Senior)
- **Coverage:** 1930 – 2022
- **Granularity:** Match-level
- **Primary Source:** Kaggle
- **Original Author:** Jahaidul Islam
- **Format:** CSV
- **Update Frequency:** Static (historical)

This dataset serves as the **foundational dataset** for the World Cup Stats project.  
All downstream entities (teams, tournaments, venues, goals, players) will ultimately reference matches.

---

## Source & Licensing

- **Source Platform:** Kaggle
- **Dataset Title:** *FIFA World Cup 1930–2022 All Match Dataset*
- **License:** CC0: Public Domain (As stated on Kaggle)
- **Usage:** Personal, educational, and portfolio use

The raw dataset is stored **unchanged** to preserve provenance and enable reproducibility.

---

## Raw File Location
data/raw/kaggle/
fifa_world_cup_1930_2022_all_matches.csv


> ⚠️ The raw file is treated as immutable.  
> No column renaming, cleaning, or normalization is performed at this stage.

---

## Source Schema (As-Is)

The following table documents the columns exactly as provided by the source dataset.

| Column Name | Observed Type | Description / Notes |
|------------|---------------|---------------------|
| Year | integer | Tournament year |
| Datetime | string | Match date & time (requires parsing) |
| HomeTeam | string | Home team country |
| AwayTeam | string | Away team country |
| HomeGoals | integer | Goals scored by home team (incl. extra time) |
| AwayGoals | integer | Goals scored by away team (incl. extra time) |
| Stadium | string | Stadium name |
| City | string | Host city |
| Country | string | Host country |
| Round | string | Tournament stage (Group, Quarterfinal, Final, etc.) |
| Attendance | integer | Match attendance (nullable) |

> Notes:
> - Team names are provided as human-readable strings.
> - Goals appear to include extra-time goals; penalty shootouts are not reflected in goal counts.
> - Some fields (e.g., Attendance) may be missing for early tournaments.

---

## Known Limitations & Assumptions

- No globally unique match identifier is provided.
- Team names may vary historically (e.g., country naming conventions).
- Kickoff times may be missing or inconsistent for early tournaments.
- Penalty shootout details are not explicitly modeled.

These limitations will be addressed **in the canonical schema layer**, not in the raw source.

---

## Role in System Architecture

This dataset represents the **raw ingestion layer** (“bronze” layer).

Planned downstream steps:
1. Map source fields to a canonical match schema
2. Generate stable internal identifiers
3. Normalize teams, tournaments, and venues
4. Support analytics, APIs, and visualizations

---

## Related Datasets (Planned)

- World Cup goals (event-level)
- World Cup teams metadata
- World Cup venues
- Wikipedia-sourced enrichment data

These will be reconciled against this dataset as the primary match backbone.

---

## Change Log

- **v1.0** – Initial dataset documentation and raw ingestion

