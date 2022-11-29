package sv.com.udb.youapp.commons.jpa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.youapp.commons.jpa.repositories.PrincipalRepository;
import sv.com.udb.youapp.commons.jpa.services.PrincipalService;
import sv.com.udb.youapp.commons.jpa.services.impl.DefaultPrincipalService;

@Configuration
public class EntitiesConfiguration {
   @Bean
   public PrincipalService principalService(PrincipalRepository repository) {
      return new DefaultPrincipalService(repository);
   }
}
