package sv.com.udb.youapp.services.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.youapp.services.authentication.properties.AuthenticationProperties;

@Configuration
public class AuthenticationConfiguration {

  @Bean
  @ConfigurationProperties("app.auth")
  public AuthenticationProperties authenticationProperties(){
    return new AuthenticationProperties();
  }

}
