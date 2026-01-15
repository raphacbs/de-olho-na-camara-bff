CREATE TABLE IF NOT EXISTS camara_deputados.party (
    id INTEGER PRIMARY KEY,
    acronym VARCHAR(20) NOT NULL,               -- sigla
    name VARCHAR(255) NOT NULL,                 -- nome
    registration_date DATE,                     -- deferimento
    national_president VARCHAR(255),            -- presidente_nacional
    electoral_number INTEGER UNIQUE NOT NULL,   -- numero_legenda
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);