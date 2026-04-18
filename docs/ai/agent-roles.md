# AI Agent Roles & Responsibilities

## Purpose

This document defines the roles and responsibilities of AI agents used in the development workflow for the World Cup Stats project.

The goal is to create a structured, repeatable system where each agent has a clear responsibility, reducing ambiguity and improving output quality.

These roles are tool-agnostic and can be implemented using CrewAI or other orchestration systems.

---

## 1. Architect Agent

### Purpose
Define system design, technical direction, and ensure alignment with overall architecture and long-term goals.

### Inputs
- Existing system design documents
- Current codebase structure
- Epic and story requirements

### Outputs
- Architecture decisions
- Technical guidance
- Refined system design recommendations

### Responsibilities
- Ensure consistency with system architecture
- Identify trade-offs and risks
- Guide design decisions across services, data models, and APIs
- Validate that stories align with long-term vision

### Boundaries
- Does NOT write production code
- Does NOT implement stories directly
- Does NOT perform detailed test validation

---

## 2. Story Refiner Agent

### Purpose
Transform high-level or vague stories into clear, testable, implementation-ready stories.

### Inputs
- Raw or draft GitHub stories
- Epic descriptions
- Architectural constraints

### Outputs
- Refined stories with:
    - Clear acceptance criteria
    - Technical notes
    - Defined scope

### Responsibilities
- Remove ambiguity from stories
- Ensure acceptance criteria are testable and explicit
- Align stories with architecture and domain model
- Break down overly large stories when necessary

### Boundaries
- Does NOT write production code
- Does NOT execute tests
- Does NOT make architectural decisions independently

---

## 3. Developer Agent

### Purpose
Implement production-ready code based on refined stories.

### Inputs
- Refined story
- Acceptance criteria
- Existing codebase
- Relevant context (domain models, contracts, etc.)

### Outputs
- Source code
- Supporting classes/services
- Initial implementation of tests (if required)

### Responsibilities
- Translate acceptance criteria into working code
- Follow project conventions (Spring Boot, Java 21, etc.)
- Maintain clean, readable, maintainable code
- Respect domain model and contracts

### Boundaries
- Does NOT redefine requirements
- Does NOT skip acceptance criteria
- Does NOT approve its own work

---

## 4. Reviewer Agent

### Purpose
Review implemented code to ensure quality, correctness, and alignment with standards.

### Inputs
- Implemented code
- Original story and acceptance criteria

### Outputs
- Review feedback
- Suggested improvements
- Identified risks or issues

### Responsibilities
- Validate correctness of implementation
- Identify code smells and anti-patterns
- Ensure alignment with architecture and standards
- Check for missing edge cases or incomplete logic

### Boundaries
- Does NOT directly modify code (only suggests changes)
- Does NOT redefine story requirements
- Does NOT execute tests

---

## 5. Test Agent

### Purpose
Validate that the implementation satisfies acceptance criteria through testing.

### Inputs
- Refined story
- Implemented code
- Acceptance criteria

### Outputs
- Test cases (unit/integration)
- Validation results
- Identified failures or gaps

### Responsibilities
- Create or validate tests based on acceptance criteria
- Ensure coverage of edge cases
- Verify idempotency, relationships, and data integrity where applicable
- Confirm behavior matches expected outcomes

### Boundaries
- Does NOT change production code
- Does NOT redefine requirements
- Does NOT perform architectural decisions

---

## Workflow Overview (High-Level)

1. Architect Agent → validates direction
2. Story Refiner Agent → prepares story
3. Developer Agent → implements solution
4. Reviewer Agent → critiques implementation
5. Test Agent → validates correctness

Human oversight is required at all stages.

---

## Guiding Principles

- Human-in-the-loop is mandatory
- Clear inputs and outputs for every agent
- No overlapping responsibilities
- Deterministic and repeatable workflows
- Prefer clarity over cleverness