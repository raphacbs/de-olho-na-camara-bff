package br.com.deolhonacamara.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;

@Log4j2
public class JsonBodyCodec implements HttpBodyCodec {

    private final String bodyContent;
    private final ObjectMapper objectMapper;

    public JsonBodyCodec(String bodyContent, ObjectMapper objectMapper) {
        this.bodyContent = bodyContent;
        this.objectMapper = objectMapper;
    }

    @Override
    public String decodeAsString(Charset charset) {
        return bodyContent;
    }

    @Override
    public <T> T decodeAs(Class<T> type) {
        try {
            if (bodyContent == null || bodyContent.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(bodyContent, type);
        } catch (Exception e) {
            log.error("Failed to decode response as {}: {}", type.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("Failed to decode response body", e);
        }
    }

    @Override
    public <T> T decodeAs(TypeRef<T> typeRef) {
        try {
            if (bodyContent == null || bodyContent.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(bodyContent,
                    objectMapper.getTypeFactory().constructType(typeRef.getType()));
        } catch (Exception e) {
            log.error("Failed to decode response as TypeRef: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to decode response body", e);
        }
    }
}

