package sv.com.udb.services.authentication.configuration;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultGoogleAuthenticationService;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
         AuthenticationProperties props, IPrincipalRepository repository) {
      return new DefaultGoogleAuthenticationService(verifier, netTransport,
            jsonFactory, credential, props.getGoogle(), repository);
   }
}
