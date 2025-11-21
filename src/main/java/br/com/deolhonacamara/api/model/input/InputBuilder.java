package br.com.deolhonacamara.api.model.input;

import java.lang.reflect.Constructor;
import java.util.UUID;

public class InputBuilder<T extends Input<?>> {
    private UUID userId;
    private Class<T> inputClass;

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

    private InputBuilder<T> inputClass(Class<T> inputClass) {
        this.inputClass = inputClass;
        return this;
    }

    public T build() {
        if (inputClass == null) {
            throw new IllegalStateException("inputClass must be provided before calling build()");
        }
        try {
            Constructor<T> ctor = inputClass.getDeclaredConstructor();
            var instance =  ctor.newInstance();
            instance.setUserId(userId);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create input instance of " + inputClass.getName(), e);
        }
    }
}
