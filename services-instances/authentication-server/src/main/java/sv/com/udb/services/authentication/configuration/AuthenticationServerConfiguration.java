package sv.com.udb.services.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.services.authentication.filter.JWTFilter;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.UserRepository;
import sv.com.udb.services.authentication.services.*;

@Configuration
public class AuthenticationServerConfiguration {

  @Bean
  @ConfigurationProperties("auth")
  public AuthenticationProperties authProperties(){
    return new AuthenticationProperties();
  }

  @Bean
  public AuthService authService(UserRepository userRepository){
    return new DefaultAuthService(userRepository);
  }

  @Bean
  public EncryptionPasswordService encryptionPasswordService(
                      AuthenticationProperties authProperties){
    return new DefaultEncryptionPasswordService(authProperties);
  }

  @Bean
  public JWTTokenProvider jwtTokenProvider(AuthenticationProperties authProperties){
    return new JWTTokenProvider(authProperties);
  }

  @Bean
  public JWTFilter jwtFilter(UserRepository userRepository,
                             JWTTokenProvider jwtTokenProvider){
    return new JWTFilter(userRepository,jwtTokenProvider);
  }


}
