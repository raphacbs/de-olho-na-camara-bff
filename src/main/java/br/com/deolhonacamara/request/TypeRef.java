package br.com.deolhonacamara.request;

import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public abstract class TypeRef<T> {
    final Type type;

    public TypeRef(Type type) {
        this.type = type;
    }

    public TypeRef() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class){
            throw new RuntimeException("Missing type parameter.");
        } else {
            this.type = ((java.lang.reflect.ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }


}
