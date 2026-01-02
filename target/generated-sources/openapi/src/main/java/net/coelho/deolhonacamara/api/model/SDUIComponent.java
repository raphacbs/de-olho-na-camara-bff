package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SDUIComponent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SDUIComponent {

  private @Nullable String id;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CONTAINER("Container"),
    
    TEXT_BLOCK("TextBlock"),
    
    BUTTON("Button"),
    
    CARD("Card"),
    
    IMAGE("Image"),
    
    SPACER("Spacer"),
    
    INPUT("Input"),
    
    AVATAR("Avatar"),
    
    ADVANCED_FILTER("AdvancedFilter");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String title;

  private @Nullable String subtitle;

  private @Nullable String text;

  /**
   * Gets or Sets variant
   */
  public enum VariantEnum {
    DISPLAY("display"),
    
    HEADLINE("headline"),
    
    TITLE("title"),
    
    BODY("body"),
    
    CAPTION("caption");

    private final String value;

    VariantEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static VariantEnum fromValue(String value) {
      for (VariantEnum b : VariantEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VariantEnum variant;

  private @Nullable String color;

  private @Nullable Integer fontSize;

  private @Nullable String fontWeight;

  /**
   * Gets or Sets textAlign
   */
  public enum TextAlignEnum {
    LEFT("left"),
    
    CENTER("center"),
    
    RIGHT("right");

    private final String value;

    TextAlignEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TextAlignEnum fromValue(String value) {
      for (TextAlignEnum b : TextAlignEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TextAlignEnum textAlign;

  /**
   * Gets or Sets direction
   */
  public enum DirectionEnum {
    ROW("row"),
    
    COLUMN("column");

    private final String value;

    DirectionEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DirectionEnum fromValue(String value) {
      for (DirectionEnum b : DirectionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DirectionEnum direction;

  /**
   * Gets or Sets justifyContent
   */
  public enum JustifyContentEnum {
    FLEX_START("flex-start"),
    
    CENTER("center"),
    
    FLEX_END("flex-end"),
    
    SPACE_BETWEEN("space-between"),
    
    SPACE_AROUND("space-around");

    private final String value;

    JustifyContentEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static JustifyContentEnum fromValue(String value) {
      for (JustifyContentEnum b : JustifyContentEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable JustifyContentEnum justifyContent;

  /**
   * Gets or Sets alignItems
   */
  public enum AlignItemsEnum {
    FLEX_START("flex-start"),
    
    CENTER("center"),
    
    FLEX_END("flex-end"),
    
    STRETCH("stretch");

    private final String value;

    AlignItemsEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AlignItemsEnum fromValue(String value) {
      for (AlignItemsEnum b : AlignItemsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AlignItemsEnum alignItems;

  private @Nullable Integer spacing;

  private @Nullable String padding;

  private @Nullable String margin;

  private @Nullable String backgroundColor;

  private @Nullable Integer borderRadius;

  private @Nullable Integer elevation;

  /**
   * Gets or Sets size
   */
  public enum SizeEnum {
    SMALL("small"),
    
    MEDIUM("medium"),
    
    LARGE("large");

    private final String value;

    SizeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SizeEnum fromValue(String value) {
      for (SizeEnum b : SizeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SizeEnum size;

  private @Nullable Boolean scrollable;

  private @Nullable Boolean horizontal;

  private @Nullable Boolean sticky;

  private @Nullable String onPress;

  @Valid
  private Map<String, Object> actionParams = new HashMap<>();

  private @Nullable String source;

  private @Nullable Integer width;

  private @Nullable Integer height;

  /**
   * Gets or Sets resizeMode
   */
  public enum ResizeModeEnum {
    COVER("cover"),
    
    CONTAIN("contain"),
    
    STRETCH("stretch"),
    
    CENTER("center");

    private final String value;

    ResizeModeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ResizeModeEnum fromValue(String value) {
      for (ResizeModeEnum b : ResizeModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ResizeModeEnum resizeMode;

  private @Nullable String placeholder;

  /**
   * Gets or Sets inputType
   */
  public enum InputTypeEnum {
    TEXT("text"),
    
    EMAIL("email"),
    
    PASSWORD("password"),
    
    NUMBER("number");

    private final String value;

    InputTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InputTypeEnum fromValue(String value) {
      for (InputTypeEnum b : InputTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable InputTypeEnum inputType;

  @Valid
  private List<@Valid SDUIComponent> children = new ArrayList<>();

  @Valid
  private Map<String, Object> style = new HashMap<>();

  @Valid
  private Map<String, Object> props = new HashMap<>();

  public SDUIComponent id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", example = "header-container", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SDUIComponent type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", example = "Container", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SDUIComponent title(String title) {
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

  public SDUIComponent subtitle(String subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  /**
   * Get subtitle
   * @return subtitle
   */
  
  @Schema(name = "subtitle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subtitle")
  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public SDUIComponent text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public SDUIComponent variant(VariantEnum variant) {
    this.variant = variant;
    return this;
  }

  /**
   * Get variant
   * @return variant
   */
  
  @Schema(name = "variant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("variant")
  public VariantEnum getVariant() {
    return variant;
  }

  public void setVariant(VariantEnum variant) {
    this.variant = variant;
  }

  public SDUIComponent color(String color) {
    this.color = color;
    return this;
  }

  /**
   * Get color
   * @return color
   */
  
  @Schema(name = "color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("color")
  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public SDUIComponent fontSize(Integer fontSize) {
    this.fontSize = fontSize;
    return this;
  }

  /**
   * Get fontSize
   * @return fontSize
   */
  
  @Schema(name = "fontSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fontSize")
  public Integer getFontSize() {
    return fontSize;
  }

  public void setFontSize(Integer fontSize) {
    this.fontSize = fontSize;
  }

  public SDUIComponent fontWeight(String fontWeight) {
    this.fontWeight = fontWeight;
    return this;
  }

  /**
   * Get fontWeight
   * @return fontWeight
   */
  
  @Schema(name = "fontWeight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fontWeight")
  public String getFontWeight() {
    return fontWeight;
  }

  public void setFontWeight(String fontWeight) {
    this.fontWeight = fontWeight;
  }

  public SDUIComponent textAlign(TextAlignEnum textAlign) {
    this.textAlign = textAlign;
    return this;
  }

  /**
   * Get textAlign
   * @return textAlign
   */
  
  @Schema(name = "textAlign", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("textAlign")
  public TextAlignEnum getTextAlign() {
    return textAlign;
  }

  public void setTextAlign(TextAlignEnum textAlign) {
    this.textAlign = textAlign;
  }

  public SDUIComponent direction(DirectionEnum direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Get direction
   * @return direction
   */
  
  @Schema(name = "direction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("direction")
  public DirectionEnum getDirection() {
    return direction;
  }

  public void setDirection(DirectionEnum direction) {
    this.direction = direction;
  }

  public SDUIComponent justifyContent(JustifyContentEnum justifyContent) {
    this.justifyContent = justifyContent;
    return this;
  }

  /**
   * Get justifyContent
   * @return justifyContent
   */
  
  @Schema(name = "justifyContent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("justifyContent")
  public JustifyContentEnum getJustifyContent() {
    return justifyContent;
  }

  public void setJustifyContent(JustifyContentEnum justifyContent) {
    this.justifyContent = justifyContent;
  }

  public SDUIComponent alignItems(AlignItemsEnum alignItems) {
    this.alignItems = alignItems;
    return this;
  }

  /**
   * Get alignItems
   * @return alignItems
   */
  
  @Schema(name = "alignItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alignItems")
  public AlignItemsEnum getAlignItems() {
    return alignItems;
  }

  public void setAlignItems(AlignItemsEnum alignItems) {
    this.alignItems = alignItems;
  }

  public SDUIComponent spacing(Integer spacing) {
    this.spacing = spacing;
    return this;
  }

  /**
   * Get spacing
   * @return spacing
   */
  
  @Schema(name = "spacing", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spacing")
  public Integer getSpacing() {
    return spacing;
  }

  public void setSpacing(Integer spacing) {
    this.spacing = spacing;
  }

  public SDUIComponent padding(String padding) {
    this.padding = padding;
    return this;
  }

  /**
   * Get padding
   * @return padding
   */
  
  @Schema(name = "padding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("padding")
  public String getPadding() {
    return padding;
  }

  public void setPadding(String padding) {
    this.padding = padding;
  }

  public SDUIComponent margin(String margin) {
    this.margin = margin;
    return this;
  }

  /**
   * Get margin
   * @return margin
   */
  
  @Schema(name = "margin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("margin")
  public String getMargin() {
    return margin;
  }

  public void setMargin(String margin) {
    this.margin = margin;
  }

  public SDUIComponent backgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  /**
   * Get backgroundColor
   * @return backgroundColor
   */
  
  @Schema(name = "backgroundColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backgroundColor")
  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public SDUIComponent borderRadius(Integer borderRadius) {
    this.borderRadius = borderRadius;
    return this;
  }

  /**
   * Get borderRadius
   * @return borderRadius
   */
  
  @Schema(name = "borderRadius", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("borderRadius")
  public Integer getBorderRadius() {
    return borderRadius;
  }

  public void setBorderRadius(Integer borderRadius) {
    this.borderRadius = borderRadius;
  }

  public SDUIComponent elevation(Integer elevation) {
    this.elevation = elevation;
    return this;
  }

  /**
   * Get elevation
   * @return elevation
   */
  
  @Schema(name = "elevation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("elevation")
  public Integer getElevation() {
    return elevation;
  }

  public void setElevation(Integer elevation) {
    this.elevation = elevation;
  }

  public SDUIComponent size(SizeEnum size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
   */
  
  @Schema(name = "size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size")
  public SizeEnum getSize() {
    return size;
  }

  public void setSize(SizeEnum size) {
    this.size = size;
  }

  public SDUIComponent scrollable(Boolean scrollable) {
    this.scrollable = scrollable;
    return this;
  }

  /**
   * Get scrollable
   * @return scrollable
   */
  
  @Schema(name = "scrollable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scrollable")
  public Boolean getScrollable() {
    return scrollable;
  }

  public void setScrollable(Boolean scrollable) {
    this.scrollable = scrollable;
  }

  public SDUIComponent horizontal(Boolean horizontal) {
    this.horizontal = horizontal;
    return this;
  }

  /**
   * Get horizontal
   * @return horizontal
   */
  
  @Schema(name = "horizontal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("horizontal")
  public Boolean getHorizontal() {
    return horizontal;
  }

  public void setHorizontal(Boolean horizontal) {
    this.horizontal = horizontal;
  }

  public SDUIComponent sticky(Boolean sticky) {
    this.sticky = sticky;
    return this;
  }

  /**
   * Get sticky
   * @return sticky
   */
  
  @Schema(name = "sticky", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sticky")
  public Boolean getSticky() {
    return sticky;
  }

  public void setSticky(Boolean sticky) {
    this.sticky = sticky;
  }

  public SDUIComponent onPress(String onPress) {
    this.onPress = onPress;
    return this;
  }

  /**
   * Get onPress
   * @return onPress
   */
  
  @Schema(name = "onPress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("onPress")
  public String getOnPress() {
    return onPress;
  }

  public void setOnPress(String onPress) {
    this.onPress = onPress;
  }

  public SDUIComponent actionParams(Map<String, Object> actionParams) {
    this.actionParams = actionParams;
    return this;
  }

  public SDUIComponent putActionParamsItem(String key, Object actionParamsItem) {
    if (this.actionParams == null) {
      this.actionParams = new HashMap<>();
    }
    this.actionParams.put(key, actionParamsItem);
    return this;
  }

  /**
   * Get actionParams
   * @return actionParams
   */
  
  @Schema(name = "actionParams", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionParams")
  public Map<String, Object> getActionParams() {
    return actionParams;
  }

  public void setActionParams(Map<String, Object> actionParams) {
    this.actionParams = actionParams;
  }

  public SDUIComponent source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public SDUIComponent width(Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Get width
   * @return width
   */
  
  @Schema(name = "width", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("width")
  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public SDUIComponent height(Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Get height
   * @return height
   */
  
  @Schema(name = "height", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("height")
  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public SDUIComponent resizeMode(ResizeModeEnum resizeMode) {
    this.resizeMode = resizeMode;
    return this;
  }

  /**
   * Get resizeMode
   * @return resizeMode
   */
  
  @Schema(name = "resizeMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resizeMode")
  public ResizeModeEnum getResizeMode() {
    return resizeMode;
  }

  public void setResizeMode(ResizeModeEnum resizeMode) {
    this.resizeMode = resizeMode;
  }

  public SDUIComponent placeholder(String placeholder) {
    this.placeholder = placeholder;
    return this;
  }

  /**
   * Get placeholder
   * @return placeholder
   */
  
  @Schema(name = "placeholder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placeholder")
  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public SDUIComponent inputType(InputTypeEnum inputType) {
    this.inputType = inputType;
    return this;
  }

  /**
   * Get inputType
   * @return inputType
   */
  
  @Schema(name = "inputType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inputType")
  public InputTypeEnum getInputType() {
    return inputType;
  }

  public void setInputType(InputTypeEnum inputType) {
    this.inputType = inputType;
  }

  public SDUIComponent children(List<@Valid SDUIComponent> children) {
    this.children = children;
    return this;
  }

  public SDUIComponent addChildrenItem(SDUIComponent childrenItem) {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(childrenItem);
    return this;
  }

  /**
   * Get children
   * @return children
   */
  @Valid 
  @Schema(name = "children", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("children")
  public List<@Valid SDUIComponent> getChildren() {
    return children;
  }

  public void setChildren(List<@Valid SDUIComponent> children) {
    this.children = children;
  }

  public SDUIComponent style(Map<String, Object> style) {
    this.style = style;
    return this;
  }

  public SDUIComponent putStyleItem(String key, Object styleItem) {
    if (this.style == null) {
      this.style = new HashMap<>();
    }
    this.style.put(key, styleItem);
    return this;
  }

  /**
   * Get style
   * @return style
   */
  
  @Schema(name = "style", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("style")
  public Map<String, Object> getStyle() {
    return style;
  }

  public void setStyle(Map<String, Object> style) {
    this.style = style;
  }

  public SDUIComponent props(Map<String, Object> props) {
    this.props = props;
    return this;
  }

  public SDUIComponent putPropsItem(String key, Object propsItem) {
    if (this.props == null) {
      this.props = new HashMap<>();
    }
    this.props.put(key, propsItem);
    return this;
  }

  /**
   * Get props
   * @return props
   */
  
  @Schema(name = "props", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("props")
  public Map<String, Object> getProps() {
    return props;
  }

  public void setProps(Map<String, Object> props) {
    this.props = props;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SDUIComponent sdUIComponent = (SDUIComponent) o;
    return Objects.equals(this.id, sdUIComponent.id) &&
        Objects.equals(this.type, sdUIComponent.type) &&
        Objects.equals(this.title, sdUIComponent.title) &&
        Objects.equals(this.subtitle, sdUIComponent.subtitle) &&
        Objects.equals(this.text, sdUIComponent.text) &&
        Objects.equals(this.variant, sdUIComponent.variant) &&
        Objects.equals(this.color, sdUIComponent.color) &&
        Objects.equals(this.fontSize, sdUIComponent.fontSize) &&
        Objects.equals(this.fontWeight, sdUIComponent.fontWeight) &&
        Objects.equals(this.textAlign, sdUIComponent.textAlign) &&
        Objects.equals(this.direction, sdUIComponent.direction) &&
        Objects.equals(this.justifyContent, sdUIComponent.justifyContent) &&
        Objects.equals(this.alignItems, sdUIComponent.alignItems) &&
        Objects.equals(this.spacing, sdUIComponent.spacing) &&
        Objects.equals(this.padding, sdUIComponent.padding) &&
        Objects.equals(this.margin, sdUIComponent.margin) &&
        Objects.equals(this.backgroundColor, sdUIComponent.backgroundColor) &&
        Objects.equals(this.borderRadius, sdUIComponent.borderRadius) &&
        Objects.equals(this.elevation, sdUIComponent.elevation) &&
        Objects.equals(this.size, sdUIComponent.size) &&
        Objects.equals(this.scrollable, sdUIComponent.scrollable) &&
        Objects.equals(this.horizontal, sdUIComponent.horizontal) &&
        Objects.equals(this.sticky, sdUIComponent.sticky) &&
        Objects.equals(this.onPress, sdUIComponent.onPress) &&
        Objects.equals(this.actionParams, sdUIComponent.actionParams) &&
        Objects.equals(this.source, sdUIComponent.source) &&
        Objects.equals(this.width, sdUIComponent.width) &&
        Objects.equals(this.height, sdUIComponent.height) &&
        Objects.equals(this.resizeMode, sdUIComponent.resizeMode) &&
        Objects.equals(this.placeholder, sdUIComponent.placeholder) &&
        Objects.equals(this.inputType, sdUIComponent.inputType) &&
        Objects.equals(this.children, sdUIComponent.children) &&
        Objects.equals(this.style, sdUIComponent.style) &&
        Objects.equals(this.props, sdUIComponent.props);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, title, subtitle, text, variant, color, fontSize, fontWeight, textAlign, direction, justifyContent, alignItems, spacing, padding, margin, backgroundColor, borderRadius, elevation, size, scrollable, horizontal, sticky, onPress, actionParams, source, width, height, resizeMode, placeholder, inputType, children, style, props);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SDUIComponent {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    subtitle: ").append(toIndentedString(subtitle)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    variant: ").append(toIndentedString(variant)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    fontSize: ").append(toIndentedString(fontSize)).append("\n");
    sb.append("    fontWeight: ").append(toIndentedString(fontWeight)).append("\n");
    sb.append("    textAlign: ").append(toIndentedString(textAlign)).append("\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
    sb.append("    justifyContent: ").append(toIndentedString(justifyContent)).append("\n");
    sb.append("    alignItems: ").append(toIndentedString(alignItems)).append("\n");
    sb.append("    spacing: ").append(toIndentedString(spacing)).append("\n");
    sb.append("    padding: ").append(toIndentedString(padding)).append("\n");
    sb.append("    margin: ").append(toIndentedString(margin)).append("\n");
    sb.append("    backgroundColor: ").append(toIndentedString(backgroundColor)).append("\n");
    sb.append("    borderRadius: ").append(toIndentedString(borderRadius)).append("\n");
    sb.append("    elevation: ").append(toIndentedString(elevation)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    scrollable: ").append(toIndentedString(scrollable)).append("\n");
    sb.append("    horizontal: ").append(toIndentedString(horizontal)).append("\n");
    sb.append("    sticky: ").append(toIndentedString(sticky)).append("\n");
    sb.append("    onPress: ").append(toIndentedString(onPress)).append("\n");
    sb.append("    actionParams: ").append(toIndentedString(actionParams)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    resizeMode: ").append(toIndentedString(resizeMode)).append("\n");
    sb.append("    placeholder: ").append(toIndentedString(placeholder)).append("\n");
    sb.append("    inputType: ").append(toIndentedString(inputType)).append("\n");
    sb.append("    children: ").append(toIndentedString(children)).append("\n");
    sb.append("    style: ").append(toIndentedString(style)).append("\n");
    sb.append("    props: ").append(toIndentedString(props)).append("\n");
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

