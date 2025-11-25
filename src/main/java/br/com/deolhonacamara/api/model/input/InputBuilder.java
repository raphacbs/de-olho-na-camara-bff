package br.com.deolhonacamara.api.model.input;

import java.lang.reflect.Constructor;
import java.util.UUID;

public class InputBuilder<T extends Input<?>> {
    private UUID userId;
    private Class<T> inputClass;
    private Integer page;
    private Integer sizePage;
    private Integer politicianId;

    private InputBuilder() {}

    public static <T extends Input<?>> InputBuilder<T> builder(Class<T> clazz) {
        InputBuilder<T> b = new InputBuilder<>();
        b.inputClass(clazz);
        return b;
    }

    public InputBuilder<T> userId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public InputBuilder<T> page(Integer page) {
        this.page = page;
        return this;
    }
    public InputBuilder<T> sizePage(Integer sizePage) {
        this.sizePage = sizePage;
        return this;
    }

    public InputBuilder<T> politicianId(Integer politicianId) {
        this.politicianId = politicianId;
        return this;
    }

    private InputBuilder<T> inputClass(Class<T> inputClass) {
        this.inputClass = inputClass;
        return this;
    }

    public T build() {
        if (inputClass == null) {
            throw new IllegalStateException("inputClass must be provided before calling build()");
        }
        if(page == null) {
            throw new IllegalStateException("page must be provided before calling build()");
        }

        if(sizePage == null) {
            throw new IllegalStateException("sizePage must be provided before calling build()");
        }

        try {
            Constructor<T> ctor = inputClass.getDeclaredConstructor();
            var instance =  ctor.newInstance();
            instance.setUserId(userId);
            instance.setPage(page);
            instance.setSizePage(sizePage);
            instance.setPropositionId(politicianId);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create input instance of " + inputClass.getName(), e);
        }
    }
}
