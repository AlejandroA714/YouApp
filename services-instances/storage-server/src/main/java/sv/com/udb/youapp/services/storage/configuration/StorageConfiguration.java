package sv.com.udb.youapp.services.storage.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.youapp.services.storage.properties.StorageProperties;

@Configuration
public class StorageConfiguration {
   @Bean
   @ConfigurationProperties(prefix = "app.storage.integration")
   public StorageProperties storageProperties() {
      return new StorageProperties();
   }
}
