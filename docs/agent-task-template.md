# Agent Task Template

Use this template when assigning a GitHub story to an AI agent.

---

## Story

Paste the GitHub issue title and description here.

---

## Goal

Describe the exact outcome expected from this story.

Example:
Implement CSV row parsing for the historical World Cup dataset and map rows into a source model.

---

## In Scope

List only the files, modules, or behaviors the agent should change.

Example:
- CSV ingestion package
- source row model
- parser unit tests

---

## Out of Scope

List what the agent must not change.

Example:
- REST controllers
- database schema
- analytics endpoints
- unrelated refactors

---

## Relevant Docs

- `docs/ai_context.md`
- `docs/agent-development-guide.md`
- `docs/data-dictionary.md`
- `docs/domain-model.md`

---

## Architecture Constraints

- Follow layered architecture
- Keep controllers thin
- Put business logic in services
- Do not expose domain entities directly in API responses
- Respect source model → canonical model separation

---

## Dataset Constraints

Current dataset supports:
- tournaments
- matches
- teams
- stadiums
- stage/group metadata

Current dataset does not support:
- players
- lineups
- goal events
- card events

---

## TDD Expectations

1. Write or update a failing test first
2. Implement the smallest change needed
3. Refactor only if necessary
4. Keep the change scoped to the story

---

## Acceptance Criteria

Paste the story acceptance criteria here.

---

## Deliverables

Example:
- new parser class
- source row model
- parser tests
- small documentation update if required

---

## Definition of Done

- tests pass
- code remains scoped to the issue
- architecture rules are respected
- no unrelated package changes