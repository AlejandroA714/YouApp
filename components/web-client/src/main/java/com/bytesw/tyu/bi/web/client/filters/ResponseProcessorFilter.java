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
public class ResponseProcessorFilter implements ExchangeFilterFunction {
   @Override
   public Mono<ClientResponse> filter(ClientRequest clientRequest,
         ExchangeFunction exchangeFunction) {
      return exchangeFunction.exchange(clientRequest)
            .flatMap(this::responseProccesor);
   }

   private Mono<ClientResponse> responseProccesor(
         ClientResponse clientResponse) {
      LOGGER.info("[WebClientService] Incoming Response: {}",
            clientResponse.statusCode());
      if (LOGGER.isTraceEnabled()) {
         clientResponse.headers().asHttpHeaders()
               .forEach((name,
                     values) -> values.forEach(value -> LOGGER.trace(
                           "[WebClientService] Response Header: \"{}={}\"",
                           name, value)));
      }
      if (clientResponse.statusCode().isError()) {
         return clientResponse.createException().flatMap(Mono::error);
      }
      else
         return Mono.just(clientResponse);
   }
}
