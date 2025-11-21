-- Adiciona novos campos na tabela politician_expense
ALTER TABLE camara_deputados.politician_expense
    ADD COLUMN IF NOT EXISTS supplier_cnpj_cpf VARCHAR(20),
    ADD COLUMN IF NOT EXISTS document_code INT,
    ADD COLUMN IF NOT EXISTS batch_code INT,
    ADD COLUMN IF NOT EXISTS document_type_code INT,
    ADD COLUMN IF NOT EXISTS document_number VARCHAR(100),
    ADD COLUMN IF NOT EXISTS reimbursement_number VARCHAR(100),
    ADD COLUMN IF NOT EXISTS installment INT,
    ADD COLUMN IF NOT EXISTS document_type VARCHAR(100);

