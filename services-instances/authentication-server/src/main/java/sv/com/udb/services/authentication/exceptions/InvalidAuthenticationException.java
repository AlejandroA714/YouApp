package sv.com.udb.services.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;

public class InvalidAuthenticationException extends ResponseStatusException {
   public InvalidAuthenticationException(Throwable cause) {
      super(HttpStatus.BAD_REQUEST, "Failed to login with google", cause);
   }

   public InvalidAuthenticationException(String message, Throwable cause) {
      super(HttpStatus.BAD_REQUEST, message, cause);
   }
}
