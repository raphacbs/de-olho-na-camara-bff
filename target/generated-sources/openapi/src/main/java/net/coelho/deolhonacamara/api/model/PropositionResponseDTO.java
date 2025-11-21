package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.PropositionDto;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PropositionResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-21T10:53:37.437611200-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PropositionResponseDTO {

  @Valid
  private List<@Valid PropositionDto> data = new ArrayList<>();

  private @Nullable Integer total;

  private @Nullable Integer page;

  private @Nullable Integer totalPages;

  private @Nullable Integer sizePage;

  public PropositionResponseDTO data(List<@Valid PropositionDto> data) {
    this.data = data;
    return this;
  }

  public PropositionResponseDTO addDataItem(PropositionDto dataItem) {
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
  public List<@Valid PropositionDto> getData() {
    return data;
  }

  public void setData(List<@Valid PropositionDto> data) {
    this.data = data;
  }

  public PropositionResponseDTO total(Integer total) {
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

  public PropositionResponseDTO page(Integer page) {
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

  public PropositionResponseDTO totalPages(Integer totalPages) {
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

  public PropositionResponseDTO sizePage(Integer sizePage) {
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
    PropositionResponseDTO propositionResponseDTO = (PropositionResponseDTO) o;
    return Objects.equals(this.data, propositionResponseDTO.data) &&
        Objects.equals(this.total, propositionResponseDTO.total) &&
        Objects.equals(this.page, propositionResponseDTO.page) &&
        Objects.equals(this.totalPages, propositionResponseDTO.totalPages) &&
        Objects.equals(this.sizePage, propositionResponseDTO.sizePage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, total, page, totalPages, sizePage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropositionResponseDTO {\n");
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

