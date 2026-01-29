package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import net.coelho.deolhonacamara.api.model.VoteDto;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PoliticianVoteDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-25T15:13:37.457952600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PoliticianVoteDto {

  private @Nullable Integer id;

  private @Nullable String voteId;

  private @Nullable Integer politicianId;

  private @Nullable String voteOption;

  private @Nullable VoteDto vote;

  public PoliticianVoteDto id(Integer id) {
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

  public PoliticianVoteDto voteId(String voteId) {
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

  public PoliticianVoteDto politicianId(Integer politicianId) {
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

  public PoliticianVoteDto voteOption(String voteOption) {
    this.voteOption = voteOption;
    return this;
  }

  /**
   * Get voteOption
   * @return voteOption
   */
  
  @Schema(name = "voteOption", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voteOption")
  public String getVoteOption() {
    return voteOption;
  }

  public void setVoteOption(String voteOption) {
    this.voteOption = voteOption;
  }

  public PoliticianVoteDto vote(VoteDto vote) {
    this.vote = vote;
    return this;
  }

  /**
   * Get vote
   * @return vote
   */
  @Valid 
  @Schema(name = "vote", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vote")
  public VoteDto getVote() {
    return vote;
  }

  public void setVote(VoteDto vote) {
    this.vote = vote;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PoliticianVoteDto politicianVoteDto = (PoliticianVoteDto) o;
    return Objects.equals(this.id, politicianVoteDto.id) &&
        Objects.equals(this.voteId, politicianVoteDto.voteId) &&
        Objects.equals(this.politicianId, politicianVoteDto.politicianId) &&
        Objects.equals(this.voteOption, politicianVoteDto.voteOption) &&
        Objects.equals(this.vote, politicianVoteDto.vote);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, voteId, politicianId, voteOption, vote);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PoliticianVoteDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    voteId: ").append(toIndentedString(voteId)).append("\n");
    sb.append("    politicianId: ").append(toIndentedString(politicianId)).append("\n");
    sb.append("    voteOption: ").append(toIndentedString(voteOption)).append("\n");
    sb.append("    vote: ").append(toIndentedString(vote)).append("\n");
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

