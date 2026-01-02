package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.SDUINavigationHeaderActionsInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SDUINavigationHeader
 */

@JsonTypeName("SDUINavigation_header")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SDUINavigationHeader {

  private @Nullable String title;

  private Boolean showBack = false;

  @Valid
  private List<@Valid SDUINavigationHeaderActionsInner> actions = new ArrayList<>();

  public SDUINavigationHeader title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public SDUINavigationHeader showBack(Boolean showBack) {
    this.showBack = showBack;
    return this;
  }

  /**
   * Get showBack
   * @return showBack
   */
  
  @Schema(name = "showBack", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("showBack")
  public Boolean getShowBack() {
    return showBack;
  }

  public void setShowBack(Boolean showBack) {
    this.showBack = showBack;
  }

  public SDUINavigationHeader actions(List<@Valid SDUINavigationHeaderActionsInner> actions) {
    this.actions = actions;
    return this;
  }

  public SDUINavigationHeader addActionsItem(SDUINavigationHeaderActionsInner actionsItem) {
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
  public List<@Valid SDUINavigationHeaderActionsInner> getActions() {
    return actions;
  }

  public void setActions(List<@Valid SDUINavigationHeaderActionsInner> actions) {
    this.actions = actions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SDUINavigationHeader sdUINavigationHeader = (SDUINavigationHeader) o;
    return Objects.equals(this.title, sdUINavigationHeader.title) &&
        Objects.equals(this.showBack, sdUINavigationHeader.showBack) &&
        Objects.equals(this.actions, sdUINavigationHeader.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, showBack, actions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SDUINavigationHeader {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    showBack: ").append(toIndentedString(showBack)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
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

