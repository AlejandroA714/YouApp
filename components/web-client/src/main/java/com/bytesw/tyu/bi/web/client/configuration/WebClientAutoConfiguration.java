package com.bytesw.tyu.bi.web.client.configuration;

import com.bytesw.tyu.bi.web.client.filters.RequestProccesorFilter;
import com.bytesw.tyu.bi.web.client.filters.ResponseProcessorFilter;
import com.bytesw.tyu.bi.web.client.services.WebClientService;
import com.bytesw.tyu.bi.web.client.services.impl.DefaultWebClientService;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.inject.Named;
import javax.net.ssl.SSLException;

@Slf4j
@Configuration
public class WebClientAutoConfiguration {
   private final String REQUEST_PROCCESOR_FILTER  = "requestProccesorFilter";
   private final String RESPONSE_PROCESSOR_FILTER = "responseProcessorFilter";
   private final String HTTP_CLIENT               = "httpClient";
   private final String SSL_CONTEXT               = "SslContext";
   private final String WEB_CLIENT_BEAN           = "web-client";

   @Profile({ "!test", "!test2" })
   @Bean(name = SSL_CONTEXT)
   public SslContext sslContextNoTest() throws SSLException {
      return SslContextBuilder.forClient().build();
   }

   @Profile({ "test", "test2" })
   @Bean(name = SSL_CONTEXT)
   public SslContext sslContextTest() throws SSLException {
      return SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
   }

   @Bean(name = HTTP_CLIENT)
   public HttpClient httpClient(@Named(SSL_CONTEXT) SslContext sslContext)
         throws SSLException {
      return HttpClient.create().wiretap(true)
            .secure(ssl -> ssl.sslContext(sslContext));
   }

   @Order(100)
   @Bean(name = REQUEST_PROCCESOR_FILTER)
   @ConditionalOnMissingBean(name = REQUEST_PROCCESOR_FILTER)
   public RequestProccesorFilter requestProccesorFilter() {
      return new RequestProccesorFilter();
   }

   @Order(100)
   @Bean(name = RESPONSE_PROCESSOR_FILTER)
   @ConditionalOnMissingBean(name = RESPONSE_PROCESSOR_FILTER)
   public ResponseProcessorFilter responseProcessorFilter() {
      return new ResponseProcessorFilter();
   }

   @Primary
   @Bean(name = WEB_CLIENT_BEAN)
   public WebClient webClient(
         @Named(REQUEST_PROCCESOR_FILTER) ExchangeFilterFunction requestProccesorFilter,
         @Named(RESPONSE_PROCESSOR_FILTER) ExchangeFilterFunction responseProcessorFilter,
         @Named(HTTP_CLIENT) HttpClient httpClient) {
      return WebClient.builder()
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .filter(requestProccesorFilter).filter(responseProcessorFilter)
            .build();
   }

   @Bean
   @ConditionalOnMissingBean
   public WebClientService webClientService(
         @Named(WEB_CLIENT_BEAN) WebClient webClient) {
      return new DefaultWebClientService(webClient);
   }
}
