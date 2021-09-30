package com.bytesw.tyu.bi.web.client.services.impl;

import com.bytesw.tyu.bi.web.client.services.WebClientService;
import com.bytesw.tyu.bi.web.client.utils.WebClientHeader;
import com.bytesw.tyu.bi.web.client.utils.WebClientParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
@AllArgsConstructor
public class DefaultWebClientService implements WebClientService {
   @Getter
   @NonNull
   private final WebClient webClient;

   @Override
   public <T extends Serializable> Flux<T> getRequest(String baseUrl,
         String path, Class<T> tClass) {
      return this.doGetRequest(baseUrl, path, null, null).bodyToFlux(tClass);
   }

   @Override
   public <T extends Serializable> Flux<T> getRequest(String baseUrl,
         String path, Class<T> tClass, WebClientHeader... headers) {
      return this.doGetRequest(baseUrl, path, null, headers).bodyToFlux(tClass);
   }

   @Override
   public <T extends Serializable> Flux<T> getRequest(String baseUrl,
         String path, Class<T> tClass, WebClientParameter... params) {
      return this.doGetRequest(baseUrl, path, params, null).bodyToFlux(tClass);
   }

   @Override
   public <T extends Serializable> Flux<T> getRequest(String baseUrl,
         String path, Class<T> tClass, WebClientHeader header,
         WebClientParameter params) {
      return this.doGetRequest(baseUrl, path,
            new WebClientParameter[] { params }, header).bodyToFlux(tClass);
   }

   @Override
   public <T extends Serializable> Flux<T> getRequest(String baseUrl,
         String path, Class<T> tClass, WebClientParameter[] params,
         WebClientHeader... headers) {
      return this.doGetRequest(baseUrl, path, params, headers)
            .bodyToFlux(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> getRequestMono(String baseUrl,
         String path, Class<T> tClass) {
      return this.doGetRequest(baseUrl, path, null, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> getRequestMono(String baseUrl,
         String path, Class<T> tClass, WebClientHeader... headers) {
      return this.doGetRequest(baseUrl, path, null, headers).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> getRequestMono(String baseUrl,
         String path, Class<T> tClass, WebClientParameter... params) {
      return this.doGetRequest(baseUrl, path, params, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> getRequestMono(String baseUrl,
         String path, Class<T> tClass, WebClientParameter param,
         WebClientHeader header) {
      return this.doGetRequest(baseUrl, path,
            new WebClientParameter[] { param }, header).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> getRequestMono(String baseUrl,
         String path, Class<T> tClass, WebClientParameter[] params,
         WebClientHeader... headers) {
      return this.doGetRequest(baseUrl, path, params, headers)
            .bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass) {
      return this.doPostRequest(baseUrl, path, body, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass,
         WebClientHeader... headers) {
      return this.doPostRequest(baseUrl, path, body, headers)
            .bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, B body, Class<T> tClass) {
      return this.doPostRequest(baseUrl, path, body, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> postRequest(
         String baseUrl, String path, B body, Class<T> tClass,
         WebClientHeader... headers) {
      return this.doPostRequest(baseUrl, path, body, headers)
            .bodyToMono(tClass);
   }

   @Override
   public <B extends Serializable> Mono<Void> postRequest(String baseUrl,
         String path, B body, WebClientHeader... headers) {
      return this.doPostRequest(baseUrl, path, body, headers).bodyToVoid();
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass) {
      return this.doPutRequest(baseUrl, path, body, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, Collection<B> body, Class<T> tClass,
         WebClientHeader... headers) {
      return this.doPutRequest(baseUrl, path, body, headers).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, B body, Class<T> tClass) {
      return this.doPutRequest(baseUrl, path, body, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> putRequest(
         String baseUrl, String path, B body, Class<T> tClass,
         WebClientHeader... headers) {
      return this.doPutRequest(baseUrl, path, body, headers).bodyToMono(tClass);
   }

   @Override
   public <B extends Serializable> Mono<Void> putRequest(String baseUrl,
         String path, B body, WebClientHeader... headers) {
      return this.doPutRequest(baseUrl, path, body, headers).bodyToVoid();
   }

   @Override
   public <T extends Serializable> Mono<T> deleteRequest(String baseUrl,
         String path, Class<T> tClass) {
      return this.doDeleteRequest(baseUrl, path, null, null).bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> deleteRequest(String baseUrl,
         String path, Class<T> tClass, WebClientHeader... headers) {
      return this.doDeleteRequest(baseUrl, path, null, headers)
            .bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> deleteRequest(String baseUrl,
         String path, Class<T> tClass, WebClientParameter... params) {
      return this.doDeleteRequest(baseUrl, path, params, null)
            .bodyToMono(tClass);
   }

   @Override
   public <T extends Serializable> Mono<T> deleteRequest(String baseUrl,
         String path, Class<T> tClass, WebClientHeader[] headers,
         WebClientParameter... params) {
      return this.doDeleteRequest(baseUrl, path, params, headers)
            .bodyToMono(tClass);
   }

   @Override
   public <B extends Serializable> Mono<Void> deleteRequestVoid(String baseUrl,
         String path, WebClientHeader[] headers, WebClientParameter... params) {
      return this.doDeleteRequest(baseUrl, path, params, headers).bodyToVoid();
   }

   @Override
   public <T extends Serializable, B extends Serializable> Mono<T> deleteRequest(
         String baseUrl, String path, Class<T> tClass, WebClientHeader header,
         WebClientParameter param) {
      return this.doDeleteRequest(baseUrl, path,
            new WebClientParameter[] { param }, header).bodyToMono(tClass);
   }

   public Logger getLogger() {
      return LOGGER;
   }

   public void trace(String payload, Object... var) {
      if (LOGGER.isTraceEnabled()) {
         LOGGER.trace(payload, var);
      }
   }
} // Class