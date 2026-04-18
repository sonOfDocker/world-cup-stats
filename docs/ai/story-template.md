# 📘 Story Template (Enforced)

---

## 🧾 Story Title
REQUIRED

---

## 🎯 Objective

REQUIRED:
- What is being built
- Why it matters
- What behavior changes

---

## 📦 Scope

### ✅ In Scope
- 

### 🚫 Out of Scope
- 

---

## 🧠 Context

REQUIRED:
- Domain references
- Dependencies
- Assumptions

---

## 📜 Acceptance Criteria

REQUIRED:
Each item must be testable.

- [ ] 
- [ ] 
- [ ] 

---

## 🏗️ Technical Notes

REQUIRED for non-trivial stories:

- Data models:
- Services:
- Constraints:
- Risks:

---

## 🧪 Test Strategy

### Approach (select one)
- [ ] Unit-first (TDD)
- [ ] Integration-first
- [ ] Mixed

---

### Test Scenarios

#### Happy Path
- 

#### Edge Cases
- 

#### Negative Cases
- 

#### Regression Risks
- 

---

## 🔴 Red Phase (Required for logic-heavy stories)

### Failing Test Definition

    @Test
    void should_fail_until_behavior_is_implemented() {
        // expected failure
    }

---

## 💻 Implementation Plan

- Steps:
- Key decisions:
- Trade-offs:

---

## 🟢 Definition of Done (MEASURABLE)

ALL must be satisfied:

- [ ] All acceptance criteria pass via tests
- [ ] All tests pass
- [ ] Tests cover:
  - [ ] Happy path
  - [ ] Edge cases
  - [ ] Negative cases
- [ ] No failing or skipped tests
- [ ] Code compiles successfully
- [ ] Build completes without errors
- [ ] Passes project linting / static analysis rules
- [ ] No TODOs or placeholder logic remain

---

## 🔍 Test Validation (Test Agent)

- [ ] All acceptance criteria mapped to tests
- [ ] Tests validate behavior (not just execution)
- [ ] Edge cases covered
- [ ] No weak or misleading assertions

---

## 🔁 Iteration Notes

If issues are found:

- Test Agent → returns to Developer
- Reviewer → returns to Developer

Story is NOT complete until all checks pass.

---

## 🧾 Final Review (Reviewer)

### Validation

- [ ] Code compiles
- [ ] Tests executed and passed
- [ ] Behavior matches acceptance criteria

### Decision
- [ ] Approved
- [ ] Changes Requested