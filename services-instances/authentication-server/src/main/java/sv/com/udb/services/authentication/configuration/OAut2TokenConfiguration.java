package sv.com.udb.services.authentication.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwsEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.util.StringUtils;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;
import sv.com.udb.services.authentication.services.impl.DefaultOAuth2TokenService;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;

@Configuration
public class OAut2TokenConfiguration {
   private static final String RSA      = "RSA";
   private static final int    KEY_SIZE = 2048;

   @Bean
   public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
      return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
   }

   @Bean
   public JWKSource<SecurityContext> jwkSource()
         throws NoSuchAlgorithmException {
      RSAKey rsaKey = generateRsa();
      JWKSet jwkSet = new JWKSet(rsaKey);
      return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
   }

   @Bean
   public TokenSettings tokenSettings(AuthenticationProperties authProperties) {
      return TokenSettings.builder()
            .accessTokenTimeToLive(authProperties.getJwt().getAccess_token())
            .refreshTokenTimeToLive(authProperties.getJwt().getRefresh_token())
            .build();
   }

   @Bean
   public IOAuth2TokenService auth2TokenService(
         RegisteredClientRepository clientRepository,
         ProviderSettings providerSettings, HttpSecurityBuilder builder) {
      JwtEncoder jwtEncoder = getJwtEncoder(builder);
      OAuth2AuthorizationService service = getAuthorizationService(builder);
      return new DefaultOAuth2TokenService(clientRepository, providerSettings,
            service, jwtEncoder);
   }

   private static RSAKey generateRsa() throws NoSuchAlgorithmException {
      KeyPair keyPair = generateRsaKey();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      return new RSAKey.Builder(publicKey).privateKey(privateKey)
            .keyID(UUID.randomUUID().toString()).build();
   }

   private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
      keyPairGenerator.initialize(KEY_SIZE);
      return keyPairGenerator.generateKeyPair();
   }

   static <B extends HttpSecurityBuilder<B>> JwtEncoder getJwtEncoder(
         B builder) {
      JwtEncoder jwtEncoder = builder.getSharedObject(JwtEncoder.class);
      if (jwtEncoder == null) {
         jwtEncoder = getOptionalBean(builder, JwtEncoder.class);
         if (jwtEncoder == null) {
            JWKSource<SecurityContext> jwkSource = getJwkSource(builder);
            jwtEncoder = new NimbusJwsEncoder(jwkSource);
         }
         builder.setSharedObject(JwtEncoder.class, jwtEncoder);
      }
      return jwtEncoder;
   }

   static <B extends HttpSecurityBuilder<B>> OAuth2AuthorizationService getAuthorizationService(
         B builder) {
      OAuth2AuthorizationService authorizationService = builder
            .getSharedObject(OAuth2AuthorizationService.class);
      if (authorizationService == null) {
         authorizationService = getOptionalBean(builder,
               OAuth2AuthorizationService.class);
         if (authorizationService == null) {
            authorizationService = new InMemoryOAuth2AuthorizationService();
         }
         builder.setSharedObject(OAuth2AuthorizationService.class,
               authorizationService);
      }
      return authorizationService;
   }

   static <B extends HttpSecurityBuilder<B>> JWKSource<SecurityContext> getJwkSource(
         B builder) {
      JWKSource<SecurityContext> jwkSource = builder
            .getSharedObject(JWKSource.class);
      if (jwkSource == null) {
         ResolvableType type = ResolvableType
               .forClassWithGenerics(JWKSource.class, SecurityContext.class);
         jwkSource = getBean(builder, type);
         builder.setSharedObject(JWKSource.class, jwkSource);
      }
      return jwkSource;
   }

   static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder,
         ResolvableType type) {
      ApplicationContext context = builder
            .getSharedObject(ApplicationContext.class);
      String[] names = context.getBeanNamesForType(type);
      if (names.length == 1) {
         return (T) context.getBean(names[0]);
      }
      if (names.length > 1) {
         throw new NoUniqueBeanDefinitionException(type, names);
      }
      throw new NoSuchBeanDefinitionException(type);
   }

   static <B extends HttpSecurityBuilder<B>, T> T getOptionalBean(B builder,
         Class<T> type) {
      Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
            builder.getSharedObject(ApplicationContext.class), type);
      if (beansMap.size() > 1) {
         throw new NoUniqueBeanDefinitionException(type, beansMap.size(),
               "Expected single matching bean of type '" + type.getName()
                     + "' but found " + beansMap.size() + ": "
                     + StringUtils.collectionToCommaDelimitedString(
                           beansMap.keySet()));
      }
      return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
   }
}
