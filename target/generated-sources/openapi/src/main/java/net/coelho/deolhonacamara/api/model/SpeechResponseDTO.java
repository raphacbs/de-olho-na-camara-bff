package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.SpeechDto;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SpeechResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-12-27T15:23:02.402204900-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SpeechResponseDTO {

  @Valid
  private List<@Valid SpeechDto> data = new ArrayList<>();

  private @Nullable Integer total;

  private @Nullable Integer page;

  private @Nullable Integer totalPages;

  private @Nullable Integer sizePage;

  public SpeechResponseDTO data(List<@Valid SpeechDto> data) {
    this.data = data;
    return this;
  }

  public SpeechResponseDTO addDataItem(SpeechDto dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public List<@Valid SpeechDto> getData() {
    return data;
  }

  public void setData(List<@Valid SpeechDto> data) {
    this.data = data;
  }

  public SpeechResponseDTO total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public SpeechResponseDTO page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public SpeechResponseDTO totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Get totalPages
   * @return totalPages
   */
  
  @Schema(name = "totalPages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public SpeechResponseDTO sizePage(Integer sizePage) {
    this.sizePage = sizePage;
    return this;
  }

  /**
   * Get sizePage
   * @return sizePage
   */
  
  @Schema(name = "sizePage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sizePage")
  public Integer getSizePage() {
    return sizePage;
  }

  public void setSizePage(Integer sizePage) {
    this.sizePage = sizePage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpeechResponseDTO speechResponseDTO = (SpeechResponseDTO) o;
    return Objects.equals(this.data, speechResponseDTO.data) &&
        Objects.equals(this.total, speechResponseDTO.total) &&
        Objects.equals(this.page, speechResponseDTO.page) &&
        Objects.equals(this.totalPages, speechResponseDTO.totalPages) &&
        Objects.equals(this.sizePage, speechResponseDTO.sizePage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, total, page, totalPages, sizePage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpeechResponseDTO {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    sizePage: ").append(toIndentedString(sizePage)).append("\n");
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

