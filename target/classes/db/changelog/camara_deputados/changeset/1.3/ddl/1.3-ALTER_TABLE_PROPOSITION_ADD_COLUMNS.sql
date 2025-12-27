-- Migration 1.3: Alter table to add missing columns to camara_deputados.proposition
-- NOTE: 1.2 was already executed in some environments; this adds the same missing columns safely.
-- Uses IF NOT EXISTS so it is idempotent across environments.

BEGIN;

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

-- Ensure index exists (safe to run even if already present)
CREATE INDEX IF NOT EXISTS idx_proposition_year ON camara_deputados.proposition (year);

COMMIT;

