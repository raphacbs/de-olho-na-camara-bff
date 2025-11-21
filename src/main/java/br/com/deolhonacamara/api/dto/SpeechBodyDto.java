package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeechBodyDto implements Serializable {

    @JsonProperty("dataHoraInicio")
    private LocalDateTime startDateTime;

    @JsonProperty("dataHoraFim")
    private LocalDateTime endDateTime;

    @JsonProperty("faseEvento.titulo")
    private String titleEvent;

    @JsonProperty("faseEvento.dataHoraFim")
    private LocalDateTime endDateTimeEvent;

    @JsonProperty("faseEvento.dataHoraInicio")
    private LocalDateTime startDateTimeEvent;
    
    @JsonProperty("keywords")
    private String keywords;
    
    @JsonProperty("sumario")
    private String summary;
    
    @JsonProperty("tipoDiscurso")
    private String speechType;
    
    @JsonProperty("transcricao")
    private String transcription;

    @JsonProperty("uriEvento")
    private String eventUri;

    @JsonProperty("urlAudio")
    private String audioUrl;

    @JsonProperty("urlTexto")
    private String textUrl;

    @JsonProperty("urlVideo")
    private String videoUrl;

}

