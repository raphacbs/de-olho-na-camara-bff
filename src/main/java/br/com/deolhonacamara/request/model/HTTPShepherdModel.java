package br.com.deolhonacamara.request.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class HTTPShepherdModel {
    private UUID id;
    private String url;
    private String type;
    private String requestBody;
    private String responseBody;
    private String params;
    private Integer statusCode;
    private LocalDateTime createdAt;
}
