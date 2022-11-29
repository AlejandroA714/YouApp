package sv.com.udb.youapp.services.authentication.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import sv.com.udb.youapp.commons.jpa.services.PrincipalService;
import sv.com.udb.youapp.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.youapp.services.authentication.repositories.JpaClientRepository;
import sv.com.udb.youapp.services.authentication.services.AuthenticationService;
import sv.com.udb.youapp.services.authentication.services.EncryptPasswordService;
import sv.com.udb.youapp.services.authentication.services.impl.DefaultAuthenticationService;
import sv.com.udb.youapp.services.authentication.services.impl.DefaultEncryptPasswordService;
import sv.com.udb.youapp.services.authentication.services.impl.DefaultRegisteredClientRepository;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class WebSecurityConfiguration {
   @Bean
   @Order(1)
   public SecurityFilterChain authorizationServerSecurityFilterChain(
         HttpSecurity http) throws Exception {
      OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
      http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());
      http.exceptionHandling(
            (exceptions) -> exceptions.authenticationEntryPoint(
                  new LoginUrlAuthenticationEntryPoint("/login")))
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
      return http.build();
   }

   @Bean
   @Order(2)
   public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
         throws Exception {
      http.authorizeHttpRequests(
            (authorize) -> authorize.anyRequest().permitAll())
            .formLogin(Customizer.withDefaults());
      return http.build();
   }

   @Bean
   public EncryptPasswordService passwordEncoder(
         AuthenticationProperties properties) {
      return new DefaultEncryptPasswordService(properties);
   }

   @Bean
   public AuthenticationService userDetailsService(
         PrincipalService principalService) {
      return new DefaultAuthenticationService(principalService);
   }

   @Bean
   public RegisteredClientRepository registeredClientRepository(
         JpaClientRepository jpaClientRepository) {
      return new DefaultRegisteredClientRepository(jpaClientRepository);
   }
   // @Bean
   // public RegisteredClientRepository registeredClientRepository() {
   // RegisteredClient registeredClient =
   // RegisteredClient.withId(UUID.randomUUID().toString())
   // .clientId("youapp")
   // .clientSecret("MkiIbZDAX4Uye7q+/Wp9BBlbU4bvEQlNYDRCVeWhdWSgnaelp4rpjKWu0zICSWxEQ5k/RxEcOCxboqZUQIIDijU+xy6AhRsriZZKneGymfn2W1Kom1MYwxGRoCwXr10AcWMu5k3zrLeh436osqi/bbu5YZVF3ggBP3jOESJ4b2htXDsQ+olv9YDt41U7y7om94bb/xbOV4odv2Tb6j8UscvniD/NHNRIdm4m53Bs0Oh9qBh6i2vI238svYbdcKdjCmr8ZpsSo/rROBNQwn+aeU5XzRZz6h0Yr19E1nj1PJVlcQPzxKI2h2Trek1TbQyjQNHl2vDiEBlAxQuOw3GuQQ==")
   // .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
   // .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
   // .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
   // .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
   // .redirectUri("https://oidcdebugger.com/debug")
   // .redirectUri("http://127.0.0.1:8080/authorized")
   // .scope(OidcScopes.OPENID)
   // .scope(OidcScopes.PROFILE)
   // .scope("message.read")
   // .scope("message.write")
   // .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
   // .build();
   //
   // return new InMemoryRegisteredClientRepository(registeredClient);
   // }

   @Bean
   public JWKSource<SecurityContext> jwkSource() {
      KeyPair keyPair = generateRsaKey();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey)
            .keyID(UUID.randomUUID().toString()).build();
      JWKSet jwkSet = new JWKSet(rsaKey);
      return new ImmutableJWKSet<>(jwkSet);
   }

   private static KeyPair generateRsaKey() {
      KeyPair keyPair;
      try {
         KeyPairGenerator keyPairGenerator = KeyPairGenerator
               .getInstance("RSA");
         keyPairGenerator.initialize(2048);
         keyPair = keyPairGenerator.generateKeyPair();
      }
      catch (Exception ex) {
         throw new IllegalStateException(ex);
      }
      return keyPair;
   }

   @Bean
   public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
      return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
   }

   @Bean
   public AuthorizationServerSettings authorizationServerSettings() {
      return AuthorizationServerSettings.builder().build();
   }
}
