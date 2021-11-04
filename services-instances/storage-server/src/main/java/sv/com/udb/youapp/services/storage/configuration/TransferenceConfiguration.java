package sv.com.udb.youapp.services.storage.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import sv.com.udb.youapp.services.storage.properties.StorageProperties;
import sv.com.udb.youapp.services.storage.properties.StorageProperties.FileConfiguration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration
public class TransferenceConfiguration {
   private static final String CLEAN                    = "clean";
   private static final String TASK                     = "task";
   private static final String ZIP                      = "*.zip";
   private static final String TRANSFER_ERROR_CHANNEL   = "transferErrorChannel";
   private static final String TRANSFER_SUCCESS_CHANNEL = "transferSuccessChannel";
   private static final String OUTBOUND_CHANNEL         = "outboundChannel";

   @PostConstruct
   protected void init() {
      LOGGER.info("Se inicializa tareas para transferencia de archivos");
   }

   @Bean
   public MessageChannel transferSuccessChannel() {
      return MessageChannels.queue().get();
   }

   @Bean
   public MessageChannel transferErrorChannel() {
      return MessageChannels.direct().get();
   }

   @Bean
   public IntegrationFlow imagePlusDigitalAccountInboundFlow(
         StorageProperties properties) throws IOException {
      FileConfiguration fileConfiguration = properties.getFileConfiguration();
      return IntegrationFlows
            .from(Files
                  .inboundAdapter(
                        fileConfiguration.getMusicRepository().getFile())
                  .regexFilter(fileConfiguration.getFilterExpresion())
                  .preventDuplicates(false).ignoreHidden(true)
                  .useWatchService(true),
                  e -> e.poller(Pollers
                        .fixedRate(fileConfiguration.getTransportTaskPeriod())))
            .log().channel(OUTBOUND_CHANNEL).get();
   }

   @Bean
   public IntegrationFlow imagePlusDigitalAccountDeleteFlow(
         StorageProperties properties) {
      return IntegrationFlows.from(TRANSFER_SUCCESS_CHANNEL).handle(p -> {
         var f = (File) p.getPayload();
         final Object task = p.getHeaders().getOrDefault(TASK, "transferences");
         try {
            f.delete();
            LOGGER.info("{}: {} Eliminado!", task, f);
         }
         catch (Exception e) {
            LOGGER.error("{}: Ocurrio un error al eliminar el archivo {}", task,
                  f);
         }
      }, e -> e.poller(Pollers.fixedDelay(
            properties.getFileConfiguration().getDeleteTaskPeriod()))).get();
   }

   @Bean
   public IntegrationFlow imagePlusDigitalAccountErrorFlow(
         StorageProperties storageProperties) {
      return IntegrationFlows.from(TRANSFER_ERROR_CHANNEL).handle(p -> {
         if (p.getPayload().getClass().isAssignableFrom(File.class)) {
            LOGGER.info("Update music with error");
         }
         else {
            LOGGER.error("Se reporto un error en la transferencia {}",
                  p.getPayload());
         }
      }).get();
   }

   @Bean
   public IntegrationFlow imagePlusDigitalAccountOutboundFlow(
         Advice transferAdvice, MessageChannel transferErrorChannel,
         StorageProperties properties) {
      return IntegrationFlows.from(OUTBOUND_CHANNEL).handle(message -> {
         LOGGER.info("ASDASDASD");
      }, e -> e.advice(transferRetryAdvice(transferErrorChannel))
            .advice(transferAdvice)).get();
   }

   @Bean
   public Advice transferRetryAdvice(MessageChannel transferErrorChannel) {
      final FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
      backOffPolicy.setBackOffPeriod(Duration.ofSeconds(10).toMillis());
      final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(5);
      RetryTemplate template = new RetryTemplate();
      template.setBackOffPolicy(backOffPolicy);
      template.setRetryPolicy(retryPolicy);
      template.setThrowLastExceptionOnExhausted(true);
      RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
      advice.setRetryTemplate(template);
      advice.setRecoveryCallback(
            new ErrorMessageSendingRecoverer(transferErrorChannel));
      return advice;
   }

   @Bean
   public Advice transferAdvice(MessageChannel transferSuccessChannel,
         MessageChannel transferErrorChannel) {
      ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
      advice.setSuccessChannel(transferSuccessChannel);
      advice.setFailureChannel(transferErrorChannel);
      return advice;
   }
}
