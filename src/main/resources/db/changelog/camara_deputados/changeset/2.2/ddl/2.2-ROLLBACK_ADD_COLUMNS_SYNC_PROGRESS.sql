-- File: 2.2-ROLLBACK_ADD_COLUMNS_SYNC_PROGRESS.sql
ALTER TABLE IF EXISTS camara_deputados.sync_progress
    DROP COLUMN IF EXISTS last_proposition_updated_at,
    DROP COLUMN IF EXISTS last_tramitacao_id,
    DROP COLUMN IF EXISTS last_proposition_id;

DROP INDEX IF EXISTS idx_sync_progress_last_proposition_id;
DROP INDEX IF EXISTS idx_sync_progress_last_tramitacao_id;

