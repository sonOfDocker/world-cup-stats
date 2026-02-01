# Canonical Model: Match

## Purpose
The canonical `Match` model is the stable, internal representation of a FIFA World Cup match.
It is designed to be:
- Consistent across tournaments (1930–present)
- Friendly to analytics and APIs
- Independent of any single source dataset’s column names

This document defines the canonical fields and their meaning.

---

## Canonical Identifier Strategy (v1)
A match will have a stable internal identifier:

- `match_id`: string (opaque, stable)

**Generation is deferred** to implementation, but the identifier must be:
- Deterministic from authoritative match attributes OR stored once and reused
- Unique across all tournaments

---

## Canonical Fields

### Required
| Field | Type | Description |
|------|------|-------------|
| match_id | string | Stable internal identifier |
| tournament_year | integer | Tournament year (e.g., 2014) |
| kickoff_datetime | string (ISO-8601) | Match kickoff datetime (normalized) |
| tournament_round | string | Stage/round (e.g., Group Stage, Quarter-finals) |
| home_team_id | string | Reference to canonical Team |
| away_team_id | string | Reference to canonical Team |
| home_goals | integer | Goals by home team (incl. extra time if applicable) |
| away_goals | integer | Goals by away team (incl. extra time if applicable) |

### Optional
| Field | Type | Description |
|------|------|-------------|
| venue_id | string | Reference to canonical Venue |
| attendance | integer | Nullable for historical gaps |
| extra_time_played | boolean | True if match went to extra time (if derivable) |
| decided_by_penalties | boolean | True if match decided by penalty shootout (if derivable) |

---

## Field Semantics & Notes

### kickoff_datetime
- Stored in ISO-8601 format
- Time zone normalization rules will be defined during ingestion
- If historical kickoff time is missing/ambiguous, store best-known value and capture uncertainty later if needed

### goals
- `home_goals` and `away_goals` reflect the final score including extra time goals
- Penalty shootout scores are not represented in these fields

### tournament_round
- A controlled vocabulary will be introduced later
- For now, store as a normalized string

---

## Invariants
These rules must always hold:
- `home_goals >= 0` and `away_goals >= 0`
- `home_team_id != away_team_id`
- `tournament_year` is a 4-digit year between 1930 and present
- If `decided_by_penalties = true`, then `tournament_round` cannot be a group-stage match (subject to historical validation)

---

## Source Lineage (v1)
Primary authoritative source for v1:
- Raw Kaggle matches dataset (1930–2022)

Future enrichment sources may add:
- Better venue metadata
- Normalized team naming history
- Extra time / penalty indicators
