package sv.com.udb.services.authentication.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.UserRepository;
import sv.com.udb.services.authentication.services.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
//@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthenticationServerConfiguration {

  private static final String RSA = "RSA";
  private static final int KEY_SIZE = 2048;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeRequests(authorizeRequests ->
        authorizeRequests.anyRequest().authenticated()
      )
      .formLogin(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    applyDefaultSecurity(http);
    return http.formLogin(Customizer.withDefaults()).build();
  }

  public static void applyDefaultSecurity(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
      new OAuth2AuthorizationServerConfigurer<>();
    RequestMatcher endpointsMatcher = authorizationServerConfigurer
      .getEndpointsMatcher();
    http
      .requestMatcher(endpointsMatcher)
      .authorizeRequests(authorizeRequests ->
        authorizeRequests.anyRequest().authenticated()
      )
      .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
      .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
      .apply(authorizationServerConfigurer);
  }
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
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
    RSAKey rsaKey = generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(AuthenticationProperties authProperties) {
    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId(authProperties.getClient().getClientId())
      .clientSecret(authProperties.getClient().getClienSecret())
      .clientAuthenticationMethod(authProperties.getClient().getAuthenticationMethod())
      .authorizationGrantType(authProperties.getClient().getGrantType())
      .redirectUri("https://oidcdebugger.com/debug")
      .scope(OidcScopes.OPENID)
      .build();

    return new InMemoryRegisteredClientRepository(registeredClient);
  }

  private static RSAKey generateRsa() throws NoSuchAlgorithmException {
    KeyPair keyPair = generateRsaKey();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
      .privateKey(privateKey)
      .keyID(UUID.randomUUID().toString())
      .build();
  }

  private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
    keyPairGenerator.initialize(KEY_SIZE);
    return keyPairGenerator.generateKeyPair();
  }


}
