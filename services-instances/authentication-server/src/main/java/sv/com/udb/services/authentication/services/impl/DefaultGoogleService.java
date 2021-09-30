package sv.com.udb.services.authentication.services.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.services.IGoogleService;

@Slf4j
@RequiredArgsConstructor
public class DefaultGoogleService implements IGoogleService {
   @NonNull
   private final GoogleIdTokenVerifier                        verifier;
   @NonNull
   private final NetHttpTransport                             netTransport;
   @NonNull
   private final JsonFactory                                  jsonFactory;
   @NonNull
   private final Credential                                   credential;
   @NonNull
   private final AuthenticationProperties.GoogleConfiguration properties;

   @Override
   public void validateToken(GoogleAuthorizationRequest principal)
         throws InvalidTokenException {
      try {
         LOGGER.trace("Validando: {}", principal);
         GoogleIdToken token = verifier.verify(principal.getIdToken());
         if (token == null) throw new InvalidTokenException();
      }
      catch (Exception e) {
         LOGGER.error("Failed to validated token ", e);
      }
   }

   @Override
   public Person getPerson(String accessToken) {
      PeopleService peopleService;
      try {
         credential.setAccessToken(accessToken);
         peopleService = new PeopleService(netTransport, jsonFactory,
               credential);
         return peopleService.people().get(properties.getResourceName())
               .setPersonFields(properties.getPersonFields()).execute();
      }
      catch (Exception e) {
         LOGGER.error("Failed to recover fields from user google");
      }
      finally {
         peopleService = null;
      }
      return null;
   }
}
