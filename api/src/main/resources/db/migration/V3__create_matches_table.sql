-- V3: Create matches table for idempotent ingestion
-- Matches use source_id to prevent duplicates from the Kaggle dataset.

CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    source_id VARCHAR(50) NOT NULL UNIQUE,
    tournament_year INTEGER,
    kickoff_datetime VARCHAR(50),
    tournament_round VARCHAR(100),
    stadium_name VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    home_team_id VARCHAR(50),
    home_team_name VARCHAR(255),
    home_team_code VARCHAR(10),
    away_team_id VARCHAR(50),
    away_team_name VARCHAR(255),
    away_team_code VARCHAR(10),
    home_goals INTEGER,
    away_goals INTEGER,
    match_result VARCHAR(20),
    score_display VARCHAR(20),
    is_draw BOOLEAN,
    is_extra_time BOOLEAN,
    is_penalty_shootout BOOLEAN,
    home_penalty_score INTEGER,
    away_penalty_score INTEGER,
    penalty_score_display VARCHAR(20),
    attendance INTEGER
);

-- Note: We are currently embedding teams and venues in the matches table to simplify 
-- Stage 1 of ingestion before moving to full normalization in later phases.
