package br.com.deolhonacamara.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

@Log4j2
public class HttpBodyCodecBodyHandler implements HttpResponse.BodyHandler<HttpBodyCodec> {

    private final ObjectMapper objectMapper;
    private final Charset charset;

    public HttpBodyCodecBodyHandler(ObjectMapper objectMapper) {
        this(objectMapper, StandardCharsets.UTF_8);
    }

    public HttpBodyCodecBodyHandler(ObjectMapper objectMapper, Charset charset) {
        this.objectMapper = objectMapper;
        this.charset = charset;
    }

    @Override
    public HttpResponse.BodySubscriber<HttpBodyCodec> apply(HttpResponse.ResponseInfo responseInfo) {
        return new HttpBodyCodecSubscriber(objectMapper, charset);
    }

    @Log4j2
    private static class HttpBodyCodecSubscriber implements HttpResponse.BodySubscriber<HttpBodyCodec> {

        private final StringBuilder bodyBuilder = new StringBuilder();
        private final ObjectMapper objectMapper;
        private final Charset charset;
        private final CompletableFuture<HttpBodyCodec> result = new CompletableFuture<>();
        private Flow.Subscription subscription;

        HttpBodyCodecSubscriber(ObjectMapper objectMapper, Charset charset) {
            this.objectMapper = objectMapper;
            this.charset = charset;
        }

        @Override
        public CompletableFuture<HttpBodyCodec> getBody() {
            return result;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(java.util.List<java.nio.ByteBuffer> item) {
            try {
                for (java.nio.ByteBuffer buffer : item) {
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    bodyBuilder.append(new String(bytes, charset));
                }
            } catch (Exception e) {
                log.error("Error processing response body chunk: {}", e.getMessage(), e);
                onError(e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            log.error("Error receiving response body: {}", throwable.getMessage(), throwable);
            result.completeExceptionally(throwable);
        }

        @Override
        public void onComplete() {
            try {
                String body = bodyBuilder.toString();
                result.complete(new JsonBodyCodec(body, objectMapper));
            } catch (Exception e) {
                log.error("Error completing response body: {}", e.getMessage(), e);
                onError(e);
            }
        }
    }
}

