-- Create table to store proposition tramitation (English names)
CREATE TABLE camara_deputados.proposition_tramitation (
    id SERIAL PRIMARY KEY,
    proposition_id INTEGER NOT NULL,
    date_time TIMESTAMP,
    sequence INTEGER,
    org_acronym VARCHAR(50),
    org_uri TEXT,
    last_reporter_uri TEXT,
    regime TEXT,
    tramitation_description TEXT,
    tramitation_type_code VARCHAR(50),
    situation_description TEXT,
    situation_code VARCHAR(50),
    dispatch TEXT,
    url TEXT,
    scope VARCHAR(100),
    appreciation VARCHAR(200),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

-- Unique constraint to avoid duplicates for the same proposition and sequence
CREATE UNIQUE INDEX uq_proposition_tramitation_prop_seq ON camara_deputados.proposition_tramitation (proposition_id, sequence);

-- Index for fast lookup by proposition_id and date_time
CREATE INDEX idx_proposition_tramitation_prop_id ON camara_deputados.proposition_tramitation (proposition_id);
CREATE INDEX idx_proposition_tramitation_date_time ON camara_deputados.proposition_tramitation (date_time);

-- Foreign key to proposition table if exists (optional)
-- ALTER TABLE proposition_tramitation ADD CONSTRAINT fk_prop FOREIGN KEY (proposition_id) REFERENCES proposition(id);
