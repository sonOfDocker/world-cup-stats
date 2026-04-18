# AI Story Template

## Purpose

This template defines the standard structure for all stories used by AI agents in the development workflow.

The goal is to ensure stories are:
- Clear
- Testable
- Unambiguous
- Implementation-ready

---

## Template

### Title

`[STORY] <Concise, action-oriented title>`

---

### Description

As a <user/developer>,  
I want <capability>,  
so that <outcome/value>.

---

### Acceptance Criteria

- Clearly defined, testable behaviors
- Each criterion must be verifiable
- Avoid vague language

#### Example Format

- When <condition>, the system <behavior>
- Given <input>, the system returns <output>
- Re-running <process> does not create duplicate records

---

### Technical Notes

- Implementation guidance (if necessary)
- References to:
    - Domain models
    - Contracts
    - Existing services
- Constraints or assumptions

---

### Constraints (Optional)

- Performance requirements
- Data integrity rules
- Technology restrictions

---

### Definition of Done

- All acceptance criteria are satisfied
- Code compiles and runs
- Tests are implemented and passing
- No major code smells or architectural violations
- Changes are committed and documented

---

## Example Story

### Title

`[STORY] Persist Match Data from Canonical Model`

---

### Description

As a developer,  
I want to persist match data from the canonical model into the database,  
so that ingested data is stored reliably and can be queried later.

---

### Acceptance Criteria

- Given a valid canonical match, it is persisted in the database
- Re-running ingestion does not create duplicate match records
- Match is correctly linked to:
    - Tournament
    - Teams
    - Venue
- Invalid data does not break the ingestion pipeline

---

### Technical Notes

- Use Spring Data JPA repositories
- Ensure idempotency via unique constraints or lookup strategy
- Follow existing entity mappings

---

### Definition of Done

- Matches are persisted correctly
- Idempotency is verified via tests
- Integration test confirms relationships