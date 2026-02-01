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
| Year | tournament_year | Authoritative | Used to derive tournament identity |
| Datetime | kickoff_datetime | Authoritative | Requires parsing into UTC timestamp |
| HomeTeam | home_team_name | Authoritative | Will later map to Team entity |
| AwayTeam | away_team_name | Authoritative | Will later map to Team entity |
| HomeGoals | home_goals | Authoritative | Includes extra-time goals |
| AwayGoals | away_goals | Authoritative | Includes extra-time goals |
| Stadium | stadium_name | Informational | Normalized later to Venue |
| City | city | Informational | Used for enrichment |
| Country | host_country | Informational | Used for enrichment |
| Round | tournament_round | Authoritative | Standardized later |
| Attendance | attendance | Informational | Nullable, historical gaps |

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
- penalty_shootout_indicator
- extra_time_indicator

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
