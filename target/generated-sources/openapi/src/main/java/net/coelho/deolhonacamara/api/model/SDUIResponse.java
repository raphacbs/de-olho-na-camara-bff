package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.SDUIAction;
import net.coelho.deolhonacamara.api.model.SDUIMetadata;
import net.coelho.deolhonacamara.api.model.SDUIScreen;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SDUIResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SDUIResponse {

  private SDUIScreen screen;

  @Valid
  private List<@Valid SDUIAction> actions = new ArrayList<>();

  private @Nullable SDUIMetadata metadata;

  public SDUIResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SDUIResponse(SDUIScreen screen) {
    this.screen = screen;
  }

  public SDUIResponse screen(SDUIScreen screen) {
    this.screen = screen;
    return this;
  }

  /**
   * Get screen
   * @return screen
   */
  @NotNull @Valid 
  @Schema(name = "screen", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("screen")
  public SDUIScreen getScreen() {
    return screen;
  }

  public void setScreen(SDUIScreen screen) {
    this.screen = screen;
  }

  public SDUIResponse actions(List<@Valid SDUIAction> actions) {
    this.actions = actions;
    return this;
  }

  public SDUIResponse addActionsItem(SDUIAction actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * Get actions
   * @return actions
   */
  @Valid 
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<@Valid SDUIAction> getActions() {
    return actions;
  }

  public void setActions(List<@Valid SDUIAction> actions) {
    this.actions = actions;
  }

  public SDUIResponse metadata(SDUIMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public SDUIMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(SDUIMetadata metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SDUIResponse sdUIResponse = (SDUIResponse) o;
    return Objects.equals(this.screen, sdUIResponse.screen) &&
        Objects.equals(this.actions, sdUIResponse.actions) &&
        Objects.equals(this.metadata, sdUIResponse.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(screen, actions, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SDUIResponse {\n");
    sb.append("    screen: ").append(toIndentedString(screen)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

