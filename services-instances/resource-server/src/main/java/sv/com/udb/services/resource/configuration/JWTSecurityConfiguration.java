package sv.com.udb.services.resource.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sv.com.udb.services.resource.converter.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class JWTSecurityConfiguration extends WebSecurityConfigurerAdapter {
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests(
            authz -> authz.antMatchers(HttpMethod.GET, "/foos/**")
                  .hasRole("ADMIN").anyRequest().authenticated())
            .oauth2ResourceServer().jwt()
            .jwtAuthenticationConverter(new JwtAuthenticationConverter() {
            });
   }
}
