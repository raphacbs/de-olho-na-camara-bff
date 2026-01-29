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
 * SpeechDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-25T15:13:37.457952600-03:00[America/Fortaleza]", comments = "Generator version: 7.13.0")
public class SpeechDto {

  private @Nullable String startDateTime;

  private @Nullable String endDateTime;

  private @Nullable String titleEvent;

  private @Nullable String endDateTimeEvent;

  private @Nullable String startDateTimeEvent;

  private @Nullable String summary;

  private @Nullable String speechType;

  private @Nullable String transcription;

  private @Nullable String eventUri;

  private @Nullable String audioUrl;

  private @Nullable String textUrl;

  private @Nullable String videoUrl;

  public SpeechDto startDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
    return this;
  }

  /**
   * Get startDateTime
   * @return startDateTime
   */
  
  @Schema(name = "startDateTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startDateTime")
  public String getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
  }

  public SpeechDto endDateTime(String endDateTime) {
    this.endDateTime = endDateTime;
    return this;
  }

  /**
   * Get endDateTime
   * @return endDateTime
   */
  
  @Schema(name = "endDateTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDateTime")
  public String getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(String endDateTime) {
    this.endDateTime = endDateTime;
  }

  public SpeechDto titleEvent(String titleEvent) {
    this.titleEvent = titleEvent;
    return this;
  }

  /**
   * Get titleEvent
   * @return titleEvent
   */
  
  @Schema(name = "titleEvent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("titleEvent")
  public String getTitleEvent() {
    return titleEvent;
  }

  public void setTitleEvent(String titleEvent) {
    this.titleEvent = titleEvent;
  }

  public SpeechDto endDateTimeEvent(String endDateTimeEvent) {
    this.endDateTimeEvent = endDateTimeEvent;
    return this;
  }

  /**
   * Get endDateTimeEvent
   * @return endDateTimeEvent
   */
  
  @Schema(name = "endDateTimeEvent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDateTimeEvent")
  public String getEndDateTimeEvent() {
    return endDateTimeEvent;
  }

  public void setEndDateTimeEvent(String endDateTimeEvent) {
    this.endDateTimeEvent = endDateTimeEvent;
  }

  public SpeechDto startDateTimeEvent(String startDateTimeEvent) {
    this.startDateTimeEvent = startDateTimeEvent;
    return this;
  }

  /**
   * Get startDateTimeEvent
   * @return startDateTimeEvent
   */
  
  @Schema(name = "startDateTimeEvent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startDateTimeEvent")
  public String getStartDateTimeEvent() {
    return startDateTimeEvent;
  }

  public void setStartDateTimeEvent(String startDateTimeEvent) {
    this.startDateTimeEvent = startDateTimeEvent;
  }

  public SpeechDto summary(String summary) {
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

  public SpeechDto speechType(String speechType) {
    this.speechType = speechType;
    return this;
  }

  /**
   * Get speechType
   * @return speechType
   */
  
  @Schema(name = "speechType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speechType")
  public String getSpeechType() {
    return speechType;
  }

  public void setSpeechType(String speechType) {
    this.speechType = speechType;
  }

  public SpeechDto transcription(String transcription) {
    this.transcription = transcription;
    return this;
  }

  /**
   * Get transcription
   * @return transcription
   */
  
  @Schema(name = "transcription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transcription")
  public String getTranscription() {
    return transcription;
  }

  public void setTranscription(String transcription) {
    this.transcription = transcription;
  }

  public SpeechDto eventUri(String eventUri) {
    this.eventUri = eventUri;
    return this;
  }

  /**
   * Get eventUri
   * @return eventUri
   */
  
  @Schema(name = "eventUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventUri")
  public String getEventUri() {
    return eventUri;
  }

  public void setEventUri(String eventUri) {
    this.eventUri = eventUri;
  }

  public SpeechDto audioUrl(String audioUrl) {
    this.audioUrl = audioUrl;
    return this;
  }

  /**
   * Get audioUrl
   * @return audioUrl
   */
  
  @Schema(name = "audioUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audioUrl")
  public String getAudioUrl() {
    return audioUrl;
  }

  public void setAudioUrl(String audioUrl) {
    this.audioUrl = audioUrl;
  }

  public SpeechDto textUrl(String textUrl) {
    this.textUrl = textUrl;
    return this;
  }

  /**
   * Get textUrl
   * @return textUrl
   */
  
  @Schema(name = "textUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("textUrl")
  public String getTextUrl() {
    return textUrl;
  }

  public void setTextUrl(String textUrl) {
    this.textUrl = textUrl;
  }

  public SpeechDto videoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
    return this;
  }

  /**
   * Get videoUrl
   * @return videoUrl
   */
  
  @Schema(name = "videoUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("videoUrl")
  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpeechDto speechDto = (SpeechDto) o;
    return Objects.equals(this.startDateTime, speechDto.startDateTime) &&
        Objects.equals(this.endDateTime, speechDto.endDateTime) &&
        Objects.equals(this.titleEvent, speechDto.titleEvent) &&
        Objects.equals(this.endDateTimeEvent, speechDto.endDateTimeEvent) &&
        Objects.equals(this.startDateTimeEvent, speechDto.startDateTimeEvent) &&
        Objects.equals(this.summary, speechDto.summary) &&
        Objects.equals(this.speechType, speechDto.speechType) &&
        Objects.equals(this.transcription, speechDto.transcription) &&
        Objects.equals(this.eventUri, speechDto.eventUri) &&
        Objects.equals(this.audioUrl, speechDto.audioUrl) &&
        Objects.equals(this.textUrl, speechDto.textUrl) &&
        Objects.equals(this.videoUrl, speechDto.videoUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDateTime, endDateTime, titleEvent, endDateTimeEvent, startDateTimeEvent, summary, speechType, transcription, eventUri, audioUrl, textUrl, videoUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpeechDto {\n");
    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    titleEvent: ").append(toIndentedString(titleEvent)).append("\n");
    sb.append("    endDateTimeEvent: ").append(toIndentedString(endDateTimeEvent)).append("\n");
    sb.append("    startDateTimeEvent: ").append(toIndentedString(startDateTimeEvent)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    speechType: ").append(toIndentedString(speechType)).append("\n");
    sb.append("    transcription: ").append(toIndentedString(transcription)).append("\n");
    sb.append("    eventUri: ").append(toIndentedString(eventUri)).append("\n");
    sb.append("    audioUrl: ").append(toIndentedString(audioUrl)).append("\n");
    sb.append("    textUrl: ").append(toIndentedString(textUrl)).append("\n");
    sb.append("    videoUrl: ").append(toIndentedString(videoUrl)).append("\n");
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

