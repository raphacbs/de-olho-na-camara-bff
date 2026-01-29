-- Script para investigar dados de proposições e despesas
-- Execute este script no banco de dados: abre-olho-camara-dev

-- 1. Verificar se existem proposições por ano
SELECT p.year, COUNT(*) as proposition_count
FROM camara_deputados.proposition p
GROUP BY p.year
ORDER BY p.year DESC
LIMIT 10;

-- 2. Verificar se existem relacionamentos entre políticos e proposições
SELECT pp.politician_id, COUNT(*) as propositions_count
FROM camara_deputados.politician_proposition pp
GROUP BY pp.politician_id
ORDER BY propositions_count DESC
LIMIT 20;

-- 3. Verificar se existem despesas por ano
SELECT year, COUNT(*) as expense_count
FROM camara_deputados.politician_expense
GROUP BY year
ORDER BY year DESC
LIMIT 10;

-- 4. Verificar despesas por político
SELECT politician_id, COUNT(*) as expense_count
FROM camara_deputados.politician_expense
GROUP BY politician_id
ORDER BY expense_count DESC
LIMIT 20;

-- 5. Teste específico para o político ID 204423 (André Ferreira)
SELECT
    'propositions' as data_type,
    COUNT(*) as total_count
FROM camara_deputados.politician_proposition pp
INNER JOIN camara_deputados.proposition p ON p.id = pp.proposition_id
WHERE pp.politician_id = 204423

UNION ALL

SELECT
    'propositions_2026' as data_type,
    COUNT(*) as total_count
FROM camara_deputados.politician_proposition pp
INNER JOIN camara_deputados.proposition p ON p.id = pp.proposition_id
WHERE pp.politician_id = 204423
AND p.year = 2026

UNION ALL

SELECT
    'expenses' as data_type,
    COUNT(*) as total_count
FROM camara_deputados.politician_expense
WHERE politician_id = 204423

UNION ALL

SELECT
    'expenses_2026' as data_type,
    COUNT(*) as total_count
FROM camara_deputados.politician_expense
WHERE politician_id = 204423
AND year = 2026;

-- 6. Amostra de dados de proposições (se existirem)
SELECT p.id, p.type, p.number, p.year, p.summary
FROM camara_deputados.proposition p
ORDER BY p.updated_at DESC
LIMIT 5;

-- 7. Amostra de dados de despesas (se existirem)
SELECT pe.politician_id, pe.year, pe.month, pe.expense_type, COUNT(*) as count
FROM camara_deputados.politician_expense pe
GROUP BY pe.politician_id, pe.year, pe.month, pe.expense_type
ORDER BY pe.updated_at DESC
LIMIT 5;

-- 8. Verificar total de políticos no banco
SELECT COUNT(*) as total_politicians
FROM camara_deputados.politicians;

-- 9. Verificar tabelas existentes no schema
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'camara_deputados'
ORDER BY table_name;

