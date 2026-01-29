package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO;
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
 * VotingWithVotesDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-25T15:13:37.457952600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class VotingWithVotesDTO {

  private @Nullable String id;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate date;

  private @Nullable String description;

  private @Nullable String organAcronym;

  @Valid
  private List<@Valid PoliticianVoteSummaryDTO> votes = new ArrayList<>();

  public VotingWithVotesDTO id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public VotingWithVotesDTO date(LocalDate date) {
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

  public VotingWithVotesDTO description(String description) {
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

  public VotingWithVotesDTO organAcronym(String organAcronym) {
    this.organAcronym = organAcronym;
    return this;
  }

  /**
   * Get organAcronym
   * @return organAcronym
   */
  
  @Schema(name = "organAcronym", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organAcronym")
  public String getOrganAcronym() {
    return organAcronym;
  }

  public void setOrganAcronym(String organAcronym) {
    this.organAcronym = organAcronym;
  }

  public VotingWithVotesDTO votes(List<@Valid PoliticianVoteSummaryDTO> votes) {
    this.votes = votes;
    return this;
  }

  public VotingWithVotesDTO addVotesItem(PoliticianVoteSummaryDTO votesItem) {
    if (this.votes == null) {
      this.votes = new ArrayList<>();
    }
    this.votes.add(votesItem);
    return this;
  }

  /**
   * Get votes
   * @return votes
   */
  @Valid 
  @Schema(name = "votes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("votes")
  public List<@Valid PoliticianVoteSummaryDTO> getVotes() {
    return votes;
  }

  public void setVotes(List<@Valid PoliticianVoteSummaryDTO> votes) {
    this.votes = votes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VotingWithVotesDTO votingWithVotesDTO = (VotingWithVotesDTO) o;
    return Objects.equals(this.id, votingWithVotesDTO.id) &&
        Objects.equals(this.date, votingWithVotesDTO.date) &&
        Objects.equals(this.description, votingWithVotesDTO.description) &&
        Objects.equals(this.organAcronym, votingWithVotesDTO.organAcronym) &&
        Objects.equals(this.votes, votingWithVotesDTO.votes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date, description, organAcronym, votes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VotingWithVotesDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    organAcronym: ").append(toIndentedString(organAcronym)).append("\n");
    sb.append("    votes: ").append(toIndentedString(votes)).append("\n");
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

