package sv.com.udb.services.authentication.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository.RegisteredClientParametersMapper;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.authentication.jackson.OAuthProviderSecurityModule;
import sv.com.udb.services.authentication.oauth.OAuth2ProviderConfigurer;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.services.impl.DefaultAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultEncryptionPasswordService;
import sv.com.udb.services.commons.models.Principal;
import sv.com.udb.services.commons.repository.IEmailTokenRepository;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.youapp.services.filter.FilterChainExceptionHandler;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthenticationServerConfiguration {
   private static final String               AUTHORITIES_CLAIM = "authorities";
   private static final String               UUID_CLAIM        = "id";
   @NonNull
   private final FilterChainExceptionHandler filterChainExceptionHandler;

   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   public SecurityFilterChain authorizationServerSecurityFilterChain(
         HttpSecurity http) throws Exception {
      applyCustomSecurity(http);
      http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
      return http.formLogin(Customizer.withDefaults()).csrf().disable().build();
   }

   @Bean
   public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
      return context -> {
         if (context.getTokenType().getValue()
               .equals(OAuth2TokenType.ACCESS_TOKEN.getValue())) {
            Authentication auth = context.getPrincipal();
            Set<String> authorities = auth.getAuthorities().stream()
                  .map(GrantedAuthority::getAuthority)
                  .collect(Collectors.toSet());
            Principal p = (Principal) auth.getPrincipal();
            context.getClaims().claim(UUID_CLAIM, p.getId());
            context.getClaims().claim(AUTHORITIES_CLAIM, authorities);
         }
      };
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
   @ConfigurationProperties("app.auth")
   public AuthenticationProperties authProperties() {
      return new AuthenticationProperties();
   }

   @Bean
   public IAuthenticationService authService(
         IPrincipalRepository userRepository,
         IEncryptionPasswordService encryptionService,
         AuthenticationProperties props, ApplicationContext context,
         ExecutorService executorService, IEmailTokenRepository tokenRepository,
         IEmailService emailService) {
      return new DefaultAuthenticationService(userRepository, encryptionService,
            props, context, executorService, tokenRepository, emailService);
   }

   @Bean
   public IEncryptionPasswordService encryptionPasswordService(
         AuthenticationProperties authProperties) {
      return new DefaultEncryptionPasswordService(authProperties);
   }

   @Bean
   public RegisteredClientParametersMapper parametersMapper(
         IEncryptionPasswordService passwordService) {
      var parametersMapper = new RegisteredClientParametersMapper();
      parametersMapper.setPasswordEncoder(passwordService);
      return parametersMapper;
   }

   @Bean
   public RegisteredClientRepository registeredClientRepository(
         AuthenticationProperties properties, TokenSettings tokenSettings,
         JdbcTemplate jdbcTemplate,
         RegisteredClientParametersMapper parametersMapper) {
      JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(
            jdbcTemplate);
      registeredClientRepository
            .setRegisteredClientParametersMapper(parametersMapper);
      properties.getClients().forEach(c -> {
         RegisteredClient registeredClient = RegisteredClient.withId(c.getId())
               .clientId(c.getClientId()).clientName(c.getClientName())
               .clientSecret(c.getClientSecret())
               .clientAuthenticationMethods(
                     ca -> ca.addAll(c.getAuthenticationMethods()))
               .authorizationGrantTypes(gt -> gt.addAll(c.getGrantTypes()))
               .redirectUris(uris -> uris.addAll(c.getRedirectUris()))
               .scopes(s -> s.addAll(c.getScopes()))
               .tokenSettings(tokenSettings).build();
         registeredClientRepository.save(registeredClient);
      });
      return registeredClientRepository;
   }

   @Bean
   public ProviderSettings providerSettings(
         AuthenticationProperties properties) {
      return ProviderSettings.builder()
            .issuer(String.format("http://%s:8083", properties.getIpAddress()))
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

   @Bean
   public RowMapper<OAuth2Authorization> rowMapper(
         RegisteredClientRepository registeredClientRepository) {
      var rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
            registeredClientRepository);
      ObjectMapper objectMapper = new ObjectMapper();
      ClassLoader classLoader = JdbcOAuth2AuthorizationService.class
            .getClassLoader();
      List<Module> securityModules = SecurityJackson2Modules
            .getModules(classLoader);
      objectMapper.registerModules(securityModules);
      objectMapper
            .registerModule(new OAuth2AuthorizationServerJackson2Module());
      objectMapper.registerModule(new OAuthProviderSecurityModule());
      rowMapper.setObjectMapper(objectMapper);
      return rowMapper;
   }

   @Bean
   public OAuth2AuthorizationService authorizationService(
         JdbcTemplate jdbcTemplate,
         RegisteredClientRepository registeredClientRepository,
         RowMapper<OAuth2Authorization> mapper) {
      var d = new JdbcOAuth2AuthorizationService(jdbcTemplate,
            registeredClientRepository);
      d.setAuthorizationRowMapper(mapper);
      return d;
   }
}
