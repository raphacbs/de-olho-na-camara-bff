-- Rollback: Remove os campos adicionados na tabela politician_expense
ALTER TABLE camara_deputados.politician_expense
    DROP COLUMN IF EXISTS supplier_cnpj_cpf,
    DROP COLUMN IF EXISTS document_code,
    DROP COLUMN IF EXISTS batch_code,
    DROP COLUMN IF EXISTS document_type_code,
    DROP COLUMN IF EXISTS document_number,
    DROP COLUMN IF EXISTS reimbursement_number,
    DROP COLUMN IF EXISTS installment,
    DROP COLUMN IF EXISTS document_type;

