package sv.com.udb.youapp.services.storage.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.components.minio.client.services.IMinioService;
import sv.com.udb.services.commons.repository.IGenreRepository;
import sv.com.udb.services.commons.repository.IMusicRepository;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.services.commons.repository.IStatusRepository;
import sv.com.udb.youapp.services.storage.properties.StorageProperties;
import sv.com.udb.youapp.services.storage.services.ITransferService;
import sv.com.udb.youapp.services.storage.services.impl.DefaultTransferService;

@Configuration
public class StorageConfiguration {
   @Bean
   @ConfigurationProperties(prefix = "app.storage.integration")
   public StorageProperties storageProperties() {
      return new StorageProperties();
   }

   @Bean
   public ObjectMapper objectMapper() {
      return new ObjectMapper().findAndRegisterModules();
   }

   @Bean
   public ITransferService transferService(StorageProperties storageProperties,
         ObjectMapper mapper, IPrincipalRepository repository,
         IMinioService minioService, IMusicRepository musicRepository,
         IGenreRepository genreRepository, IStatusRepository statusRepository) {
      return new DefaultTransferService(storageProperties, mapper, repository,
            minioService, musicRepository, genreRepository, statusRepository);
   }
}
