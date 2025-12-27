package br.com.deolhonacamara.request;

import br.com.deolhonacamara.exception.RequesterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.util.PropertyPlaceholderHelper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static br.com.deolhonacamara.request.RequestType.PATCH;

@Log4j2
public class Requester<T> {

    public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Set<String> headerParams;
    private final Set<String> queryParams;
    private final Set<String> pathParams;
    @Getter
    private final String fullEndpoint;
    private final Class<T> type;
    private final Duration timeout;
    private final String contentType;

    public Requester(HttpClient httpClient,
                     Environment environment,
                     String url,
                      String endpoint,
                     String timeout,
                     Class<T> type,
                     String contentType,
                     ObjectMapper objectMapper) {

        this.httpClient = httpClient;
        this.objectMapper = objectMapper;//new ObjectMapper();

        PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");

        String resolveUrl = resolvePlaceholder(environment, placeholderHelper, url);
        String resolveEndpoint = resolvePlaceholder(environment, placeholderHelper, endpoint);
        this.fullEndpoint = resolveUrl + resolveEndpoint;

        int i = fullEndpoint.indexOf("!");
        if (i != -1) {
            this.headerParams = processOtherParams(fullEndpoint.substring(i + 1));
        } else {
            this.headerParams = Collections.emptySet();
        }

        int j = fullEndpoint.indexOf("?");
        if (j != -1) {
            this.queryParams = processOtherParams(fullEndpoint.substring(j + 1));
        } else {
            this.queryParams = Collections.emptySet();
        }

        String resolvedTimeout = resolvePlaceholder(environment, placeholderHelper, timeout);
        this.pathParams = processPathParams(fullEndpoint);
        this.timeout = parseDuration(resolvedTimeout);
        this.contentType = contentType;
        this.type = type;
    }

    private static void methodBuilder(RequestType reqType, HttpRequest.Builder builder, HttpRequest.BodyPublisher bodyPublisher) {
        switch (reqType) {
            case GET -> builder.GET();
            case POST -> builder.POST(bodyPublisher);
            case PUT -> builder.PUT(bodyPublisher);
            case DELETE -> builder.DELETE();
            case PATCH -> builder.method(PATCH.name(), bodyPublisher);
        }
    }

    public Response<T> process(RequestType reqType, Map<String, Object> params, HttpRequest.BodyPublisher bodyPublisher) {
        try {
            HttpURIBuilder uriBuilder = new HttpURIBuilder();
            Map<String, Object> headerValues = new HashMap<>();
            String[] urls = getUrl();
            if (params != null) {
                params.forEach((k, v) -> processParams(k, v, headerValues, uriBuilder, urls));
            }
            URI uri = uriBuilder.url(urls[0]).build();
            HttpRequest.Builder builder = HttpRequest.newBuilder(uri);
            methodBuilder(reqType, builder, bodyPublisher);
            builder.header("content-type", contentType);
            headerValues.forEach((k, v) -> builder.header(k, v.toString()));
            if (timeout != null) {
                builder.timeout(timeout);
            }
            // TODO configurar o authorization

            HttpRequest req = builder.build();
            HttpBodyCodecBodyHandler bodyHandler = new HttpBodyCodecBodyHandler(objectMapper, DEFAULT_ENCODING);
            HttpResponse<HttpBodyCodec> response = httpClient.send(req, bodyHandler);

            return new Response<>(type, response);

        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public Response<T> request() {
        return process(RequestType.GET, Collections.emptyMap(), null);
    }

    public Response<T> request(Map<String, Object> params) {
        return process(RequestType.GET, params, null);
    }

    public Response<T> request(Map<String, Object> params, HttpRequest.BodyPublisher bodyPublisher) {
        return process(RequestType.GET, params, bodyPublisher);
    }

    public Response<T> post(Map<String, Object> params, HttpRequest.BodyPublisher bodyPublisher) {
        return process(RequestType.POST, params, bodyPublisher);
    }

    public Response<T> put(Map<String, Object> params, HttpRequest.BodyPublisher bodyPublisher) {
        return process(RequestType.PUT, params, bodyPublisher);
    }

    public Response<T> delete(Map<String, Object> params) {
        return process(RequestType.DELETE, params, null);
    }

    public Response<T> patch(Map<String, Object> params, HttpRequest.BodyPublisher bodyPublisher) {
        return process(PATCH, params, bodyPublisher);
    }

    public static <T> RequesterBuilder<T> builder(HttpClient httpClient, Environment environment, Class<T> type, ObjectMapper objectMapper) {
        return new RequesterBuilder<>(httpClient, environment, type, objectMapper);
    }

    private String resolvePlaceholder(Environment environment, PropertyPlaceholderHelper helper, String value) {
        if (value == null) {
            return "";
        }
        return helper.replacePlaceholders(value, environment::getProperty);
    }

    private Set<String> processPathParams(String url) {
        Set<String> result = new HashSet<>();
        int start = 0;
        while ((start = url.indexOf("{", start)) != -1) {
            int end = url.indexOf("}", start);
            if (end != -1) {
                String param = url.substring(start + 1, end);
                result.add(param);
                start = end + 1;
            } else {
                break;
            }
        }
        return result;
    }

    private Set<String> processOtherParams(String params) {
        Set<String> result = new HashSet<>();
        if (params != null && !params.isEmpty()) {
            String[] splitParams = params.split("&");
            for (String param : splitParams) {
                if (!param.isEmpty()) {
                    result.add(param);
                }
            }
        }
        return result;
    }

    private Duration parseDuration(String durationStr) {
        final Pattern DURATION_PATTERN = Pattern.compile("^(-?[0-9]+)(d|h|m|s|ms)$");
        if (durationStr == null || durationStr.isEmpty() || "0".equals(durationStr)) {
            return Duration.ZERO;
        } else {
            Matcher matcher = DURATION_PATTERN.matcher(durationStr);
            if (matcher.matches()) {
                long ttl = Long.parseLong(matcher.group(1));
                return switch (matcher.group(2)) {
                    case "d" -> Duration.ofDays(ttl);
                    case "h" -> Duration.ofHours(ttl);
                    case "m" -> Duration.ofMinutes(ttl);
                    case "s" -> Duration.ofSeconds(ttl);
                    case "ms" -> Duration.ofMillis(ttl);
                    default -> throw new IllegalArgumentException("Unrecognized duration unit: " + ttl);
                };
            } else {
                throw new IllegalArgumentException("Invalid duration format: " + durationStr);
            }
        }
    }

    private String[] getUrl() {
        return new String[]{fullEndpoint};
    }

    private void processParams(String k, Object v, Map<String, Object> headerValues, HttpURIBuilder uriBuilder,
                               String[] urls) {
        if (pathParams.contains(k)) {
            urls[0] = urls[0].replace("{" + k + "}",
                    URLEncoder.encode(v == null ? "" : v.toString(), DEFAULT_ENCODING).replace("+", "%20"));
        } else if (queryParams.contains(k)) {
            if (v != null) {
                uriBuilder.addQueryParam(k, v.toString());
            }
        } else if (headerParams.contains(k)) {
            if (v != null) {
                headerValues.put(k, v.toString());
            }
        } else {
            throw new RequesterException("Unrecognized parameter " + k);
        }
    }

}
