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
 * PoliticianVoteWithPropositionDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-25T15:13:37.457952600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PoliticianVoteWithPropositionDTO {

  private @Nullable Long id;

  private @Nullable Integer politicianId;

  private @Nullable String vote;

  private @Nullable String voteId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate voteDate;

  private @Nullable String votingDescription;

  private @Nullable String propositionSummary;

  private @Nullable Integer propositionYear;

  private @Nullable String propositionDetailedSummary;

  public PoliticianVoteWithPropositionDTO id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PoliticianVoteWithPropositionDTO politicianId(Integer politicianId) {
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

  public PoliticianVoteWithPropositionDTO vote(String vote) {
    this.vote = vote;
    return this;
  }

  /**
   * Get vote
   * @return vote
   */
  
  @Schema(name = "vote", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vote")
  public String getVote() {
    return vote;
  }

  public void setVote(String vote) {
    this.vote = vote;
  }

  public PoliticianVoteWithPropositionDTO voteId(String voteId) {
    this.voteId = voteId;
    return this;
  }

  /**
   * Get voteId
   * @return voteId
   */
  
  @Schema(name = "voteId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voteId")
  public String getVoteId() {
    return voteId;
  }

  public void setVoteId(String voteId) {
    this.voteId = voteId;
  }

  public PoliticianVoteWithPropositionDTO voteDate(LocalDate voteDate) {
    this.voteDate = voteDate;
    return this;
  }

  /**
   * Get voteDate
   * @return voteDate
   */
  @Valid 
  @Schema(name = "voteDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voteDate")
  public LocalDate getVoteDate() {
    return voteDate;
  }

  public void setVoteDate(LocalDate voteDate) {
    this.voteDate = voteDate;
  }

  public PoliticianVoteWithPropositionDTO votingDescription(String votingDescription) {
    this.votingDescription = votingDescription;
    return this;
  }

  /**
   * Get votingDescription
   * @return votingDescription
   */
  
  @Schema(name = "votingDescription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("votingDescription")
  public String getVotingDescription() {
    return votingDescription;
  }

  public void setVotingDescription(String votingDescription) {
    this.votingDescription = votingDescription;
  }

  public PoliticianVoteWithPropositionDTO propositionSummary(String propositionSummary) {
    this.propositionSummary = propositionSummary;
    return this;
  }

  /**
   * Get propositionSummary
   * @return propositionSummary
   */
  
  @Schema(name = "propositionSummary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("propositionSummary")
  public String getPropositionSummary() {
    return propositionSummary;
  }

  public void setPropositionSummary(String propositionSummary) {
    this.propositionSummary = propositionSummary;
  }

  public PoliticianVoteWithPropositionDTO propositionYear(Integer propositionYear) {
    this.propositionYear = propositionYear;
    return this;
  }

  /**
   * Get propositionYear
   * @return propositionYear
   */
  
  @Schema(name = "propositionYear", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("propositionYear")
  public Integer getPropositionYear() {
    return propositionYear;
  }

  public void setPropositionYear(Integer propositionYear) {
    this.propositionYear = propositionYear;
  }

  public PoliticianVoteWithPropositionDTO propositionDetailedSummary(String propositionDetailedSummary) {
    this.propositionDetailedSummary = propositionDetailedSummary;
    return this;
  }

  /**
   * Get propositionDetailedSummary
   * @return propositionDetailedSummary
   */
  
  @Schema(name = "propositionDetailedSummary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("propositionDetailedSummary")
  public String getPropositionDetailedSummary() {
    return propositionDetailedSummary;
  }

  public void setPropositionDetailedSummary(String propositionDetailedSummary) {
    this.propositionDetailedSummary = propositionDetailedSummary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PoliticianVoteWithPropositionDTO politicianVoteWithPropositionDTO = (PoliticianVoteWithPropositionDTO) o;
    return Objects.equals(this.id, politicianVoteWithPropositionDTO.id) &&
        Objects.equals(this.politicianId, politicianVoteWithPropositionDTO.politicianId) &&
        Objects.equals(this.vote, politicianVoteWithPropositionDTO.vote) &&
        Objects.equals(this.voteId, politicianVoteWithPropositionDTO.voteId) &&
        Objects.equals(this.voteDate, politicianVoteWithPropositionDTO.voteDate) &&
        Objects.equals(this.votingDescription, politicianVoteWithPropositionDTO.votingDescription) &&
        Objects.equals(this.propositionSummary, politicianVoteWithPropositionDTO.propositionSummary) &&
        Objects.equals(this.propositionYear, politicianVoteWithPropositionDTO.propositionYear) &&
        Objects.equals(this.propositionDetailedSummary, politicianVoteWithPropositionDTO.propositionDetailedSummary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, politicianId, vote, voteId, voteDate, votingDescription, propositionSummary, propositionYear, propositionDetailedSummary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PoliticianVoteWithPropositionDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    politicianId: ").append(toIndentedString(politicianId)).append("\n");
    sb.append("    vote: ").append(toIndentedString(vote)).append("\n");
    sb.append("    voteId: ").append(toIndentedString(voteId)).append("\n");
    sb.append("    voteDate: ").append(toIndentedString(voteDate)).append("\n");
    sb.append("    votingDescription: ").append(toIndentedString(votingDescription)).append("\n");
    sb.append("    propositionSummary: ").append(toIndentedString(propositionSummary)).append("\n");
    sb.append("    propositionYear: ").append(toIndentedString(propositionYear)).append("\n");
    sb.append("    propositionDetailedSummary: ").append(toIndentedString(propositionDetailedSummary)).append("\n");
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

