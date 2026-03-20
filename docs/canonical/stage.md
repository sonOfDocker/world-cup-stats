# Canonical Model: Stage

## Purpose
The canonical `Stage` (or Round) model represents the competitive stage of a match within a tournament. It helps categorize matches for analytics such as group-stage performance versus knockout progression.

---

## Canonical Fields

### Required
| Field | Type | Description |
|------|------|-------------|
| name | string | Human-readable stage name (e.g. Group stage, Round of 16, Final) |

### Optional
| Field | Type | Description |
|------|------|-------------|
| group_name | string | Specific group identifier if applicable (e.g. Group A) |
| group_stage | boolean | True if the match is part of the initial group-stage rounds |
| knockout_stage | boolean | True if the match is a knockout round (single-elimination) |

---

## Controlled Vocabulary (Proposed)
While v1 accepts raw strings, future phases will introduce a controlled vocabulary:
- `GROUP_STAGE`
- `ROUND_OF_16`
- `QUARTER_FINALS`
- `SEMI_FINALS`
- `FINAL`
- `PLAYOFF_FOR_THIRD_PLACE`

---

## Invariants
- `group_stage` and `knockout_stage` are mutually exclusive for a single match (in most historical contexts).
- `group_name` should only be present if `group_stage` is true.

---

## Source Lineage (v1)
Primary authoritative source for v1:
- Raw Kaggle matches dataset (Stage Name, Group Name, Group Stage, Knockout Stage)
