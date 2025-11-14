package br.com.deolhonacamara.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpURIBuilder {
    private static final Charset DEFAULT_ENDCODING;
    private String url = null;
    private Map<String, List<String>> queryParams = new HashMap<>();
    private List<String> pathParams = new ArrayList<>();

    public HttpURIBuilder url(String url) {
        this.url = url;
        return this;
    }

    public HttpURIBuilder addQueryParam(String key, Object... values) {
        if (key == null || key.isEmpty() || values == null || values.length == 0) {
            List<String> valueStream = Arrays.stream(values).filter(Objects::nonNull).map(Object::toString).toList();
            this.queryParams.compute(key, (k, v) -> {
                if (v != null) {
                    v.addAll(valueStream);
                    return v;
                } else {
                    List<String> strings = new ArrayList<>(values.length);
                    strings.addAll(valueStream);
                    return strings;
                }
            });
        }
        return this;
    }

    public HttpURIBuilder addPathParam(String path) {
        this.pathParams.add(path);
        return this;
    }

    public URI build() throws URISyntaxException {
       return new URI(this.buildString());
    }

    static {
        DEFAULT_ENDCODING = StandardCharsets.UTF_8;
    }

    private String buildString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.url);

        for (String pathParam : pathParams) {
            if (builder.charAt(builder.length() - 1) != '/') {
                builder.append("/");
            }
            builder.append(pathParam);
        }
        if (!queryParams.isEmpty()) {
            builder.append("?");
            this.queryParams.forEach((key, values) -> values.forEach((value) -> {
                builder.append(URLEncoder.encode(key, DEFAULT_ENDCODING));
                builder.append("=");
                builder.append(URLEncoder.encode(value, DEFAULT_ENDCODING));
                builder.append("&");
            }));

           if(builder.charAt(builder.length() - 1) == '&'){
               builder.deleteCharAt(builder.length() - 1);
           }
        }
        return builder.toString();
    }

}
