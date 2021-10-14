package sv.com.udb.services.authentication.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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
}
