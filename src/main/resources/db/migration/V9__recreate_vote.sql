-- Recreate vote table with new structure
DROP TABLE IF EXISTS camara_deputados.politician_vote;
DROP TABLE IF EXISTS camara_deputados.vote;

CREATE TABLE IF NOT EXISTS camara_deputados.vote (
    id VARCHAR(50) PRIMARY KEY,
    approval INTEGER,
    date DATE,
    registration_date_time TIMESTAMP,
    last_voting_open_date_time TIMESTAMP,
    last_voting_open_description TEXT,
    description TEXT,
    event_id INTEGER,
    organ_id INTEGER,
    organ_acronym VARCHAR(50),
    uri TEXT,
    event_uri TEXT,
    organ_uri TEXT,
    registered_effects JSONB,
    possible_objects JSONB,
    affected_propositions JSONB,
    last_proposition_presentation JSONB,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS camara_deputados.politician_vote (
    id SERIAL PRIMARY KEY,
    vote_registration_date TIMESTAMP,
    politician_id INTEGER,
    vote_type VARCHAR(50),
    vote_id VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),
    FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id),
    FOREIGN KEY (vote_id) REFERENCES camara_deputados.vote(id)
);
