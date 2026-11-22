CREATE TABLE IF NOT EXISTS camara_deputados.sync_schedule (
    flow_name VARCHAR(100) PRIMARY KEY,
    cron_expression VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

INSERT INTO camara_deputados.sync_schedule (flow_name, cron_expression, enabled)
VALUES
    ('Politicians', '0 0 23 * * *', true),
    ('Expenses', '0 0 1 * * *', true),
    ('Votings', '0 0 2 * * *', true),
    ('Speeches', '0 0 3 * * *', true),
    ('Propositions', '0 0 4 * * *', true),
    ('PropositionTramitations', '0 0 5 * * *', true),
    ('RecentPropositions', '0 0 6 * * *', true)
ON CONFLICT (flow_name) DO NOTHING;
