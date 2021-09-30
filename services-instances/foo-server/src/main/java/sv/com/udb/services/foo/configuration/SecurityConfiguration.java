package sv.com.udb.services.foo.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
   @Override
   protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .authorizeRequests(a -> a
        .antMatchers("/", "/login", "/error", "/webjars/**").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login();
    // @formatter:on
   }
}