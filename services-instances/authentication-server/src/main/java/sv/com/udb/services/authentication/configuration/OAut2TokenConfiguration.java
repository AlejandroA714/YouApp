package sv.com.udb.services.authentication.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class OAut2TokenConfiguration {
   private static String JKS_FILE     = "youapp.jks";
   private static String JKS_PASSWORD = "youapp2201";
   private static String JKS_ALIAS    = "youapp";
   private static String JKS_KEY      = "b353b5e1-20a1-41cc-a6ff-78e14a3a5c32";

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
            .reuseRefreshTokens(false).build();
   }

   private static RSAKey generateRsa() throws NoSuchAlgorithmException {
      KeyPair keyPair = generateRsaKey();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(JKS_KEY)
            .build();
   }

   private static KeyPair generateRsaKey() {
      KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
            new ClassPathResource(JKS_FILE), JKS_PASSWORD.toCharArray());
      return keyStoreKeyFactory.getKeyPair(JKS_ALIAS);
   }
}
