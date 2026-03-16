# Agent Development Guide

This document provides guidance for AI agents contributing to the repository.

Agents must follow these rules when implementing GitHub issues.

---

# Primary Goal

The goal of this repository is to demonstrate a **production-style data platform architecture** using historical FIFA World Cup match data.

Agents should prioritize:

- clean architecture
- test-driven development
- small incremental commits
- minimal scope changes

---

# Development Workflow

For each GitHub story:

1. Read the issue description and acceptance criteria
2. Identify the relevant package or module
3. Write a failing test
4. Implement the minimal code necessary to pass the test
5. Refactor if needed
6. Ensure existing tests still pass

---

# Architecture Rules

The architecture follows a layered pattern.

Controller Layer  
Handles HTTP requests and responses.

Service Layer  
Contains business logic.

Repository Layer  
Handles persistence.

Domain Layer  
Represents core domain models.

DTO Layer  
Defines API response models.

Agents must respect this separation.

---

# Data Pipeline Structure

Data flows through the following stages:

CSV Dataset  
→ Source Row Model  
→ Canonical Domain Model  
→ Database Persistence  
→ REST API

Agents should **not bypass these stages**.

---

# Domain Modeling Guidelines

Current dataset supports **match-level data only**.

Core domain entities:

- Tournament
- Match
- Team
- Stadium
- Stage

Agents should NOT implement:

- Player
- Lineup
- Goal events
- Card events

These require additional datasets.

---

# Testing Expectations

All new features should include tests.

Preferred patterns:

- unit tests for services
- integration tests for controllers
- repository tests for persistence logic

Tests should be written **before implementing the feature** when possible.

---

# Coding Guidelines

Controllers should remain thin.

Services should contain business logic.

Repositories should only interact with the database.

DTOs should be used for API responses.

Domain entities should not be exposed directly in API responses.

---

# Scope Control

Agents should avoid:

- refactoring unrelated modules
- renaming packages without instruction
- introducing new frameworks
- restructuring project architecture

Changes should remain scoped to the issue being implemented.

---

# Documentation

When implementing new functionality, agents should update documentation if needed.

Relevant files may include:

- architecture documentation
- domain model documentation
- API documentation

---

# When In Doubt

If an issue is ambiguous:

- prefer the **simplest implementation**
- avoid architectural changes
- follow existing code patterns