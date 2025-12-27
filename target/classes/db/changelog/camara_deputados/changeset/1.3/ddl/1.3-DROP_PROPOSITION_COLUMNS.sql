-- Rollback for 1.3: drop columns added to camara_deputados.proposition (safe/idempotent)

ALTER TABLE IF EXISTS camara_deputados.proposition
    DROP COLUMN IF EXISTS uri,
    DROP COLUMN IF EXISTS code_type,
    DROP COLUMN IF EXISTS detailed_summary,
    DROP COLUMN IF EXISTS status_date_time,
    DROP COLUMN IF EXISTS status_last_reporter_uri,
    DROP COLUMN IF EXISTS status_tramitation_description,
    DROP COLUMN IF EXISTS status_tramitation_type_code,
    DROP COLUMN IF EXISTS status_situation_description,
    DROP COLUMN IF EXISTS status_situation_code,
    DROP COLUMN IF EXISTS status_dispatch,
    DROP COLUMN IF EXISTS status_url,
    DROP COLUMN IF EXISTS status_scope,
    DROP COLUMN IF EXISTS status_appreciation,
    DROP COLUMN IF EXISTS uri_orgao_numerador,
    DROP COLUMN IF EXISTS uri_autores,
    DROP COLUMN IF EXISTS type_description,
    DROP COLUMN IF EXISTS keywords,
    DROP COLUMN IF EXISTS uri_prop_principal,
    DROP COLUMN IF EXISTS uri_prop_anterior,
    DROP COLUMN IF EXISTS uri_prop_posterior,
    DROP COLUMN IF EXISTS url_inteiro_teor,
    DROP COLUMN IF EXISTS urn_final,
    DROP COLUMN IF EXISTS text,
    DROP COLUMN IF EXISTS justification,
    DROP COLUMN IF EXISTS created_at,
    DROP COLUMN IF EXISTS updated_at;

-- Index may still exist; do not drop idx_proposition_year here to avoid affecting other setups.

