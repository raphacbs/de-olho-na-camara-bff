package br.com.deolhonacamara.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public final class QueryStringBuilder {

    private QueryStringBuilder() {}

    public static String build(Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String encodedName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String encodedValue = entry.getValue() == null ? "" : URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            joiner.add(encodedName + "=" + encodedValue);
        }
        return joiner.toString();
    }
}

