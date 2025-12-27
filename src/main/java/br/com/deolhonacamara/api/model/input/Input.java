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
    @Getter
    @Setter
    private Integer propositionId;

    public Input(UUID userId) {
        this.filters = new HashMap<>();
    }

    public <T> void addFilter(String key, T value) {
        this.filters.put(key, value);
    }

    public <T> void removeFilter(String key) {
        this.filters.remove(key);
    }

    public <T> boolean hasFilter(String key) {
        return this.filters.containsKey(key);
    }

    public <T> Map<String, T> getAllFilters() {
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : this.filters.entrySet()) {
            result.put(entry.getKey(), (T) entry.getValue());
        }
        return result;
    }

    public <T> void setFilters(Map<String, T> filters) {
        this.filters.clear();
        for (Map.Entry<String, T> entry : filters.entrySet()) {
            this.filters.put(entry.getKey(), entry.getValue());
        }
    }

    public <T> void addFilters(Map<String, T> filters) {
        for (Map.Entry<String, T> entry : filters.entrySet()) {
            this.filters.put(entry.getKey(), entry.getValue());
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T getFilter(String key) {
        return (T) this.filters.get(key);
    }


}
