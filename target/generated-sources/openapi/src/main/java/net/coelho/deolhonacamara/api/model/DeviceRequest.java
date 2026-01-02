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
 * DeviceRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class DeviceRequest {

  private String fcmToken;

  public DeviceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DeviceRequest(String fcmToken) {
    this.fcmToken = fcmToken;
  }

  public DeviceRequest fcmToken(String fcmToken) {
    this.fcmToken = fcmToken;
    return this;
  }

  /**
   * Firebase Cloud Messaging token
   * @return fcmToken
   */
  @NotNull 
  @Schema(name = "fcmToken", description = "Firebase Cloud Messaging token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fcmToken")
  public String getFcmToken() {
    return fcmToken;
  }

  public void setFcmToken(String fcmToken) {
    this.fcmToken = fcmToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceRequest deviceRequest = (DeviceRequest) o;
    return Objects.equals(this.fcmToken, deviceRequest.fcmToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fcmToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceRequest {\n");
    sb.append("    fcmToken: ").append(toIndentedString(fcmToken)).append("\n");
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

