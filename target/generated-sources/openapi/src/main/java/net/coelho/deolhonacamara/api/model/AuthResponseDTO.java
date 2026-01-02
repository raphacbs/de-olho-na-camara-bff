package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * AuthResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T16:04:43.208263200-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class AuthResponseDTO {

  private @Nullable String accessToken;

  private @Nullable String refreshToken;

  private @Nullable String tokenType;

  private @Nullable Integer expireIn;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expireAt;

  public AuthResponseDTO accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Get accessToken
   * @return accessToken
   */
  
  @Schema(name = "accessToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accessToken")
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public AuthResponseDTO refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  /**
   * Get refreshToken
   * @return refreshToken
   */
  
  @Schema(name = "refreshToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refreshToken")
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public AuthResponseDTO tokenType(String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * Get tokenType
   * @return tokenType
   */
  
  @Schema(name = "tokenType", example = "Bearer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tokenType")
  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public AuthResponseDTO expireIn(Integer expireIn) {
    this.expireIn = expireIn;
    return this;
  }

  /**
   * Get expireIn
   * @return expireIn
   */
  
  @Schema(name = "expireIn", example = "3600", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expireIn")
  public Integer getExpireIn() {
    return expireIn;
  }

  public void setExpireIn(Integer expireIn) {
    this.expireIn = expireIn;
  }

  public AuthResponseDTO expireAt(OffsetDateTime expireAt) {
    this.expireAt = expireAt;
    return this;
  }

  /**
   * Get expireAt
   * @return expireAt
   */
  @Valid 
  @Schema(name = "expireAt", example = "2025-12-28T16:43:57Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expireAt")
  public OffsetDateTime getExpireAt() {
    return expireAt;
  }

  public void setExpireAt(OffsetDateTime expireAt) {
    this.expireAt = expireAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthResponseDTO authResponseDTO = (AuthResponseDTO) o;
    return Objects.equals(this.accessToken, authResponseDTO.accessToken) &&
        Objects.equals(this.refreshToken, authResponseDTO.refreshToken) &&
        Objects.equals(this.tokenType, authResponseDTO.tokenType) &&
        Objects.equals(this.expireIn, authResponseDTO.expireIn) &&
        Objects.equals(this.expireAt, authResponseDTO.expireAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, refreshToken, tokenType, expireIn, expireAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthResponseDTO {\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    expireIn: ").append(toIndentedString(expireIn)).append("\n");
    sb.append("    expireAt: ").append(toIndentedString(expireAt)).append("\n");
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

