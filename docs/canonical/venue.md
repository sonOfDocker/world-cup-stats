# Canonical Model: Venue

## Purpose
The canonical `Venue` model represents the location where a World Cup match was played.

The raw dataset contains venue details as strings (Stadium, City, Country). This canonical model establishes:
- A stable internal identifier (`venue_id`)
- A preferred stadium name and location fields
- A place to record naming variants and inconsistencies

---

## Canonical Fields

### Required
| Field | Type | Description |
|------|------|-------------|
| venue_id | string | Stable internal identifier |
| stadium_name | string | Preferred stadium/stage name |
| city | string | Host city |
| country | string | Host country |

### Optional
| Field | Type | Description |
|------|------|-------------|
| aliases | array[string] | Known alternate stadium names |
| latitude | number | If enriched later |
| longitude | number | If enriched later |

---

## Identifier Strategy (v1)
- `venue_id` is a stable opaque string.
- For v1, `venue_id` may be derived deterministically from a normalized tuple:
  `(stadium_name, city, country)` OR stored via a lookup table once created.
- Once assigned, a `venue_id` must never change.

---

## Normalization Rules (v1)

### 1) Trim + whitespace normalization
- Trim leading/trailing whitespace
- Collapse repeated internal whitespace to a single space

### 2) Case normalization
- Case-insensitive matching
- Preserve preferred casing in stored fields

### 3) Stadium punctuation normalization (matching only)
For matching and lookup, ignore:
- periods
- commas
- extra hyphens

(Do not remove meaningful characters from stored `stadium_name`.)

### 4) Composite identity
A venue is identified by **stadium + city + country**, not stadium name alone.

Rationale:
- Stadium names may repeat across countries/cities
- Stadium renames can be handled through aliases without changing identity

### 5) Missing or partial values
If a raw row is missing one or more venue fields:
- Still ingest the match
- Set venue fields to best-known values
- Leave `venue_id` null in the canonical Match if necessary (v1 allowed)

---

## Invariants
- `stadium_name` must be non-empty when `venue_id` is present
- `city` and `country` must be non-empty when `venue_id` is present
- `venue_id` is unique
- `(stadium_name, city, country)` should be unique under case-insensitive comparison

---

## Source Lineage (v1)
Primary source for v1 venue strings:
- Raw Kaggle matches dataset (Stadium, City, Country)

Future enrichment sources may add:
- Geocoding (lat/long)
- Stadium renaming timelines
- Capacity metadata
