package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PoliticianVoteSummaryDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PoliticianVoteSummaryDTO {

  private @Nullable Integer politicianId;

  private @Nullable String politicianName;

  private @Nullable String voteType;

  public PoliticianVoteSummaryDTO politicianId(Integer politicianId) {
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

  public PoliticianVoteSummaryDTO politicianName(String politicianName) {
    this.politicianName = politicianName;
    return this;
  }

  /**
   * Get politicianName
   * @return politicianName
   */
  
  @Schema(name = "politicianName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("politicianName")
  public String getPoliticianName() {
    return politicianName;
  }

  public void setPoliticianName(String politicianName) {
    this.politicianName = politicianName;
  }

  public PoliticianVoteSummaryDTO voteType(String voteType) {
    this.voteType = voteType;
    return this;
  }

  /**
   * Get voteType
   * @return voteType
   */
  
  @Schema(name = "voteType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voteType")
  public String getVoteType() {
    return voteType;
  }

  public void setVoteType(String voteType) {
    this.voteType = voteType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PoliticianVoteSummaryDTO politicianVoteSummaryDTO = (PoliticianVoteSummaryDTO) o;
    return Objects.equals(this.politicianId, politicianVoteSummaryDTO.politicianId) &&
        Objects.equals(this.politicianName, politicianVoteSummaryDTO.politicianName) &&
        Objects.equals(this.voteType, politicianVoteSummaryDTO.voteType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(politicianId, politicianName, voteType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PoliticianVoteSummaryDTO {\n");
    sb.append("    politicianId: ").append(toIndentedString(politicianId)).append("\n");
    sb.append("    politicianName: ").append(toIndentedString(politicianName)).append("\n");
    sb.append("    voteType: ").append(toIndentedString(voteType)).append("\n");
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

