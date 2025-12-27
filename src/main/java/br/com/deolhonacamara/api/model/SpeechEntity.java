package br.com.deolhonacamara.api.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeechEntity {
    private Integer politicianId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String titleEvent;
    private LocalDateTime startDateTimeEvent;
    private LocalDateTime endDateTimeEvent;
    private String keywords;
    private String summary;
    private String speechType;
    private String transcription;
    private String eventUri;
    private String audioUrl;
    private String textUrl;
    private String videoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

