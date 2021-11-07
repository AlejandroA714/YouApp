package sv.com.udb.components.minio.client.configuration;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.components.minio.client.properties.MinioClientProperties;
import sv.com.udb.components.minio.client.services.IMinioService;
import sv.com.udb.components.minio.client.services.impl.DefaultMinioService;

@Slf4j
@Configuration
public class MinioClientConfiguration {
   @Bean
   @ConfigurationProperties("app.minio.client")
   public MinioClientProperties minioClientProperties() {
      return new MinioClientProperties();
   }

   @Bean
   public MinioClient minioClient(MinioClientProperties properties) {
      return MinioClient.builder().endpoint(properties.getUrl())
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .build();
   }

   @Bean
   public IMinioService minioService(MinioClient minioClient,
         MinioClientProperties properties) {
      return new DefaultMinioService(minioClient, properties);
   }
}
