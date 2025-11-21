package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseEntity {
    private Integer id;
    private Integer politicianId;
    private Integer year;
    private Integer month;
    private String expenseType;
    private String supplier;
    private String supplierCnpjCpf;
    private BigDecimal documentValue;
    private BigDecimal netValue;
    private BigDecimal glosaValue;
    private LocalDateTime documentDate;
    private String documentUrl;
    private Integer documentCode;
    private Integer batchCode;
    private Integer documentTypeCode;
    private String documentNumber;
    private String reimbursementNumber;
    private Integer installment;
    private String documentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

