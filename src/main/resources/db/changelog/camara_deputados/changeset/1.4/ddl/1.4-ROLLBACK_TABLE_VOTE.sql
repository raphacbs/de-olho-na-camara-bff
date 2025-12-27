-- Rollback: Recreate tables with original structure
DROP TABLE IF EXISTS camara_deputados.politician_vote;
DROP TABLE IF EXISTS camara_deputados.vote;

CREATE TABLE IF NOT EXISTS camara_deputados.vote (
    id VARCHAR(50) PRIMARY KEY,
    date DATE,
    description TEXT,
    summary TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);
