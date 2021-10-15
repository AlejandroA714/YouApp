package sv.com.udb.services.authentication.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import sv.com.udb.services.authentication.oauth.OAuth2ProviderConfigurer;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IEmailTokenRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.services.impl.DefaultAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultEncryptionPasswordService;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration(proxyBeanMethods = false)
public class AuthenticationServerConfiguration {
   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   public SecurityFilterChain authorizationServerSecurityFilterChain(
         HttpSecurity http) throws Exception {
      applyCustomSecurity(http);
      return http.formLogin(Customizer.withDefaults()).csrf().disable().build();
   }

   @Bean
   public ObjectMapper objectMapper() {
      return new ObjectMapper().findAndRegisterModules().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   }

   @Bean
   public ExecutorService executorService() {
      return Executors.newWorkStealingPool(4);
   }

   @Bean
   public RoleHierarchy roleHierarchy(AuthenticationProperties authProperties) {
      RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
      roleHierarchy.setHierarchy(authProperties.getRoleHierarchy());
      return roleHierarchy;
   }

   @Bean
   @ConfigurationProperties("auth")
   public AuthenticationProperties authProperties() {
      return new AuthenticationProperties();
   }

   @Bean
   public IAuthenticationService authService(
         IPrincipalRepository userRepository,
         IEncryptionPasswordService encryptionService,
         AuthenticationProperties props, ApplicationContext context,
         ExecutorService executorService,
         IEmailTokenRepository tokenRepository) {
      return new DefaultAuthenticationService(userRepository, encryptionService,
            props, context, executorService, tokenRepository);
   }

   @Bean
   public IEncryptionPasswordService encryptionPasswordService(
         AuthenticationProperties authProperties) {
      return new DefaultEncryptionPasswordService(authProperties);
   }

   @Bean
   public RegisteredClientRepository registeredClientRepository(
         AuthenticationProperties authProperties, TokenSettings tokenSettings) {
      RegisteredClient registeredClient = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId(authProperties.getClient().getClientId())
            .clientSecret(authProperties.getClient().getClientSecret())
            .clientAuthenticationMethods(ca -> ca.addAll(
                  authProperties.getClient().getAuthenticationMethods()))
            .authorizationGrantTypes(
                  gt -> gt.addAll(authProperties.getClient().getGrantTypes()))
            .redirectUris(uris -> uris.addAll(authProperties.getRedirectUris()))
            .scope(OidcScopes.OPENID).tokenSettings(tokenSettings).build();
      return new InMemoryRegisteredClientRepository(registeredClient);
   }

   @Bean
   public ProviderSettings providerSettings() {
      return ProviderSettings.builder().issuer("http://auth-server:8083")
            .build();
   }

   public static void applyCustomSecurity(HttpSecurity http) throws Exception {
      OAuth2ProviderConfigurer<HttpSecurity> googleProviderConfigurer = new OAuth2ProviderConfigurer<>();
      OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
      RequestMatcher defaultEnponitsMatcher = authorizationServerConfigurer
            .getEndpointsMatcher();
      RequestMatcher googleEndpointMatcher = googleProviderConfigurer
            .getEndpointsMatcher();
      http.requestMatchers(x -> x.requestMatchers(defaultEnponitsMatcher,
            googleEndpointMatcher))
            .authorizeRequests(authorizeRequests -> authorizeRequests
                  .anyRequest().authenticated())
            .csrf(csrf -> csrf.ignoringRequestMatchers(defaultEnponitsMatcher,
                  googleEndpointMatcher))
            .apply(googleProviderConfigurer);
      http.apply(authorizationServerConfigurer);
   }
}
