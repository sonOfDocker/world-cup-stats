# Canonical Model: Team

## Purpose
The canonical `Team` model represents a national team participating in FIFA World Cup matches.

The raw dataset provides team names as strings. This canonical model establishes:
- A stable internal identifier (`team_id`)
- A preferred display name
- A place to record historical naming variants

---

## Canonical Fields

### Required
| Field | Type | Description |
|------|------|-------------|
| team_id | string | Stable internal identifier |
| name | string | Preferred display name for the team |

### Optional
| Field | Type | Description |
|------|------|-------------|
| fifa_code | string | 3-letter FIFA code (if available later) |
| iso_code | string | ISO country code (if available later) |
| aliases | array[string] | Known historical or alternate names |
| active_from_year | integer | First year name/identity applies (if modeled later) |
| active_to_year | integer | Last year name/identity applies (if modeled later) |

---

## Identifier Strategy (v1)
- `team_id` is a stable opaque string.
- For v1, `team_id` may be derived from a normalized team name (deterministic) OR stored as a lookup table once created.
- Once assigned, a `team_id` must never change.

**Important:** v1 assumes that raw team strings map to a single canonical team unless explicitly split later.

---

## Normalization Rules (v1)

### 1) Trim + whitespace normalization
- Trim leading/trailing whitespace
- Collapse repeated internal whitespace to a single space

### 2) Case normalization
- Case-insensitive comparison for matching purposes
- Preserve preferred casing in `name`

### 3) Punctuation normalization (matching only)
For matching and lookup, ignore:
- periods
- commas
- extra hyphens

(Do not remove meaningful characters from the stored `name`.)

### 4) Alias handling
If a raw team string is not found as an exact canonical `name`,
attempt matching against `aliases`.

### 5) Unknown / ambiguous values
If a raw string cannot be confidently mapped:
- Create a new Team record with `name = raw value`
- Add a note in a future “exceptions” list (to be defined)
- Do not block ingestion

---

## Examples (Illustrative)
These are examples of the types of cases we expect to handle:

- `"USA"` vs `"United States"`
- `"Korea Republic"` vs `"South Korea"`
- Historical variants that may require aliases

Exact alias lists are out of scope for v1 and will be built iteratively.

---

## Invariants
- `name` must be non-empty
- `team_id` is unique
- No two teams may share the same canonical `name` (case-insensitive)

---

## Source Lineage (v1)
Primary source for v1 team strings:
- Raw Kaggle matches dataset (HomeTeam, AwayTeam)

Future enrichment sources may add:
- FIFA codes
- ISO codes
- Historical naming timelines
