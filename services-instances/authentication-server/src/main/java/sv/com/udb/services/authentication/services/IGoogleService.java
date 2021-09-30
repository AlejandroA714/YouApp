package sv.com.udb.services.authentication.services;

import com.google.api.services.people.v1.model.Person;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;

public interface IGoogleService {
   void validateToken(GoogleAuthorizationRequest principal)
         throws InvalidTokenException;

   Person getPerson(String accessToken);
}
