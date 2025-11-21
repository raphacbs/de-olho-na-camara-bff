package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseBodyDto implements Serializable {
    @JsonProperty("ano")
    private Integer year;
    
    @JsonProperty("mes")
    private Integer month;
    
    @JsonProperty("tipoDespesa")
    private String expenseType;
    
    @JsonProperty("nomeFornecedor")
    private String supplier;
    
    @JsonProperty("cnpjCpfFornecedor")
    private String supplierCnpjCpf;
    
    @JsonProperty("valorDocumento")
    private BigDecimal documentValue;
    
    @JsonProperty("valorLiquido")
    private BigDecimal netValue;
    
    @JsonProperty("valorGlosa")
    private BigDecimal glosaValue;
    
    @JsonProperty("dataDocumento")
    private LocalDateTime documentDate;
    
    @JsonProperty("urlDocumento")
    private String documentUrl;
    
    @JsonProperty("codDocumento")
    private Integer documentCode;
    
    @JsonProperty("codLote")
    private Integer batchCode;
    
    @JsonProperty("codTipoDocumento")
    private Integer documentTypeCode;
    
    @JsonProperty("numDocumento")
    private String documentNumber;
    
    @JsonProperty("numRessarcimento")
    private String reimbursementNumber;
    
    @JsonProperty("parcela")
    private Integer installment;
    
    @JsonProperty("tipoDocumento")
    private String documentType;
}

