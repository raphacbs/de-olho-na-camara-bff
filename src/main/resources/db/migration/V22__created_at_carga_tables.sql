-- Garante created_at nas tabelas preenchidas pelas cargas.
-- Linhas antigas ficam nulas. Inserções novas recebem now() quando a coluna não vier no INSERT.
DO $$
DECLARE
    tbl text;
BEGIN
    FOREACH tbl IN ARRAY ARRAY[
        'politicians',
        'politician_expense',
        'voting',
        'politician_vote',
        'speeches',
        'proposition',
        'politician_proposition',
        'presence',
        'party',
        'proposition_tramitation'
    ]
    LOOP
        EXECUTE format(
            'ALTER TABLE camara_deputados.%I ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ',
            tbl
        );
        EXECUTE format(
            'ALTER TABLE camara_deputados.%I ALTER COLUMN created_at SET DEFAULT now()',
            tbl
        );
    END LOOP;
END $$;
