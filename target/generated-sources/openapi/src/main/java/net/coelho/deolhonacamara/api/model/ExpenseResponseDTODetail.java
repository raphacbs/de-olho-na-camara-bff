package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExpenseResponseDTODetail
 */

@JsonTypeName("ExpenseResponseDTO_detail")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-12-27T15:23:02.402204900-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class ExpenseResponseDTODetail {

  private @Nullable Integer year;

  private @Nullable Integer month;

  private @Nullable String totalExpenses;

  public ExpenseResponseDTODetail year(Integer year) {
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

  public ExpenseResponseDTODetail month(Integer month) {
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

  public ExpenseResponseDTODetail totalExpenses(String totalExpenses) {
    this.totalExpenses = totalExpenses;
    return this;
  }

  /**
   * Get totalExpenses
   * @return totalExpenses
   */
  
  @Schema(name = "totalExpenses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalExpenses")
  public String getTotalExpenses() {
    return totalExpenses;
  }

  public void setTotalExpenses(String totalExpenses) {
    this.totalExpenses = totalExpenses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpenseResponseDTODetail expenseResponseDTODetail = (ExpenseResponseDTODetail) o;
    return Objects.equals(this.year, expenseResponseDTODetail.year) &&
        Objects.equals(this.month, expenseResponseDTODetail.month) &&
        Objects.equals(this.totalExpenses, expenseResponseDTODetail.totalExpenses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, month, totalExpenses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpenseResponseDTODetail {\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    totalExpenses: ").append(toIndentedString(totalExpenses)).append("\n");
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

