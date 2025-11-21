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
 * PropositionDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-21T10:53:37.437611200-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PropositionDto {

  private @Nullable Integer id;

  private @Nullable String type;

  private @Nullable Integer number;

  private @Nullable Integer year;

  private @Nullable String summary;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate presentationDate;

  private @Nullable Object status;

  public PropositionDto id(Integer id) {
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

  public PropositionDto type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public PropositionDto number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
   */
  
  @Schema(name = "number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("number")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public PropositionDto year(Integer year) {
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

  public PropositionDto summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PropositionDto presentationDate(LocalDate presentationDate) {
    this.presentationDate = presentationDate;
    return this;
  }

  /**
   * Get presentationDate
   * @return presentationDate
   */
  @Valid 
  @Schema(name = "presentationDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("presentationDate")
  public LocalDate getPresentationDate() {
    return presentationDate;
  }

  public void setPresentationDate(LocalDate presentationDate) {
    this.presentationDate = presentationDate;
  }

  public PropositionDto status(Object status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public Object getStatus() {
    return status;
  }

  public void setStatus(Object status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropositionDto propositionDto = (PropositionDto) o;
    return Objects.equals(this.id, propositionDto.id) &&
        Objects.equals(this.type, propositionDto.type) &&
        Objects.equals(this.number, propositionDto.number) &&
        Objects.equals(this.year, propositionDto.year) &&
        Objects.equals(this.summary, propositionDto.summary) &&
        Objects.equals(this.presentationDate, propositionDto.presentationDate) &&
        Objects.equals(this.status, propositionDto.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, number, year, summary, presentationDate, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropositionDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    presentationDate: ").append(toIndentedString(presentationDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

