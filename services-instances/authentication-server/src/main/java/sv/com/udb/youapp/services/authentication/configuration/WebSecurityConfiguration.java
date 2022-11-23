package sv.com.udb.youapp.services.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfiguration {
   @Bean
   public SecurityWebFilterChain springSecurityFilterChain(
         ServerHttpSecurity http) {
      http.authorizeExchange().anyExchange().permitAll().and().csrf().disable().formLogin();
      return http.build();
   }
}
