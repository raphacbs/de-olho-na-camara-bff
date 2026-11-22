-- Create sync_progress table to track extraction progress for each execution
CREATE TABLE IF NOT EXISTS camara_deputados.sync_progress (
    id SERIAL PRIMARY KEY,
    flow_name VARCHAR(100) NOT NULL,
    execution_id VARCHAR(100) NOT NULL, -- Unique identifier for each execution
    status VARCHAR(20) NOT NULL DEFAULT 'running', -- running, completed, failed
    total_pages INTEGER,
    current_page INTEGER NOT NULL DEFAULT 0,
    start_time TIMESTAMPTZ DEFAULT now(),
    end_time TIMESTAMPTZ,
    last_updated TIMESTAMPTZ DEFAULT now(),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),
    UNIQUE(flow_name, execution_id)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_sync_progress_flow_name ON camara_deputados.sync_progress(flow_name);
CREATE INDEX IF NOT EXISTS idx_sync_progress_status ON camara_deputados.sync_progress(status);
CREATE INDEX IF NOT EXISTS idx_sync_progress_start_time ON camara_deputados.sync_progress(start_time DESC);
