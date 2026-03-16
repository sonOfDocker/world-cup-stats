# Data Dictionary: FIFA World Cup Historical Match Dataset

## Purpose

This document describes the current CSV dataset used by the World Cup Stats project.

The dataset contains **historical FIFA World Cup match-level data** from **1930–2022**.

It is the initial source for:

- CSV ingestion
- source row parsing
- canonical domain modeling
- database persistence
- read APIs
- analytical queries

This dataset does **not** contain player-level event data.

---

# Dataset Scope

The dataset supports modeling the following concepts:

- Tournament
- Match
- Team
- Stadium
- City
- Country
- Stage
- Group
- Match result
- Penalty shootout summary

The dataset does **not** support modeling:

- Player
- Lineup
- Goal scorer
- Card events
- Substitutions
- Referee assignments
- Manager assignments

Those require additional datasets.

---

# Encoding Note

The CSV may contain non-UTF-8 encoded values and special score characters.

Example:
- `4–1` may appear with a special dash character

Agents should be careful when reading and parsing string fields.

---

# Column Dictionary

## Key Id
**Type:** integer  
**Example:** `1`  
**Meaning:** Internal row identifier for the dataset.

Notes:
- Useful as a raw source row key
- Should not automatically become the canonical match identifier

---

## Tournament Id
**Type:** string  
**Example:** `WC-1930`  
**Meaning:** Source identifier for a World Cup tournament.

Canonical mapping:
- `Tournament.sourceId`

---

## tournament Name
**Type:** string  
**Example:** `1930 FIFA World Cup`  
**Meaning:** Human-readable tournament name.

Notes:
- Column casing is inconsistent
- Preserve source naming in source-row model only
- Normalize in canonical model

Canonical mapping:
- `Tournament.name`

---

## Match Id
**Type:** string  
**Example:** `M-1930-01`  
**Meaning:** Source identifier for a match.

Canonical mapping:
- `Match.sourceId`

---

## Match Name
**Type:** string  
**Example:** `France v Mexico`  
**Meaning:** Human-readable match label.

Notes:
- Good for display
- Should not be the primary identity of a match

Canonical mapping:
- `Match.displayName`

---

## Stage Name
**Type:** string  
**Example:** `group stage`  
**Meaning:** Competition stage for the match.

Canonical mapping:
- `Stage.name`
- or a simple stage field on `Match` in early phases

Examples:
- group stage
- round of 16
- quarter-finals
- semi-finals
- final

---

## Group Name
**Type:** string  
**Example:** `Group 1`  
**Meaning:** Group identifier for group-stage matches.

Notes:
- May be null or not meaningful for knockout matches

Canonical mapping:
- `Match.groupName`

---

## Group Stage
**Type:** integer / boolean-like  
**Example:** `1`  
**Meaning:** Indicates whether the match is part of the group stage.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- `Match.groupStage`

---

## Knockout Stage
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether the match is part of the knockout stage.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- `Match.knockoutStage`

---

## Replayed
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether the match was replayed.

Canonical mapping:
- `Match.replayed`

---

## Replay
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether this row represents the replay match itself.

Canonical mapping:
- `Match.replay`

Notes:
- Keep both source fields until their meaning is fully validated

---

## Match Date
**Type:** string/date  
**Example:** `7/13/1930`  
**Meaning:** Match date.

Canonical mapping:
- `Match.date`

Notes:
- Parse carefully
- Confirm expected date format in tests

---

## Match Time
**Type:** string/time  
**Example:** `15:00`  
**Meaning:** Scheduled kickoff time.

Canonical mapping:
- `Match.time`

Notes:
- Time zone information may be absent

---

## Stadium Id
**Type:** string  
**Example:** `S-193`  
**Meaning:** Source identifier for the stadium.

Canonical mapping:
- `Stadium.sourceId`

---

## Stadium Name
**Type:** string  
**Example:** `Estadio Pocitos`  
**Meaning:** Stadium name.

Canonical mapping:
- `Stadium.name`

---

## City Name
**Type:** string  
**Example:** `Montevideo`  
**Meaning:** City where the match was played.

Canonical mapping:
- `Stadium.cityName`
- or `Venue.cityName`

---

## Country Name
**Type:** string  
**Example:** `Uruguay`  
**Meaning:** Country where the match was played.

Canonical mapping:
- `Stadium.countryName`
- or `Venue.countryName`

---

## Home Team Id
**Type:** string  
**Example:** `T-28`  
**Meaning:** Source identifier for home team.

Canonical mapping:
- `Team.sourceId`

---

## Home Team Name
**Type:** string  
**Example:** `France`  
**Meaning:** Home team display name.

