package sv.com.udb.components.web.client.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface ResponseDesc {
   <T extends Serializable> Flux<T> bodyToFlux(Class<T> tClass);

   <T extends Serializable> Mono<T> bodyToMono(Class<T> tClass);

   Mono<Void> bodyToVoid();
}
