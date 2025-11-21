-- tabela de políticos sincronizados da API da Câmara
CREATE TABLE IF NOT EXISTS camara_deputados.politicians (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    party VARCHAR(50),
    party_uri VARCHAR(255),
    state VARCHAR(5),
    legislature_id INT,
    email VARCHAR(255),
    uri VARCHAR(255),
    photo_url VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_politicians_party_state
ON camara_deputados.politicians (party, state);

CREATE INDEX IF NOT EXISTS idx_politicians_state
ON camara_deputados.politicians (state);

-- tabela que relaciona usuários com políticos seguidos
CREATE TABLE IF NOT EXISTS camara_deputados.user_followed_politicians (
    user_id UUID NOT NULL,
    politician_id INT NOT NULL,
    followed_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, politician_id),
    CONSTRAINT fk_user_follow_user FOREIGN KEY (user_id) REFERENCES authentication.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_follow_politician FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_followed_politician
ON camara_deputados.user_followed_politicians (politician_id);

CREATE TABLE IF NOT EXISTS camara_deputados.politician_expense (
    id SERIAL PRIMARY KEY,
    politician_id INT NOT NULL,
    year INT NOT NULL,
    month INT NOT NULL,
    expense_type VARCHAR(250),
    supplier VARCHAR(300),
    document_value NUMERIC(12,2),
    net_value NUMERIC(12,2),
    glosa_value NUMERIC(12,2),
    document_date DATE,
    document_url TEXT,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_expense_politician FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_expense_politician
ON camara_deputados.politician_expense (politician_id);

CREATE INDEX IF NOT EXISTS idx_expense_year_month
ON camara_deputados.politician_expense (year, month);


CREATE TABLE IF NOT EXISTS camara_deputados.vote (
    id VARCHAR(50) PRIMARY KEY,
    date DATE,
    description TEXT,
    summary TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);


CREATE TABLE IF NOT EXISTS camara_deputados.politician_vote (
    id SERIAL PRIMARY KEY,
    vote_id VARCHAR(50) NOT NULL,
    politician_id INT NOT NULL,
    vote_option VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT fk_pol_vote_vote FOREIGN KEY (vote_id) REFERENCES camara_deputados.vote(id) ON DELETE CASCADE,
    CONSTRAINT fk_pol_vote_polititian FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_pol_vote_politician
ON camara_deputados.politician_vote (politician_id);

CREATE TABLE IF NOT EXISTS camara_deputados.speeches (
    politician_id INT NOT NULL,
    start_datetime TIMESTAMPTZ,
    end_datetime TIMESTAMPTZ,
    title_event VARCHAR(255),
    start_datetime_event TIMESTAMPTZ,
    end_datetime_event TIMESTAMPTZ,
    keywords TEXT,
    summary TEXT,
    speech_type VARCHAR(100),
    transcription TEXT,
    event_uri VARCHAR(255),
    audio_url VARCHAR(255),
    text_url VARCHAR(255),
    video_url VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),
    PRIMARY KEY (politician_id, start_datetime),
    CONSTRAINT fk_speech_politician FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_speech_politician
ON camara_deputados.speeches (politician_id);

CREATE TABLE IF NOT EXISTS camara_deputados.proposition (
    id INT PRIMARY KEY,
    type VARCHAR(50),
    number INT,
    year INT,
    summary TEXT,
    presentation_date DATE,
    status JSONB,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_proposition_year
ON camara_deputados.proposition (year);

CREATE TABLE IF NOT EXISTS camara_deputados.politician_proposition (
    id SERIAL PRIMARY KEY,
    politician_id INT NOT NULL,
    proposition_id INT NOT NULL,
    CONSTRAINT fk_pol_prop_politician FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE,
    CONSTRAINT fk_pol_prop_proposition FOREIGN KEY (proposition_id) REFERENCES camara_deputados.proposition(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_pol_prop_politician
ON camara_deputados.politician_proposition (politician_id);

CREATE TABLE IF NOT EXISTS camara_deputados.presence (
    id SERIAL PRIMARY KEY,
    politician_id INT NOT NULL,
    date DATE,
    description TEXT,
    status VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT fk_presence_politician FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_presence_politician
ON camara_deputados.presence (politician_id);

CREATE TABLE IF NOT EXISTS camara_deputados.user_device (
    user_id UUID NOT NULL,
    fcm_token TEXT NOT NULL,
    updated_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (user_id, fcm_token),
    CONSTRAINT fk_user_device_user FOREIGN KEY (user_id) REFERENCES authentication.users(id) ON DELETE CASCADE
);







