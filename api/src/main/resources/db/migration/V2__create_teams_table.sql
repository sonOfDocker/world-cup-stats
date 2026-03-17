-- V2: Create teams table
-- This migration creates the core table for teams as part of Phase 3.

CREATE TABLE teams (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE
);

-- Insert some initial data for testing/proving connectivity
INSERT INTO teams (id, name, code) VALUES ('bra', 'Brazil', 'BRA');
INSERT INTO teams (id, name, code) VALUES ('ger', 'Germany', 'GER');
INSERT INTO teams (id, name, code) VALUES ('arg', 'Argentina', 'ARG');
INSERT INTO teams (id, name, code) VALUES ('fra', 'France', 'FRA');
INSERT INTO teams (id, name, code) VALUES ('ita', 'Italy', 'ITA');
INSERT INTO teams (id, name, code) VALUES ('esp', 'Spain', 'ESP');
INSERT INTO teams (id, name, code) VALUES ('eng', 'England', 'ENG');
INSERT INTO teams (id, name, code) VALUES ('ned', 'Netherlands', 'NED');
