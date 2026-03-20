# Canonical Model: Tournament

## Purpose
The canonical `Tournament` model represents a single FIFA World Cup competition. It provides the high-level context for all matches, teams, and venues within that specific competition.

---

## Canonical Fields

### Required
| Field | Type | Description |
|------|------|-------------|
| source_id | string | Source-specific identifier (e.g. WC-1930) |
| name | string | Human-readable tournament name (e.g. 1930 FIFA World Cup) |
| year | integer | 4-digit year of the tournament (e.g. 1930) |

---

## Identifier Strategy (v1)
- `source_id` is currently used as the primary identifier derived from the raw dataset.
- A stable internal `tournament_id` (opaque) may be introduced in future phases.

---

## Invariants
- `year` must be between 1930 and the current/near-future year.
- `name` must be non-empty.
- `source_id` is unique across the dataset.

---

## Source Lineage (v1)
Primary authoritative source for v1:
- Raw Kaggle matches dataset (Tournament Id, tournament Name)
