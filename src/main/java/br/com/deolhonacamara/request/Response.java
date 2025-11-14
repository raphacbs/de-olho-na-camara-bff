package br.com.deolhonacamara.request;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.net.http.HttpResponse;

@Data
@Log4j2
public class Response<T> {
    private final Class<T> type;
    private final HttpResponse<HttpBodyCodec> httpResponse;

    public int statusCode() {
        return httpResponse.statusCode();
    }

    public T body() {
        return httpResponse.body() != null ? httpResponse.body().decodeAs(type) : null;
    }

    public <E> E error(Class<E> errorType) {
       try{
           return httpResponse.body().decodeAs(errorType);
       } catch (Exception e) {
           log.warn("Failed to decode error body: {}", e.getMessage());
           log.info("Returning null.");
           return  null;
       }
    }
}
