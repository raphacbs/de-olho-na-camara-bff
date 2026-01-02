package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.SDUIComponent;
import net.coelho.deolhonacamara.api.model.SDUINavigation;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SDUIScreen
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SDUIScreen {

  private String id;

  private @Nullable String title;

  private @Nullable SDUINavigation navigation;

  @Valid
  private List<@Valid SDUIComponent> components = new ArrayList<>();

  public SDUIScreen() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SDUIScreen(String id, List<@Valid SDUIComponent> components) {
    this.id = id;
    this.components = components;
  }

  public SDUIScreen id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "home", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SDUIScreen title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", example = "De Olho na Câmara", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public SDUIScreen navigation(SDUINavigation navigation) {
    this.navigation = navigation;
    return this;
  }

  /**
   * Get navigation
   * @return navigation
   */
  @Valid 
  @Schema(name = "navigation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("navigation")
  public SDUINavigation getNavigation() {
    return navigation;
  }

  public void setNavigation(SDUINavigation navigation) {
    this.navigation = navigation;
  }

  public SDUIScreen components(List<@Valid SDUIComponent> components) {
    this.components = components;
    return this;
  }

  public SDUIScreen addComponentsItem(SDUIComponent componentsItem) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(componentsItem);
    return this;
  }

  /**
   * Get components
   * @return components
   */
  @NotNull @Valid 
  @Schema(name = "components", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("components")
  public List<@Valid SDUIComponent> getComponents() {
    return components;
  }

  public void setComponents(List<@Valid SDUIComponent> components) {
    this.components = components;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SDUIScreen sdUIScreen = (SDUIScreen) o;
    return Objects.equals(this.id, sdUIScreen.id) &&
        Objects.equals(this.title, sdUIScreen.title) &&
        Objects.equals(this.navigation, sdUIScreen.navigation) &&
        Objects.equals(this.components, sdUIScreen.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, navigation, components);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SDUIScreen {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    navigation: ").append(toIndentedString(navigation)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
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

