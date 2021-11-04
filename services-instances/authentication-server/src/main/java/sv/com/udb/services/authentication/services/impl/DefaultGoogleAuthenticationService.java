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
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.models.GooglePrincipal;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DefaultGoogleAuthenticationService
      implements IGoogleAuthenticationService {
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
   @NonNull
   private final IPrincipalRepository                         principalRepository;

   @Override
   public GoogleIdToken validateToken(GoogleAuthorizationRequest principal)
         throws InvalidTokenException {
      try {
         LOGGER.trace("Validando: {}", principal);
         GoogleIdToken token = verifier.verify(principal.getIdToken());
         // if (token == null) throw new InvalidTokenException();
         principal.setAuthenticated(true);
         return token;
      }
      catch (Exception e) {
         LOGGER.error("Failed to validated token ", e);
         throw new InvalidTokenException(e);
      }
   }

   @Override
   public GooglePrincipal registerIfNotExits(
         GoogleAuthorizationRequest authorizationRequest) {
      var principal = authorizationRequest.getPrincipal();
      LOGGER.info("Trying to register google user: {}", principal);
      Optional<YouAppPrincipal> opt = principalRepository
            .findByIdWithRoles(principal.getId());
      if (!opt.isPresent()) {
         var youprincipal = YouAppPrincipal.from(principal);
         Person p = this.getPerson(authorizationRequest.getAccessToken());
         if (null != p && null != p.getBirthdays()) {
            var birthday = p.getBirthdays().stream().findFirst();
            if (birthday.isPresent()) {
               var date = birthday.get().getDate();
               youprincipal.setBirthday(LocalDate.of(date.getYear(),
                     date.getMonth(), date.getDay()));
            }
         }
         principalRepository.save(youprincipal);
         LOGGER.info("Google user: {} register", principal.getEmail());
         principal.setAuthorities(youprincipal.getAuthorities());
      }
      else {
         LOGGER.info("Google User already exits");
         principal.setAuthorities(opt.get().getAuthorities());
      }
      return principal;
   }

   @Override
   public Person getPerson(String accessToken) {
      PeopleService peopleService;
      try {
         LOGGER.trace("Trying to get Person...");
         credential.setAccessToken(accessToken);
         peopleService = new PeopleService(netTransport, jsonFactory,
               credential);
         return peopleService.people().get(properties.getResourceName())
               .setPersonFields(properties.getPersonFields()).execute();
      }
      catch (Exception e) {
         LOGGER.error("Failed to recover fields from user google");
         return null;
      }
      finally {
         peopleService = null;
      }
   }
}
