package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExpenseDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class ExpenseDto {

  private @Nullable Integer id;

  private @Nullable Integer politicianId;

  private @Nullable Integer year;

  private @Nullable Integer month;

  private @Nullable String expenseType;

  private @Nullable String supplier;

  private @Nullable String documentValue;

  private @Nullable String netValue;

  private @Nullable String glosaValue;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate documentDate;

  private @Nullable String documentUrl;

  public ExpenseDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ExpenseDto politicianId(Integer politicianId) {
    this.politicianId = politicianId;
    return this;
  }

  /**
   * Get politicianId
   * @return politicianId
   */
  
  @Schema(name = "politicianId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("politicianId")
  public Integer getPoliticianId() {
    return politicianId;
  }

  public void setPoliticianId(Integer politicianId) {
    this.politicianId = politicianId;
  }

  public ExpenseDto year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public ExpenseDto month(Integer month) {
    this.month = month;
    return this;
  }

  /**
   * Get month
   * @return month
   */
  
  @Schema(name = "month", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("month")
  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public ExpenseDto expenseType(String expenseType) {
    this.expenseType = expenseType;
    return this;
  }

  /**
   * Get expenseType
   * @return expenseType
   */
  
  @Schema(name = "expenseType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expenseType")
  public String getExpenseType() {
    return expenseType;
  }

  public void setExpenseType(String expenseType) {
    this.expenseType = expenseType;
  }

  public ExpenseDto supplier(String supplier) {
    this.supplier = supplier;
    return this;
  }

  /**
   * Get supplier
   * @return supplier
   */
  
  @Schema(name = "supplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supplier")
  public String getSupplier() {
    return supplier;
  }

  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  public ExpenseDto documentValue(String documentValue) {
    this.documentValue = documentValue;
    return this;
  }

  /**
   * Get documentValue
   * @return documentValue
   */
  
  @Schema(name = "documentValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentValue")
  public String getDocumentValue() {
    return documentValue;
  }

  public void setDocumentValue(String documentValue) {
    this.documentValue = documentValue;
  }

  public ExpenseDto netValue(String netValue) {
    this.netValue = netValue;
    return this;
  }

  /**
   * Get netValue
   * @return netValue
   */
  
  @Schema(name = "netValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("netValue")
  public String getNetValue() {
    return netValue;
  }

  public void setNetValue(String netValue) {
    this.netValue = netValue;
  }

  public ExpenseDto glosaValue(String glosaValue) {
    this.glosaValue = glosaValue;
    return this;
  }

  /**
   * Get glosaValue
   * @return glosaValue
   */
  
  @Schema(name = "glosaValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("glosaValue")
  public String getGlosaValue() {
    return glosaValue;
  }

  public void setGlosaValue(String glosaValue) {
    this.glosaValue = glosaValue;
  }

  public ExpenseDto documentDate(LocalDate documentDate) {
    this.documentDate = documentDate;
    return this;
  }

  /**
   * Get documentDate
   * @return documentDate
   */
  @Valid 
  @Schema(name = "documentDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentDate")
  public LocalDate getDocumentDate() {
    return documentDate;
  }

  public void setDocumentDate(LocalDate documentDate) {
    this.documentDate = documentDate;
  }

  public ExpenseDto documentUrl(String documentUrl) {
    this.documentUrl = documentUrl;
    return this;
  }

  /**
   * Get documentUrl
   * @return documentUrl
   */
  
  @Schema(name = "documentUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentUrl")
  public String getDocumentUrl() {
    return documentUrl;
  }

  public void setDocumentUrl(String documentUrl) {
    this.documentUrl = documentUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpenseDto expenseDto = (ExpenseDto) o;
    return Objects.equals(this.id, expenseDto.id) &&
        Objects.equals(this.politicianId, expenseDto.politicianId) &&
        Objects.equals(this.year, expenseDto.year) &&
        Objects.equals(this.month, expenseDto.month) &&
        Objects.equals(this.expenseType, expenseDto.expenseType) &&
        Objects.equals(this.supplier, expenseDto.supplier) &&
        Objects.equals(this.documentValue, expenseDto.documentValue) &&
        Objects.equals(this.netValue, expenseDto.netValue) &&
        Objects.equals(this.glosaValue, expenseDto.glosaValue) &&
        Objects.equals(this.documentDate, expenseDto.documentDate) &&
        Objects.equals(this.documentUrl, expenseDto.documentUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, politicianId, year, month, expenseType, supplier, documentValue, netValue, glosaValue, documentDate, documentUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpenseDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    politicianId: ").append(toIndentedString(politicianId)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    expenseType: ").append(toIndentedString(expenseType)).append("\n");
    sb.append("    supplier: ").append(toIndentedString(supplier)).append("\n");
    sb.append("    documentValue: ").append(toIndentedString(documentValue)).append("\n");
    sb.append("    netValue: ").append(toIndentedString(netValue)).append("\n");
    sb.append("    glosaValue: ").append(toIndentedString(glosaValue)).append("\n");
    sb.append("    documentDate: ").append(toIndentedString(documentDate)).append("\n");
    sb.append("    documentUrl: ").append(toIndentedString(documentUrl)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

