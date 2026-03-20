# Match: Raw → Canonical Mapping Contract

## Purpose
This document defines how fields from the **raw World Cup matches dataset**
map into the project’s **canonical Match entity**.

It establishes:
- Which raw fields are considered authoritative
- Which fields require transformation or normalization
- Which concepts are deferred to downstream enrichment

No transformations are implemented here.  
This is a declarative contract only.

---

## Canonical Entity: Match (Conceptual)

A canonical `Match` represents a single World Cup match played
between two national teams at a specific place and time.

Each match will eventually have:
- A stable internal identifier
- Normalized team, tournament, and venue references
- Consistent temporal representation

---

## Raw → Canonical Field Mapping

| Raw Source Column | Canonical Field | Authority | Notes |
|------------------|----------------|-----------|-------|
| Match Id | source_id | Authoritative | Primary stable source ID (e.g., M-1930-01) |
| Tournament Id | tournament_id | Authoritative | Source tournament reference |
| tournament Name | tournament_name | Authoritative | Normalize to Tournament entity |
| Stage Name | tournament_round | Authoritative | Map to Stage.name |
| Match Date | kickoff_datetime (part) | Authoritative | Combine with Match Time to ISO-8601 |
| Match Time | kickoff_datetime (part) | Authoritative | Combine with Match Date to ISO-8601 |
| Home Team Name | home_team_name | Authoritative | Normalize to Team entity |
| Home Team Code | fifa_code (home) | Authoritative | Map to Team.fifa_code |
| Away Team Name | away_team_name | Authoritative | Normalize to Team entity |
| Away Team Code | fifa_code (away) | Authoritative | Map to Team.fifa_code |
| Home Team Score | home_goals | Authoritative | Includes extra-time goals |
| Away Team Score | away_goals | Authoritative | Includes extra-time goals |
| Stadium Name | stadium_name | Authoritative | Normalized to Venue.stadium_name |
| City Name | city | Authoritative | Map to Venue.city |
| Country Name | country | Authoritative | Map to Venue.country |
| Attendance | attendance | Informational | Nullable, historical gaps |
| Extra Time | extra_time_played | Authoritative | Binary (1/0) → Boolean |
| Penalty Shootout | decided_by_penalties | Authoritative | Binary (1/0) → Boolean |
| Home Team Score Penalties | home_penalty_score | Authoritative | Map to Match.homePenaltyScore |
| Away Team Score Penalties | away_penalty_score | Authoritative | Map to Match.awayPenaltyScore |

---

## Fields Not Present in Raw Dataset

The following canonical fields are **not available** in the raw dataset
and will be derived or enriched later:

- match_id (internal, stable)
- tournament_id
- home_team_id
- away_team_id
- venue_id
- match_status (scheduled / completed)

---

## Explicit Non-Goals

This contract does **not**:
- Define database schemas
- Generate identifiers
- Normalize team or venue names
- Infer match outcomes beyond goal counts

Those concerns belong to downstream canonical and analytics layers.

---

## Stability Guarantee

This mapping contract is stable unless:
- The raw dataset source is replaced, or
- A new authoritative raw dataset is introduced

Changes here should be rare and intentional.
