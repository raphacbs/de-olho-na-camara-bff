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
 * PoliticianDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-12-27T15:23:02.402204900-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PoliticianDto {

  private @Nullable Integer id;

  private @Nullable String name;

  private @Nullable String party;

  private @Nullable String partyUri;

  private @Nullable String state;

  private @Nullable Integer legislatureId;

  private @Nullable String email;

  private @Nullable String uri;

  private @Nullable String photoUrl;

  public PoliticianDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", example = "204379", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public PoliticianDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Acácio Favacho", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PoliticianDto party(String party) {
    this.party = party;
    return this;
  }

  /**
   * Get party
   * @return party
   */
  
  @Schema(name = "party", example = "MDB", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party")
  public String getParty() {
    return party;
  }

  public void setParty(String party) {
    this.party = party;
  }

  public PoliticianDto partyUri(String partyUri) {
    this.partyUri = partyUri;
    return this;
  }

  /**
   * Get partyUri
   * @return partyUri
   */
  
  @Schema(name = "partyUri", example = "https://dadosabertos.camara.leg.br/api/v2/partidos/36899", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyUri")
  public String getPartyUri() {
    return partyUri;
  }

  public void setPartyUri(String partyUri) {
    this.partyUri = partyUri;
  }

  public PoliticianDto state(String state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", example = "AP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public PoliticianDto legislatureId(Integer legislatureId) {
    this.legislatureId = legislatureId;
    return this;
  }

  /**
   * Get legislatureId
   * @return legislatureId
   */
  
  @Schema(name = "legislatureId", example = "57", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("legislatureId")
  public Integer getLegislatureId() {
    return legislatureId;
  }

  public void setLegislatureId(Integer legislatureId) {
    this.legislatureId = legislatureId;
  }

  public PoliticianDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", example = "dep.acaciofavacho@camara.leg.br", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public PoliticianDto uri(String uri) {
    this.uri = uri;
    return this;
  }

  /**
   * Get uri
   * @return uri
   */
  
  @Schema(name = "uri", example = "https://dadosabertos.camara.leg.br/api/v2/deputados/204379", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uri")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public PoliticianDto photoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
    return this;
  }

  /**
   * Get photoUrl
   * @return photoUrl
   */
  
  @Schema(name = "photoUrl", example = "https://www.camara.leg.br/internet/deputado/bandep/204379.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("photoUrl")
  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PoliticianDto politicianDto = (PoliticianDto) o;
    return Objects.equals(this.id, politicianDto.id) &&
        Objects.equals(this.name, politicianDto.name) &&
        Objects.equals(this.party, politicianDto.party) &&
        Objects.equals(this.partyUri, politicianDto.partyUri) &&
        Objects.equals(this.state, politicianDto.state) &&
        Objects.equals(this.legislatureId, politicianDto.legislatureId) &&
        Objects.equals(this.email, politicianDto.email) &&
        Objects.equals(this.uri, politicianDto.uri) &&
        Objects.equals(this.photoUrl, politicianDto.photoUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, party, partyUri, state, legislatureId, email, uri, photoUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PoliticianDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    party: ").append(toIndentedString(party)).append("\n");
    sb.append("    partyUri: ").append(toIndentedString(partyUri)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    legislatureId: ").append(toIndentedString(legislatureId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    photoUrl: ").append(toIndentedString(photoUrl)).append("\n");
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

