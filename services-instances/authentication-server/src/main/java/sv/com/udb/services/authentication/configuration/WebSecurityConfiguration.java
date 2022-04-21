package sv.com.udb.services.authentication.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
   @Bean
   public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
         throws Exception {
      http.authorizeRequests(authorizeRequests -> authorizeRequests
            .antMatchers("/v1/auth/*").permitAll().antMatchers("/favicon.ico")
            .permitAll().antMatchers("/v1/auth/reset-password/*").permitAll())
            .csrf().disable().formLogin(Customizer.withDefaults());
      return http.build();
   }
}
