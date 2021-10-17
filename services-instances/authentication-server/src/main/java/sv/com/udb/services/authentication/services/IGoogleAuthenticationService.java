package sv.com.udb.services.authentication.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.services.people.v1.model.Person;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;

public interface IGoogleAuthenticationService {
   GoogleIdToken validateToken(GoogleAuthorizationRequest principal)
         throws InvalidTokenException;

   void registerIfNotExits(GoogleAuthorizationRequest principal);

   Person getPerson(String accessToken);
}
