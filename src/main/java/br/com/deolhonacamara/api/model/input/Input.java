package br.com.deolhonacamara.api.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public abstract class Input<B> {
    private final Map<String, Object> filters;
    @Getter
    @Setter
    private UUID userId;
    @Getter
    @Setter
    private Integer page;
    @Getter
    @Setter
    private Integer sizePage;

    public Input(UUID userId) {
        this.filters = new HashMap<>();
    }

    public <T> void addFilter(String key, T value) {
        this.filters.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getFilter(String key) {
        return (T) this.filters.get(key);
    }


}
