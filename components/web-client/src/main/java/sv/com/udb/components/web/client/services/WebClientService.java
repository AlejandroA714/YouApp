package sv.com.udb.components.web.client.services;

import sv.com.udb.components.web.client.interfaces.DefaultResponseSpec;
import sv.com.udb.components.web.client.interfaces.ResponseDesc;
import sv.com.udb.components.web.client.utils.WebClientHeader;
import sv.com.udb.components.web.client.utils.WebClientParameter;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.function.Consumer;

public interface WebClientService {
   /*** GET-Flux ***/
   <T extends Serializable> Flux<T> getRequest(String baseUrl, String path,
         Class<T> tClass);

   <T extends Serializable> Flux<T> getRequest(String baseUrl, String path,
         Class<T> tClass, WebClientHeader... headers);

   <T extends Serializable> Flux<T> getRequest(String baseUrl, String path,
         Class<T> tClass, WebClientParameter... params);

   <T extends Serializable> Flux<T> getRequest(String baseUrl, String path,
         Class<T> tClass, WebClientHeader header, WebClientParameter params);

   <T extends Serializable> Flux<T> getRequest(String baseUrl, String path,
         Class<T> tClass, WebClientParameter[] params,
         WebClientHeader... headers);

   /*** GET-Mono **/
   <T extends Serializable> Mono<T> getRequestMono(String baseUrl, String path,
         Class<T> tClass);

   <T extends Serializable> Mono<T> getRequestMono(String baseUrl, String path,
         Class<T> tClass, WebClientHeader... headers);

   <T extends Serializable> Mono<T> getRequestMono(String baseUrl, String path,
         Class<T> tClass, WebClientParameter... params);

   <T extends Serializable> Mono<T> getRequestMono(String baseUrl, String path,
         Class<T> tClass, WebClientParameter param, WebClientHeader header);

   <T extends Serializable> Mono<T> getRequestMono(String baseUrl, String path,
         Class<T> tClass, WebClientParameter[] params,
         WebClientHeader... headers);

   /*** POST-Mono **/
   <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass);

   <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass,
         WebClientHeader... headers);

   <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, B body, Class<T> tClass);

   <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, B body, Class<T> tClass,
         WebClientHeader... headers);

   <B extends Serializable> Mono<Void> postRequest(String baseUrl, String path,
         B body, WebClientHeader... headers);

   /*** PUT-Mono **/
   <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass);

   <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass,
         WebClientHeader... headers);

   <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, B body, Class<T> tClass);

   <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, B body, Class<T> tClass,
         WebClientHeader... headers);

   <B extends Serializable> Mono<Void> putRequest(String baseUrl, String path,
         B body, WebClientHeader... headers);

   /*** DELETE-Mono ***/
   <T extends Serializable> Mono<T> deleteRequest(String baseUrl, String path,
         Class<T> tClass);

   <T extends Serializable> Mono<T> deleteRequest(String baseUrl, String path,
         Class<T> tClass, WebClientHeader... headers);

   <T extends Serializable> Mono<T> deleteRequest(String baseUrl, String path,
         Class<T> tClass, WebClientParameter... params);

   <T extends Serializable> Mono<T> deleteRequest(String baseUrl, String path,
         Class<T> tClass, WebClientHeader[] headers,
         WebClientParameter... params);

   <T extends Serializable, B extends Serializable> Mono<T> deleteRequest(
         String baseUrl, String path, Class<T> tClass, WebClientHeader header,
         WebClientParameter param);

   <B extends Serializable> Mono<Void> deleteRequestVoid(String baseUrl,
         String path, WebClientHeader[] headers, WebClientParameter... param);

   WebClient getWebClient();

   Logger getLogger();

   default <T extends Serializable> ResponseDesc doGetRequest(String baseUrl,
         String path, WebClientParameter[] params, WebClientHeader... headers) {
      Mono<ClientResponse> res = getWebClient().get()
            .uri(requestUriSpec(baseUrl, path, params))
            .headers(requestHeadersSpec(headers)).exchange();
      return new DefaultResponseSpec(res, baseUrl, path, params);
   }

   default <T extends Serializable> ResponseDesc doPostRequest(String baseUrl,
         String path, Object body, WebClientHeader... headers) {
      var requestBodySpec = getWebClient().post()
            .uri(requestUriSpec(baseUrl, path, null))
            .headers(requestHeadersSpec(headers));
      if (null != body) requestBodySpec.bodyValue(body);
      Mono<ClientResponse> res = requestBodySpec.exchange();
      return new DefaultResponseSpec(res, baseUrl, path, body);
   }

   default <T extends Serializable> ResponseDesc doPutRequest(String baseUrl,
         String path, Object body, WebClientHeader... headers) {
      var requestBodySpec = getWebClient().put()
            .uri(requestUriSpec(baseUrl, path, null))
            .headers(requestHeadersSpec(headers));
      if (null != body) requestBodySpec.bodyValue(body);
      Mono<ClientResponse> res = requestBodySpec.exchange();
      return new DefaultResponseSpec(res, baseUrl, path, body);
   }

   default <T extends Serializable> ResponseDesc doDeleteRequest(String baseUrl,
         String path, WebClientParameter[] params, WebClientHeader... headers) {
      var requestBodySpec = getWebClient().delete()
            .uri(requestUriSpec(baseUrl, path, params))
            .headers(requestHeadersSpec(headers));
      Mono<ClientResponse> res = requestBodySpec.exchange();
      return new DefaultResponseSpec(res, baseUrl, path, params);
   }

   default Consumer<HttpHeaders> requestHeadersSpec(
         WebClientHeader... headers) {
      return httpHeaders -> {
         if (headers == null) return;
         for (WebClientHeader header : headers) {
            if (header == null || header.getName() == null
                  || header.getValue() == null)
               break;
            httpHeaders.add(header.getName(), header.getValue());
         }
         getLogger().info("Se crearon los siguientes headers: {}", httpHeaders);
      };
   }

   default URI requestUriSpec(String baseUrl, String path,
         WebClientParameter... params) {
      Assert.notNull(baseUrl, "baseUrl cannot be null");
      Assert.notNull(path, "path cannot be null");
      if (params == null) return UriComponentsBuilder.fromUriString(baseUrl)
            .path(path).build().toUri();
      else {
         MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
         for (WebClientParameter param : params) {
            if (param == null || param.getName() == null
                  || param.getValue() == null)
               break;
            if (param.getValue() instanceof Collection) {
               for (Object o : ((Collection) param.getValue())) {
                  queryParams.add(param.getName(), o.toString());
               }
            }
            else {
               queryParams.add(param.getName(), param.getValue().toString());
            }
         }
         return UriComponentsBuilder.fromUriString(baseUrl).path(path)
               .replaceQueryParams(queryParams).build().toUri();
      }
   }
}