Canonical mapping:
- `Team.name`

---

## Home Team Code
**Type:** string  
**Example:** `FRA`  
**Meaning:** Home team code.

Canonical mapping:
- `Team.code`

---

## Away Team Id
**Type:** string  
**Example:** `T-44`  
**Meaning:** Source identifier for away team.

Canonical mapping:
- `Team.sourceId`

---

## Away Team Name
**Type:** string  
**Example:** `Mexico`  
**Meaning:** Away team display name.

Canonical mapping:
- `Team.name`

---

## Away Team Code
**Type:** string  
**Example:** `MEX`  
**Meaning:** Away team code.

Canonical mapping:
- `Team.code`

---

## Score
**Type:** string  
**Example:** `4–1` or encoded variation  
**Meaning:** Human-readable score string.

Notes:
- This is a display field
- Prefer numeric score columns for logic
- Be careful with special dash characters

Canonical mapping:
- `Match.scoreDisplay`

---

## Home Team Score
**Type:** integer  
**Example:** `4`  
**Meaning:** Goals scored by home team in regulation/extra time result context.

Canonical mapping:
- `Match.homeScore`

---

## Away Team Score
**Type:** integer  
**Example:** `1`  
**Meaning:** Goals scored by away team in regulation/extra time result context.

Canonical mapping:
- `Match.awayScore`

---

## Home Team Score Margin
**Type:** integer  
**Example:** `3`  
**Meaning:** Goal difference from home team perspective.

Canonical mapping:
- derived field only, not necessarily needed in canonical model

---

## Away Team Score Margin
**Type:** integer  
**Example:** `-3`  
**Meaning:** Goal difference from away team perspective.

Canonical mapping:
- derived field only, not necessarily needed in canonical model

---

## Extra Time
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether extra time occurred.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- `Match.extraTime`

---

## Penalty Shootout
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether the match went to penalties.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- `Match.penaltyShootout`

---

## Score Penalties
**Type:** string  
**Example:** `0-0`  
**Meaning:** Display form of penalty score.

Notes:
- May be a placeholder even when no shootout occurred
- Validate semantics before relying on it

Canonical mapping:
- `Match.penaltyScoreDisplay`

---

## Home Team Score Penalties
**Type:** integer  
**Example:** `0`  
**Meaning:** Home team shootout score.

Canonical mapping:
- `Match.homePenaltyScore`

---

## Away Team Score Penalties
**Type:** integer  
**Example:** `0`  
**Meaning:** Away team shootout score.

Canonical mapping:
- `Match.awayPenaltyScore`

---

## Result
**Type:** string  
**Example:** `home team win`  
**Meaning:** Human-readable match outcome.

Canonical mapping:
- `Match.result`

Examples:
- home team win
- away team win
- draw

---

## Home Team Win
**Type:** integer / boolean-like  
**Example:** `1`  
**Meaning:** Indicates whether the home team won.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- derived field or convenience field

---

## Away Team Win
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether the away team won.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- derived field or convenience field

---

## Draw
**Type:** integer / boolean-like  
**Example:** `0`  
**Meaning:** Indicates whether the match ended in a draw.

Interpretation:
- `1` = yes
- `0` = no

Canonical mapping:
- derived field or convenience field

---

# Recommended Source Row Model

Suggested source row model name:

`WorldCupMatchCsvRow`

This model should reflect the CSV closely, including awkward source field names if necessary.

Example responsibilities:
- hold raw parsed values
- preserve source semantics
- avoid business logic beyond light parsing support

---

# Recommended Canonical Model Focus

Based on this dataset, the initial canonical model should focus on:

- Tournament
- Match
- Team
- Stadium
- Stage

Optional supporting value objects:
- Score
- PenaltyShootout
- MatchOutcome

Player should remain a **future extension**, not an initial implemented entity.

---

# Initial Validation Rules

Examples of validations agents should consider:

- `Tournament Id` must not be blank
- `Match Id` must not be blank
- `Home Team Name` must not be blank
- `Away Team Name` must not be blank
- `Home Team Score` must be numeric
- `Away Team Score` must be numeric
- `Match Date` must be parseable
- `Penalty Shootout = 0` should usually align with penalty scores being zero or ignorable

Validation should be introduced incrementally and covered by tests.

---

# Known Modeling Guidance

Use the dataset as the starting point, but do not let the CSV define the final domain model.

Preferred flow:

CSV  
→ Source Row Model  
→ Canonical Domain Model  
→ Persistence Model  
→ API DTOs

Do not:
- expose raw CSV rows directly from APIs
- treat source field names as final domain language
- introduce player-level APIs without a supporting dataset