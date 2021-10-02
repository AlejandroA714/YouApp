package sv.com.udb.services.authentication.configuration;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultEncryptionPasswordService;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.services.impl.DefaultGoogleAuthenticationService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class AuthenticationServerConfiguration {
   @Bean
   public SecurityFilterChain authorizationServerSecurityFilterChain(
         HttpSecurity http) throws Exception {
      OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
      return http.formLogin(Customizer.withDefaults()).csrf().disable().build();
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
         IPrincipalRepository userRepository) {
      return new DefaultAuthenticationService(userRepository);
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
}
