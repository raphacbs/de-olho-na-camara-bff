package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.coelho.deolhonacamara.api.model.VotingWithVotesDTO;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VotingWithVotesResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class VotingWithVotesResponseDTO {

  @Valid
  private List<@Valid VotingWithVotesDTO> data = new ArrayList<>();

  private @Nullable Integer total;

  private @Nullable Integer page;

  private @Nullable Integer totalPages;

  private @Nullable Integer sizePage;

  public VotingWithVotesResponseDTO data(List<@Valid VotingWithVotesDTO> data) {
    this.data = data;
    return this;
  }

  public VotingWithVotesResponseDTO addDataItem(VotingWithVotesDTO dataItem) {
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
  public List<@Valid VotingWithVotesDTO> getData() {
    return data;
  }

  public void setData(List<@Valid VotingWithVotesDTO> data) {
    this.data = data;
  }

  public VotingWithVotesResponseDTO total(Integer total) {
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

  public VotingWithVotesResponseDTO page(Integer page) {
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

  public VotingWithVotesResponseDTO totalPages(Integer totalPages) {
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

  public VotingWithVotesResponseDTO sizePage(Integer sizePage) {
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
    VotingWithVotesResponseDTO votingWithVotesResponseDTO = (VotingWithVotesResponseDTO) o;
    return Objects.equals(this.data, votingWithVotesResponseDTO.data) &&
        Objects.equals(this.total, votingWithVotesResponseDTO.total) &&
        Objects.equals(this.page, votingWithVotesResponseDTO.page) &&
        Objects.equals(this.totalPages, votingWithVotesResponseDTO.totalPages) &&
        Objects.equals(this.sizePage, votingWithVotesResponseDTO.sizePage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, total, page, totalPages, sizePage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VotingWithVotesResponseDTO {\n");
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

