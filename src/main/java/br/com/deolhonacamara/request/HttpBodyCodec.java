package br.com.deolhonacamara.request;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public interface HttpBodyCodec {

    default String decodeAsString(){
        return this.decodeAsString(StandardCharsets.UTF_8);
    }

    String decodeAsString(Charset set);

    <T> T decodeAs(Class<T> type);
    <T> T decodeAs(TypeRef<T> type);


}
