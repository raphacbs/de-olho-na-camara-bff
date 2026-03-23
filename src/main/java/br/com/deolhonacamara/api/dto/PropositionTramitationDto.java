package br.com.deolhonacamara.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropositionTramitationDto {
    private LocalDateTime dateTime;
    private Integer sequence;
    private String orgAcronym;
    private String orgUri;
    private String lastReporterUri;
    private String regime;
    private String tramitationDescription;
    private String tramitationTypeCode;
    private String situationDescription;
    private String situationCode;
    private String dispatch;
    private String url;
    private String scope;
    private String appreciation;
}
