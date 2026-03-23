-- File: 2.2-ADD_COLUMNS_SYNC_PROGRESS.sql
ALTER TABLE IF EXISTS camara_deputados.sync_progress
    ADD COLUMN IF NOT EXISTS last_proposition_id BIGINT,
    ADD COLUMN IF NOT EXISTS last_tramitacao_id BIGINT,
    ADD COLUMN IF NOT EXISTS last_proposition_updated_at TIMESTAMPTZ;

CREATE INDEX IF NOT EXISTS idx_sync_progress_last_proposition_id
    ON camara_deputados.sync_progress(last_proposition_id);
CREATE INDEX IF NOT EXISTS idx_sync_progress_last_tramitacao_id
    ON camara_deputados.sync_progress(last_tramitacao_id);

