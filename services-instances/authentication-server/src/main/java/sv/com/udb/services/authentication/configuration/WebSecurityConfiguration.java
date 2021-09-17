/*
package sv.com.udb.services.authentication.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import sv.com.udb.services.authentication.services.AuthService;
import sv.com.udb.services.authentication.services.EncryptionPasswordService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(-2)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @NonNull
  private final AuthService authService;
  @NonNull
  private final EncryptionPasswordService encryptionPasswordService;

  private static final String AUTH_URI = "/v1/auth/*";
  private static final String RESET_PASSWORD_URI = "/v1/auth/reset-password/*";

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.userDetailsService(authService)
      .passwordEncoder(encryptionPasswordService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().authorizeRequests()
      .antMatchers(AUTH_URI, RESET_PASSWORD_URI).permitAll()
      .antMatchers("/**").authenticated()
      .and().oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
      .formLogin();
    //http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }


}
*/