package br.com.deolhonacamara.request.repository;

import br.com.deolhonacamara.request.model.HTTPShepherdModel;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HTTPShepherdRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void logRequest(HTTPShepherdModel HTTPShepherdModel) {
        String INSERT_LOG_SQL = "INSERT INTO tb_tx_http (url, type, request_body, response_body, " +
                "params, status_code, created_at) VALUES (:url, :type, :requestBody, :responseBody, :params, :statusCode, :createdAt)";

        namedParameterJdbcTemplate.update(INSERT_LOG_SQL,
                buildParameters(HTTPShepherdModel)
        );
    }

    private MapSqlParameterSource buildParameters(HTTPShepherdModel HTTPShepherdModel) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("url", HTTPShepherdModel.getUrl());
        parameters.addValue("type", HTTPShepherdModel.getType());
        parameters.addValue("requestBody", HTTPShepherdModel.getRequestBody());
        parameters.addValue("responseBody", HTTPShepherdModel.getResponseBody());
        parameters.addValue("params", HTTPShepherdModel.getParams());
        parameters.addValue("statusCode", HTTPShepherdModel.getStatusCode());
        parameters.addValue("createdAt", HTTPShepherdModel.getCreatedAt());
        return parameters;
    }
}
