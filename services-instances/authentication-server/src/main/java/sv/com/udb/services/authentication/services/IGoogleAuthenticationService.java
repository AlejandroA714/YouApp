package sv.com.udb.services.authentication.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.services.people.v1.model.Person;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.models.GooglePrincipal;

public interface IGoogleAuthenticationService {
   GoogleIdToken validateToken(GoogleAuthorizationRequest principal)
         throws InvalidTokenException;

   GooglePrincipal registerIfNotExits(GoogleAuthorizationRequest principal);

   Person getPerson(String accessToken);
}
