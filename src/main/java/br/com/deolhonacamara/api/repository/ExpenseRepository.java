package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.ExpenseEntity;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.input.ExpenseInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ExpenseRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageResponse<ExpenseEntity> findByPoliticianId(ExpenseInput input, Pageable pageable) {
        StringBuilder where = new StringBuilder(" WHERE politician_id = :politicianId");
        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", input.getPoliticianId());

        if (input.getYear() != null) {
            where.append(" AND year = :year");
            params.put("year", input.getYear());
        }

        if (input.getMonth() != null) {
            where.append(" AND month = :month");
            params.put("month", input.getMonth());
        }

        String sql = """
            SELECT id, politician_id, year, month, expense_type, supplier, supplier_cnpj_cpf,
                   document_value, net_value, glosa_value, document_date, document_url,
                   document_code, batch_code, document_type_code, document_number,
                   reimbursement_number, installment, document_type,
                   created_at, updated_at
            FROM politician_expense
        """ + where + """
            ORDER BY year DESC, month DESC, document_date DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM politician_expense " + where;

        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<ExpenseEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public void upsertExpense(ExpenseEntity expense) {
        String sql = """
            INSERT INTO politician_expense
                (politician_id, year, month, expense_type, supplier, supplier_cnpj_cpf,
                 document_value, net_value, glosa_value, document_date, document_url,
                 document_code, batch_code, document_type_code, document_number,
                 reimbursement_number, installment, document_type, updated_at)
            VALUES
                (:politicianId, :year, :month, :expenseType, :supplier, :supplierCnpjCpf,
                 :documentValue, :netValue, :glosaValue, :documentDate, :documentUrl,
                 :documentCode, :batchCode, :documentTypeCode, :documentNumber,
                 :reimbursementNumber, :installment, :documentType, CURRENT_TIMESTAMP)
            ON CONFLICT DO NOTHING;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", expense.getPoliticianId());
        params.put("year", expense.getYear());
        params.put("month", expense.getMonth());
        params.put("expenseType", expense.getExpenseType() != null ? expense.getExpenseType() : "");
        params.put("supplier", expense.getSupplier() != null ? expense.getSupplier() : "");
        params.put("supplierCnpjCpf", expense.getSupplierCnpjCpf() != null ? expense.getSupplierCnpjCpf() : "");
        params.put("documentValue", expense.getDocumentValue() != null ? expense.getDocumentValue() : java.math.BigDecimal.ZERO);
        params.put("netValue", expense.getNetValue() != null ? expense.getNetValue() : java.math.BigDecimal.ZERO);
        params.put("glosaValue", expense.getGlosaValue() != null ? expense.getGlosaValue() : java.math.BigDecimal.ZERO);
        params.put("documentDate", expense.getDocumentDate() != null ? expense.getDocumentDate() : java.time.LocalDate.now());
        params.put("documentUrl", expense.getDocumentUrl() != null ? expense.getDocumentUrl() : "");
        params.put("documentCode", expense.getDocumentCode());
        params.put("batchCode", expense.getBatchCode());
        params.put("documentTypeCode", expense.getDocumentTypeCode());
        params.put("documentNumber", expense.getDocumentNumber() != null ? expense.getDocumentNumber() : "");
        params.put("reimbursementNumber", expense.getReimbursementNumber() != null ? expense.getReimbursementNumber() : "");
        params.put("installment", expense.getInstallment());
        params.put("documentType", expense.getDocumentType() != null ? expense.getDocumentType() : "");

        jdbcTemplate.update(sql, params);
    }

    private ExpenseEntity mapRow(ResultSet rs) throws SQLException {
        return ExpenseEntity.builder()
                .id(rs.getInt("id"))
                .politicianId(rs.getInt("politician_id"))
                .year(rs.getInt("year"))
                .month(rs.getInt("month"))
                .expenseType(rs.getString("expense_type"))
                .supplier(rs.getString("supplier"))
                .supplierCnpjCpf(rs.getString("supplier_cnpj_cpf"))
                .documentValue(rs.getBigDecimal("document_value"))
                .netValue(rs.getBigDecimal("net_value"))
                .glosaValue(rs.getBigDecimal("glosa_value"))
                .documentDate(rs.getDate("document_date") != null ? rs.getDate("document_date").toLocalDate().atStartOfDay() : null)
                .documentUrl(rs.getString("document_url"))
                .documentCode(rs.getObject("document_code") != null ? rs.getInt("document_code") : null)
                .batchCode(rs.getObject("batch_code") != null ? rs.getInt("batch_code") : null)
                .documentTypeCode(rs.getObject("document_type_code") != null ? rs.getInt("document_type_code") : null)
                .documentNumber(rs.getString("document_number"))
                .reimbursementNumber(rs.getString("reimbursement_number"))
                .installment(rs.getObject("installment") != null ? rs.getInt("installment") : null)
                .documentType(rs.getString("document_type"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }

    public Integer countByPoliticianIdAndYear(Integer politicianId, Integer year) {
        String sql = """
            SELECT COUNT(*) FROM politician_expense
            WHERE politician_id = :politicianId
            AND year = :year
        """;
        Integer result = jdbcTemplate.queryForObject(sql,
            java.util.Map.of("politicianId", politicianId, "year", year),
            Integer.class);
        return result != null ? result : 0;
    }
}

