-- Alter table to add missing columns to camara_deputados.proposition based on PropositionEntity
-- This migration is safe for an already-in-use DB: it uses IF NOT EXISTS for each column.

ALTER TABLE IF EXISTS camara_deputados.proposition
    ADD COLUMN IF NOT EXISTS uri VARCHAR(255),
    ADD COLUMN IF NOT EXISTS code_type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS detailed_summary TEXT,
    ADD COLUMN IF NOT EXISTS status_date_time TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS status_last_reporter_uri VARCHAR(255),
    ADD COLUMN IF NOT EXISTS status_tramitation_description TEXT,
    ADD COLUMN IF NOT EXISTS status_tramitation_type_code VARCHAR(50),
    ADD COLUMN IF NOT EXISTS status_situation_description TEXT,
    ADD COLUMN IF NOT EXISTS status_situation_code VARCHAR(50),
    ADD COLUMN IF NOT EXISTS status_dispatch TEXT,
    ADD COLUMN IF NOT EXISTS status_url VARCHAR(255),
    ADD COLUMN IF NOT EXISTS status_scope VARCHAR(255),
    ADD COLUMN IF NOT EXISTS status_appreciation VARCHAR(255),
    ADD COLUMN IF NOT EXISTS uri_orgao_numerador VARCHAR(255),
    ADD COLUMN IF NOT EXISTS uri_autores TEXT,
    ADD COLUMN IF NOT EXISTS type_description VARCHAR(255),
    ADD COLUMN IF NOT EXISTS keywords TEXT,
    ADD COLUMN IF NOT EXISTS uri_prop_principal VARCHAR(255),
    ADD COLUMN IF NOT EXISTS uri_prop_anterior VARCHAR(255),
    ADD COLUMN IF NOT EXISTS uri_prop_posterior VARCHAR(255),
    ADD COLUMN IF NOT EXISTS url_inteiro_teor VARCHAR(255),
    ADD COLUMN IF NOT EXISTS urn_final VARCHAR(255),
    ADD COLUMN IF NOT EXISTS text TEXT,
    ADD COLUMN IF NOT EXISTS justification TEXT,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ DEFAULT now(),
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ DEFAULT now();

-- Create index if not exists for year is already present in create, but ensure it's present
CREATE INDEX IF NOT EXISTS idx_proposition_year ON camara_deputados.proposition (year);

