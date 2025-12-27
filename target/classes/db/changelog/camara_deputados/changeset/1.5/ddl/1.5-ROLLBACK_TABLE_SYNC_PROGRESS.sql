-- Rollback sync_progress table creation
DROP INDEX IF EXISTS camara_deputados.idx_sync_progress_start_time;
DROP INDEX IF EXISTS camara_deputados.idx_sync_progress_status;
DROP INDEX IF EXISTS camara_deputados.idx_sync_progress_flow_name;
DROP TABLE IF EXISTS camara_deputados.sync_progress;
