-- V1: Initial schema setup
-- This migration ensures Flyway is working and provides a baseline for future changes.

CREATE TABLE schema_verification (
    id SERIAL PRIMARY KEY,
    verified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO schema_verification (verified_at) VALUES (CURRENT_TIMESTAMP);
