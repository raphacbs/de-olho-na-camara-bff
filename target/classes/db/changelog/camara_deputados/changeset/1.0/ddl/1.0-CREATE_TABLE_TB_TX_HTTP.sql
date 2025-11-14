CREATE TABLE IF NOT EXISTS camara_deputados.tb_tx_http (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    url varchar(255),
    type varchar(50),
    request_body JSONB,
    response_body varchar(1000),
    params varchar(1000),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT true
);