package br.com.deolhonacamara.request;

import br.com.deolhonacamara.request.model.HTTPShepherdModel;
import br.com.deolhonacamara.request.repository.HTTPShepherdRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Log4j2
public class HTTPShepherd<B, O> {
    private final Requester<O> requester;
    private final HTTPShepherdRepository httpShepherdRepository;
    private final Class<O> type;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String url;

    HTTPShepherd(Requester<O> requester,
                  HTTPShepherdRepository httpShepherdRepository,
                  Class<O> type,
                  String url) {
        this.requester = requester;
        this.httpShepherdRepository = httpShepherdRepository;
        this.type = type;
        this.url = url;
    }

    public static  <I, O> HTTPShepherdBuilder<I, O> builder(HttpClient httpClient, Environment environment, Class<O> type) {
        return new HTTPShepherdBuilder<I, O>(httpClient, environment, type);
    }

    public O request(Map<String, Object> params) {
        return execute(RequestType.GET, params, null);
    }

    public O request() {
        return execute(RequestType.GET, null, null);
    }

    private O execute(RequestType requestType, Map<String, Object> params, B body){
        Response<O> response = requester.process(requestType, params, ofJson(body));

        var httpShepherdModel = HTTPShepherdModel.builder()
                .url(this.url)
                .type(requestType.name())
                .params(params != null ? params.toString() : null)
                .requestBody(body != null ? body.toString() : null)
                .responseBody(response != null && response.body() != null ? response.body().toString() : null)
                .createdAt(LocalDateTime.now())
                .build();

        httpShepherdRepository.logRequest(httpShepherdModel);
        return response.body();
    }

    private HttpRequest.BodyPublisher ofJson(B body){
        String jsonBody = null;
        if (body != null) {
            try {
                jsonBody = objectMapper.writeValueAsString(body);
            } catch (Exception e) {
                log.error("Failed to convert body to JSON: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to convert body to JSON", e);
            }
        }

        return jsonBody != null
                ? HttpRequest.BodyPublishers.ofString(jsonBody)
                : null;
    }

}
