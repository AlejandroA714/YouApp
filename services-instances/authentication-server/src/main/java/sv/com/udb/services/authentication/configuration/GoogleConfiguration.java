package sv.com.udb.services.authentication.configuration;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.StringUtils;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IOAuthRegistrationRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.repository.IRoleRepository;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;
import sv.com.udb.services.authentication.services.impl.DefaultGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultGoogleOAuth2Provider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Configuration
public class GoogleConfiguration {
   @Bean
   public NetHttpTransport httpTransport()
         throws GeneralSecurityException, IOException {
      return GoogleNetHttpTransport.newTrustedTransport();
   }

   @Bean
   public JsonFactory jsonFactory() {
      return new GsonFactory();
   }

   @Bean
   public Credential credential() {
      return new Credential(BearerToken.authorizationHeaderAccessMethod());
   }

   @Bean
   public GoogleIdTokenVerifier verifier(NetHttpTransport httpTransport,
         JsonFactory jsonFactory, AuthenticationProperties authProperties) {
      return new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
            .setAudience(authProperties.getGoogle().getClientId()).build();
   }

   @Bean
   public IGoogleAuthenticationService googleService(
         GoogleIdTokenVerifier verifier, NetHttpTransport netTransport,
         JsonFactory jsonFactory, Credential credential,
         AuthenticationProperties props) {
      return new DefaultGoogleAuthenticationService(verifier, netTransport,
            jsonFactory, credential, props.getGoogle());
   }
}
