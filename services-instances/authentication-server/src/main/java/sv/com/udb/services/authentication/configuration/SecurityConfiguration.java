package sv.com.udb.services.authentication.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AUTH;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sv.com.udb.services.authentication.services.AuthService;
import sv.com.udb.services.authentication.services.EncryptionPasswordService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @NonNull
  private final AuthService authService;
  @NonNull
  private final EncryptionPasswordService encryptionPasswordService;
  private static final String AUTH_URI = "/v1/auth/*";
  private static final String RESET_PASSWORD_URI = "/v1/auth/reset-password/*";

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.userDetailsService(authService)
      .passwordEncoder(encryptionPasswordService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().disable().authorizeRequests()
      .antMatchers(AUTH_URI, RESET_PASSWORD_URI).permitAll()
      .antMatchers("/**").authenticated()
      .and().oauth2ResourceServer()
      .jwt();

  }


}
