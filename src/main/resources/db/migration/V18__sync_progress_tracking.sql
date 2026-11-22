ALTER TABLE camara_deputados.sync_progress
    ADD COLUMN IF NOT EXISTS processed_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS error_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS last_cursor VARCHAR(120);

COMMENT ON COLUMN camara_deputados.sync_progress.current_page IS 'Unidade já concluída do fluxo: página da API ou deputado processado. Não mistura os dois.';
COMMENT ON COLUMN camara_deputados.sync_progress.total_pages IS 'Total fixo desta execução. Gravado uma vez; não é reduzido nem trocado no meio.';
COMMENT ON COLUMN camara_deputados.sync_progress.last_cursor IS 'Último item concluído (id do deputado, id da votação ou id da proposição).';
COMMENT ON COLUMN camara_deputados.sync_progress.last_proposition_id IS 'Cursor de retomada da tramitação: id da proposição.';
COMMENT ON COLUMN camara_deputados.sync_progress.last_tramitacao_id IS 'Cursor de retomada dentro da proposição: sequência da tramitação.';

CREATE INDEX IF NOT EXISTS idx_sync_progress_flow_start
    ON camara_deputados.sync_progress (flow_name, start_time DESC);
