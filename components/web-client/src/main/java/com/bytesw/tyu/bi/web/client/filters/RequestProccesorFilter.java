package com.bytesw.tyu.bi.web.client.filters;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@NoArgsConstructor
public class RequestProccesorFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest,
            ExchangeFunction exchangeFunction) {
        LOGGER.info("[WebClientService] {}:[{}]", clientRequest.method(),
                clientRequest.url());
        if (LOGGER.isTraceEnabled()) {
            clientRequest.headers()
                    .forEach((name,
                            values) -> values.forEach(value -> LOGGER.trace(
                                    "[WebClientService] Header: \"{}={}\"",
                                    name, value)));
        }
        return exchangeFunction.exchange(clientRequest);
    }
}
