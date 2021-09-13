package sv.com.udb.services.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.services.authentication.repository.UserRepository;
import sv.com.udb.services.authentication.services.AuthService;
import sv.com.udb.services.authentication.services.DefaultAuthService;

@Configuration
public class AuthenticationServerConfiguration {

  @Bean
  public AuthService authService(UserRepository userRepository){
    return new DefaultAuthService(userRepository);
  }

}
