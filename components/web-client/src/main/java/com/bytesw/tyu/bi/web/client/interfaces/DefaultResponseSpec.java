package com.bytesw.tyu.bi.web.client.interfaces;

import com.bytesw.tyu.bi.web.client.exceptions.WebClientException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class DefaultResponseSpec implements ResponseDesc {
    @NonNull
    private final Mono<ClientResponse> clientResponse;
    @NonNull
    private final String               baseUrl;
    @NonNull
    private final String               path;
    private Object                     body;

    @Override
    public <T extends Serializable> Flux<T> bodyToFlux(Class<T> tClass) {
        Assert.notNull(tClass, "Class type cannot be null");
        return clientResponse.flatMapMany(r -> r.bodyToFlux(tClass))
                .onErrorResume(this::doOnError);
    }

    @Override
    public <T extends Serializable> Mono<T> bodyToMono(Class<T> tClass) {
        Assert.notNull(tClass, "Class type cannot be null");
        return clientResponse.flatMap(r -> r.bodyToMono(tClass))
                .onErrorResume(this::doOnError);
    }

    @Override
    public Mono<Void> bodyToVoid() {
        return clientResponse.doOnError(this::doOnVoidError).then();
    }

    private <T extends Serializable> Mono<Void> doOnVoidError(Throwable ex) {
        return processError(ex);
    }

    private <T extends Serializable> Mono<? extends T> doOnError(Throwable ex) {
        return processError(ex);
    }

    private Mono processError(Throwable ex) {
        LOGGER.error("\"{}{}\", fallo debido a: {}", baseUrl, path,
                ex.getMessage());
        if (ex instanceof WebClientResponseException)
            return Mono.error(new WebClientException(
                    (WebClientResponseException) ex, ex.getMessage(), body));
        else {
            return clientResponse
                    .flatMap(cr -> cr.createException().flatMap(e -> Mono.just(
                            new WebClientException(e, ex.getMessage(), body))));
        }
    }
}