package br.com.deolhonacamara.request;

import br.com.deolhonacamara.request.repository.HTTPShepherdRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.env.Environment;

import java.net.http.HttpClient;

@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RequesterBuilder<T> {
    private final HttpClient httpClient;
    private final Environment environment;
    private final Class<T> type;

    @Setter
    private String url;
    @Setter
    private String endpoint = "";
    @Setter
    private String timeout = "";
    @Setter
    private String contentType = "application/json";
    @Setter
    private HTTPShepherdRepository repository;

    public Requester<T> build() {
        validateRequiredFields();
        return new Requester<>(httpClient, environment, url, endpoint, timeout, type, contentType);
    }

    private void validateRequiredFields() {
        if (this.url == null) {
            throw new IllegalArgumentException("URL is required to build HTTPShepherd");
        }
        if (this.timeout == null) {
            throw new IllegalArgumentException("Timeout is required to build HTTPShepherd");
        }
        if (this.type == null) {
            throw new IllegalArgumentException("Type is required to build HTTPShepherd");
        }
        if (this.httpClient == null) {
            throw new IllegalArgumentException("httpClient is required to build HTTPShepherd");
        }
        if (this.environment == null) {
            throw new IllegalArgumentException("environment is required to build HTTPShepherd");
        }
        if (this.endpoint == null) {
            throw new IllegalArgumentException("endpoint is required to build HTTPShepherd");
        }
    }


}
