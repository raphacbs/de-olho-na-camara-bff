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
 * SDUIMetadata
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SDUIMetadata {

  private @Nullable String version;

  private Boolean cache = true;

  private @Nullable Integer ttl;

  public SDUIMetadata version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", example = "1.0.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public SDUIMetadata cache(Boolean cache) {
    this.cache = cache;
    return this;
  }

  /**
   * Get cache
   * @return cache
   */
  
  @Schema(name = "cache", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache")
  public Boolean getCache() {
    return cache;
  }

  public void setCache(Boolean cache) {
    this.cache = cache;
  }

  public SDUIMetadata ttl(Integer ttl) {
    this.ttl = ttl;
    return this;
  }

  /**
   * Time to live in seconds
   * @return ttl
   */
  
  @Schema(name = "ttl", description = "Time to live in seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ttl")
  public Integer getTtl() {
    return ttl;
  }

  public void setTtl(Integer ttl) {
    this.ttl = ttl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SDUIMetadata sdUIMetadata = (SDUIMetadata) o;
    return Objects.equals(this.version, sdUIMetadata.version) &&
        Objects.equals(this.cache, sdUIMetadata.cache) &&
        Objects.equals(this.ttl, sdUIMetadata.ttl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, cache, ttl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SDUIMetadata {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    cache: ").append(toIndentedString(cache)).append("\n");
    sb.append("    ttl: ").append(toIndentedString(ttl)).append("\n");
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

