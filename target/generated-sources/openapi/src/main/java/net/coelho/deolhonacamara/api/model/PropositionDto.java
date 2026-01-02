package net.coelho.deolhonacamara.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
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
 * PropositionDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-02T15:19:49.071322600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class PropositionDto {

  private @Nullable Integer politicianId;

  private @Nullable Integer id;

  private @Nullable String uri;

  private @Nullable String type;

  private @Nullable String codeType;

  private @Nullable Integer number;

  private @Nullable Integer year;

  private @Nullable String summary;

  private @Nullable String detailedSummary;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate presentationDate;

  private @Nullable String statusDateTime;

  private @Nullable String statusLastReporterUri;

  private @Nullable String statusTramitationDescription;

  private @Nullable String statusTramitationTypeCode;

  private @Nullable String statusSituationDescription;

  private @Nullable String statusSituationCode;

  private @Nullable String statusDispatch;

  private @Nullable String statusUrl;

  private @Nullable String statusScope;

  private @Nullable String statusAppreciation;

  private @Nullable String uriOrgaoNumerador;

  private @Nullable String uriAutores;

  private @Nullable String typeDescription;

  private @Nullable String keywords;

  private @Nullable String uriPropPrincipal;

  private @Nullable String uriPropAnterior;

  private @Nullable String uriPropPosterior;

  private @Nullable String urlInteiroTeor;

  private @Nullable String urnFinal;

  private @Nullable String text;

  private @Nullable String justification;

  private @Nullable String createdAt;

  private @Nullable String updatedAt;

  private @Nullable Object status;

  public PropositionDto politicianId(Integer politicianId) {
    this.politicianId = politicianId;
    return this;
  }

  /**
   * Get politicianId
   * @return politicianId
   */
  
  @Schema(name = "politicianId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("politicianId")
  public Integer getPoliticianId() {
    return politicianId;
  }

  public void setPoliticianId(Integer politicianId) {
    this.politicianId = politicianId;
  }

  public PropositionDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public PropositionDto uri(String uri) {
    this.uri = uri;
    return this;
  }

  /**
   * Get uri
   * @return uri
   */
  
  @Schema(name = "uri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uri")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public PropositionDto type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public PropositionDto codeType(String codeType) {
    this.codeType = codeType;
    return this;
  }

  /**
   * Get codeType
   * @return codeType
   */
  
  @Schema(name = "codeType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("codeType")
  public String getCodeType() {
    return codeType;
  }

  public void setCodeType(String codeType) {
    this.codeType = codeType;
  }

  public PropositionDto number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
   */
  
  @Schema(name = "number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("number")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public PropositionDto year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public PropositionDto summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PropositionDto detailedSummary(String detailedSummary) {
    this.detailedSummary = detailedSummary;
    return this;
  }

  /**
   * Get detailedSummary
   * @return detailedSummary
   */
  
  @Schema(name = "detailedSummary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detailedSummary")
  public String getDetailedSummary() {
    return detailedSummary;
  }

  public void setDetailedSummary(String detailedSummary) {
    this.detailedSummary = detailedSummary;
  }

  public PropositionDto presentationDate(LocalDate presentationDate) {
    this.presentationDate = presentationDate;
    return this;
  }

  /**
   * Get presentationDate
   * @return presentationDate
   */
  @Valid 
  @Schema(name = "presentationDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("presentationDate")
  public LocalDate getPresentationDate() {
    return presentationDate;
  }

  public void setPresentationDate(LocalDate presentationDate) {
    this.presentationDate = presentationDate;
  }

  public PropositionDto statusDateTime(String statusDateTime) {
    this.statusDateTime = statusDateTime;
    return this;
  }

  /**
   * Get statusDateTime
   * @return statusDateTime
   */
  
  @Schema(name = "statusDateTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusDateTime")
  public String getStatusDateTime() {
    return statusDateTime;
  }

  public void setStatusDateTime(String statusDateTime) {
    this.statusDateTime = statusDateTime;
  }

  public PropositionDto statusLastReporterUri(String statusLastReporterUri) {
    this.statusLastReporterUri = statusLastReporterUri;
    return this;
  }

  /**
   * Get statusLastReporterUri
   * @return statusLastReporterUri
   */
  
  @Schema(name = "statusLastReporterUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusLastReporterUri")
  public String getStatusLastReporterUri() {
    return statusLastReporterUri;
  }

  public void setStatusLastReporterUri(String statusLastReporterUri) {
    this.statusLastReporterUri = statusLastReporterUri;
  }

  public PropositionDto statusTramitationDescription(String statusTramitationDescription) {
    this.statusTramitationDescription = statusTramitationDescription;
    return this;
  }

  /**
   * Get statusTramitationDescription
   * @return statusTramitationDescription
   */
  
  @Schema(name = "statusTramitationDescription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusTramitationDescription")
  public String getStatusTramitationDescription() {
    return statusTramitationDescription;
  }

  public void setStatusTramitationDescription(String statusTramitationDescription) {
    this.statusTramitationDescription = statusTramitationDescription;
  }

  public PropositionDto statusTramitationTypeCode(String statusTramitationTypeCode) {
    this.statusTramitationTypeCode = statusTramitationTypeCode;
    return this;
  }

  /**
   * Get statusTramitationTypeCode
   * @return statusTramitationTypeCode
   */
  
  @Schema(name = "statusTramitationTypeCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusTramitationTypeCode")
  public String getStatusTramitationTypeCode() {
    return statusTramitationTypeCode;
  }

  public void setStatusTramitationTypeCode(String statusTramitationTypeCode) {
    this.statusTramitationTypeCode = statusTramitationTypeCode;
  }

  public PropositionDto statusSituationDescription(String statusSituationDescription) {
    this.statusSituationDescription = statusSituationDescription;
    return this;
  }

  /**
   * Get statusSituationDescription
   * @return statusSituationDescription
   */
  
  @Schema(name = "statusSituationDescription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusSituationDescription")
  public String getStatusSituationDescription() {
    return statusSituationDescription;
  }

  public void setStatusSituationDescription(String statusSituationDescription) {
    this.statusSituationDescription = statusSituationDescription;
  }

  public PropositionDto statusSituationCode(String statusSituationCode) {
    this.statusSituationCode = statusSituationCode;
    return this;
  }

  /**
   * Get statusSituationCode
   * @return statusSituationCode
   */
  
  @Schema(name = "statusSituationCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusSituationCode")
  public String getStatusSituationCode() {
    return statusSituationCode;
  }

  public void setStatusSituationCode(String statusSituationCode) {
    this.statusSituationCode = statusSituationCode;
  }

  public PropositionDto statusDispatch(String statusDispatch) {
    this.statusDispatch = statusDispatch;
    return this;
  }

  /**
   * Get statusDispatch
   * @return statusDispatch
   */
  
  @Schema(name = "statusDispatch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusDispatch")
  public String getStatusDispatch() {
    return statusDispatch;
  }

  public void setStatusDispatch(String statusDispatch) {
    this.statusDispatch = statusDispatch;
  }

  public PropositionDto statusUrl(String statusUrl) {
    this.statusUrl = statusUrl;
    return this;
  }

  /**
   * Get statusUrl
   * @return statusUrl
   */
  
  @Schema(name = "statusUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusUrl")
  public String getStatusUrl() {
    return statusUrl;
  }

  public void setStatusUrl(String statusUrl) {
    this.statusUrl = statusUrl;
  }

  public PropositionDto statusScope(String statusScope) {
    this.statusScope = statusScope;
    return this;
  }

  /**
   * Get statusScope
   * @return statusScope
   */
  
  @Schema(name = "statusScope", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusScope")
  public String getStatusScope() {
    return statusScope;
  }

  public void setStatusScope(String statusScope) {
    this.statusScope = statusScope;
  }

  public PropositionDto statusAppreciation(String statusAppreciation) {
    this.statusAppreciation = statusAppreciation;
    return this;
  }

  /**
   * Get statusAppreciation
   * @return statusAppreciation
   */
  
  @Schema(name = "statusAppreciation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusAppreciation")
  public String getStatusAppreciation() {
    return statusAppreciation;
  }

  public void setStatusAppreciation(String statusAppreciation) {
    this.statusAppreciation = statusAppreciation;
  }

  public PropositionDto uriOrgaoNumerador(String uriOrgaoNumerador) {
    this.uriOrgaoNumerador = uriOrgaoNumerador;
    return this;
  }

  /**
   * Get uriOrgaoNumerador
   * @return uriOrgaoNumerador
   */
  
  @Schema(name = "uriOrgaoNumerador", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uriOrgaoNumerador")
  public String getUriOrgaoNumerador() {
    return uriOrgaoNumerador;
  }

  public void setUriOrgaoNumerador(String uriOrgaoNumerador) {
    this.uriOrgaoNumerador = uriOrgaoNumerador;
  }

  public PropositionDto uriAutores(String uriAutores) {
    this.uriAutores = uriAutores;
    return this;
  }

  /**
   * Get uriAutores
   * @return uriAutores
   */
  
  @Schema(name = "uriAutores", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uriAutores")
  public String getUriAutores() {
    return uriAutores;
  }

  public void setUriAutores(String uriAutores) {
    this.uriAutores = uriAutores;
  }

  public PropositionDto typeDescription(String typeDescription) {
    this.typeDescription = typeDescription;
    return this;
  }

  /**
   * Get typeDescription
   * @return typeDescription
   */
  
  @Schema(name = "typeDescription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("typeDescription")
  public String getTypeDescription() {
    return typeDescription;
  }

  public void setTypeDescription(String typeDescription) {
    this.typeDescription = typeDescription;
  }

  public PropositionDto keywords(String keywords) {
    this.keywords = keywords;
    return this;
  }

  /**
   * Get keywords
   * @return keywords
   */
  
  @Schema(name = "keywords", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keywords")
  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public PropositionDto uriPropPrincipal(String uriPropPrincipal) {
    this.uriPropPrincipal = uriPropPrincipal;
    return this;
  }

  /**
   * Get uriPropPrincipal
   * @return uriPropPrincipal
   */
  
  @Schema(name = "uriPropPrincipal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uriPropPrincipal")
  public String getUriPropPrincipal() {
    return uriPropPrincipal;
  }

  public void setUriPropPrincipal(String uriPropPrincipal) {
    this.uriPropPrincipal = uriPropPrincipal;
  }

  public PropositionDto uriPropAnterior(String uriPropAnterior) {
    this.uriPropAnterior = uriPropAnterior;
    return this;
  }

  /**
   * Get uriPropAnterior
   * @return uriPropAnterior
   */
  
  @Schema(name = "uriPropAnterior", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uriPropAnterior")
  public String getUriPropAnterior() {
    return uriPropAnterior;
  }

  public void setUriPropAnterior(String uriPropAnterior) {
    this.uriPropAnterior = uriPropAnterior;
  }

  public PropositionDto uriPropPosterior(String uriPropPosterior) {
    this.uriPropPosterior = uriPropPosterior;
    return this;
  }

  /**
   * Get uriPropPosterior
   * @return uriPropPosterior
   */
  
  @Schema(name = "uriPropPosterior", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uriPropPosterior")
  public String getUriPropPosterior() {
    return uriPropPosterior;
  }

  public void setUriPropPosterior(String uriPropPosterior) {
    this.uriPropPosterior = uriPropPosterior;
  }

  public PropositionDto urlInteiroTeor(String urlInteiroTeor) {
    this.urlInteiroTeor = urlInteiroTeor;
    return this;
  }

  /**
   * Get urlInteiroTeor
   * @return urlInteiroTeor
   */
  
  @Schema(name = "urlInteiroTeor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("urlInteiroTeor")
  public String getUrlInteiroTeor() {
    return urlInteiroTeor;
  }

  public void setUrlInteiroTeor(String urlInteiroTeor) {
    this.urlInteiroTeor = urlInteiroTeor;
  }

  public PropositionDto urnFinal(String urnFinal) {
    this.urnFinal = urnFinal;
    return this;
  }

  /**
   * Get urnFinal
   * @return urnFinal
   */
  
  @Schema(name = "urnFinal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("urnFinal")
  public String getUrnFinal() {
    return urnFinal;
  }

  public void setUrnFinal(String urnFinal) {
    this.urnFinal = urnFinal;
  }

  public PropositionDto text(String text) {
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

  public PropositionDto justification(String justification) {
    this.justification = justification;
    return this;
  }

  /**
   * Get justification
   * @return justification
   */
  
  @Schema(name = "justification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("justification")
  public String getJustification() {
    return justification;
  }

  public void setJustification(String justification) {
    this.justification = justification;
  }

  public PropositionDto createdAt(String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public PropositionDto updatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public PropositionDto status(Object status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public Object getStatus() {
    return status;
  }

  public void setStatus(Object status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropositionDto propositionDto = (PropositionDto) o;
    return Objects.equals(this.politicianId, propositionDto.politicianId) &&
        Objects.equals(this.id, propositionDto.id) &&
        Objects.equals(this.uri, propositionDto.uri) &&
        Objects.equals(this.type, propositionDto.type) &&
        Objects.equals(this.codeType, propositionDto.codeType) &&
        Objects.equals(this.number, propositionDto.number) &&
        Objects.equals(this.year, propositionDto.year) &&
        Objects.equals(this.summary, propositionDto.summary) &&
        Objects.equals(this.detailedSummary, propositionDto.detailedSummary) &&
        Objects.equals(this.presentationDate, propositionDto.presentationDate) &&
        Objects.equals(this.statusDateTime, propositionDto.statusDateTime) &&
        Objects.equals(this.statusLastReporterUri, propositionDto.statusLastReporterUri) &&
        Objects.equals(this.statusTramitationDescription, propositionDto.statusTramitationDescription) &&
        Objects.equals(this.statusTramitationTypeCode, propositionDto.statusTramitationTypeCode) &&
        Objects.equals(this.statusSituationDescription, propositionDto.statusSituationDescription) &&
        Objects.equals(this.statusSituationCode, propositionDto.statusSituationCode) &&
        Objects.equals(this.statusDispatch, propositionDto.statusDispatch) &&
        Objects.equals(this.statusUrl, propositionDto.statusUrl) &&
        Objects.equals(this.statusScope, propositionDto.statusScope) &&
        Objects.equals(this.statusAppreciation, propositionDto.statusAppreciation) &&
        Objects.equals(this.uriOrgaoNumerador, propositionDto.uriOrgaoNumerador) &&
        Objects.equals(this.uriAutores, propositionDto.uriAutores) &&
        Objects.equals(this.typeDescription, propositionDto.typeDescription) &&
        Objects.equals(this.keywords, propositionDto.keywords) &&
        Objects.equals(this.uriPropPrincipal, propositionDto.uriPropPrincipal) &&
        Objects.equals(this.uriPropAnterior, propositionDto.uriPropAnterior) &&
        Objects.equals(this.uriPropPosterior, propositionDto.uriPropPosterior) &&
        Objects.equals(this.urlInteiroTeor, propositionDto.urlInteiroTeor) &&
        Objects.equals(this.urnFinal, propositionDto.urnFinal) &&
        Objects.equals(this.text, propositionDto.text) &&
        Objects.equals(this.justification, propositionDto.justification) &&
        Objects.equals(this.createdAt, propositionDto.createdAt) &&
        Objects.equals(this.updatedAt, propositionDto.updatedAt) &&
        Objects.equals(this.status, propositionDto.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(politicianId, id, uri, type, codeType, number, year, summary, detailedSummary, presentationDate, statusDateTime, statusLastReporterUri, statusTramitationDescription, statusTramitationTypeCode, statusSituationDescription, statusSituationCode, statusDispatch, statusUrl, statusScope, statusAppreciation, uriOrgaoNumerador, uriAutores, typeDescription, keywords, uriPropPrincipal, uriPropAnterior, uriPropPosterior, urlInteiroTeor, urnFinal, text, justification, createdAt, updatedAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropositionDto {\n");
    sb.append("    politicianId: ").append(toIndentedString(politicianId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    codeType: ").append(toIndentedString(codeType)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    detailedSummary: ").append(toIndentedString(detailedSummary)).append("\n");
    sb.append("    presentationDate: ").append(toIndentedString(presentationDate)).append("\n");
    sb.append("    statusDateTime: ").append(toIndentedString(statusDateTime)).append("\n");
    sb.append("    statusLastReporterUri: ").append(toIndentedString(statusLastReporterUri)).append("\n");
    sb.append("    statusTramitationDescription: ").append(toIndentedString(statusTramitationDescription)).append("\n");
    sb.append("    statusTramitationTypeCode: ").append(toIndentedString(statusTramitationTypeCode)).append("\n");
    sb.append("    statusSituationDescription: ").append(toIndentedString(statusSituationDescription)).append("\n");
    sb.append("    statusSituationCode: ").append(toIndentedString(statusSituationCode)).append("\n");
    sb.append("    statusDispatch: ").append(toIndentedString(statusDispatch)).append("\n");
    sb.append("    statusUrl: ").append(toIndentedString(statusUrl)).append("\n");
    sb.append("    statusScope: ").append(toIndentedString(statusScope)).append("\n");
    sb.append("    statusAppreciation: ").append(toIndentedString(statusAppreciation)).append("\n");
    sb.append("    uriOrgaoNumerador: ").append(toIndentedString(uriOrgaoNumerador)).append("\n");
    sb.append("    uriAutores: ").append(toIndentedString(uriAutores)).append("\n");
    sb.append("    typeDescription: ").append(toIndentedString(typeDescription)).append("\n");
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    uriPropPrincipal: ").append(toIndentedString(uriPropPrincipal)).append("\n");
    sb.append("    uriPropAnterior: ").append(toIndentedString(uriPropAnterior)).append("\n");
    sb.append("    uriPropPosterior: ").append(toIndentedString(uriPropPosterior)).append("\n");
    sb.append("    urlInteiroTeor: ").append(toIndentedString(urlInteiroTeor)).append("\n");
    sb.append("    urnFinal: ").append(toIndentedString(urnFinal)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    justification: ").append(toIndentedString(justification)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

