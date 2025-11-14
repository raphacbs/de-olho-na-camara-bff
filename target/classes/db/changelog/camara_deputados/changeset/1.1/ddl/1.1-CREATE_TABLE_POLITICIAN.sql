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


-- tabela que relaciona usuários com políticos seguidos
CREATE TABLE IF NOT EXISTS camara_deputados.user_followed_politicians (
    user_id UUID NOT NULL,
    politician_id INT NOT NULL,
    followed_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, politician_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES authentication.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_politician FOREIGN KEY (politician_id) REFERENCES camara_deputados.politicians(id) ON DELETE CASCADE
);



