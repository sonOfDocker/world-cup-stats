# 🤖 Agent Roles & Responsibilities (Enforced)

---

## 🧠 Core Principle

> Responsibility is separated by **phase**, not artifact type.

- Test Agent defines **what should be tested**
- Developer Agent implements **code and tests required to pass**
- Reviewer validates **quality and completeness**
- No role has ambiguous ownership

---

## 🔁 Workflow (Iterative, Not Linear)

1. Architect / Story Refiner
2. Test Agent (Test Strategy + Optional Red Phase)
3. Developer Agent (Implementation + Tests)
4. Test Agent (Validation Review)
5. Reviewer Agent (Final Review)

### Iteration Rule

If issues are found:
- Test Agent → sends back to Developer
- Reviewer → sends back to Developer

This loop continues until:
- All acceptance criteria are met
- Tests are valid and meaningful
- Code quality meets standards

A story is NOT complete until all agents approve.

---

## 📘 Architect / Story Refiner Agent

### Responsibilities
- Define:
  - Scope
  - Acceptance criteria
  - Technical constraints
- Select test approach (TDD / Integration / Mixed)

### Boundary
- DOES NOT participate in implementation
- ONLY engaged for:
  - New patterns
  - Domain model changes
  - Architectural decisions

---

## 🧪 Test Agent

### Responsibilities

Owns:
- Test strategy
- Test scenario design
- Coverage validation

### Before Implementation
- Define:
  - Happy path
  - Edge cases
  - Negative cases
  - Regression risks
- Optionally provide failing tests (Red Phase)

### After Implementation
- Validate:
  - All acceptance criteria are tested
  - Edge cases are covered
  - Tests are meaningful (not superficial)

### Boundary

- DOES NOT own final test implementation
- DOES NOT maintain test files long-term
- DOES NOT implement production code

> The Test Agent defines correctness, not code.

---

## 💻 Developer Agent

### Responsibilities

Owns:
- Production code
- Test implementation required for completion

### During Implementation
- Write and update tests as needed to:
  - Satisfy acceptance criteria
  - Support refactoring
  - Achieve passing state

### Boundary

- MUST NOT ignore Test Agent strategy
- MUST ensure:
  - Tests pass
  - Tests validate real behavior

> The Developer Agent owns delivery of working, tested code.

---

## 🔍 Reviewer Agent

### Responsibilities

Owns final validation of:

- Code quality
- Design alignment
- Test quality
- Story completeness

### What Reviewer MUST Do

- Run the build
- Execute tests
- Verify:
  - Code compiles
  - Tests pass
  - Behavior matches acceptance criteria

### Boundary

- DOES NOT rewrite large portions of code
- DOES NOT introduce new features

> Reviewer validates execution, not just inspection.

---

## ⚖️ Responsibility Summary

| Area                  | Owner            |
|-----------------------|------------------|
| Test Strategy         | Test Agent       |
| Test Implementation   | Developer Agent  |
| Production Code       | Developer Agent  |
| Test Validation       | Test Agent       |
| Execution Verification| Reviewer Agent   |
| Architecture Decisions| Architect Agent  |

---

## 🚫 Anti-Patterns

DO NOT:
- Split tests and code ownership rigidly
- Allow Reviewer to approve without running tests
- Allow stories to complete without iteration

---

## ✅ Final Rule

> A story is complete ONLY when:
- Acceptance criteria are satisfied
- Tests pass and are meaningful
- All agents approve