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
 * PresenceDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-21T10:53:37.437611200-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PresenceDto {

  private @Nullable Integer id;

  private @Nullable Integer politicianId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate date;

  private @Nullable String description;

  private @Nullable String status;

  public PresenceDto id(Integer id) {
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

  public PresenceDto politicianId(Integer politicianId) {
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

  public PresenceDto date(LocalDate date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
   */
  @Valid 
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public PresenceDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PresenceDto status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
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
    PresenceDto presenceDto = (PresenceDto) o;
    return Objects.equals(this.id, presenceDto.id) &&
        Objects.equals(this.politicianId, presenceDto.politicianId) &&
        Objects.equals(this.date, presenceDto.date) &&
        Objects.equals(this.description, presenceDto.description) &&
        Objects.equals(this.status, presenceDto.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, politicianId, date, description, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PresenceDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    politicianId: ").append(toIndentedString(politicianId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

